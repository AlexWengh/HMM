package hmm.automation.handlers;

import hmm.automation.AutomationEditorInput;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PartInitException;

public class NewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			AutomationEditorInput input = new AutomationEditorInput();
			new OpenHandler().openEditor(input);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

}
