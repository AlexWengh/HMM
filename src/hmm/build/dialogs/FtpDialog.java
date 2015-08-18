package hmm.build.dialogs;

import hmm.build.actions.DeleteFtp;
import hmm.build.actions.ModifyFtp;
import hmm.build.settings.FtpSetting;
import hmm.build.util.IPAddressValidator;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class FtpDialog extends TitleAreaDialog {
	
	private ListViewer viewer;
	private Text ftpIp;
	private String ftpIpStr = "";
	private Text ftpPort;
	private String ftpPortStr = "";
	List<FtpSetting> ftpUsers = new ArrayList<FtpSetting>();
	
	public String getFtpIpStr() {
		return ftpIpStr;
	}

	public void setFtpIpStr(String ftpIp) {
		this.ftpIpStr = ftpIp;
	}

	public int getFtpPortStr() {
		return Integer.parseInt(ftpPortStr);
	}

	public void setFtpPortStr(int ftpPort) {
		this.ftpPortStr = String.valueOf(ftpPort);
	}

	public List<FtpSetting> getFtpUsers() {
		return ftpUsers;
	}

	public void setFtpUsers(List<FtpSetting> ftpUsers) {
		this.ftpUsers = ftpUsers;
	}

	public FtpDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	@Override
	protected void okPressed() {
		ftpIpStr = ftpIp.getText().trim();
		if(new IPAddressValidator().validate(ftpIpStr) == false) {
			setErrorMessage("FTP IP Address is not valid.");
			return;
		}
		ftpPortStr = ftpPort.getText().trim();
		int ftpPortInt = 0;
		try {
			ftpPortInt = Integer.valueOf(ftpPortStr);
		} catch (NumberFormatException e) {
			setErrorMessage("FTP port is not a number");
			return;
		}
		if(ftpPortInt <= 0 || ftpPortInt >= 65535) {
			setErrorMessage("FTP Port is not valid.");
			return;
		}
		super.okPressed();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent =  (Composite) super.createDialogArea(parent);
		setTitle();
		createArea(parent);
		return parent;
	}
	
	protected void setTitle() {
		setTitle("FTP Configuration");
		setMessage("Set the configured FTP User/Pass here for HanMeiMei clients.");
	}

	protected void createArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Enter FTP IP address:");
		ftpIp = new Text(area, SWT.BORDER | SWT.SINGLE);
		ftpIp.setText(ftpIpStr);
		ftpIp.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		label = new Label(area, SWT.LEFT);
		label.setText("Enter FTP Port:");
		ftpPort = new Text(area, SWT.BORDER | SWT.SINGLE);
		ftpPort.setText(ftpPortStr);
		ftpPort.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		label = new Label(area, SWT.LEFT);
		label.setText("Manage FTP accounts:");
		viewer = new ListViewer(area, SWT.BORDER | SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL);
		viewer.getList().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			@Override
			public void dispose() {}
			@Override
			public Object[] getElements(Object inputElement) {
				@SuppressWarnings("unchecked")
				List<FtpSetting> users = (List<FtpSetting>) inputElement;
				return users.toArray();
			}
		});
		viewer.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				FtpSetting model = (FtpSetting) element;
	            return model.getUser();
	        }
			@Override
	        public Image getImage(Object element) {
	            return null;
	        }
		});
		viewer.setInput(ftpUsers);
		
		MenuManager menuManager = new MenuManager();
        menuManager.add(new ModifyFtp(viewer));
        menuManager.add(new DeleteFtp(viewer));
        Menu menu = menuManager.createContextMenu(viewer.getList());
        viewer.getList().setMenu(menu);
		
		Button add = new Button(area, SWT.LEFT);
		add.setText("Add FTP Account...");
		add.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FtpAccountDialog dialog = new FtpAccountDialog(area.getShell());
				if(IDialogConstants.OK_ID == dialog.open()) {
					ftpUsers.add(dialog.getFtpModel());
					viewer.refresh();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	@Override
	protected Point getInitialSize() {
		return new Point(500, 600);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}
}
