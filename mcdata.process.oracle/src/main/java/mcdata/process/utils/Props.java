package mcdata.process.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class Props {

	public static String getProp(String propName) {
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream("mcdata.properties"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prop.getProperty(propName);
	}
	
}
