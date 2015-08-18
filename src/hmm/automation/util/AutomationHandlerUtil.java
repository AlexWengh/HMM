package hmm.automation.util;

import hmm.automation.AutomationEditor;
import hmm.automation.AutomationEditorInput;
import hmm.automation.models.TreeNode;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class AutomationHandlerUtil {
	
	public TreeViewer getTreeViewer(ExecutionEvent event) {
		AutomationEditor editor = (AutomationEditor) HandlerUtil.getActiveEditor(event);
		TreeViewer viewer = editor.getViewer();
		return viewer;
	}
	
	public TreeViewer getTreeViewer() { 
		AutomationEditor editor = (AutomationEditor) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		TreeViewer viewer = editor.getViewer();
		return viewer;
	}
	
	public void addNodeToTreeViewer(ExecutionEvent event, TreeNode node, String parameterName) throws ExecutionException {
		TreeViewer viewer = getTreeViewer(event);
		String paramValue = event.getParameter(parameterName);
		if(paramValue != null) {
			if(paramValue.equals("true")) {
				AutomationEditorInput input = (AutomationEditorInput) viewer.getInput();
				TreeNode root = input.getRootModel();
				root.addChild(node);
				viewer.refresh();
				viewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
			}
		} else {
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if(!selection.isEmpty()) {
				TreeNode parent = (TreeNode) selection.getFirstElement();
				parent.addChild(node);
				viewer.refresh();
				viewer.expandToLevel(node, TreeViewer.ALL_LEVELS);
			}
		}
	}

}
