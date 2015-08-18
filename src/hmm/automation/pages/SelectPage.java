package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Select;
import hmm.automation.models.Select.SelectMethod;
import hmm.automation.models.Select.SelectOperation;
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

public class SelectPage extends AbstractPage {
	
	private Combo operation;
	private Combo method;
	private Text value;
	private Select select;
	
	public SelectPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		select = (Select) selection;
	}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Select", "Select an option or checkbox");
		
		Label operatonLabel = toolkit.createLabel(client, "Operation  ");
		operatonLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		operation = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		operation.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		for (Entry<SelectOperation, String> entry : Select.getOperationMap().entrySet()) {
			operation.add(entry.getValue());
		}
		
		Label methodLabel = toolkit.createLabel(client, "Method  ");
		methodLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		method = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		method.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		for (Entry<SelectMethod, String> entry : Select.getMethodMap().entrySet()) {
			method.add(entry.getValue());
		}
		
		Label valueLabel = toolkit.createLabel(client, "Value  ");
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		value = toolkit.createText(client, "", SWT.SINGLE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		operation.addFocusListener(getFocusListener());
		method.addFocusListener(getFocusListener());
		value.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(select, null);
	}
	
	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : operation.getItems()) {
			if(str.equals(Select.getOperationMap().get(select.getOperation()))) {
				operation.select(index);
				break;
			}
			index++;
		}
		index = 0;
		for (String str : method.getItems()) {
			if(str.equals(Select.getMethodMap().get(select.getMethod()))) {
				method.select(index);
				break;
			}
			index++;
		}
		value.setText(select.getValue());
	}
	
	@Override
	public void setModelValues() {
		String val = value.getText().trim();
		for (Entry<SelectOperation, String> entry : Select.getOperationMap().entrySet()) {
			if(entry.getValue().equals(operation.getText())) {
				select.setOperation(entry.getKey());
				break;
			}
		}
		for (Entry<SelectMethod, String> entry : Select.getMethodMap().entrySet()) {
			if(entry.getValue().equals(method.getText())) {
				select.setMethod(entry.getKey());
				break;
			}
		}
		select.setValue(val);
		
		select.setValid(true);
		if(val.isEmpty())
			select.setValid(false);
		else {
			if(method.getText().equals(Select.getMethodMap().get(SelectMethod.INDEX))) {
				try {
					int index = Integer.parseInt(val);
					if(index < 0)
						select.setValid(false);
				} catch (NumberFormatException e) {
					select.setValid(false);
				}
			}
		}
	}
	
	@Override
	public void addRemoveValidatorMessages() {
		if(!select.isValid())
			form.getMessageManager().addMessage(select, select.getInvalidMessage(), null, IMessageProvider.ERROR, value);
		else
			form.getMessageManager().removeMessage(select, value);
	}

}
