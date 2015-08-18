package hmm.automation.execute;

import hmm.automation.models.Variable;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ExecuteContext {
	
	private WebDriver driver;
	private WebElement webElement;
	private List<Variable> variables = new ArrayList<Variable>();
	private boolean enterIf = false;
	private boolean enterElse = false;

	public WebDriver getDriver() {
		return driver;
	}

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}

	public WebElement getWebElement() {
		return webElement;
	}

	public void setWebElement(WebElement webElement) {
		this.webElement = webElement;
	}
	
	public void addVariable(Variable variable) {
		Variable needRemove = null;
		for (Variable var : variables) {
			if(var.getName().equals(variable.getName())) {
				needRemove = var;
				break;
			}
		}
		if(needRemove != null)
			variables.remove(needRemove);
		variables.add(variable);
	}
	
	public Variable getVariable(String name) {
		Variable ret = null;
		for (Variable var : variables) {
			if(var.getName().equals(name)) {
				ret = var;
				break;
			}
		}
		return ret;
	}
	
	public boolean isEnterIf() {
		return enterIf;
	}

	public void setEnterIf(boolean enterSubNode) {
		this.enterIf = enterSubNode;
	}
	
	public boolean isEnterElse() {
		return enterElse;
	}

	public void setEnterElse(boolean enterElse) {
		this.enterElse = enterElse;
	}

}
