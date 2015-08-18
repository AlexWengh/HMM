package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Set;
import hmm.automation.models.TreeNode;
import hmm.automation.models.Variable;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class SetPage extends AbstractPage {
	
	private Combo variableName;
	private Text value;
	private Set set;
	
	public SetPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		set = (Set) selection;
		variableName.removeAll();
		for (Variable var : Variable.getVariableList(set))
			variableName.add(var.getName());
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Set", "Set value of a Variable");
		
		Label variableLabel = toolkit.createLabel(client, "Variable Name  ");
		variableLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		variableName = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		variableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label valueLabel = toolkit.createLabel(client, "Value  ");
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		value = toolkit.createText(client, "", SWT.SINGLE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		variableName.addFocusListener(getFocusListener());
		value.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(set, null);
	}

	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : variableName.getItems()) {
			String name = set.getVariable() == null ? "" : set.getVariable().getName();
			if(str.equals(name)) {
				variableName.select(index);
				break;
			}
			index++;
		}
		value.setText(set.getValue());
	}

	@Override
	public void setModelValues() {
		set.setVariable(set.getVariable(variableName.getText()));
		String valueStr = value.getText().trim();
		set.setValue(valueStr);
		if(set.getVariable() == null || valueStr.isEmpty())
			set.setValid(false);
		else
			set.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!set.isValid()) {
			if(set.getVariable() == null)
				form.getMessageManager().addMessage(set, set.getInvalidMessage(), null, IMessageProvider.ERROR, variableName);
			else
				form.getMessageManager().addMessage(set, set.getInvalidMessage(), null, IMessageProvider.ERROR, value);
		}
		else {
			form.getMessageManager().removeMessage(set, variableName);
			form.getMessageManager().removeMessage(set, value);
		}
	}

}
