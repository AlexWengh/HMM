package hmm.automation.handlers;

import hmm.automation.models.Variable;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class VariableHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AutomationHandlerUtil().addNodeToTreeViewer(event, new Variable(), "hmm.automation.command.parameter.variable");
		return null;
	}

}
