package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Loop;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class LoopPage extends AbstractPage {

	private Text countText;
	private Loop loop;
	
	public LoopPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		loop = (Loop) selection;
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		final Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Loop", "Loop some times");
		Label label = toolkit.createLabel(client, "Loop Times  ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		countText = toolkit.createText(client, "", SWT.SINGLE);
		countText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		countText.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(loop, null);
	}

	@Override
	public void setWidgetValues() {
		countText.setText(String.valueOf(loop.getCount()));
	}

	@Override
	public void setModelValues() {
		String countStr = countText.getText();
		int value = -1;
		boolean valid = true;
		try {
			value = Integer.parseInt(countStr);
		} catch (NumberFormatException e) {
			valid = false;
		}
		if(value <= 0 || value >= 65536) {
			valid = false;
			value = 0;
		}
		loop.setCount(value);
		loop.setValid(valid);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!loop.isValid())
			form.getMessageManager().addMessage(loop, loop.getInvalidMessage(), null, IMessageProvider.ERROR, countText);
		else
			form.getMessageManager().removeMessage(loop, countText);
	}

}
