package hmm.automation.handlers;

import hmm.automation.models.Switch;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class SwitchHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AutomationHandlerUtil().addNodeToTreeViewer(event, new Switch(), "hmm.automation.command.parameter.switch");
		return null;
	}

}
