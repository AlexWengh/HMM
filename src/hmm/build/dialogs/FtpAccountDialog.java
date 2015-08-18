package hmm.build.dialogs;

import hmm.build.settings.FtpSetting;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FtpAccountDialog extends TitleAreaDialog {

	private Text user;
	private Text pass;
	private FtpSetting model;
	
	public FtpSetting getFtpModel() {
		return (FtpSetting) model.clone();
	}
	
	public void setFtpModel(FtpSetting model) {
		this.model = (FtpSetting) model.clone();
	}
	
	
	public FtpAccountDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void okPressed() {
		String nameStr = user.getText().trim();
		String passStr = pass.getText().trim();
		if(nameStr.isEmpty() || passStr.isEmpty())
			setErrorMessage("User Name & Password must not be empty.");
		else {
			model = new FtpSetting(nameStr, passStr);
			super.okPressed();
		}
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent =  (Composite) super.createDialogArea(parent);
		setTitle();
		createArea(parent);
		return parent;
	}
	
	protected void setTitle() {
		setTitle("FTP Account");
		setMessage("Create/Modify a FTP Account.");
	}

	protected void createArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Label label = new Label(area, SWT.LEFT);
		label.setText("Account User Name:");
		user = new Text(area, SWT.BORDER | SWT.SINGLE);
		user.setText(model == null ? "" : model.getUser());
		user.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		label = new Label(area, SWT.LEFT);
		label.setText("Account Password:");
		pass = new Text(area, SWT.BORDER | SWT.SINGLE | SWT.PASSWORD);
		pass.setText(model == null ? "" : model.getPass());
		pass.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}

}
