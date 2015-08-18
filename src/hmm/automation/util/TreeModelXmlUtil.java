package hmm.automation.util;

import hmm.automation.models.Root;
import hmm.automation.models.TreeNode;
import hmm.build.util.ShowMessageUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class TreeModelXmlUtil {
	
	public void saveToXml(TreeNode root, File xmlFile) {
		Document document = DocumentHelper.createDocument();
		Element element = (Element) root.toXml().clone();
		document.add(element);
		for (TreeNode child : root.getChildren()) {
			saveNodeToXml(child, element);
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
			new ShowMessageUtil().showNormalError(e.getMessage());
			return;
		}
		XMLWriter writer = new XMLWriter(fileWriter, format);
		try {
			writer.write(document);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError(e.getMessage());
			return;
		}
	}
	
	private void saveNodeToXml(TreeNode node, Element element) {
		Element subElement = (Element) node.toXml().clone();
		element.add(subElement);
		for (TreeNode child : node.getChildren()) {
			saveNodeToXml(child, subElement);
		}
	}
	public TreeNode loadFromXml(File xmlFile) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new FileReader(xmlFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("xml file not found: " + e.getMessage());
		} catch (DocumentException e) {
			e.printStackTrace();
			new ShowMessageUtil().showNormalError("parse xml exception: " + e.getMessage());
		}
		
		Element rootElement = document.getRootElement();
		TreeNode root = null;
		root = new Root().createFromXml(null, rootElement);
		if(root == null) {
			new ShowMessageUtil().showNormalError("parse xml exception: " + "cannot find root element");
			return root;
		}
		for (Iterator<?> it = rootElement.elementIterator(); it.hasNext();) {    
            Element element = (Element) it.next();
            loadNodeFromXml(root, element);
		}
		return root;
	}
	
	private void loadNodeFromXml(TreeNode parent, Element element) {
		TreeNode curNode = null;
		TreeNode temp = null;
		try {
			temp = (TreeNode) Class.forName(parent.getClass().getPackage().getName() + "." + element.getName()).newInstance();
		} catch (ClassNotFoundException e) {
			return;
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		curNode = temp.createFromXml(parent, element);
		parent.addChild(curNode);
		for (Iterator<?> it = element.elementIterator(); it.hasNext();) {    
            Element subElement = (Element) it.next();
            loadNodeFromXml(curNode, subElement);
		}
	}
	
	public boolean isTreeValid(TreeNode node) {
		if(node.isValid()) {
			for (TreeNode child : node.getChildren()) {
				if(!isTreeValid(child))
					return false;
			}
			return true;
		}
		return false;
	}

}
