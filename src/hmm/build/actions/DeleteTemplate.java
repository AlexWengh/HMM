package hmm.build.actions;

import hmm.build.settings.TemplateVariable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.TableItem;

public class DeleteTemplate extends Action {
	
	private TableViewer viewer;

	public DeleteTemplate(TableViewer templateViewer) {
		setText("Delete");
		viewer = templateViewer;
	}
	
	@Override
	public void run() {
		TableItem[] selItems = viewer.getTable().getSelection();
		if(selItems.length > 0) {
			String name = selItems[0].getText(0);
			if(name.equals(TemplateVariable.HMM_DIR) || name.equals(TemplateVariable.BUILD_DIR))
				return;
			TemplateVariable.getInstance().removeTempateVariable(name);
			viewer.refresh();
		}
	}

}
