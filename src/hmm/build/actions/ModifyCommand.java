package hmm.build.actions;

import hmm.build.dialogs.CommandDialog;
import hmm.build.settings.CommandLines;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class ModifyCommand extends Action {
	
	private TableViewer viewer;

	public ModifyCommand(TableViewer commandsViewer) {
		setText("Modify");
		viewer = commandsViewer;
	}

	@Override
	public void run() {
		int index = viewer.getTable().getSelectionIndex();
		if(index != -1) {
			CommandDialog dialog = new CommandDialog(viewer.getTable().getShell());
			TableItem[] selItems = viewer.getTable().getSelection();
			dialog.setCommand(selItems[0].getText());
			if(IDialogConstants.OK_ID == dialog.open()) {
				String command = dialog.getCommand();
				CommandLines.getInstance().modifyCommand(index, command);
				viewer.refresh();
			}
		}
	}

}
