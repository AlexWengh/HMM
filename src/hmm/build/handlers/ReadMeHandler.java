package hmm.build.handlers;

import hmm.Activator;
import hmm.build.util.ShowMessageUtil;

import java.awt.Desktop;
import java.net.URI;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;

public class ReadMeHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) {
		if (Desktop.isDesktopSupported()) {
			try {
				Desktop desktop = Desktop.getDesktop();
				String path = FileLocator.toFileURL(Platform.getBundle(Activator.PLUGIN_ID).getEntry("")).toString();
				path += "html/help/quickstart.html";
				desktop.browse(new URI(path));
			} catch (Exception e) {
				e.printStackTrace();
				new ShowMessageUtil().showNormalError("Open file in external browser failed by error: " + e.getMessage());
			}
		} else {
			new ShowMessageUtil().showNormalError("Cannot open the file in external browser, you system does not support this Java method.");
		}
		return null;
	}

}
