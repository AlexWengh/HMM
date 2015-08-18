package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Print;
import hmm.automation.models.TreeNode;
import hmm.automation.models.Variable;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class PrintPage extends AbstractPage {
	
	private Combo variableName;
	private Print print;
	
	public PrintPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		print = (Print) selection;
		variableName.removeAll();
		for (Variable var : Variable.getVariableList(print))
			variableName.add(var.getName());
		if(print.getVariable() != null) {
			print.setValid(true);
			automationBlock.getTreeViewer().update(print, null);
		}
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Print", "Print value of a variable");
		Label label = toolkit.createLabel(client, "Variable Name  ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		variableName = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		variableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		variableName.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(print, null);
	}

	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : variableName.getItems()) {
			String name = print.getVariable() == null ? "" : print.getVariable().getName();
			if(str.equals(name)) {
				variableName.select(index);
				break;
			}
			index++;
		}
	}

	@Override
	public void setModelValues() {
		print.setVariable(print.getVariable(variableName.getText()));
		if(print.getVariable() == null)
			print.setValid(false);
		else
			print.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!print.isValid())
			form.getMessageManager().addMessage(print, print.getInvalidMessage(), null, IMessageProvider.ERROR, variableName);
		else
			form.getMessageManager().removeMessage(print, variableName);
	}

}
