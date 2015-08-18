package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Class;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import java.io.File;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class ClassPage extends AbstractPage {

	private Text path;
	private Class classs;
	private Button button;

	public ClassPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		classs = (Class) selection;
	}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		final Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Class", "Run a java class with Selenium automation code");
		
		((GridLayout) client.getLayout()).numColumns = 3;
		Label pathLabel = toolkit.createLabel(client, "File Path  ");
		pathLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		path = toolkit.createText(client, "", SWT.SINGLE);
		path.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		button = toolkit.createButton(client, "Browse...", SWT.PUSH);
		button.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false));
		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(client.getShell(), SWT.OPEN);
				dialog.setFilterExtensions(new String[] {"*.class"});
				String pathStr = dialog.open();
				if(pathStr != null) {
					path.setText(pathStr);
					path.setFocus();
				}
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		path.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(classs, null);
	}
	
	@Override
	public void setWidgetValues() {
		path.setText(classs.getClassFilePath());
	}

	@Override
	public void setModelValues() {
		String filePath = path.getText().trim();
		File file = new File(filePath);
		classs.setClassFilePath(filePath);
		if(!file.exists() || !filePath.endsWith(".class"))
			classs.setValid(false);
		else
			classs.setValid(true);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!classs.isValid())
			form.getMessageManager().addMessage(classs, classs.getInvalidMessage(), null, IMessageProvider.ERROR, button);
		else
			form.getMessageManager().removeMessage(classs, button);
	}

}
