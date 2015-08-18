package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Loop extends LogicTreeNode {
	
	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "Loop [count" + "=" + "\"" + count + "\"]";
	}

	@Override
	public String getInvalidMessage() {
		return "Count must be a positive integer less than 65536";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element loopElement = DocumentHelper.createElement("Loop");
		Element countElement = loopElement.addElement("count");
		countElement.addText(String.valueOf(count));
		return loopElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Loop loop = new Loop();
		Element countElement = (Element) element.selectSingleNode("count");
		try {
			loop.setCount(Integer.parseInt(countElement.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return loop;
	}

}
