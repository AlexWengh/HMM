package hmm.build.execute;

import hmm.Activator;
import hmm.automation.execute.RunAutomationJob;
import hmm.automation.models.TreeNode;
import hmm.automation.util.TreeModelXmlUtil;
import hmm.build.console.Console;
import hmm.build.console.ConsoleReader;
import hmm.build.settings.CommandLines;
import hmm.build.settings.TemplateVariable;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

public class RunCommandsJob extends Job {

	private IProgressMonitor monitor;
	private Process shell;
	private BufferedWriter writer;
	private ConsoleReader readerThread;
	private ConsoleReader readerErrorThread;
	private BufferedReader reader;
	private BufferedReader errorReader;
	private ArrayList<File> replacedFiles = new ArrayList<File>();
	private String familyStr = "HMM.COMMAND";
	
	public RunCommandsJob(String name) {
		super(name);
		setUser(false);
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
		List<String> commands = CommandLines.getInstance().getCommands();
		if(commands.size() != 0) {
			try {
				prepare();
				runCommandLines();
				terminate();
			} catch (Exception e) {
				return error(e);
			}
			if(monitor.isCanceled()) {
				monitor.done();
				String cancelStr = "Commands Execution Canceled.";
				Console.getCommandInstance().writeLine("\n");
				Console.getCommandInstance().writeErrorLine(cancelStr);
				return new Status(Status.CANCEL, Activator.PLUGIN_ID, cancelStr);
			}
		}
		monitor.done();
		String succStr = "Commands Execution Successfully Completed.";
		Console.getCommandInstance().writeLine("\n");
		Console.getCommandInstance().writeLine(succStr);
		return new Status(Status.OK, Activator.PLUGIN_ID, succStr);
	}
	
	private IStatus error(Exception e) {
		e.printStackTrace();
		terminateOnError();
		String errorStr = "Commands Execution Stopped by Errors.";
		Console.getCommandInstance().writeErrorLine(errorStr);
		monitor.done();
		return new Status(Status.ERROR, Activator.PLUGIN_ID, errorStr, e);
	}

	private void prepare() throws IOException {
		monitor.beginTask("", IProgressMonitor.UNKNOWN);
		replacedFiles.clear();
		String osName = System.getProperty("os.name");
		String shellCmd = "cmd";
		if(!osName.toLowerCase().contains("win"))
			shellCmd = "sh";
		shell = Runtime.getRuntime().exec(shellCmd);
		InputStream fis = shell.getInputStream();
		reader = new BufferedReader(new InputStreamReader(fis));
		InputStream fisErr = shell.getErrorStream();
		errorReader = new BufferedReader(new InputStreamReader(fisErr));
		OutputStream fos = shell.getOutputStream();
		writer = new BufferedWriter(new OutputStreamWriter(fos));
		readerThread = new ConsoleReader(reader);
		readerErrorThread = new ConsoleReader(errorReader);
		readerThread.start();
		readerErrorThread.start();
	}
	
	private void runCommandLines() throws IOException, InterruptedException {
		List<String> commands = CommandLines.getInstance().getCommands();
		for (String cmd : commands) {
			if(monitor.isCanceled())
				break;
			monitor.subTask("Running Command: " + cmd);
			String str = replaceTemplateStrings(cmd);
			if(str.startsWith("auto ") && str.length() > 5) {
				runAutomationScript(str.substring(5));
				continue;
			}
			writer.write(str + "\n");
			writer.flush();
		}
		writer.write("exit\n");
		writer.flush();
	}
	
	private String replaceTemplateStrings(String command) throws IOException {
		Map<String, String> templates = TemplateVariable.getInstance().getTemplateVariables();
		for (Entry<String, String> entry : templates.entrySet()) {
			if(command.contains(entry.getKey()))
				command = command.replace(entry.getKey(), entry.getValue());
		}
		if(command.endsWith(" --template")) {
			command = command.substring(0, command.indexOf(" --template"));
			String[] sections = command.split(" ");
			for (String str : sections) {
				File file = new File(str);
				if(file.isFile() && file.exists()) {
					replacedFiles.add(file);
					replaceTemplateStringsInFile(file);
				}
			}
		}
		return command;
	}
	
	private void replaceTemplateStringsInFile(File file) throws IOException {
		String path = file.getPath();
		if(path.charAt(path.length() - 1) == File.separatorChar)
			path = path.substring(0, path.length() - 1);
		File tempFile = new File(path + ".hmm.temp.replaced");
		if(tempFile.exists())
			throw new IOException("The temp replacing file already exists");
		if(!tempFile.createNewFile())
			throw new IOException("Creating the temp replacing file failed");

		Map<String, String> templates = TemplateVariable.getInstance().getTemplateVariables();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		String line = null;
		while((line = reader.readLine()) != null) {
			for (Entry<String, String> entry : templates.entrySet()) {
				if(line.contains(entry.getKey()))
					line = line.replace(entry.getKey(), entry.getValue());
			}
			writer.write(line + "\n");
		}
		reader.close();
		writer.close();
		if(!file.renameTo(new File(path + ".hmm.temp")))
			throw new IOException("Rename original file name: " + file.getPath() + " \nTo hmm temp file name failed");
		if(!tempFile.renameTo(new File(path)))
			throw new IOException("Rename replaced file name: " + tempFile.getPath() + "\nTo original file name failed");
	}
	
	private void terminate() throws IOException {
		while(readerThread.isAlive() || readerErrorThread.isAlive()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		for (File file : replacedFiles) {
			String path = file.getPath();
			if(path.charAt(path.length() - 1) == File.separatorChar)
				path = path.substring(0, path.length() - 1);
			File oriFile = new File(path + ".hmm.temp");
			if(!file.delete())
				throw new IOException("Deleting replaced file failed");
			oriFile.renameTo(file);
		}
		replacedFiles.clear();
		
		if(shell != null)
			shell.destroy();
	}
	
	private void terminateOnError() {
		if(writer != null) {
			try {
				writer.write("exit\n");
				writer.flush();
				terminate();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void runAutomationScript(String filePath) throws IOException, InterruptedException {
		File file = new File(filePath);
		TreeNode root = new TreeModelXmlUtil().loadFromXml(file);
		if(root == null || !root.hasChildren()) {
			throw new IOException("The automation script file: " + filePath + " cannot be recognized.");
		} else {
			boolean isTreeValid = new TreeModelXmlUtil().isTreeValid(root);
			if(!isTreeValid)
				throw new IOException("The automaton script file: " + filePath + " has invalid elements");
		}
		RunAutomationJob job = new RunAutomationJob("Running: " + file.getName(), root);
		job.setFamilyStr("HMM.AUTOMATION.SUB");
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
