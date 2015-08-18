package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Find extends WebTreeNode {

	public enum FindMethod {ID, NAME, XPATH, TAGNAME, LINKTEXT};
	
	private static Map<FindMethod, String> methodMap = new LinkedHashMap<FindMethod, String>();
	
	static {
		methodMap.put(FindMethod.ID, "Id");
		methodMap.put(FindMethod.NAME, "Name");
		methodMap.put(FindMethod.XPATH, "Xpath");
		methodMap.put(FindMethod.TAGNAME, "TagName");
		methodMap.put(FindMethod.LINKTEXT, "LinkText");
	}
	
	public static Map<FindMethod, String> getMethodMap() {
		return methodMap;
	}
	
	private String value = "";
	private FindMethod method = FindMethod.ID;
		
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public FindMethod getMethod() {
		return method;
	}
	
	public FindMethod getMethod(String text) {
		for (Entry<FindMethod, String> entry : methodMap.entrySet()) {
			if(entry.getValue().equals(text))
				return entry.getKey();
		}
		return null;
	}

	public void setMethod(FindMethod method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "Find [" + methodMap.get(method) + "=" + "\"" + value + "\"]";
	}
	
	@Override
	public String getInvalidMessage() {
		return "Text is empty";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		WebDriver driver = context.getDriver();
		By by = null;
		switch (method) {
			case ID:
				by = By.id(value);
				break;
			case NAME:
				by = By.name(value);
				break;
			case XPATH:
				by = By.xpath(value);
				break;
			case TAGNAME:
				by = By.tagName(value);
				break;
			case LINKTEXT:
				by = By.linkText(value);
				break;
			default:
				break;
		}
		WebElement element = driver.findElement(by);
		context.setWebElement(element);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element findElement = DocumentHelper.createElement("Find");
		Element methodElement = findElement.addElement("method");
		Element valueElement = findElement.addElement("value");
		methodElement.addText(methodMap.get(method));
		valueElement.addText(value);
		return findElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Find find = new Find();
		Element methodElement = (Element) element.selectSingleNode("method");
		find.setMethod(getMethod(methodElement.getText()));
		Element valueElement = (Element) element.selectSingleNode("value");
		find.setValue(valueElement.getText());
		return find;
	}

}
