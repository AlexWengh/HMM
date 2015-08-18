package hmm.automation;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;


public class AutomationEditor extends FormEditor {
	
	public static final String ID = "HMM.automation.editor";
	
	private AutomationPage page;

	public AutomationEditor() {}

	@Override
	protected void addPages() {
		try {
			page = new AutomationPage(this);
			addPage(page);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	public TreeViewer getViewer() {
		return page.getViewer();
	}

}
