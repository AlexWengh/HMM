package hmm.build.actions;

import hmm.build.settings.CommandLines;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;

public class DeleteCommand extends Action {
	
	private TableViewer viewer;

	public DeleteCommand(TableViewer commandsViewer) {
		setText("Delete");
		viewer = commandsViewer;
	}
	
	@Override
	public void run() {
		int index = viewer.getTable().getSelectionIndex();
		if(index != -1) {
			CommandLines.getInstance().removeCommand(index);
			viewer.refresh();
		}
	}

}
