package hmm.build.ftp;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;
import it.sauronsoftware.ftp4j.FTPListParseException;

public class FtpTool {
	
	private FtpInfo info;
	private FTPClient client = new FTPClient();
	private String localRootPath = "";

	public FtpTool(FtpInfo ftpInfo, String localRootPath) {
		info = ftpInfo;
		if(localRootPath.charAt(localRootPath.length() - 1) == File.separatorChar)
			localRootPath = localRootPath.substring(0, localRootPath.length() - 1);
		this.localRootPath = localRootPath;
		client.setType(FTPClient.TYPE_BINARY);
	}
	
	public void connect() throws FtpToolException {
		try {
			client.connect(info.getFtpIp(), info.getFtpPort());
			if(client.isCompressionSupported())
				client.setCompressionEnabled(true);
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	private void error(Exception e) throws FtpToolException {
		FtpToolException exception = new FtpToolException(e);
		throw exception;
	}
	
	public void login() throws FtpToolException {
		try {
			client.login(info.getFtpSetting().getUser(), info.getFtpSetting().getPass());
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	public void disconnect() throws FtpToolException {
		try {
			client.disconnect(true);
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	public List<FTPFile> getFileList() throws FtpToolException {
		FTPFile[] list = null;
		try {
			list = client.list();
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		} catch (FTPDataTransferException e) {
			error(e);
		} catch (FTPAbortedException e) {
			error(e);
		} catch (FTPListParseException e) {
			error(e);
		}
		return Arrays.asList(list);
	}
	
	public void createDir(String folderName) throws FtpToolException {
		try {
			client.createDirectory(folderName);
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	public void changeCurDir(String path) throws FtpToolException {
		try {
			client.changeDirectory(path);
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	public String getCurDir() throws FtpToolException {
		String curDir = "";
		try {
			curDir = client.currentDirectory();
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
		return curDir;
	}
	
	public void changeToParentDir() throws FtpToolException {
		try {
			client.changeDirectoryUp();
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		}
	}
	
	public void downloadFile(FTPFile file, File localFile) throws FtpToolException {
		try {
			client.download(file.getName(), localFile, new FtpTransferListener());
		} catch (IllegalStateException e) {
			error(e);
		} catch (IOException e) {
			error(e);
		} catch (FTPIllegalReplyException e) {
			error(e);
		} catch (FTPException e) {
			error(e);
		} catch (FTPDataTransferException e) {
			error(e);
		} catch (FTPAbortedException e) {
			error(e);
		}
	}
	
	public String getLocalRootPath() {
		return localRootPath;
	}

}
