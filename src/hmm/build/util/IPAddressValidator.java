package hmm.build.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPAddressValidator {

	private Pattern pattern;
	private Matcher matcher;

	private static final String IPADDRESS_PATTERN =  "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
													+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
													+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
													+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	public IPAddressValidator() {
		pattern = Pattern.compile(IPADDRESS_PATTERN);
	}

	public boolean validate(final String ip) {
		matcher = pattern.matcher(ip);
		return matcher.matches();
	}
}
