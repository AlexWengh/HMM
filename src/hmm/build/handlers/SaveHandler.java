package hmm.build.handlers;

import hmm.build.util.SettingsUtil;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

public class SaveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		SettingsUtil.getInstance().saveSettings();
		return null;
	}	

}
