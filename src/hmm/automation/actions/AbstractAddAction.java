package hmm.automation.actions;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.menus.CommandContributionItem;

public abstract class AbstractAddAction extends Action implements IMenuCreator {

	protected MenuManager manager;
	protected ToolBar bar;
	
	public AbstractAddAction(ToolBar bar, String name) {
		super(name, IAction.AS_DROP_DOWN_MENU);
		this.bar = bar;
		setMenuCreator(this);
	}
	
	@Override
	public void run() {
		createMenu((Control) bar);
		Menu menu =  manager.getMenu();
		menu.setVisible(true);
	}
	
	@Override
	public void dispose() {}

	@Override
	public Menu getMenu(Control parent) {
		createMenu(parent);
		Menu menu =  manager.getMenu();
		return menu;
	}

	private void createMenu(Control parent) {
		if(manager == null) {
			manager = new MenuManager();
			List<CommandContributionItem> items = getContrbutionItems();
			for (CommandContributionItem item : items)
				manager.add(item);
			manager.createContextMenu(parent);
		}
	}

	@Override
	public Menu getMenu(Menu parent) {
		return null;
	}
	
	protected abstract List<CommandContributionItem> getContrbutionItems();
}
