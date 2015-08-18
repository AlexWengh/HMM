package hmm.automation.models;

import java.io.File;
import java.lang.reflect.Method;

import hmm.automation.execute.ExecuteContext;
import hmm.automation.util.FileSystemClassLoader;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Class extends LogicTreeNode {
	
	private String classFilePath = "";

	public String getClassFilePath() {
		return classFilePath;
	}

	public void setClassFilePath(String classFilePath) {
		this.classFilePath = classFilePath;
	}

	@Override
	public String toString() {
		return "Class [Path =\"" + classFilePath + "\"]";
	}

	@Override
	public String getInvalidMessage() {
		return "Class File Path is not valid";
	}

	@Override
	public ExecuteContext execute(ExecuteContext context) throws Exception {
		int pos = classFilePath.lastIndexOf(File.separatorChar);
		String folder = classFilePath.substring(0, pos);
		String file = classFilePath.substring(pos + 1);
		file = file.replace(".class", "");
		FileSystemClassLoader loader = new FileSystemClassLoader(folder);
		java.lang.Class<?> classs = loader.findClass(file);
		Object obj = classs.newInstance();
		Method method = classs.getMethod("run");
		method.invoke(obj);
		return context;
	}

	@Override
	protected Element toXmlElement() {
		Element classElement = DocumentHelper.createElement("Class");
		Element pathElement = classElement.addElement("filePath");
		pathElement.addText(classFilePath);
		return classElement;
	}

	@Override
	protected TreeNode createFromXmlElement(TreeNode parent, Element element) {
		Class classs = new Class();
		Element pathElement = (Element) element.selectSingleNode("filePath");
		classs.setClassFilePath(pathElement.getText());
		return classs;
	}

}
