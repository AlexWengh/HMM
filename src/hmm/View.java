package hmm;

import hmm.build.actions.DeleteCommand;
import hmm.build.actions.DeleteTemplate;
import hmm.build.actions.ModifyCommand;
import hmm.build.actions.ModifyTemplate;
import hmm.build.dialogs.CommandDialog;
import hmm.build.dialogs.FtpDialog;
import hmm.build.dialogs.ServerIpDialog;
import hmm.build.dialogs.TemplateVariableDialog;
import hmm.build.dialogs.TimeScheduleDialog;
import hmm.build.dialogs.WorkingDirDialog;
import hmm.build.settings.CommandLines;
import hmm.build.settings.Settings;
import hmm.build.settings.TemplateVariable;
import hmm.build.sockets.SocketServer;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class View extends ViewPart {
	public static final String ID = "HMM.view";

	private FormToolkit toolkit;
	private Form form;
	private TableViewer templateViewer;
	private MenuManager commandMenuManager;
	private Button up;
	private Button down;
	private Hyperlink addCommand;
	private Hyperlink scheduleTime;
	private Hyperlink rootDir;
	private Hyperlink ftp;
	private Button client;
	private Hyperlink addTemplate;
	private MenuManager templateMenuManager;

	/**
	 * The constructor.
	 */
	public View() {}

	/**
	 * This is a callback that will allow us to create the viewer and
	 * initialize it.
	 */
	public void createPartControl(final Composite parent) {
		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.getBody().setLayout(layout);
		form.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		form.setText("HanMeiMei Automation System");
		toolkit.decorateFormHeading(form);
		
		Section commands = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
		commands.setText("Shell Commands");
		commands.setDescription("List Shell Commands for Automation Build process.");
		commands.setLayout(layout);
		commands.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite commandsClient = toolkit.createComposite(commands);
		commands.setClient(commandsClient);
		layout = new GridLayout();
		layout.numColumns = 2;
		commandsClient.setLayout(layout);
		commandsClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Table table = toolkit.createTable(commandsClient, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayout(new GridLayout());
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		table.setHeaderVisible(true);
        table.setLinesVisible(false);
        TableColumn col = new TableColumn(table, SWT.LEFT);
        col.setText("Commands");
        autoAdjustColumnWidth(col, table);
        final TableViewer commandsViewer = new TableViewer(table);
        commandsViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			
			@Override
			public void dispose() {}
			
			@Override
			public Object[] getElements(Object inputElement) {
				@SuppressWarnings("unchecked")
				List<String> list = (List<String>)inputElement;
				return list.toArray();
			}
		});
        commandsViewer.setInput(CommandLines.getInstance().getCommands());
        commandMenuManager = new MenuManager();
        commandMenuManager.add(new ModifyCommand(commandsViewer));
        commandMenuManager.add(new DeleteCommand(commandsViewer));
        Menu menu = commandMenuManager.createContextMenu(table);
        table.setMenu(menu);
        
        Composite buttons = toolkit.createComposite(commandsClient);
        layout = new GridLayout();
		buttons.setLayout(layout);
		layout.verticalSpacing = 100;
		buttons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
        up = toolkit.createButton(buttons, "Up", SWT.PUSH);
        up.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
        up.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = commandsViewer.getTable().getSelectionIndex();
				if(index != -1) {
					CommandLines.getInstance().exchangeCommand(index, index - 1 > 0 ? index - 1 : 0);
					commandsViewer.refresh();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
        down = toolkit.createButton(buttons, "Down", SWT.PUSH);
        down.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true, false));
        down.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = commandsViewer.getTable().getSelectionIndex();
				int size = commandsViewer.getTable().getItemCount();
				if(index != -1) {
					CommandLines.getInstance().exchangeCommand(index, index + 1 < size ? index + 1 : size - 1);
					commandsViewer.refresh();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
        
        Section ops = toolkit.createSection(form.getBody(), Section.DESCRIPTION | Section.TITLE_BAR);
        ops.descriptionVerticalSpacing = commands.getTextClientHeightDifference();
        ops.setText("Settings");
        ops.setDescription("Click Hyperlinks to set HanMeiMei Automation System.");
        ops.setLayout(new GridLayout());
        ops.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		Composite opsClient = toolkit.createComposite(ops);
		ops.setClient(opsClient);
		layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 15;
		layout.horizontalSpacing = 30;
		opsClient.setLayout(layout);
		opsClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		addCommand = toolkit.createHyperlink(opsClient, "Add Command", SWT.WRAP);
		addCommand.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				CommandDialog dialog = new CommandDialog(parent.getShell());
				if(IDialogConstants.OK_ID == dialog.open()) {
					CommandLines.getInstance().addCommand(dialog.getCommand());
					commandsViewer.refresh();
				}
			}
		});
		scheduleTime = toolkit.createHyperlink(opsClient, "Schedule Build Time", SWT.WRAP);
		scheduleTime.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				TimeScheduleDialog dialog = new TimeScheduleDialog(parent.getShell());
				Date date = Settings.getInstance().getBuildTime();
				if(date == null)
					date = new Date();
				dialog.setDate(date);
				dialog.setIntervalDays(Settings.getInstance().getIntervalDays());
				if(IDialogConstants.OK_ID == dialog.open()) {
					Settings.getInstance().setBuildTime(dialog.getDate());
					Settings.getInstance().setIntervalDays(dialog.getIntervalDays());
				}
			}
		});
		rootDir = toolkit.createHyperlink(opsClient, "Working Directory", SWT.WRAP);
		
		ftp = toolkit.createHyperlink(opsClient, "FTP Configuration", SWT.PUSH);
		ftp.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				FtpDialog dialog = new FtpDialog(parent.getShell());
				dialog.setFtpIpStr(Settings.getInstance().getFtpIp());
				dialog.setFtpPortStr(Settings.getInstance().getFtpPort());
				dialog.setFtpUsers(Settings.getInstance().getFtpSettings());
				if(IDialogConstants.OK_ID == dialog.open()) {
					Settings.getInstance().setFtpIp(dialog.getFtpIpStr());
					Settings.getInstance().setFtpPort(dialog.getFtpPortStr());
					Settings.getInstance().setFtpSettings(dialog.getFtpUsers());
				}
			}
		});
		client = toolkit.createButton(opsClient, "Use HanMeiMei as a Socket Client", SWT.CHECK);
		client.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 2, 1));
		if(!Settings.getInstance().getServerIp().isEmpty())
			client.setSelection(true);
		client.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SocketServer socketServer = Settings.getInstance().getSocketServer();
				if(socketServer.isAlive()) {
					socketServer.quit();
					try {
						socketServer.join();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				if(client.getSelection()) {
					ServerIpDialog dialog = new ServerIpDialog(parent.getShell());
					if(dialog.open() == IDialogConstants.OK_ID) {
						String ip = dialog.getIp();
						Settings.getInstance().setServerIp(ip);
						ftp.setEnabled(false);
						return;
					} else {
						client.setSelection(false);
						Settings.getInstance().setServerIp("");
					}
				} else {
					Settings.getInstance().setServerIp("");
				}
				ftp.setEnabled(true);
				socketServer = new SocketServer();
				Settings.getInstance().setSocketServer(socketServer);
				socketServer.start();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		Section template = toolkit.createSection(opsClient, Section.DESCRIPTION | Section.TITLE_BAR);
		template.setText("Template Variables");
		template.setDescription("List Template Variables for Shell Commands.");
		template.setLayout(new GridLayout());
		template.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		Composite templateClient = toolkit.createComposite(template);
		template.setClient(templateClient);
		layout = new GridLayout();
		templateClient.setLayout(layout);
		templateClient.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		addTemplate = toolkit.createHyperlink(templateClient, "Add Template Variable", SWT.WRAP);
		
		table = toolkit.createTable(templateClient, SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL);
		table.setLayout(new GridLayout());
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
		table.setHeaderVisible(true);
        table.setLinesVisible(false);
        TableColumn colName = new TableColumn(table, SWT.LEFT);
        colName.setText("Name");
        TableColumn colVal = new TableColumn(table, SWT.LEFT);
        colVal.setText("Value");
        autoAdjustColumnWidth(colName, colVal, table);
        templateViewer = new TableViewer(table);
        templateViewer.setContentProvider(new IStructuredContentProvider() {
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
			
			@Override
			public void dispose() {}			
			
			@Override
			public Object[] getElements(Object inputElement) {
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) inputElement;
				return map.keySet().toArray();
			}
		});
        templateViewer.setLabelProvider(new ITableLabelProvider() {
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
			public String getColumnText(Object element, int columnIndex) {
				String ret = "";
				if(columnIndex == 0)
					ret = (String) element;
				else if(columnIndex == 1) {
					String key = (String)element;
					ret = TemplateVariable.getInstance().getValue(key);
				}
				return ret;
			}
			
			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
		});
        templateViewer.setInput(TemplateVariable.getInstance().getTemplateVariables());
        templateMenuManager = new MenuManager();
        templateMenuManager.add(new ModifyTemplate(templateViewer));
        templateMenuManager.add(new DeleteTemplate(templateViewer));
        menu = templateMenuManager.createContextMenu(table);
        table.setMenu(menu);
        
        addTemplate.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				TemplateVariableDialog dialog = new TemplateVariableDialog(parent.getShell());
				if(IDialogConstants.OK_ID == dialog.open()) {
					String name = dialog.getName();
					String value = dialog.getValue();
					TemplateVariable.getInstance().addTemplateVariable(name, value);
					templateViewer.refresh();
				}
			}
		});
        
        rootDir.addHyperlinkListener(new HyperlinkAdapter() {
			public void linkActivated(HyperlinkEvent e) {
				WorkingDirDialog dialog = new WorkingDirDialog(parent.getShell());
				if(IDialogConstants.OK_ID == dialog.open()) {
					String wrkDir = dialog.getWorkingDir();
					TemplateVariable.getInstance().modifyTemplateVariable(TemplateVariable.HMM_DIR, wrkDir);
					templateViewer.refresh();
				}
			}
		});
	}
	
	private void autoAdjustColumnWidth(final TableColumn col, final Table table) {
		final Composite comp = table.getParent();
		comp.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = comp.getClientArea();
				Point preferredSize = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * comp.getBorderWidth();
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					if(comp.getVerticalBar() != null) {
						Point vBarSize = comp.getVerticalBar().getSize();
						width -= vBarSize.x;
					}
				}
				col.setWidth(width - 75 > 0 ? width - 75 : 0);
				comp.setSize(area.width, area.height);
			}
		});
	}

	private void autoAdjustColumnWidth(final TableColumn colName, final TableColumn colVal, final Table table) {
		final Composite comp = table.getParent();
		comp.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle area = comp.getClientArea();
				Point preferredSize = comp.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				int width = area.width - 2 * comp.getBorderWidth();
				if (preferredSize.y > area.height + table.getHeaderHeight()) {
					if(comp.getVerticalBar() != null) {
						Point vBarSize = comp.getVerticalBar().getSize();
						width -= vBarSize.x;
					}
				}
				colName.setWidth(width / 3 > 0 ? width / 3 : 0);
				colVal.setWidth(width - colName.getWidth() - 15 > 0 ? width - colName.getWidth() - 15 : 0);
				comp.setSize(area.width, area.height);
			}
		});
	}

	/**
	 * Passing the focus request to the form.
	 */
	public void setFocus() {
		form.setFocus();
	}

	/**
	 * Disposes the toolkit
	 */
	public void dispose() {
		toolkit.dispose();
		super.dispose();
	}
	
	public void disable() {
		commandMenuManager.getMenu().setEnabled(false);
		up.setEnabled(false);
		down.setEnabled(false);
		addCommand.setEnabled(false);
		scheduleTime.setEnabled(false);
		rootDir.setEnabled(false);
		ftp.setEnabled(false);
		client.setEnabled(false);
		addTemplate.setEnabled(false);
		templateMenuManager.getMenu().setEnabled(false);
	}
	
	public void enable() {
		commandMenuManager.getMenu().setEnabled(true);
		up.setEnabled(true);
		down.setEnabled(true);
		addCommand.setEnabled(true);
		scheduleTime.setEnabled(true);
		rootDir.setEnabled(true);
		ftp.setEnabled(true);
		client.setEnabled(true);
		addTemplate.setEnabled(true);
		templateMenuManager.getMenu().setEnabled(true);
	}
	
	public TableViewer getTemplateViewer() {
		return templateViewer;
	}
}
