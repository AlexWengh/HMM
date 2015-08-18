package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Input extends WebTreeNode {

	private String inputText = "";
	
	public void setInputText(String text) {
		this.inputText = text;
	}
	
	public String getInputText() {
		return inputText;
	}
	
	@Override
	public String toString() {
		return "Input [text=\"" + inputText + "\"]";
	}
	
	@Override
	public String getInvalidMessage() {
		return "Text is empty";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getWebElement().sendKeys(inputText);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element inputElement = DocumentHelper.createElement("Input");
		Element inputTextElement = inputElement.addElement("inputText");
		inputTextElement.addText(inputText);
		return inputElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Input input = new Input();
		Element inputTextElement = (Element) element.selectSingleNode("inputText");
		input.setInputText(inputTextElement.getText());
		return input;
	}

}
