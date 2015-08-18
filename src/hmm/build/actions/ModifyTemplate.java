package hmm.build.actions;

import hmm.build.dialogs.TemplateVariableDialog;
import hmm.build.settings.TemplateVariable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class ModifyTemplate extends Action {

	private TableViewer viewer;
	
	public ModifyTemplate(TableViewer templateViewer) {
		setText("Modify");
		viewer = templateViewer;
	}
	
	@Override
	public void run() {
		TableItem[] selItems = viewer.getTable().getSelection();
		if(selItems.length > 0) {
			TemplateVariableDialog dialog = new TemplateVariableDialog(viewer.getTable().getShell());
			dialog.setName(selItems[0].getText(0));
			dialog.setValue(selItems[0].getText(1));
			dialog.disableName();
			if(IDialogConstants.OK_ID == dialog.open()) {
				String name = dialog.getName();
				String value = dialog.getValue();
				TemplateVariable.getInstance().modifyTemplateVariable(name, value);
				viewer.refresh();
			}
		}
	}

}
