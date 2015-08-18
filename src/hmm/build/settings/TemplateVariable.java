package hmm.build.settings;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TemplateVariable {
	
	private static TemplateVariable templateVariable = new TemplateVariable();
	
	private Map<String, String> map = new HashMap<String, String>();
		
	public final static String HMM_DIR = "${HMMDIR}";
	public final static String BUILD_DIR = "${BUILD}";

	private TemplateVariable() {
		map.put(BUILD_DIR, "yyyy-MM-dd_HH_mm_ss");
		String workingDir = File.listRoots()[0].getAbsolutePath() + "HanMeiMei";
		map.put(HMM_DIR, workingDir);
	}
	
	public static TemplateVariable getInstance() {
		return templateVariable;
	}
	
	public Map<String, String> getTemplateVariables() {
		return map;
	}
	
	public void addTemplateVariable(String name, String value) {
		if(map.containsKey(name))
			return;
		map.put(name, value);
	}
	
	public void modifyTemplateVariable(String name, String newValue) {
		map.put(name, newValue);
	}
	
	public void removeTempateVariable(String name) {
		map.remove(name);
	}
	
	public String getValue(String name) {
		return map.get(name);
	}
	
}
