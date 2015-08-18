package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.TreeNode;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

public abstract class AbstractPage implements IDetailsPage, IPageInterface {
	
	protected AutomationBlock automationBlock;
	protected IManagedForm form;
	protected FocusListener focusListener;
	
	public AbstractPage(AutomationBlock automationBlock) {
		this.automationBlock = automationBlock;
	}

	@Override
	public void initialize(IManagedForm form) {
		this.form = form;
	}

	@Override
	public void dispose() {}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public void commit(boolean onSave) {}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {}

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		automationBlock.setShowingPage(this);
		IStructuredSelection ss = (IStructuredSelection) selection;
		if(!ss.isEmpty()) {
			TreeNode node = (TreeNode) ss.getFirstElement();
			setTreeModelFromSelection(node);
			setWidgetValues();
			addRemoveValidatorMessages();
		}
	}

	@Override
	public void createContents(Composite parent) {
		onCreateContents(parent);
	}
	
	public FocusListener getFocusListener() {
		if(focusListener == null) {
			focusListener = new FocusListener() {
				@Override
				public void focusLost(FocusEvent e) {
					setModelValues();
					addRemoveValidatorMessages();
					updateTreeViewerElement();
				}
				
				@Override
				public void focusGained(FocusEvent e) {}
			};
		}
		return focusListener;
	}
	
	abstract public void setTreeModelFromSelection(TreeNode selection);
	
	abstract public void onCreateContents(Composite parent);
	
	abstract public void updateTreeViewerElement();

}
