package hmm.build.settings;

public class FtpSetting implements Cloneable {

	private String user = "";
	private String pass = "";
	private boolean using;
	
	public FtpSetting(String user, String pass) {
		this.user = user;
		this.pass = pass;
	}

	public String getUser() {
		return user;
	}
	
	public String getPass() {
		return pass;
	}
	
	public boolean isUsing() {
		return using;
	}
	
	public void setUsing(boolean using) {
		this.using = using;
	}
	
	public Object clone() {
		FtpSetting model = new FtpSetting(user, pass);
		return model;
	}
}
