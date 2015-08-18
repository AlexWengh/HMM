package hmm.automation;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public class AutomationPage extends FormPage {
	
	private AutomationBlock block;
	
	public AutomationPage(FormEditor editor) {
		super(editor, "hmm.automation.AutomationPage", editor.getEditorInput().getName());
		block = new AutomationBlock(this);
	}
	
	protected void createFormContent(final IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		form.setText("HanMeiMei Automation");
		toolkit.decorateFormHeading(form.getForm());
		form.getForm().addMessageHyperlinkListener(new HyperlinkAdapter());
		form.getMessageManager().setDecorationPosition(SWT.RIGHT | SWT.BOTTOM);
		block.createContent(managedForm);
	}

	public TreeViewer getViewer() {
		return block.getTreeViewer();
	}

	public AutomationBlock getAutomationBlock() {
		return block;
	}
	
}
