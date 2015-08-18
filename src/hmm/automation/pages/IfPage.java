package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Else;
import hmm.automation.models.If;
import hmm.automation.models.If.CompareMethod;
import hmm.automation.models.TreeNode;
import hmm.automation.models.Variable;
import hmm.automation.util.PageUtil;

import java.util.List;
import java.util.Map.Entry;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class IfPage extends AbstractPage {

	private Combo leftVariableName;
	private Combo method;
	private Text rightValue;
	private If iff;
	
	public IfPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}
	
	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		iff = (If) selection;
		leftVariableName.removeAll();
		for (Variable var : Variable.getVariableList(iff))
			leftVariableName.add(var.getName());
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "If", "If logic judgement");
		
		Label leftLabel = toolkit.createLabel(client, "Variable Name  ");
		leftLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		leftVariableName = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		leftVariableName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		Label methodLabel = toolkit.createLabel(client, "Method  ");
		methodLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		method = new Combo(client, SWT.DROP_DOWN | SWT.READ_ONLY);
		method.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		for (Entry<CompareMethod, String> entry : If.getMethodMap().entrySet())
			method.add(entry.getValue());
		
		Label rightLabel = toolkit.createLabel(client, "Value  ");
		rightLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		rightValue = toolkit.createText(client, "", SWT.SINGLE);
		rightValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		leftVariableName.addFocusListener(getFocusListener());
		method.addFocusListener(getFocusListener());
		rightValue.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(iff, null);
	}

	@Override
	public void setWidgetValues() {
		int index = 0;
		for (String str : leftVariableName.getItems()) {
			String name = iff.getvariableLeft() == null ? "" : iff.getvariableLeft().getName();
			if(str.equals(name)) {
				leftVariableName.select(index);
				break;
			}
			index++;
		}
		index = 0;
		for (String str : method.getItems()) {
			if(str.equals(If.getMethodMap().get(iff.getMethod()))) {
				method.select(index);
				break;
			}
			index++;
		}
		rightValue.setText(iff.getValueRight());
	}

	@Override
	public void setModelValues() {
		iff.setVariableLeft(iff.getVariable(leftVariableName.getText()));
		iff.setMethod(iff.getMethod(method.getText()));
		String value = rightValue.getText().trim();
		iff.setValueRight(value);
		if(iff.getvariableLeft() == null || value.isEmpty())
			iff.setValid(false);
		else
			iff.setValid(true);

		if(iff.isValid()) {
			List<TreeNode> children = iff.getParent().getChildren();
			int size = children.size();
			for(int i = 0; i < size; ++i) {
				TreeNode child = children.get(i);
				if(child == iff && (i + 1 < size)) {
					TreeNode next = children.get(i + 1);
					if( (next instanceof Else) && !next.isValid() ) {
						next.setValid(true);
						automationBlock.getTreeViewer().update(next, null);
					}
					break;
				}
			}
		}
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!iff.isValid())
			form.getMessageManager().addMessage(iff, iff.getInvalidMessage(), null, IMessageProvider.ERROR, rightValue);
		else
			form.getMessageManager().removeMessage(iff, rightValue);
	}

}
