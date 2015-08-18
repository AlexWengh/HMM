package hmm;

import hmm.automation.AutomationEditor;
import hmm.build.console.Console;

import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;

public class PagePartListener implements IPartListener2 {
	
	private int openedEditorNumber;

	@Override
	public void partActivated(IWorkbenchPartReference partRef) {
		if (partRef.getPart(false) instanceof AutomationEditor)
			Console.getAutomationInstance().showConsole();
		else if(partRef.getPart(false) instanceof View)
			Console.getCommandInstance().showConsole();
	}

	@Override
	public void partBroughtToTop(IWorkbenchPartReference partRef) {
		partActivated(partRef);
	}

	@Override
	public void partClosed(IWorkbenchPartReference partRef) {
		if (partRef.getPart(false) instanceof AutomationEditor) {
			IWorkbenchPage page = partRef.getPage();
			IViewReference view = page.findViewReference(View.ID);
			--openedEditorNumber;
			if (openedEditorNumber == 0) {
				page.setEditorAreaVisible(false);
				if(view != null)
					page.setPartState(view, IWorkbenchPage.STATE_RESTORED);
			}
		}
		partActivated(partRef);
	}

	@Override
	public void partDeactivated(IWorkbenchPartReference partRef) {}
	
	@Override
	public void partOpened(IWorkbenchPartReference partRef) {
		if (partRef.getPart(false) instanceof AutomationEditor) {
			IWorkbenchPage page = partRef.getPage();
			IViewReference view = page.findViewReference(View.ID);
			if(view != null)
				page.setPartState(view, IWorkbenchPage.STATE_MINIMIZED);
			++openedEditorNumber;
		}
		partActivated(partRef);
	}

	@Override
	public void partHidden(IWorkbenchPartReference partRef) {}

	@Override
	public void partVisible(IWorkbenchPartReference partRef) {}

	@Override
	public void partInputChanged(IWorkbenchPartReference partRef) {}

}
