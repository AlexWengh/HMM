package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Switch;
import hmm.automation.models.Switch.SwitchMethod;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SwitchPage extends AbstractPage {

	private Combo method;
	private Text value;
	private Switch switchTo;
	
	public SwitchPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		switchTo = (Switch) selection;
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Switch", "Switch to another frame or window");
		
		Label methodLabel = toolkit.createLabel(client, "Method  ");
		methodLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		method = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		method.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		for (Entry<SwitchMethod, String> entry : Switch.getMethodMap().entrySet()) {
			method.add(entry.getValue());
		}
		
		Label valueLabel = toolkit.createLabel(client, "Value  ");
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		value = toolkit.createText(client, "", SWT.SINGLE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		method.addFocusListener(getFocusListener());
		value.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(switchTo, null);
	}

	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : method.getItems()) {
			if(str.equals(Switch.getMethodMap().get(switchTo.getMethod()))) {
				method.select(index);
				break;
			}
			index++;
		}
		value.setText(switchTo.getValue());
	}

	@Override
	public void setModelValues() {
		String val = value.getText().trim();
		switchTo.setMethod(switchTo.getMethod(method.getText()));
		switchTo.setValue(val);
		if(val.isEmpty())
			switchTo.setValid(false);
		else
			switchTo.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!switchTo.isValid())
			form.getMessageManager().addMessage(switchTo, switchTo.getInvalidMessage(), null, IMessageProvider.ERROR, value);
		else
			form.getMessageManager().removeMessage(switchTo, value);
	}

}
