package tip.apoteket.util;

import oracle.jdbc.pool.OracleDataSource;

public class DataSource {

	public static OracleDataSource getDataStore(String env) throws Exception{
		OracleDataSource ds = new OracleDataSource();
		
		Configuration config = new Configuration(env,null);
		
		ds.setDriverType("thin");
		ds.setServerName(config.getProperty("servername"));
		ds.setPortNumber(new Integer(config.getProperty("portnumber")));
		ds.setDatabaseName(config.getProperty("databasename"));
		ds.setUser(config.getProperty("user"));
		ds.setPassword(config.getProperty("password"));
		
		return ds;
	}
	
	
	
	public static void main(String[] args) throws Exception {
		OracleDataSource ds = getDataStore("dev");
		System.out.println(ds.getDatabaseName() + ds.getPortNumber());
	}

}
