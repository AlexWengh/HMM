package hmm.automation.actions;

import hmm.automation.models.LogicTreeNode;

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

public class AddLogicAction extends AbstractAddAction {
	
	public AddLogicAction(ToolBar bar) {
		super(bar, "Logic");
	}
	
	protected List<CommandContributionItem> getContrbutionItems() {
		List<CommandContributionItem> items = new ArrayList<CommandContributionItem>();
		IWorkbench workbench = PlatformUI.getWorkbench();
		
		CommandContributionItemParameter param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.variable", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		Map<String, String> map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.variable", "true");
		param.parameters = map;
		CommandContributionItem item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.print", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.print", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.set", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.set", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.if", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.if", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.else", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.else", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
		param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.loop", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.loop", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item);
		
	/*	To Do: Work out when will the class element work correctly? Is it needed since the class itself already contains all logic?
	 *  param = new CommandContributionItemParameter(
				workbench, null, "hmm.automation.command.class", SWT.PUSH);
		param.icon = LogicTreeNode.getImageDescriptor();
		map = new HashMap<String, String>();
		map.put("hmm.automation.command.parameter.class", "true");
		param.parameters = map;
		item = new CommandContributionItem(param);
		items.add(item); */
		
		return items;
	}

}
