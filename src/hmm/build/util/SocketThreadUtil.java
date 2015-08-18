package hmm.build.util;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

import org.eclipse.swt.widgets.Display;

public class SocketThreadUtil {

	public CharsetDecoder createCharsetDecoder() {
		CharsetDecoder decoder = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			decoder = charset.newDecoder();
		} catch (Exception e) {
			e.printStackTrace();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					new ShowMessageUtil().showFatalError("Can't find UTF-8 Charset Decoder on this machine.");
				}
			});
		}
		return decoder;
	}
	
	public CharsetEncoder createCharsetEncoder() {
		CharsetEncoder encoder = null;
		try {
			Charset charset = Charset.forName("UTF-8");
			encoder = charset.newEncoder();
		} catch (Exception e) {
			e.printStackTrace();
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					new ShowMessageUtil().showFatalError("Can't find UTF-8 Charset Encoder on this machine.");
				}
			});
		}
		return encoder;
	}

}
