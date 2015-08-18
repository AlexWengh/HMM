package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Click extends WebTreeNode {
	
	public Click() {
		super();
		setValid(true);
	}

	@Override
	public String toString() {
		return "Click";
	}
	
	@Override
	public String getInvalidMessage() {
		return "";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getWebElement().click();
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element clickElement = DocumentHelper.createElement("Click");
		return clickElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		return new Click();
	}

}
