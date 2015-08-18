package hmm.automation;

import hmm.Activator;
import hmm.automation.actions.AddLogicAction;
import hmm.automation.actions.AddWebAction;
import hmm.automation.actions.RunAction;
import hmm.automation.actions.StopAction;
import hmm.automation.models.LogicTreeNode;
import hmm.automation.models.TreeNode;
import hmm.automation.models.WebTreeNode;
import hmm.automation.pages.AutomationPageProvider;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class AutomationBlock extends MasterDetailsBlock {

	private FormPage page;
	private TreeViewer viewer;
	private Section section;
	private RunAction runAction = new RunAction(this);
	private StopAction stopAction = new StopAction();
	private Map<IDetailsPage, Section> pageSectionMap = new HashMap<IDetailsPage, Section>();
	private IDetailsPage showingPage;
	
	public AutomationBlock(FormPage page) {
		this.page = page;
	}
	
	public TreeViewer getTreeViewer() {
		return viewer;
	}
	
	public Section getSection() {
		return section;
	}
	
	public RunAction getRunAction() {
		return runAction;
	}
	
	public StopAction getStopAction() {
		return stopAction;
	}
	
	public void setShowingPage(IDetailsPage page) {
		this.showingPage = page;
	}
	
	public IDetailsPage getShowingPage() {
		return showingPage;
	}
	
	public Section findShowingSectionForPart() {
		return pageSectionMap.get(showingPage);
	}
	
	public void addPageSection(IDetailsPage page, Section section) {
		pageSectionMap.put(page, section);
	}
	
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		section = toolkit.createSection(parent, Section.DESCRIPTION | Section.TITLE_BAR);
		section.setText("HanMeiMei Automation Logic");
		section.setDescription("Add/Modify/Remove HanMeiMei Automation Logic below.");
		section.marginWidth = 10;
		section.marginHeight = 5;
		
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		toolkit.paintBordersFor(client);
		section.setClient(client);
		
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		
		viewer = new TreeViewer(toolkit.createTree(client, SWT.SINGLE | SWT.BORDER));
		viewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new ITreeContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			
			@Override
			public void dispose() {}
			
			@Override
			public boolean hasChildren(Object element) {
				TreeNode node = (TreeNode) element;
				return node.hasChildren();
			}
			
			@Override
			public Object getParent(Object element) {
				TreeNode node = (TreeNode) element;
				return node.getParent();
			}
			
			@Override
			public Object[] getElements(Object inputElement) {
				AutomationEditorInput input = (AutomationEditorInput) inputElement;
				return input.getRootModel().getChildren().toArray();
			}
			
			@Override
			public Object[] getChildren(Object parentElement) {
				TreeNode node = (TreeNode) parentElement;
				return node.getChildren().toArray();
			}
		});
		
		viewer.setLabelProvider(new DecoratingLabelProvider(new ILabelProvider() {			
			@Override
			public void removeListener(ILabelProviderListener listener) {}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {}
			
			@Override
			public void addListener(ILabelProviderListener listener) {}
			
			@Override
			public String getText(Object element) {
				TreeNode node = (TreeNode) element;
				return node.toString();
			}
			
			@Override
			public Image getImage(Object element) {
				if(element instanceof WebTreeNode)
					return WebTreeNode.getImage();
				if(element instanceof LogicTreeNode)
					return LogicTreeNode.getImage();
				return null;
			}
		}, new ILabelDecorator() {
			@Override
			public void removeListener(ILabelProviderListener listener) {}
			
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
			
			@Override
			public void dispose() {}
			
			@Override
			public void addListener(ILabelProviderListener listener) {}
			
			@Override
			public String decorateText(String text, Object element) {
				return null;
			}
			
			@Override
			public Image decorateImage(Image image, Object element) {
				TreeNode node = (TreeNode) element;
				if(!node.isValid()) {
					Image errImage = AbstractUIPlugin.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
							"images/error.gif").createImage();
					ImageData imageData = image.getImageData();
					ImageData errData = errImage.getImageData();
					Image copyImage = new Image(image.getDevice(), imageData.width, imageData.height);
					GC gc = new GC(copyImage);
					gc.drawImage(image, 0, 0);
					gc.drawImage(errImage, 0, imageData.height - errData.height);
					gc.dispose();
					return copyImage;
				}
				return null;
			}
		}) );
		
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		viewer.setInput(page.getEditor().getEditorInput());
		viewer.expandAll();

		MenuManager menuManager = new MenuManager();
		menuManager.setRemoveAllWhenShown(true);
		Menu menu = menuManager.createContextMenu(viewer.getTree());
		viewer.getTree().setMenu(menu);
		page.getEditor().getEditorSite().registerContextMenu(menuManager, viewer);
		
		ToolBarManager manager = new ToolBarManager(SWT.FLAT);
		ToolBar bar = manager.createControl(section);
		AddLogicAction logicAction = new AddLogicAction(bar);
		AddWebAction webAction = new AddWebAction(bar);
		manager.add(logicAction);
		manager.add(webAction);
		manager.update(true);
		section.setTextClient(bar);
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageProvider(new AutomationPageProvider(this));
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		ScrolledForm form = managedForm.getForm();
		IToolBarManager manager = form.getToolBarManager();
		manager.add(runAction);
		manager.add(stopAction);
		sashForm.setWeights(new int[] {2,1});
	}

}
