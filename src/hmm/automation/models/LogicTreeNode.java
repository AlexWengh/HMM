package hmm.automation.models;

import hmm.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public abstract class LogicTreeNode extends TreeNode {
	
	public static Image getImage() {
		return getImageDescriptor().createImage();
	}
	
	public static ImageDescriptor getImageDescriptor() {
		return AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "images/element_logic.gif");
	}

}
