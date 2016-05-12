package connection;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesLoader {
	InputStream inputStream;
	Map<String, String> result = new HashMap<String, String>();
	
	public Map<String, String> getPropValues() throws IOException{
		try {
			Properties prop = new Properties();
			String propFileName = "config.properties";
			inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
			//inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(propFileName);
			
			if (inputStream != null) {
				prop.load(inputStream);
			} else {
				throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
			}
			
			
			result.put("url", prop.getProperty("url"));
			result.put("user", prop.getProperty("user"));
			result.put("password", prop.getProperty("password"));
			
		}
		finally{
			
		}
		return result;
	}
}
