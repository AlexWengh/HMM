package hmm.automation.actions;

import hmm.automation.models.WebTreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

public class AddWebAction extends AbstractAddAction {

	public AddWebAction(ToolBar bar) {
		super(bar, "Web");
	}
	
	protected List<CommandContributionItem> getContrbutionItems() {
		List<CommandContributionItem> items = new ArrayList<CommandContributionItem>();
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		CommandContributionItemParameter param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.find", SWT.PUSH);
		param.icon = WebTreeNode.getImageDescriptor();
		Map<String, String> map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.find", "true");
		param.parameters = map;
		CommandContributionItem item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.navigate", SWT.PUSH);
		param.icon = WebTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.navigate", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.wait", SWT.PUSH);
		param.icon = WebTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.wait", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
	/*	To Do: test the switch element, when will it take effect?
	 * 	param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.switch", SWT.PUSH);
		param.icon = WebTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.switch", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item); */
		
		return items;
	}
}
