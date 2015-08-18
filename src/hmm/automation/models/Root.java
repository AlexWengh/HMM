package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Root extends TreeNode {
	
	public Root() {
		super();
		setValid(true);
	}
	
	@Override
	public String toString() {
		return "Root";
	}
	
	@Override
	public String getInvalidMessage() {
		return "";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element element = DocumentHelper.createElement("Root");
		return element;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		return new Root();
	}

}
