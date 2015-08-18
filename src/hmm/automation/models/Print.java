package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;
import hmm.build.console.Console;

import java.util.List;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.viewers.TreeViewer;

public class Print extends LogicTreeNode implements IVariableRelatedNode {
	
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
	
	public Print() {}
	
	public Print(TreeNode parent) {
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
		return "Print [variable" + "=" + (variable == null ? "null" : variable.getName()) + "]";
	}

	@Override
	public String getInvalidMessage() {
		return "Variable name cannot be empty";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		Console.getAutomationInstance().writeLine(context.getVariable(variable.getName()).getValue());
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element printElement = DocumentHelper.createElement("Print");
		Element varElement = printElement.addElement("variableName");
		varElement.addText(variable.getName());
		return printElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Print print = new Print(parent);
		Element varElement = (Element) element.selectSingleNode("variableName");
		print.setVariable(getVariable(varElement.getText()));
		return print;
	}

	@Override
	public void refreshRelatedVariable(TreeViewer viewer) {
		setValid(false);
		setVariable(null);
		viewer.update(this, null);
	}

}
