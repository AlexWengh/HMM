package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.TreeNode;
import hmm.automation.models.Variable;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class VariablePage extends AbstractPage {
	
	private Text name;
	private Text value;
	private Variable variable;
	
	public VariablePage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		variable = (Variable) selection;
	}
	
	@Override
	public void onCreateContents(Composite parent) {		
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Variable", "Define a Variable");
		
		Label nameLabel = toolkit.createLabel(client, "Name  ");
		nameLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		name = toolkit.createText(client, "", SWT.SINGLE);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label valueLabel = toolkit.createLabel(client, "Value  ");
		valueLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		value = toolkit.createText(client, "", SWT.SINGLE);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		name.addFocusListener(getFocusListener());
		value.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(variable, null);
	}

	@Override
	public void setWidgetValues() {
		name.setText(variable.getName());
		value.setText(variable.getValue());
	}

	@Override
	public void setModelValues() {
		String oldName = variable.getName();
		variable.setName(name.getText());
		variable.setValue(value.getText());
		variable.setValid(true);
		if(variable.getName().isEmpty()) {
			variable.setValid(false);
		} else {
			for (Variable var : Variable.getVariableList(variable)) {
				if(var.getName().equals(variable.getName())) {
					variable.setValid(false);
					return;
				}
			}
		}
		if(!oldName.isEmpty() && variable.getName().isEmpty()) {
			TreeViewer viewer = automationBlock.getTreeViewer();
			variable.refreshAffectedNodes(viewer);
		}
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!variable.isValid())
			form.getMessageManager().addMessage(variable, variable.getInvalidMessage(), null, IMessageProvider.ERROR, name);
		else
			form.getMessageManager().removeMessage(variable, name);
	}

}
