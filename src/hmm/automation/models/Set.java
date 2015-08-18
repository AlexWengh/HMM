package hmm.automation.models;

import java.util.List;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.viewers.TreeViewer;

public class Set extends LogicTreeNode implements IVariableRelatedNode {
	
	private Variable variable;
	private String value = "";

	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
		if(variable != null)
			variable.addVariableMap(this);
	}
	
	public Variable getVariable(String name) {
		List<Variable> variables = Variable.getVariableList(this);
		for (Variable variable : variables) {
			if(variable.getName().equals(name))
				return variable;
		}
		return null;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public Set() {}
	
	public Set(TreeNode parent) {
		setParent(parent);
	}
	
	@Override
	public void setParent(TreeNode parent) {
		super.setParent(parent);
		initialize();
	}
	
	private void initialize() {
		List<Variable> variables = Variable.getVariableList(this);
		if(variables.size() > 0)
			setVariable(variables.get(0));
	}

	@Override
	public String toString() {
		return "Set [" + (variable == null ? "null" : variable.getName()) + "=" + "\"" + value + "\"]";
	}

	@Override
	public String getInvalidMessage() {
		return "Variable name cannot be empty, or Value cannot be empty";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getVariable(variable.getName()).setValue(value);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element setElement = DocumentHelper.createElement("Set");
		Element varElement = setElement.addElement("variableName");
		Element valueElement = setElement.addElement("value");
		varElement.addText(variable.getName());
		valueElement.addText(value);
		return setElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Set set = new Set(parent);
		Element varElement = (Element) element.selectSingleNode("variableName");
		set.setVariable(getVariable(varElement.getText()));
		Element valueElement = (Element) element.selectSingleNode("value");
		set.setValue(valueElement.getText());
		return set;
	}

	@Override
	public void refreshRelatedVariable(TreeViewer viewer) {
		setValid(false);
		setVariable(null);
		viewer.update(this, null);
	}

}
