package mcdata.api.common;

import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {

	private Properties prop = null;

	public PropertiesReader(String file) throws Exception {
		InputStream is = PropertiesReader.class.getResourceAsStream("/" + file);
		// System.out.println("../../../../"+file);
		prop = new Properties();
		prop.load(is);
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		if (prop != null) {
			return prop.getProperty(key);
		} else {
			return null;
		}
	}

}