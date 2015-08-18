package hmm.automation.util;

import hmm.automation.AutomationBlock;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

public class PageUtil {
	
	public static Composite createHeaders(AutomationBlock automationBlock, IDetailsPage page, FormToolkit toolkit, 
									Composite parent, String sectionText, String sectionDescription) {
		
		TableWrapLayout twlayout = new TableWrapLayout();
		twlayout.topMargin = 5;
		twlayout.leftMargin = 5;
		twlayout.rightMargin = 2;
		twlayout.bottomMargin = 2;
		parent.setLayout(twlayout);
		
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		section.marginWidth = 10;
		section.setText(sectionText);
		section.setDescription(sectionDescription);
		automationBlock.addPageSection(page, section);
		
		Composite client = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 5;
		layout.marginHeight = 10;
		client.setLayout(layout);
		client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		toolkit.paintBordersFor(client);
		section.setClient(client);
		return client;
	}
	
}
