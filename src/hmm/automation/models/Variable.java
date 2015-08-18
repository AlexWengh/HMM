package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.viewers.TreeViewer;

public class Variable extends LogicTreeNode {
	
	private String name = "";
	private String value = "";
	
	private static Map<Variable, List<TreeNode>> variableMap = new HashMap<Variable, List<TreeNode>>();
	
	public Variable() {
		variableMap.put(this, new ArrayList<TreeNode>());
	}
	
	public void addVariableMap(TreeNode node) {
		variableMap.get(this).add(node);
	}
	
	public void refreshAffectedNodes(TreeViewer viewer) {
		for (TreeNode node : variableMap.get(this)) {
			if(node instanceof IVariableRelatedNode)
				((IVariableRelatedNode) node).refreshRelatedVariable(viewer);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Variable [name=\"" + name + "\", " + "value=\"" + value + "\"]";
	}

	@Override
	public String getInvalidMessage() {
		return "Name already used, or Name is empty";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		Variable variable = new Variable();
		variable.setName(name);
		variable.setValue(value);
		context.addVariable(variable);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element varElement = DocumentHelper.createElement("Variable");
		Element nameElement = varElement.addElement("name");
		Element valueElement = varElement.addElement("value");
		nameElement.addText(name);
		valueElement.addText(value);
		return varElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Variable variable = new Variable();
		Element nameElement = (Element) element.selectSingleNode("name");
		Element valueElement = (Element) element.selectSingleNode("value");
		variable.setName(nameElement.getText());
		variable.setValue(valueElement.getText());
		return variable;
	}

	public static List<Variable> getVariableList(TreeNode node) {
		List<Variable> variables = new ArrayList<Variable>();
		if(node != null) {
			TreeNode parent = node.getParent();
			while(parent != null) {
				for (TreeNode sub : parent.getChildren()) {
					if(sub == node)
						break;
					if(sub instanceof Variable) {
						Variable variable = (Variable) sub;
						if(!variable.getName().isEmpty() && variable.isValid())
							variables.add(variable);
					}
				}
				parent = parent.getParent();
			}
		}
		return variables;
	}

}
