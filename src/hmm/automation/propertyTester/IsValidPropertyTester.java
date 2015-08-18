package hmm.automation.propertyTester;

import hmm.automation.models.TreeNode;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

public class IsValidPropertyTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args,
							Object expectedValue) {
		if(property.equals("isValid")) {
			TreeViewer viewer = new AutomationHandlerUtil().getTreeViewer();
			IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
			if(!selection.isEmpty()) {
				TreeNode node = (TreeNode) selection.getFirstElement();
				return node.isValid();
			}
		}
		return false;
	}

}
