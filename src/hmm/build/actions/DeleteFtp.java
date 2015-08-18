package hmm.build.actions;

import hmm.build.settings.FtpSetting;
import hmm.build.settings.Settings;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;

public class DeleteFtp extends Action {
	
	private ListViewer viewer;
	
	public DeleteFtp(ListViewer viewer) {
		this.viewer = viewer;
		setText("Delete");
	}
	
	@Override
	public void run() {
		IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		if(selection != null) {
			FtpSetting model = (FtpSetting)selection.getFirstElement();
			if(model != null) {
				Settings.getInstance().removeFtpSettings(model);
				viewer.refresh();
			}
		}
	}
}
