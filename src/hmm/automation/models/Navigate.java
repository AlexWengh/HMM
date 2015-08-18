package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Navigate extends WebTreeNode {

	private String url = "";
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	@Override
	public String toString() {
		return "Navigate [url=\"" + url + "\"]";
	}
	
	@Override
	public String getInvalidMessage() {
		return "Url is empty";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		context.getDriver().get(url);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element navigateElement = DocumentHelper.createElement("Navigate");
		Element urlElement = navigateElement.addElement("url");
		urlElement.addText(url);
		return navigateElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Navigate navigate = new Navigate();
		Element urlElement = (Element) element.selectSingleNode("url");
		navigate.setUrl(urlElement.getText());
		return navigate;
	}

}
