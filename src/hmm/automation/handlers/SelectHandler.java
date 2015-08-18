package hmm.automation.handlers;

import hmm.automation.models.Select;
import hmm.automation.util.AutomationHandlerUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class SelectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		new AutomationHandlerUtil().addNodeToTreeViewer(event, new Select(), "hmm.automation.command.parameter.select");
		return null;
	}

}
