package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;
import hmm.build.console.Console;

import java.util.LinkedList;
import java.util.List;

import org.dom4j.Element;

public abstract class TreeNode {
	
	private TreeNode parent;
	
	private List<TreeNode> children = new LinkedList<TreeNode>();
	
	private boolean valid;
	
	public void addChild(TreeNode node) {
		children.add(node);
		node.setParent(this);
	}
	
	public void removeChild(TreeNode node) {
		children.remove(node);
		node.setParent(null);
	}
	
	public TreeNode getParent() {
		return parent;
	}
	
	public void setParent(TreeNode parent) {
		this.parent = parent;
	}
	
	public boolean hasChildren() {
		return children.size() == 0 ? false : true;
	}
	
	public List<TreeNode> getChildren() {
		return children;
	}
	
	public void setValid(boolean valid) {
		this.valid = valid;
	}
	
	public boolean isValid() {
		return valid;
	}
	
	public Element toXml() {
		Element element = toXmlElement();
		Element validElement = element.addElement("valid");
		validElement.setText(String.valueOf(isValid()));
		return element;
	}
	
	public TreeNode createFromXml(TreeNode parent, Element element) {
		TreeNode node = createFromXmlElement(parent, element);
		Element validElement = (Element) element.selectSingleNode("valid");
		if(validElement.getText().equals(String.valueOf(true)))
			node.setValid(true);
		else
			node.setValid(false);
		return node;
	}
	
	public abstract String toString();
	
	public abstract String getInvalidMessage();
	
	public ExecuteContext exec(ExecuteContext context) throws Exception {
		if(toString() != "Root")
			Console.getAutomationInstance().writeLine("Running:\t" + toString());
		try {
			context =  execute(context);
		} catch (final Exception e) {
			Console.getAutomationInstance().writeErrorLine("Error:\t" + e.getMessage());
			throw e;
		}
		return context;
	}
	
	public abstract ExecuteContext execute(ExecuteContext context) throws Exception;
	
	protected abstract Element toXmlElement();
	
	protected abstract TreeNode createFromXmlElement(TreeNode parent, Element element);

}
