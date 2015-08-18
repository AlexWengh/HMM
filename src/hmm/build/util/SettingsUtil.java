package hmm.build.util;

import hmm.build.ftp.FtpInfo;
import hmm.build.settings.CommandLines;
import hmm.build.settings.FtpSetting;
import hmm.build.settings.Settings;
import hmm.build.settings.TemplateVariable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.runtime.Platform;

public class SettingsUtil {
	
	private static SettingsUtil xmlOp = new SettingsUtil();
	
	private File xmlFile;
	
	private SettingsUtil() {
		String path = Platform.getInstallLocation().getURL().getPath();
		if(path.charAt(path.length() - 1) != File.separatorChar)
			path += File.separatorChar;
		path += "hmm.conf";
		xmlFile = new File(path);
	}
	
	public static SettingsUtil getInstance() {
		return xmlOp;
	}
	
	public void loadSettings() {
		SAXReader reader = new SAXReader();
		Document document = null;
		if (!xmlFile.exists() || xmlFile.length() == 0)
			return;
		try {
			document = reader.read(new FileReader(xmlFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("setting file not found: " + e.getMessage());
		} catch (DocumentException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("parse xml exception: " + e.getMessage());
		}
		
		List<?> list = document.selectNodes("//command");
		for (Object object : list) {
			Element sub = (Element) object;
			String str = sub.getText();
			CommandLines.getInstance().addCommand(str);
		}
		
		Element root = (Element) document.selectSingleNode("root");
		
		Element buildTime = (Element) root.selectSingleNode("buildTime");
		String str = buildTime.getText();
		if(!str.isEmpty()) {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
			Date date = null;
			try {
				date = dateFormat.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
				new ShowMessageUtil().showNormalError("date format exception: " + e.getMessage());
			}
			Settings.getInstance().setBuildTime(date);
		}
		
		Element days = (Element) root.selectSingleNode("intervalDays");
		Settings.getInstance().setIntervalDays(Integer.parseInt(days.getText()));
		
		Element ip = (Element) root.selectSingleNode("serverIP");
		Settings.getInstance().setServerIp(ip.getText());
		
		Element ftpIp = (Element) root.selectSingleNode("ftpIP");
		Settings.getInstance().setFtpIp(ftpIp.getText());
		
		Element ftpPort = (Element) root.selectSingleNode("ftpPort");
		String strstr = ftpPort.getText().trim();
		Settings.getInstance().setFtpPort(Integer.parseInt(strstr));
		
		list = root.selectNodes("//account");
		for (Object object : list) {
			Element sub = (Element) object;
			Element user = (Element) sub.selectSingleNode("username");
			Element pass = (Element) sub.selectSingleNode("password");
			String userStr = user.getText();
			String passStr = pass.getText();
			Settings.getInstance().addFtpSettings(new FtpSetting(userStr, passStr));
		}
		
		list = document.selectNodes("//variable");
		for (Object object : list) {
			Element sub = (Element) object;
			Element name = (Element) sub.selectSingleNode("name");
			Element value = (Element) sub.selectSingleNode("value");
			String nameStr = name.getText();
			String valueStr = value.getText();
			TemplateVariable.getInstance().modifyTemplateVariable(nameStr, valueStr);
		}
	}
	
	public void saveSettings() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
		
		Element commands = root.addElement("commands");
		List<String> commList = CommandLines.getInstance().getCommands();
		for (String string : commList) {
			Element command = commands.addElement("command");
			command.setText(string);
		}
		
		Element dateElem = root.addElement("buildTime");
		Date date = Settings.getInstance().getBuildTime();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String str = "";
		if(date != null)
			str = dateFormat.format(date);
		dateElem.setText(str);
		
		Element days = root.addElement("intervalDays");
		int day = Settings.getInstance().getIntervalDays();
		days.setText(String.valueOf(day));
		
		Element ip = root.addElement("serverIP");
		String address = Settings.getInstance().getServerIp();
		ip.setText(address);
		
		Element ftpIp = root.addElement("ftpIP");
		String ftpIpStr = Settings.getInstance().getFtpIp();
		ftpIp.setText(ftpIpStr);
		
		Element ftpPort = root.addElement("ftpPort");
		String ftpPortStr = String.valueOf(Settings.getInstance().getFtpPort());
		ftpPort.setText(ftpPortStr);
		
		Element ftpAccounts = root.addElement("ftpAccounts");
		List<FtpSetting> models = Settings.getInstance().getFtpSettings();
		for (FtpSetting ftpModel : models) {
			Element var = ftpAccounts.addElement("account");
			String name = ftpModel.getUser();
			Element nameElem = var.addElement("username");
			nameElem.setText(name);
			String pass = ftpModel.getPass();
			Element passElem = var.addElement("password");
			passElem.setText(pass);
		}
		
		Element templates = root.addElement("templates");
		Map<String, String> varMap = TemplateVariable.getInstance().getTemplateVariables();
		for (String key : varMap.keySet()) {
			if(key.equals(TemplateVariable.BUILD_DIR))
				continue;
			Element var = templates.addElement("variable");
			Element name = var.addElement("name");
			name.setText(key);
			Element val = var.addElement("value");
			val.setText(varMap.get(key));
		}
		
		xmlFile.delete();
		try {
			if(xmlFile.createNewFile() == false)
				throw new IOException("cannot create settings xml file");
		} catch (IOException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("save settings to xml file failed: " + e.getMessage());
		}
		
		OutputFormat format = new OutputFormat();
		format.setIndentSize(4);
		format.setNewlines(true);
		format.setEncoding("utf-8");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(xmlFile);
		} catch (IOException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("save settings to xml file failed: " + e.getMessage());
		}
		XMLWriter writer = new XMLWriter(fileWriter, format);
		try {
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("save settings to xml file failed: " + e.getMessage());
		}
	}
	
	public String getSendXml(FtpSetting model) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");
    	
    	Element ftpIp = root.addElement("ftpIP");
		String ftpIpStr = Settings.getInstance().getFtpIp();
		ftpIp.setText(ftpIpStr);
		
		Element ftpPort = root.addElement("ftpPort");
		String ftpPortStr = String.valueOf(Settings.getInstance().getFtpPort());
		ftpPort.setText(ftpPortStr);
		
		String username = model.getUser();
		Element usernameElem = root.addElement("username");
		usernameElem.setText(username);
		
		String password = model.getPass();
		Element passwordElem = root.addElement("password");
		passwordElem.setText(password);
		
		return document.asXML();
	}

	public FtpInfo parseReceivedXml(String response) {
		Document document;
		try {
			document = DocumentHelper.parseText(response);
		} catch (DocumentException e) {
			e.printStackTrace();
			return null;
		}
		Element root = (Element) document.selectSingleNode("root");
		Element ftpIp = (Element) root.selectSingleNode("ftpIP");
		Element ftpPort = (Element) root.selectSingleNode("ftpPort");
		Element usernameElem = (Element) root.selectSingleNode("username");
		Element passwordElem = (Element) root.selectSingleNode("password");
		
		String ftpIpStr = ftpIp.getText();
		int portNum = Integer.parseInt(ftpPort.getText());
		String username = usernameElem.getText();
		String password = passwordElem.getText();
		FtpSetting setting = new FtpSetting(username, password);
		
		FtpInfo info = new FtpInfo(ftpIpStr, portNum, setting);
		return info;
	}
	
	public boolean isWorkingDirCreated() {
		String dir = TemplateVariable.getInstance().getValue(TemplateVariable.HMM_DIR);
		File file = new File(dir);
		if(file.exists()) {
			if(!file.isDirectory()) {
				new ShowMessageUtil().showNormalError("The Working Directory: \"" + file.getPath() + "\" is not a valid directory.");
				return false;
			}
			return true;
		}
		boolean create = new ShowMessageUtil().showQuestion("The Working Directory: \"" + file.getPath()
				 + "\" does not exist, it must be existed prior to run any Job.\n\nDo you want to create it?");
		if(create) {
			if(!file.mkdir()) {
				new ShowMessageUtil().showNormalError("Creat Working Directory: \"" + file.getPath() 
						+ "\" encountered an error.");
				return false;
			}
			return true;
		}
		return false;
	}
}
