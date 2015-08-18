package hmm.automation.handlers;

import hmm.automation.AutomationEditor;
import hmm.automation.AutomationEditorInput;
import hmm.automation.models.TreeNode;
import hmm.automation.util.TreeModelXmlUtil;
import hmm.build.util.ShowMessageUtil;

import java.io.File;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveShell(event);
		FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
		fileDialog.setText("Open file");
		fileDialog.setFilterExtensions(new String[] {"*.xml", "*.*"});
		String filePath = fileDialog.open();
		if(filePath == null)
			return null;
		File file = new File(filePath);
		TreeNode root = new TreeModelXmlUtil().loadFromXml(file);
		if(root == null) {
			new ShowMessageUtil().showNormalError("The file is not a vaild automation file.");
			return null;
		}
		
		try {
			AutomationEditorInput input = new AutomationEditorInput(file.getName(), root);
			openEditor(input);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void openEditor(AutomationEditorInput input) throws PartInitException {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		page.openEditor(input, AutomationEditor.ID);
	}

}
