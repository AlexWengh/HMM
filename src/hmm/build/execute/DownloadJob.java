package hmm.build.execute;

import it.sauronsoftware.ftp4j.FTPFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

import hmm.Activator;
import hmm.build.console.Console;
import hmm.build.ftp.FtpTool;
import hmm.build.ftp.FtpToolException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class DownloadJob extends Job {
	
	private FtpTool ftpTool;
	private IProgressMonitor monitor;
	private String familyStr = "HMM.AUTOMATION.DOWNLOAD";
	private String currentRemoteDir = "";

	public DownloadJob(String name, FtpTool ftpTool) {
		super(name);
		setUser(false);
		this.ftpTool = ftpTool;
	}
	
	public String getFamily() {
		return familyStr;
	}
	
	@Override
	public boolean belongsTo(Object family) {
		if(family instanceof String) {
			String str = (String) family;
			if(str.equals(familyStr))
				return true;
		}
		return false;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		this.monitor = monitor;
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		try {
			currentRemoteDir = ftpTool.getCurDir();
			if(currentRemoteDir.charAt(0) == '/' || currentRemoteDir.charAt(0) == '\\')
				currentRemoteDir = currentRemoteDir.substring(1);
			downloadDir();
		} catch (FtpToolException e) {
			return error(e);
		} catch (IOException e) {
			return error(e);
		}
		if(monitor.isCanceled()) {
			monitor.done();
			String cancelStr = "Downloading Latest Build is Canceled.";
			Console.getCommandInstance().writeErrorLine(cancelStr);
			return new Status(Status.CANCEL, Activator.PLUGIN_ID, cancelStr);
		}
		monitor.done();
		String succStr = "Downloading Latest Build Successfully Completed.";
		Console.getCommandInstance().writeLine(succStr);
		return new Status(Status.OK, Activator.PLUGIN_ID, succStr);
	}

	private void downloadDir() throws FtpToolException, IOException {
		if(monitor.isCanceled())
			return;
		List<FTPFile> files = ftpTool.getFileList();
		for (FTPFile ftpFile : files) {
			if(monitor.isCanceled())
				return;
			String name = ftpFile.getName();
			if(ftpFile.getType() == FTPFile.TYPE_DIRECTORY) {
				if(!name.equals(".") && !name.equals("..")) {
					String tempCurrentRemoteDir = currentRemoteDir;
					String localPath = getLocalFilePath(name);
					File localDir = new File(localPath);
					if(!localDir.mkdir())
						throw new IOException("Create Local Folder Failed");
					ftpTool.changeCurDir(name);
					currentRemoteDir += "/" + name;
					downloadDir();
					ftpTool.changeToParentDir();
					currentRemoteDir = tempCurrentRemoteDir;
				}
			} else {
				monitor.subTask("Downloading: " + currentRemoteDir + "/" + ftpFile.getName());
				String localPath = getLocalFilePath(name);
				File localFile = new File(localPath);
				ftpTool.downloadFile(ftpFile, localFile);
			}
		}
	}
	
	private String getLocalFilePath(String remoteFileName) {
		String curRemotePath = currentRemoteDir.replace('/', File.separatorChar);
		String localPath = ftpTool.getLocalRootPath() + File.separatorChar + curRemotePath
				 + File.separatorChar + remoteFileName;
		return localPath;
	}
	
	private IStatus error(Exception e) {
		e.printStackTrace();
		String errorStr = "Download file from Ftp server failed with error.";
		Console.getCommandInstance().writeErrorLine(errorStr);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}

}
