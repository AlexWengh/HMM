package hmm.build.actions;

import hmm.build.dialogs.FtpAccountDialog;
import hmm.build.settings.FtpSetting;
import hmm.build.settings.Settings;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;

public class ModifyFtp extends Action {
	
	private ListViewer viewer;
	
	public ModifyFtp(ListViewer viewer) {
		this.viewer = viewer;
		setText("Modify");
	}
	
	@Override
	public void run() {
		int index = viewer.getList().getSelectionIndex();
		if(index != -1) {
			FtpSetting model = (FtpSetting) ((IStructuredSelection) viewer.getSelection()).getFirstElement();
			FtpAccountDialog dialog = new FtpAccountDialog(viewer.getList().getShell());
			dialog.setFtpModel(model);
			if(IDialogConstants.OK_ID == dialog.open()) {
				model = dialog.getFtpModel();
				Settings.getInstance().modifyFtpSettings(index, model);
				viewer.refresh();
			}
		}
	}
}
