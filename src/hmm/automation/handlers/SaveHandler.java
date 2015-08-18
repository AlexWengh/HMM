package hmm.automation.handlers;

import hmm.automation.AutomationEditorInput;
import hmm.automation.util.TreeModelXmlUtil;
import hmm.build.util.ShowMessageUtil;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;

public class SaveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorInput editorInput = HandlerUtil.getActiveEditorInput(event);
		if(editorInput instanceof AutomationEditorInput) {
			AutomationEditorInput input = (AutomationEditorInput) editorInput;
			Shell shell = HandlerUtil.getActiveShell(event);
			FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
			fileDialog.setText("Save to File");
			fileDialog.setFileName(input.getName());
			fileDialog.setFilterExtensions(new String[] {"*.xml", "*.*"});
			fileDialog.setOverwrite(true);
			String filePath = fileDialog.open();
			if(filePath == null)
				return null;
			File file = new File(filePath);
			try {
				if(file.exists()) {
					if(file.delete() == false)
						throw new IOException("cannot overwrite file: " + file.getPath());
				}
				if(false == file.createNewFile())
					throw new IOException("cannot overwrite file: " + file.getPath());
			} catch (IOException e) {
				e.printStackTrace();
				new ShowMessageUtil().showNormalError(e.getMessage());
				return null;
			}
			new TreeModelXmlUtil().saveToXml(input.getRootModel(), file);
		}
		return null;
	}

}
