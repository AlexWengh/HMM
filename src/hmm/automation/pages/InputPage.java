package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Input;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class InputPage extends AbstractPage {

	private Text value;
	private Input input;
	
	public InputPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		input = (Input) selection;
	}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Input", "Input text");
				
		Label valueLabel = toolkit.createLabel(client, "Value  ");
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		value = toolkit.createText(client, "", SWT.SINGLE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		value.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(input, null);
	}

	@Override
	public void setWidgetValues() {
		value.setText(input.getInputText());
	}

	@Override
	public void setModelValues() {
		String val = value.getText().trim();
		input.setInputText(val);
		if(val.isEmpty())
			input.setValid(false);
		else
			input.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!input.isValid())
			form.getMessageManager().addMessage(input, input.getInvalidMessage(), null, IMessageProvider.ERROR, value);
		else
			form.getMessageManager().removeMessage(input, value);
	}

}
