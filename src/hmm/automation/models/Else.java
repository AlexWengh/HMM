package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Else extends LogicTreeNode {

	@Override
	public String toString() {
		return "Else";
	}

	@Override
	public String getInvalidMessage() {
		return "No matching If element found";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element elseElement = DocumentHelper.createElement("Else");
		return elseElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		return new Else();
	}

}
