package hmm.build.console;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.ui.part.IPageSite;

public class ConsolePageParticipant implements IConsolePageParticipant {

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public void init(IPageBookViewPage page, IConsole console) {
		IPageSite pageSite = page.getSite();
		IWorkbenchPage workbenchPage = pageSite.getPage();
		IViewPart viewPart = workbenchPage.findView(IConsoleConstants.ID_CONSOLE_VIEW);
		IViewSite viewSite = viewPart.getViewSite();
		IActionBars actionBars = viewSite.getActionBars();
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		IContributionItem[] items = toolBarManager.getItems();
		for(int i = 0; i < items.length; ++i) {
			IContributionItem item = items[i];
			if(item instanceof ActionContributionItem) {
				IAction action = ((ActionContributionItem) item).getAction();
				String text = action.getText();
				if(text.equals("Pi&n Console") || text.equals("Open Console"))
					toolBarManager.remove(item);
			}
		}
	}

	@Override
	public void dispose() {}

	@Override
	public void activated() {}

	@Override
	public void deactivated() {}

}
