package hmm.build.settings;


import hmm.build.sockets.SocketServer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Settings {
	private static Settings settings = new Settings();
	private String serverIp = "";
	private int serverPort = 21314;
	private Date time;
	private int intervalDays = 1;
	private String ftpIp = "";
	private int ftpPort = 21;
	private List<FtpSetting> ftpSettings = new ArrayList<FtpSetting>();
	private SocketServer socketServer = new SocketServer();

	private Settings() {}
	
	public static Settings getInstance() {
		return settings;
	}
	
	public void setServerIp(String ip) {
		serverIp = ip;
	}
	
	public String getServerIp() {
		return serverIp;
	}

	public int getServerPort() {
		return serverPort;
	}
	
	public void setBuildTime(Date date) {
		this.time = (Date) date.clone();
	}
	
	public Date getBuildTime() {
		if(time == null)
			return time;
		return (Date) time.clone();
	}
	
	public void setIntervalDays(int days) {
		intervalDays = days;
	}
	
	public int getIntervalDays() {
		return intervalDays;
	}
	
	public void setFtpIp(String ip) {
		ftpIp = ip;
	}
	
	public String getFtpIp() {
		return ftpIp;
	}
	
	public void setFtpPort(int port) {
		ftpPort = port;
	}
	
	public int getFtpPort() {
		return ftpPort;
	}
	
	public void setFtpSettings(List<FtpSetting> list) {
		ftpSettings = list;
	}
	
	public List<FtpSetting> getFtpSettings() {
		return ftpSettings;
	}
	
	public void addFtpSettings(FtpSetting model) {
		ftpSettings.add(model);
	}
	
	public void removeFtpSettings(FtpSetting model) {
		ftpSettings.remove(model);
	}
	
	public void modifyFtpSettings(int index, FtpSetting model) {
		ftpSettings.set(index, model);
	}
	
	public SocketServer getSocketServer() {
		return socketServer;
	}

	public void setSocketServer(SocketServer socketServer) {
		this.socketServer = socketServer;
	}
}
