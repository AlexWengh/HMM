package hmm.build.dialogs;

import hmm.build.settings.TemplateVariable;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class TemplateVariableDialog extends TitleAreaDialog {

	private Text name;
	private String nameStr = "";
	private Text value;
	private String valueStr = "";
	private boolean disableNameText = false;
	
	public TemplateVariableDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public String getName() {
		return nameStr;
	}
	
	public void setName(String name) {
		nameStr = name;
	}
	
	public String getValue() {
		return valueStr;
	}
	
	public void setValue(String value) {
		valueStr = value;
	}
	
	@Override
	protected void okPressed() {
		nameStr = name.getText().trim();
		valueStr = value.getText().trim();
		if(nameStr.isEmpty()) {
			setErrorMessage("The Name field must not be empty.");
			return;
		}
		if(valueStr.isEmpty()) {
			setErrorMessage("The Value field must not be empty.");
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
		setTitle("Template Variable");
		setMessage("Add/Modify a Template Variable");
	}

	protected void createArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Enter the name:");
		name = new Text(area, SWT.BORDER | SWT.SINGLE);
		name.setText(nameStr);
		name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		if(disableNameText)
			name.setEnabled(false);
		name.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(name.getText().trim().equals(TemplateVariable.HMM_DIR) || name.getText().trim().equals(TemplateVariable.BUILD_DIR))
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				else
					getButton(IDialogConstants.OK_ID).setEnabled(true);
			}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		label = new Label(area, SWT.LEFT);
		label.setText("Enter the value:");
		value = new Text(area, SWT.BORDER | SWT.SINGLE);
		value.setText(valueStr);
		value.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(700, 270);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}

	public void disableName() {
		disableNameText = true;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		if(nameStr.equals(TemplateVariable.HMM_DIR) || nameStr.equals(TemplateVariable.BUILD_DIR))
			getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

}
