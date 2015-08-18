package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openqa.selenium.WebElement;

public class Select extends WebTreeNode {
	
	public enum SelectOperation {SELECT, DESELECT}
	public enum SelectMethod {VISIBLETEXT, VALUE, INDEX}
	
	private static Map<SelectOperation, String> mapOperation = new LinkedHashMap<Select.SelectOperation, String>();
	private static Map<SelectMethod, String> mapMethod = new LinkedHashMap<SelectMethod, String>();
	
	static {
		mapOperation.put(SelectOperation.SELECT, "Select");
		mapOperation.put(SelectOperation.DESELECT, "Deselect");
		mapMethod.put(SelectMethod.VISIBLETEXT, "VisibleText");
		mapMethod.put(SelectMethod.VALUE, "Value");
		mapMethod.put(SelectMethod.INDEX, "Index");
	}
	
	public static Map<SelectOperation, String> getOperationMap() {
		return mapOperation;
	}
	
	public static Map<SelectMethod, String> getMethodMap() {
		return mapMethod;
	}
	
	private SelectOperation operation = SelectOperation.SELECT;
	private SelectMethod method = SelectMethod.VISIBLETEXT;
	private String value = "";
	
	public SelectOperation getOperation() {
		return operation;
	}
	
	public void setOperation(SelectOperation operation) {
		this.operation = operation;
	}

	public SelectMethod getMethod() {
		return method;
	}

	public void setMethod(SelectMethod method) {
		this.method = method;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Select [" + mapOperation.get(operation) + ", " + mapMethod.get(method) + "=\"" + value + "\"]";
	}
	
	@Override
	public String getInvalidMessage() {
		if(getValue().isEmpty())
			return "Text is empty";
		return "Index must be a non-negative integer";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		WebElement element = context.getWebElement();
		org.openqa.selenium.support.ui.Select select = new org.openqa.selenium.support.ui.Select(element);
		switch (operation) {
			case SELECT:
				executeSelect(select);
				break;
			case DESELECT:
				executeDeselect(select);
				break;
			default:
				break;
			}
		return context;
	}

	private void executeSelect(org.openqa.selenium.support.ui.Select select) throws Exception {
		switch (method) {
			case VISIBLETEXT:
				select.selectByVisibleText(value);
				break;
			case VALUE:
				select.selectByValue(value);
				break;
			case INDEX:
				select.selectByIndex(Integer.parseInt(value));
				break;
			default:
				break;
		}
	}
	
	private void executeDeselect(org.openqa.selenium.support.ui.Select select) throws Exception {
		switch (method) {
			case VISIBLETEXT:
				select.deselectByVisibleText(value);
				break;
			case VALUE:
				select.deselectByValue(value);
				break;
			case INDEX:
				select.deselectByIndex(Integer.parseInt(value));
				break;
			default:
				break;
		}
	}

	@Override
	protected Element toXmlElement() {
		Element selectElement = DocumentHelper.createElement("Select");
		Element operationElement = selectElement.addElement("operation");
		Element methodElement = selectElement.addElement("method");
		Element valueElement = selectElement.addElement("value");
		operationElement.addText(mapOperation.get(operation));
		methodElement.addText(mapMethod.get(method));
		valueElement.addText(value);
		return selectElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Select select = new Select();
		Element operationElement = (Element) element.selectSingleNode("operation");
		for (Entry<SelectOperation, String> entry : mapOperation.entrySet()) {
			if(entry.getValue().equals(operationElement.getText())) {
				select.setOperation(entry.getKey());
				break;
			}
		}
		Element methodElement = (Element) element.selectSingleNode("method");
		for (Entry<SelectMethod, String> entry : mapMethod.entrySet()) {
			if(entry.getValue().equals(methodElement.getText())) {
				select.setMethod(entry.getKey());
				break;
			}
		}
		Element valueElement = (Element) element.selectSingleNode("value");
		select.setValue(valueElement.getText());
		return select;
	}

}
