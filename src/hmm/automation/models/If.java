package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.eclipse.jface.viewers.TreeViewer;

public class If extends LogicTreeNode implements IVariableRelatedNode {
	
	public static enum CompareMethod {EQUALS};
	private static Map<CompareMethod, String> methodMap = new LinkedHashMap<CompareMethod, String>();
	
	static {
		methodMap.put(CompareMethod.EQUALS, "equals");
	}
	
	public static Map<CompareMethod, String> getMethodMap() {
		return methodMap;
	}
	
	private Variable variableLeft;
	private String valueRight = "";
	private CompareMethod method = CompareMethod.EQUALS;
	
	public Variable getvariableLeft() {
		return variableLeft;
	}

	public void setVariableLeft(Variable variableLeft) {
		this.variableLeft = variableLeft;
		if(variableLeft != null)
			variableLeft.addVariableMap(this);
	}

	public String getValueRight() {
		return valueRight;
	}

	public void setValueRight(String valueRight) {
		this.valueRight = valueRight;
	}
	
	public Variable getVariable(String name) {
		List<Variable> variables = Variable.getVariableList(this);
		for (Variable variable : variables) {
			if(variable.getName().equals(name))
				return variable;
		}
		return null;
	}
	
	public CompareMethod getMethod() {
		return method;
	}
	
	public CompareMethod getMethod(String text) {
		for (Entry<CompareMethod, String> entry : methodMap.entrySet()) {
			if(entry.getValue().equals(text))
				return entry.getKey();
		}
		return null;
	}

	public void setMethod(CompareMethod method) {
		this.method = method;
	}

	public If() {}
	
	public If(TreeNode parent) {
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
			setVariableLeft(variables.get(0));
	}

	@Override
	public String toString() {
		return "If [" + (variableLeft == null ? "null" : variableLeft.getName()) + " " + methodMap.get(method) + " " + valueRight + "\"]";
	}

	@Override
	public String getInvalidMessage() {
		List<Variable> variables = Variable.getVariableList(this);
		if(variables.size() == 0)
			return "Cannot find any variable defined in context";
		if(variableLeft == null || variableLeft.getName().isEmpty())
			return "Variable Name cannot be empty";
		return "Value cannot be empty";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		if(context.getVariable(variableLeft.getName()).getValue().equals(valueRight))
			context.setEnterIf(true);
		else
			context.setEnterIf(false);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element ifElement = DocumentHelper.createElement("If");
		Element leftElement = ifElement.addElement("variableLeft");
		Element methodElement = ifElement.addElement("method");
		Element rightElement = ifElement.addElement("valueRight");
		leftElement.addText(variableLeft != null ? variableLeft.getName() : "");
		methodElement.addText(methodMap.get(method));
		rightElement.addText(valueRight);
		return ifElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		If iff = new If(parent);
		Element leftElement = (Element) element.selectSingleNode("variableLeft");
		iff.setVariableLeft(getVariable(leftElement.getName()));
		Element methodElement = (Element) element.selectSingleNode("method");
		iff.setMethod(getMethod(methodElement.getText()));
		Element rightElement = (Element) element.selectSingleNode("valueRight");
		iff.setValueRight(rightElement.getText());
		return iff;
	}

	@Override
	public void refreshRelatedVariable(TreeViewer viewer) {
		setValid(false);
		setVariableLeft(null);
		viewer.update(this, null);
	}

}
