package hmm.build.dialogs;


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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class CommandDialog extends TitleAreaDialog {

	private Text command;
	private String commandStr = "";

	public CommandDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public String getCommand() {
		return commandStr;
	}
	
	public void setCommand(String command) {
		commandStr = command;
	}
	
	@Override
	protected void okPressed() {
		commandStr = command.getText().trim();
		if(commandStr.isEmpty())
			setErrorMessage("The Command must not be empty.");
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
		setTitle("Add Command");		
		setMessage("Add/Modify a Command");
	}

	protected void createArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1, true);
		layout.marginWidth = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Enter the command:");
		command = new Text(area, SWT.BORDER | SWT.SINGLE);
		command.setText(commandStr);
		command.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		final Button useTemplateBtn = new Button(area, SWT.CHECK);
		useTemplateBtn.setText("Add \"--template\" at the last of the command to replace all the template strings in the provided file.");
		useTemplateBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		useTemplateBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String str = command.getText();
				if(useTemplateBtn.getSelection()) {
					str += " --template";
					command.setText(str);
				} else if(str.endsWith(" --template")) {
					str = str.replace(" --template", "");
					command.setText(str);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		Button selectAutomationFileBtn = new Button(parent, SWT.PUSH);
		GridLayout layout = (GridLayout) parent.getLayout();
		layout.numColumns++;
		layout.makeColumnsEqualWidth = false;
		selectAutomationFileBtn.setText("Generate command by choose one automation script file ...");
		selectAutomationFileBtn.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		selectAutomationFileBtn.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
				dialog.setText("Select an automation script file");
				dialog.setFilterExtensions(new String[] {"*.xml", "*.*"});
				String filePath = dialog.open();
				if(filePath != null) {
					if(filePath.endsWith(".xml"))
						filePath = "auto " + filePath;
					command.setText(filePath);
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		super.createButtonsForButtonBar(parent);
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(700, 260);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}
}
