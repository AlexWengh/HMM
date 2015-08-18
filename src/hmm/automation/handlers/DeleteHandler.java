package hmm.automation.handlers;

import hmm.automation.AutomationBlock;
import hmm.automation.AutomationEditor;
import hmm.automation.AutomationPage;
import hmm.automation.models.TreeNode;
import hmm.automation.pages.IPageInterface;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TreeViewer viewer = new AutomationHandlerUtil().getTreeViewer(event);
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if(!selection.isEmpty()) {
			TreeNode node = (TreeNode) selection.getFirstElement();
			AutomationEditor editor = (AutomationEditor) HandlerUtil.getActiveEditor(event);
			AutomationPage page = (AutomationPage) editor.getActivePageInstance();
			AutomationBlock block = page.getAutomationBlock();
			node.setValid(true);
			((IPageInterface) block.getShowingPage()).addRemoveValidatorMessages();
			node.getParent().removeChild(node);
			viewer.refresh();
			viewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
		}
		return null;
	}

}
