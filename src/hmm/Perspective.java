package hmm;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;

public class Perspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		layout.setEditorAreaVisible(false);
		layout.setFixed(false);
		String editArea = layout.getEditorArea();

        IFolderLayout console = layout.createFolder( "Message Consoles" ,IPageLayout.BOTTOM, 0.8f ,editArea);
        console.addView(IConsoleConstants.ID_CONSOLE_VIEW);
        layout.getViewLayout(IConsoleConstants.ID_CONSOLE_VIEW).setCloseable(false);
	}

}
