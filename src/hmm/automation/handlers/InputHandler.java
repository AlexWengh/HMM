package hmm.automation.handlers;

import hmm.automation.models.Input;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class InputHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AutomationHandlerUtil().addNodeToTreeViewer(event, new Input(), "hmm.automation.command.parameter.input");
		return null;
	}

}
