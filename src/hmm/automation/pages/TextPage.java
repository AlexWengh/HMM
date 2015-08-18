package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Text;
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

public class TextPage extends AbstractPage {
	
	private Combo variableName;
	private Text text;

	public TextPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}
	
	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		text = (Text) selection;
		variableName.removeAll();
		for (Variable var : Variable.getVariableList(text))
			variableName.add(var.getName());
		if(text.getVariable() != null) {
			text.setValid(true);
			automationBlock.getTreeViewer().update(text, null);
		}
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Text", "Fetch current Web Element's Text value into a Variable");
		Label label = toolkit.createLabel(client, "Variable Name  ");
		label.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		variableName = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		variableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		variableName.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(text, null);
	}

	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : variableName.getItems()) {
			String name = text.getVariable() == null ? "" : text.getVariable().getName();
			if(str.equals(name)) {
				variableName.select(index);
				break;
			}
			index++;
		}
	}

	@Override
	public void setModelValues() {
		text.setVariable(text.getVariable(variableName.getText()));
		if(text.getVariable() == null)
			text.setValid(false);
		else
			text.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!text.isValid())
			form.getMessageManager().addMessage(text, text.getInvalidMessage(), null, IMessageProvider.ERROR, variableName);
		else
			form.getMessageManager().removeMessage(text, variableName);
	}

}
