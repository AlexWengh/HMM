package hmm;

import hmm.build.console.ConsoleFactory;
import hmm.build.settings.Settings;
import hmm.build.sockets.SocketServer;
import hmm.build.util.SettingsUtil;

import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

	public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
		super(configurer);
	}

	public ActionBarAdvisor createActionBarAdvisor(
			IActionBarConfigurer configurer) {
		return new ApplicationActionBarAdvisor(configurer);
	}

	public void preWindowOpen() {
		SettingsUtil.getInstance().loadSettings();
		IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
		configurer.setShowCoolBar(false);
		configurer.setShowProgressIndicator(true);
		configurer.setTitle("HanMeiMei");
		configurer.setInitialSize(new Point(1024, 768));
		if(Settings.getInstance().getServerIp().isEmpty())
			Settings.getInstance().getSocketServer().start();
	}
	
	@Override
	public void postWindowOpen() {
		ConsoleFactory cf = new ConsoleFactory();
        cf.openConsole();
        getWindowConfigurer().getWindow().getActivePage().addPartListener(new PagePartListener());
		super.postWindowOpen();
	}
	
	@Override
	public boolean preWindowShellClose() {
		cleanUp();
		return super.preWindowShellClose();
	}
	
	@Override
	public void postWindowClose() {
		cleanUp();
		super.postWindowClose();
	}
	
	private void cleanUp() {
		SettingsUtil.getInstance().saveSettings();
		SocketServer server = Settings.getInstance().getSocketServer();
		server.quit();
		try {
			server.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
