package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.TreeNode;
import hmm.automation.models.Wait;
import hmm.automation.util.PageUtil;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class WaitPage extends AbstractPage {

	private Text seconds;
	private Wait wait;
	
	public WaitPage(AutomationBlock automationBlock) {
		super(automationBlock);
	}

	@Override
	public void setTreeModelFromSelection(TreeNode selection) {
		wait = (Wait) selection;
	}

	@Override
	public void onCreateContents(Composite parent) {
		FormToolkit toolkit = form.getToolkit();
		Composite client = PageUtil.createHeaders(automationBlock, this, toolkit, parent, "Wait", "Wait several seconds");

		Label secondsLabel = toolkit.createLabel(client, "Seconds  ");
		secondsLabel.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false));
		seconds = toolkit.createText(client, "", SWT.SINGLE);
		seconds.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		seconds.addFocusListener(getFocusListener());
	}
	
	@Override
	public void updateTreeViewerElement() {
		automationBlock.getTreeViewer().update(wait, null);
	}

	@Override
	public void setWidgetValues() {
		String sec = String.valueOf(wait.getSeconds());
		seconds.setText(sec);
	}

	@Override
	public void setModelValues() {
		String value = seconds.getText().trim();
		int sec = 0;
		wait.setValid(true);
		try {
			sec = Integer.parseInt(value);
			if(sec <= 0)
				wait.setValid(false);
		} catch (NumberFormatException e) {
			wait.setValid(false);
		}
		wait.setSeconds(sec);
	}

	@Override
	public void addRemoveValidatorMessages() {
		if(!wait.isValid())
			form.getMessageManager().addMessage(wait, wait.getInvalidMessage(), null, IMessageProvider.ERROR, seconds);
		else
			form.getMessageManager().removeMessage(wait, seconds);
	}

}
