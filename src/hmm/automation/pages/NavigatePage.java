package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Navigate;
import hmm.automation.models.TreeNode;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class NavigatePage extends AbstractPage {

	private Text url;
	private Navigate navigate;

	public NavigatePage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		navigate = (Navigate) selection;
	}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Navigate", "Navigate to url");

		Label urlLabel = toolkit.createLabel(client, "Url  ");
		urlLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		url = toolkit.createText(client, "", SWT.SINGLE);
		url.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		url.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(navigate, null);
	}

	@Override
	public void setWidgetValues() {
		url.setText(navigate.getUrl());
	}

	@Override
	public void setModelValues() {
		String value = url.getText().trim();
		if(value.isEmpty())
			navigate.setValid(false);
		else
			navigate.setValid(true);
		if(!value.startsWith("http://") && !value.startsWith("https://"))
			value = "http://" + value;
		navigate.setUrl(value);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!navigate.isValid())
			form.getMessageManager().addMessage(navigate, navigate.getInvalidMessage(), null, IMessageProvider.ERROR, url);
		else
			form.getMessageManager().removeMessage(navigate, url);
	}

}
