package hmm.automation.models;

import hmm.automation.execute.ExecuteContext;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Wait extends WebTreeNode {

	private int seconds;
	
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public int getSeconds() {
		return seconds;
	}
	
	@Override
	public String toString() {
		return "Wait " + "[seconds=" + String.valueOf(seconds) + "]";
	}
	
	@Override
	public String getInvalidMessage() {
		return "Seconds must be a positive integer";
	}
	
	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		WebDriver driver = context.getDriver();
		new WebDriverWait(driver, seconds).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return false;
            }
		});
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element waitElement = DocumentHelper.createElement("Wait");
		Element secondsElement = waitElement.addElement("seconds");
		secondsElement.addText(String.valueOf(seconds));
		return waitElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Wait wait = new Wait();
		Element waitElement = (Element) element.selectSingleNode("seconds");
		try {
			wait.setSeconds(Integer.parseInt(waitElement.getText()));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return wait;
	}

}
