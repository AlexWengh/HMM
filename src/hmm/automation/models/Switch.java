package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openqa.selenium.WebDriver;

public class Switch extends WebTreeNode {

	public static enum SwitchMethod {FRAME, WINDOW};
	
	private static Map<SwitchMethod, String> methodMap = new LinkedHashMap<SwitchMethod, String>();
	
	static {
		methodMap.put(SwitchMethod.FRAME, "FrameId");
		methodMap.put(SwitchMethod.WINDOW, "WindowName");
	}
	
	public static Map<SwitchMethod, String> getMethodMap() {
		return methodMap;
	}
	
	private String value = "";
	private SwitchMethod method = SwitchMethod.FRAME;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public SwitchMethod getMethod() {
		return method;
	}
	
	public SwitchMethod getMethod(String text) {
		for (Entry<SwitchMethod, String> entry : methodMap.entrySet()) {
			if(entry.getValue().equals(text))
				return entry.getKey();
		}
		return null;
	}

	public void setMethod(SwitchMethod method) {
		this.method = method;
	}

	@Override
	public String toString() {
		return "Switch [" + methodMap.get(method) + "=\"" + value + "\"]";
	}
	
	@Override
	public String getInvalidMessage() {
		return "Text is empty";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		WebDriver driver = context.getDriver();
		switch (method) {
			case FRAME:
				driver.switchTo().frame(value);
				break;
			case WINDOW:
				driver.switchTo().window(value);
			default:
				break;
		}
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element switchElement = DocumentHelper.createElement("Switch");
		Element methodElement = switchElement.addElement("method");
		Element valueElement = switchElement.addElement("value");
		methodElement.addText(methodMap.get(method));
		valueElement.addText(value);
		return switchElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Switch switchTo = new Switch();
		Element methodElement = (Element) element.selectSingleNode("method");
		switchTo.setMethod(getMethod(methodElement.getText()));
		Element valueElement = (Element) element.selectSingleNode("value");
		switchTo.setValue(valueElement.getText());
		return switchTo;
	}

}
