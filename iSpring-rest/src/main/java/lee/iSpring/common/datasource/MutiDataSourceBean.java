package lee.iSpring.common.datasource;


import lee.iSpring.common.bean.DBConnString;
import lee.iSpring.common.util.DBConnStrUtil;
import lee.iSpring.common.util.LoggerUtil;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MutiDataSourceBean extends AbstractRoutingDataSource {


	private static String defaultDBConnID = "0";
	private Map<Object,DataSource> dsMap  = new HashMap<Object, DataSource>();

	@Override
	public void afterPropertiesSet(){
		synchronized (objHelper) {
			try {
				initailizeDefaltDataSource();
				super.afterPropertiesSet();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化默认添加一个数据源
	 */
	private void initailizeDefaltDataSource(){

		try {
			DBConnString dbConnString = DBConnStrUtil.getDBConnStringByID(defaultDBConnID);
			DataSource dataSource = initDataSource(dbConnString.getUserID(),dbConnString.getPassword(),dbConnString.getUrl(),dbConnString.getDriverName());
			dsMap.put(dbConnString.getDbConnID(), dataSource);
			this.setTargetDataSources(dsMap);
			setDefaultTargetDataSource(dataSource);
			DataSourceContextHolder.defaultDBConnID = dbConnString.getDbConnID();
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	private DataSource initDataSource(String userName, String password, String url, String driver){

		Properties properties = new Properties();
		properties.setProperty("driverClassName", driver);
		properties.setProperty("url", url);
		properties.setProperty("username", userName);
		properties.setProperty("password", password);
		DataSource dataSource = null;

		try {
		  Properties resource = new Properties();
		  InputStream in = getClass().getResourceAsStream("/dbcp.properties");
		  resource.load(in);
		  properties.setProperty("initialSize", StringUtils.trim(resource.getProperty("initialSize")));
		  properties.setProperty("maxActive", StringUtils.trim(resource.getProperty("maxActive")));
		  properties.setProperty("minIdle",  StringUtils.trim(resource.getProperty("minIdle")));
		  properties.setProperty("maxIdle",  StringUtils.trim(resource.getProperty("maxIdle")));
		  properties.setProperty("maxWait", StringUtils.trim(resource.getProperty("maxWait")));
		  properties.setProperty("validationQuery", StringUtils.trim(resource.getProperty("validationQuery")));
		  properties.setProperty("testWhileIdle", StringUtils.trim(resource.getProperty("testWhileIdle")));
		  properties.setProperty("testOnBorrow", StringUtils.trim(resource.getProperty("testOnBorrow")));
		  properties.setProperty("timeBetweenEvictionRunsMillis", StringUtils.trim(resource.getProperty("timeBetweenEvictionRunsMillis")));
		  properties.setProperty("minEvictableIdleTimeMillis", StringUtils.trim(resource.getProperty("minEvictableIdleTimeMillis")));
		  properties.setProperty("numTestsPerEvictionRun", StringUtils.trim(resource.getProperty("numTestsPerEvictionRun")));
		  properties.setProperty("removeAbandoned", StringUtils.trim(resource.getProperty("removeAbandoned")));
		  properties.setProperty("removeAbandonedTimeout", StringUtils.trim(resource.getProperty("removeAbandonedTimeout")));
		  dataSource = org.apache.commons.dbcp.BasicDataSourceFactory.createDataSource(properties);
		}catch (Exception e){
			LoggerUtil.error("数据源创建失败！ driverName="+driver, e);
		}
		 return dataSource;
    }

	/*
	 * 将获取的数据源缓存起来
	 */
    public void addDataSource(DBConnString dbConnString) throws NumberFormatException, RemoteException, Exception {
   	 synchronized (objHelper) {
		String driverName = dbConnString.getDriverName();
		DataSource dataSource = initDataSource(dbConnString.getUserID(), dbConnString.getPassword(), dbConnString.getUrl(), driverName);
		super.addDataSource(dbConnString.getDbConnID(), dataSource);
   	   }
    }

	/**
	 * 该动态切换数据源方案的关键 1 将数据源以key-dataSource的形式保存在map中
	 *                        2 实现该方法，当进行数据库连接时会会调用此方法获取数据源
	 */
	@Override
	protected Object determineCurrentLookupKey() {
		String dataSource = DataSourceContextHolder.getDataSourceType();
        return dataSource;
	}

	@Override
	public void setDefaultTargetDataSource(Object defaultTargetDataSource) {
		super.setDefaultTargetDataSource(defaultTargetDataSource);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setTargetDataSources(@SuppressWarnings("rawtypes") Map targetDataSources) {
		super.setTargetDataSources(targetDataSources);
	}
}
