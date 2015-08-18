package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ClickPage extends AbstractPage {

	public ClickPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Click", "Click a web element");
		
		Label clickLabel = toolkit.createLabel(client, "There is no settings related to this operation.");
		clickLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
	}
	
	@Override
	public void updateTreeViewerElement() {}

	@Override
	public void setWidgetValues() {}

	@Override
	public void setModelValues() {}

	@Override
	public void addRemoveValidatorMessages() {}

}
