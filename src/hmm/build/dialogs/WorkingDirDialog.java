package hmm.build.dialogs;

import hmm.build.settings.TemplateVariable;

import java.io.File;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class WorkingDirDialog extends TitleAreaDialog {

	private Text dir;
	private String dirStr = TemplateVariable.getInstance().getValue(TemplateVariable.HMM_DIR);
	
	public WorkingDirDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public String getWorkingDir() {
		return dirStr;
	}
	
	public void setWorkingDir(String str) {
		dirStr = str;
	}
	
	@Override
	protected void okPressed() {
		dirStr = dir.getText().trim();
		File file = new File(dirStr);
		if(!file.isDirectory() || !file.exists())
			setErrorMessage("The specified working directory does not exist.");
		else
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
		setTitle("Working Directory");
		setMessage("Set the Working Directory where HanMeiMei download/generate files into.");
	}

	protected void createArea(Composite parent) {
		final Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Set the Working Directory:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
		
		dir = new Text(area, SWT.BORDER | SWT.SINGLE);
		dir.setText(dirStr);
		dir.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		Button btn = new Button(area, SWT.PUSH);
		btn.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		btn.setText("Browse...");
		btn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				DirectoryDialog dialog = new DirectoryDialog(area.getShell());
				String temp = dialog.open();
				if(temp != null) {
					dir.setText(temp);
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
		return new Point(700, 230);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}

}
