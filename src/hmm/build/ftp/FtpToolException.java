package hmm.build.ftp;

public class FtpToolException extends Exception {

	/**
	 * serialVersionUID is auto generated.
	 */
	private static final long serialVersionUID = 3681558358175952029L;
	
	Exception exception = new Exception();
	
	public FtpToolException(Exception e) {
		exception = e;
	}
	
	public void printStackTrace() {
		exception.printStackTrace();
	}
	
	public String getMessage() {
		return exception.getMessage();
	}

}
