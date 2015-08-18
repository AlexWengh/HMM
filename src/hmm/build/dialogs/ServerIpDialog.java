package hmm.build.dialogs;

import hmm.build.ftp.FtpInfo;
import hmm.build.sockets.SocketClient;
import hmm.build.util.IPAddressValidator;

import java.io.IOException;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class ServerIpDialog extends TitleAreaDialog {

	private Text ip;
	private String ipStr = "";
	Button button;
	ProgressBar progress;
	ProgressBar progressComplete;
	Text testResult;
	Composite stack;
	StackLayout stackLayout;
	Composite pageProgress;
	Composite pageProgressComplete;
	private static final String IP_NOT_VALID = "The inputted IP address is not valid.";
	
	public ServerIpDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public String getIp() {
		return ipStr;
	}
	
	public void setIp(String ip) {
		ipStr = ip;
	}
	
	@Override
	protected void okPressed() {
		ipStr = ip.getText().trim();
		if(new IPAddressValidator().validate(ipStr)) {
			super.okPressed();
		} else {
			setErrorMessage(IP_NOT_VALID);
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
		setTitle("Server IP");		
		setMessage("Set the HanMeiMei Server IP where HanMeiMei client connected to.");
	}

	protected void createArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Enter the IP address:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));
		ip = new Text(area, SWT.BORDER | SWT.SINGLE);
		ip.setText(ipStr);
		ip.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		button = new Button(area, SWT.PUSH);
		button.setText("Test Connection");
		button.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		
		stack = new Composite(area, SWT.NONE);
		stackLayout = new StackLayout();
		stack.setLayout(stackLayout);
		stack.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		pageProgress = new Composite(stack, SWT.NONE);
		pageProgress.setLayout(new RowLayout());
		pageProgressComplete = new Composite(stack, SWT.NONE);
		pageProgressComplete.setLayout(new RowLayout());
		
		progress = new ProgressBar(pageProgress, SWT.INDETERMINATE);
		progress.setVisible(false);
		progress.setToolTipText("Testing connection, please wait...");
		progress.setLayoutData(new RowData());
		
		progressComplete = new ProgressBar(pageProgressComplete, SWT.HORIZONTAL);
		progressComplete.setSelection(progressComplete.getMaximum());
		progressComplete.setVisible(false);
		progressComplete.setLayoutData(new RowData());
		
		testResult = new Text(area, SWT.LEFT | SWT.MULTI | SWT.WRAP);
		testResult.setEditable(false);
		testResult.setBackground(label.getBackground());
		testResult.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		testResult.setText("");
		testResult.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_RED));
		
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ipStr = ip.getText().trim();
				if(new IPAddressValidator().validate(ipStr) == false) {
					setErrorMessage(IP_NOT_VALID);
					return;
				}
				setErrorMessage(null);
				testResult.setText("");
				disableAll();
				Runnable test = new Runnable() {
					@Override
					public void run() {
						SocketClient client = new SocketClient();
						client.setServerIp(ipStr);
						try {
							FtpInfo info = client.getFtpInformation();
							if(info != null)
								client.releaseUsingFtp();
						} catch (IOException e) {
							e.printStackTrace();
							testConnectionFailed(e.getMessage());
							return;
						}
						testConnectionSucceed();
					}
				};
				Thread thread = new Thread(test);
				thread.start();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});	
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}
	
	public void testConnectionSucceed() {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				testResult.setText("Test Connetion to Server Successfully Completed.");
				enableAll();
			}
		});
	}
	
	public void testConnectionFailed(final String failReason) {
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				if(failReason != null)
					testResult.setText("Test Connetion to Server Failed by Error:\n" + failReason);
				else
					testResult.setText("Test Connetion to Server Failed by Error.");
				enableAll();
			}
		});
	}
	
	private void disableAll() {
		ip.setEnabled(false);
		button.setEnabled(false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(false);
		progressComplete.setVisible(false);
		progress.setVisible(true);
		stackLayout.topControl = pageProgress;
		stack.layout();
	}
	
	private void enableAll() {
		ip.setEnabled(true);
		button.setEnabled(true);
		getButton(IDialogConstants.OK_ID).setEnabled(true);
		getButton(IDialogConstants.CANCEL_ID).setEnabled(true);
		progress.setVisible(false);
		progressComplete.setVisible(true);
		stackLayout.topControl = pageProgressComplete;
		stack.layout();
	}
}
