package hmm.automation;

import hmm.automation.models.Root;
import hmm.automation.models.TreeNode;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class AutomationEditorInput implements IEditorInput {
	
	private static int num = 1;
	
	private String name = "Untitled_";

	private TreeNode model;
	
	public AutomationEditorInput() {
		this.name = name + String.valueOf(num);
		num++;
		model = new Root();
	}
	
	public AutomationEditorInput(String name, TreeNode model) {
		this.name = name;
		this.model = model;
	}
	
	public TreeNode getRootModel() {
		return model;
	}

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return true;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
				ISharedImages.IMG_OBJ_ELEMENT);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return name;
	}

}
