package hmm.build.dialogs;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class TimeScheduleDialog extends TitleAreaDialog {
	
	private DateTime time;
	private Combo interval;
	private Calendar calendar = Calendar.getInstance();
	private int intervalDays = 1;

	public TimeScheduleDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(getShellStyle() | SWT.RESIZE);
	}
	
	public Date getDate() {
		return calendar.getTime();
	}
	
	public void setDate(Date date) {
		calendar.setTime(date);
	}
	
	public int getIntervalDays() {
		return intervalDays;
	}
	
	public void setIntervalDays(int days) {
		intervalDays = days;
	}
	
	@Override
	protected void okPressed() {
		calendar.set(Calendar.HOUR_OF_DAY, time.getHours());
		calendar.set(Calendar.MINUTE, time.getMinutes());
		calendar.set(Calendar.SECOND, time.getSeconds());
		intervalDays = Integer.parseInt(interval.getText());
		super.okPressed();
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		parent =  (Composite) super.createDialogArea(parent);
		setTitle();
		createArea(parent);
		return parent;
	}
	
	protected void setTitle() {
		setTitle("Schedule Build Time");
		setMessage("Set the Build Time and Interval Days.");
	}

	protected void createArea(Composite parent) {
		Composite area = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, true);
		layout.marginWidth = 10;
		layout.horizontalSpacing = 10;
		area.setLayout(layout);
		area.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Label label = new Label(area, SWT.LEFT);
		label.setText("Set the build start time and interval days:");
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));
		
		time = new DateTime(area, SWT.TIME);
		time.setTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND));
		time.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		
		interval = new Combo(area, SWT.DROP_DOWN | SWT.BORDER);
		for(int i = 1; i <= 14; ++i)
			interval.add(String.valueOf(i));
		interval.select(intervalDays - 1);
		interval.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
	}
	
	@Override
	protected Point getInitialSize() {
		return new Point(400, 220);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("HanMeiMei");
	}

}
