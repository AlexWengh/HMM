package hmm.build.ftp;

import hmm.build.settings.FtpSetting;

public class FtpInfo {
	
	private String ftpIp = "";
	private int ftpPort;
	private FtpSetting ftpSetting;
	
	public FtpInfo(String ftpIp, int ftpPort, FtpSetting ftpSetting) {
		this.ftpIp = ftpIp;
		this.ftpPort = ftpPort;
		this.ftpSetting = ftpSetting;
	}

	public String getFtpIp() {
		return ftpIp;
	}

	public int getFtpPort() {
		return ftpPort;
	}

	public FtpSetting getFtpSetting() {
		return ftpSetting;
	}
	
}
