package hmm.automation.pages;

import hmm.automation.AutomationBlock;
import hmm.automation.models.Click;
import hmm.automation.models.Else;
import hmm.automation.models.Find;
import hmm.automation.models.If;
import hmm.automation.models.Input;
import hmm.automation.models.Loop;
import hmm.automation.models.Navigate;
import hmm.automation.models.Print;
import hmm.automation.models.Select;
import hmm.automation.models.Set;
import hmm.automation.models.Submit;
import hmm.automation.models.Switch;
import hmm.automation.models.Text;
import hmm.automation.models.Variable;
import hmm.automation.models.Wait;

import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;

public class AutomationPageProvider implements IDetailsPageProvider {
	
	private AutomationBlock automationBlock;
	
	public AutomationPageProvider(AutomationBlock automationBlock) {
		this.automationBlock = automationBlock;
	}

	@Override
	public Object getPageKey(Object object) {
		return object;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if(key instanceof hmm.automation.models.Class)
			return new ClassPage(automationBlock);
		if(key instanceof Click)
			return new ClickPage(automationBlock);
		if(key instanceof Else)
			return new ElsePage(automationBlock);
		if(key instanceof Find)
			return new FindPage(automationBlock);
		if(key instanceof If)
			return new IfPage(automationBlock);
		if(key instanceof Input)
			return new InputPage(automationBlock);
		if(key instanceof Loop)
			return new LoopPage(automationBlock);
		if(key instanceof Navigate)
			return new NavigatePage(automationBlock);
		if(key instanceof Print)
			return new PrintPage(automationBlock);
		if(key instanceof Select)
			return new SelectPage(automationBlock);
		if(key instanceof Set)
			return new SetPage(automationBlock);
		if(key instanceof Submit)
			return new SubmitPage(automationBlock);
		if(key instanceof Switch)
			return new SwitchPage(automationBlock);
		if(key instanceof Text)
			return new TextPage(automationBlock);
		if(key instanceof Variable)
			return new VariablePage(automationBlock);
		if(key instanceof Wait)
			return new WaitPage(automationBlock);
		return null;
	}

}
