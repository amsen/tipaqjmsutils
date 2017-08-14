package tip.apoteket.util;

import java.util.Properties;

public class Configuration {

	Properties _configFile;
	String _env;
	String _integration;
	
	public Configuration(String env, String integration){
		_configFile = new Properties();
		_env = env;
		_integration = integration;
		try{
			_configFile.load(this.getClass().getClassLoader().getResourceAsStream("config.properties"));
		}
		catch(Exception ex){
			System.out.println("Could not load config file. Please check the path.");
			ex.printStackTrace();
		}
	}
	
	public String getProperty(String key) throws Exception{
		if(this._configFile.getProperty(_env+"."+_integration+"."+key) != null){
			return this._configFile.getProperty(_env+"."+_integration+"."+key);
		}
		else if(this._configFile.getProperty(_env+"."+key) != null){
			return this._configFile.getProperty(_env+"."+key);
		}
		else{
			throw new Exception("Error reading configuration!!!");
		}
	}
	
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration("sitp", "items");
		System.out.println(conf.getProperty("servername"));
	}

}