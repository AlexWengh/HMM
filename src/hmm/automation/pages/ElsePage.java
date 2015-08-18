package hmm.automation.pages;

import java.util.List;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Else;
import hmm.automation.models.If;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ElsePage extends AbstractPage {
	
	private Else elses;
	private Label elseLabel;
	
	public ElsePage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		elses = (Else) selection;
	}
	
	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Else", "The Else block");
		
		elseLabel = toolkit.createLabel(client, "There is no settings related to this operation.");
		elseLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(elses, null);
	}

	@Override
	public void setWidgetValues() {
		setModelValues();
	}

	@Override
	public void setModelValues() {
		TreeNode parent = elses.getParent();
		List<TreeNode> children = parent.getChildren();
		int size = children.size();
		int i = 0;
		boolean isValid = false;
		for(i = 0; i < size; ++i) {
			TreeNode sub = children.get(i);
			if( (sub instanceof If) && sub.isValid() && 
					(i + 1 < size) && children.get(i + 1) == elses) {
				isValid = true;
				break;
			} else if(sub == elses) {
				break;
			}
		}
		elses.setValid(isValid);
		updateTreeViewerElement();
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!elses.isValid())
			form.getMessageManager().addMessage(elses, elses.getInvalidMessage(), null, IMessageProvider.ERROR, elseLabel);
		else
			form.getMessageManager().removeMessage(elses, elseLabel);
	}

}
