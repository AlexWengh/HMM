package hmm.automation.handlers;

import hmm.automation.models.Else;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class ElseHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AutomationHandlerUtil().addNodeToTreeViewer(event, new Else(), "hmm.automation.command.parameter.else");
		return null;
	}

}
