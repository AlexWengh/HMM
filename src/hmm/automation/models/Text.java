package hmm.automation.models;

import java.util.List;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Text extends WebTreeNode {
	
	private Variable variable;

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
	
	public Text() {}
	
	public Text(TreeNode parent) {
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
		return "Text [variable" + "=" + (variable == null ? "null" : variable.getName()) + "]";
	}

	@Override
	public String getInvalidMessage() {
		return "Variable name cannot be empty";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getVariable(variable.getName()).setValue(context.getWebElement().getText());
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element textElement = DocumentHelper.createElement("Text");
		Element varElement = textElement.addElement("variableName");
		varElement.addText(variable.getName());
		return textElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Text text = new Text(parent);
		Element varElement = (Element) element.selectSingleNode("variableName");
		text.setVariable(getVariable(varElement.getText()));
		return text;
	}

}
