package hmm.build.execute;

import hmm.Activator;
import hmm.View;
import hmm.build.console.Console;
import hmm.build.ftp.FtpInfo;
import hmm.build.ftp.FtpTool;
import hmm.build.ftp.FtpToolException;
import hmm.build.settings.TemplateVariable;
import hmm.build.sockets.SocketClient;
import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class ScheduledBuildClientJob extends Job {
	
	private IProgressMonitor monitor;
	private FtpInfo ftpInfo;
	private FtpTool ftpTool;
	private FTPFile latestBuildFolder;

	public ScheduledBuildClientJob(String name) {
		super(name);
		setUser(false);
	}
	
	@Override
	public boolean belongsTo(Object family) {
		if(family instanceof String) {
			String str = (String) family;
			if(str.equals("HMM.SCHEDULE"))
				return true;
		}
		return false;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		try {
			ftpInfo = getServerFtpInfo();
		} catch (IOException e) {
			return getServerFtpInfoError(e);
		}
		if(ftpInfo == null)
			return noAvailableFtpUser();
		if(monitor.isCanceled())
			return cancelRun();
		
		try {
			latestBuildFolder = findServerLatestBuild();
		} catch (FtpToolException e) {
			return ftpTransferError(e);
		}
		if(latestBuildFolder == null)
			return notFindLatestBuildOnServer();
		if(monitor.isCanceled())
			return cancelRun();
		
		try {
			downloadLatestBuild();
		} catch (FtpToolException e) {
			return ftpTransferError(e);
		} catch (InterruptedException e) {
			return error(e);
		} catch (IOException e) {
			return error(e);
		}
		if(monitor.isCanceled())
			return cancelRun();
		
		String error = disconnectFtp();
		if(!error.isEmpty()) {
			Console.getCommandInstance().writeLine(error);
			return ftpTransferError(new FtpToolException(new Exception(error)));
		}
		
		try {
			releaseAllocatedFtp();
		} catch (IOException e) {
			return releaseFtpError(e);
		}
		if(monitor.isCanceled())
			return cancelRun();
		
		try {
			runCommands();
		} catch (InterruptedException e) {
			return error(e);
		}
		if(monitor.isCanceled())
			return cancelRun();
		
		monitor.done();
		String succStr = "Scheduled Build Execution Successfully Completed.";
		Console.getCommandInstance().writeLine(succStr);
		return new Status(Status.OK, Activator.PLUGIN_ID, succStr);
	}

	private FtpInfo getServerFtpInfo() throws IOException {
		monitor.subTask("Get Ftp Information from HanMeimei Server ...");
		SocketClient client = new SocketClient();
		FtpInfo info = client.getFtpInformation();
		return info;
	}
	
	private IStatus getServerFtpInfoError(IOException e) {
		e.printStackTrace();
		String errorStr = "Get Ftp Information from HanMeimei Server Error.";
		Console.getCommandInstance().writeErrorLine(errorStr);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}
	

	private IStatus noAvailableFtpUser() {
		String noFtpUser = "All configured ftp user on server are in using, no available user to connect to ftp server, aborted.";
		Console.getCommandInstance().writeErrorLine(noFtpUser);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, noFtpUser);
	}
	
	private IStatus cancelRun() {
		String cancelStr = "Scheduled Job Execution is Canceled.";
		cancelStr += disconnectFtp();
		monitor.done();
		Console.getCommandInstance().writeErrorLine(cancelStr);
		return new Status(Status.CANCEL, Activator.PLUGIN_ID, cancelStr);
	}
	
	private String disconnectFtp() {
		if(ftpTool != null) {
			try {
				ftpTool.disconnect();
			} catch (FtpToolException e) {
				e.printStackTrace();
				return "\n  And disconnect Ftp Server encountered error: " + e.getMessage();
			} finally {
				ftpTool = null;
			}
		}
		return "";
	}

	private FTPFile findServerLatestBuild() throws FtpToolException {
		monitor.subTask("Finding the latest build source folder on Ftp server ...");
		ftpTool = new FtpTool(ftpInfo, TemplateVariable.getInstance().getValue(TemplateVariable.HMM_DIR));
		List<FTPFile> files = null;
		ftpTool.connect();
		ftpTool.login();
		files = ftpTool.getFileList();
		for (FTPFile ftpFile : files) {
			if(ftpFile.getType() == FTPFile.TYPE_DIRECTORY) {
				String str = ftpFile.getName();
				if(str.matches("^\\d{4}-[0-1]\\d-[0-3]\\d_[0-2]\\d_[0-6]\\d_[0-6]\\d"))
					if ( (latestBuildFolder == null) || (latestBuildFolder.getName().compareTo(str) < 0) )
						latestBuildFolder = ftpFile;
			}
		}
		return latestBuildFolder;
	}
	
	private IStatus ftpTransferError(FtpToolException e) {
		e.printStackTrace();
		String errorStr = "Communication with Ftp server failed with error.";
		errorStr += disconnectFtp();
		Console.getCommandInstance().writeErrorLine(errorStr);
		try {
			releaseAllocatedFtp();
		} catch (IOException e1) {
			releaseFtpError(e1);
		}
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}
	
	private IStatus notFindLatestBuildOnServer() {
		String errorStr = "Cannot find the latest build source folder on server, " +
				"pleaes note the name of the folder which contains the latest build source files" +
				" must follow the format: \"yyyy-MM-dd_HH_mm_ss\"";
		errorStr += disconnectFtp();
		Console.getCommandInstance().writeErrorLine(errorStr);
		try {
			releaseAllocatedFtp();
		} catch (IOException e1) {
			releaseFtpError(e1);
		}
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr);
	}
	
	private void downloadLatestBuild() throws FtpToolException, InterruptedException, IOException {
		monitor.subTask("Downloading the latest build source files from Ftp server ...");
		ftpTool.changeCurDir(latestBuildFolder.getName());
		String localRootFolder = ftpTool.getLocalRootPath() + File.separatorChar + latestBuildFolder.getName();
		if(new File(localRootFolder).exists())
			throw new IOException("File or Directory already exists, Scheduled Build Job cannot Complete");
		if(!new File(localRootFolder).mkdir())
			throw new IOException("Create Local Root Folder Failed");
		TemplateVariable.getInstance().modifyTemplateVariable(TemplateVariable.BUILD_DIR, latestBuildFolder.getName());
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				View view = (View) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("HMM.view");
				view.getTemplateViewer().refresh();
			}
		});
		DownloadJob downloadJob = new DownloadJob("Download the Latest Build Source", ftpTool);
		downloadJob.schedule(100);
		try {
			getJobManager().join(downloadJob.getFamily(), monitor);
		} catch (OperationCanceledException e) {
			if(!downloadJob.cancel())
				downloadJob.join();
		}
		if(!downloadJob.getResult().isOK())
			monitor.setCanceled(true);
	}
	
	private IStatus error(Exception e) {
		e.printStackTrace();
		String errorStr = "Scheduled Job Execution Stopped by Errors.";
		errorStr += disconnectFtp();
		Console.getCommandInstance().writeErrorLine(errorStr);
		try {
			releaseAllocatedFtp();
		} catch (IOException e1) {
			releaseFtpError(e1);
		}
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}

	private void releaseAllocatedFtp() throws IOException {
		monitor.subTask("HanMeiMei Socket Server to release currently using Ftp information ...");
		SocketClient client = new SocketClient();
		client.releaseUsingFtp();
	}
	
	private IStatus releaseFtpError(IOException e) {
		e.printStackTrace();
		String errorStr = "Communicating with HanMeiMei Socket Server to release currently using Ftp information Failed.";
		Console.getCommandInstance().writeErrorLine(errorStr);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}
	
	private void runCommands() throws InterruptedException {
		monitor.subTask("Running Configured Commands ...");
		RunCommandsJob job = new RunCommandsJob("Running Configured Commands");
		job.schedule(100);
		try {
			getJobManager().join(job.getFamily(), monitor);
		} catch (OperationCanceledException e) {
			if(!job.cancel())
				job.join();
		}
		if(!job.getResult().isOK())
			monitor.setCanceled(true);
	}

}
