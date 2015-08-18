package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Submit extends WebTreeNode {
	
	public Submit() {
		super();
		setValid(true);
	}

	@Override
	public String toString() {
		return "Submit";
	}
	
	@Override
	public String getInvalidMessage() {
		return "";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getWebElement().submit();
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element submitElement = DocumentHelper.createElement("Submit");
		return submitElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		return new Submit();
	}

}
