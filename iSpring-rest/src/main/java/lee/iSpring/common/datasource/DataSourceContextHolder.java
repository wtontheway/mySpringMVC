package lee.iSpring.common.datasource;


import lee.iSpring.common.constant.RequestCacheDataItem;
import lee.iSpring.common.util.DBConnStrUtil;

public class DataSourceContextHolder {

	public static String defaultDBConnID = "0";

	public static void setDataSource(String key) {
		RequestCacheDataItem.DB_CONN_ID.set(key);
	}

	public static String getDataSourceType() {
		String dbConnID = RequestCacheDataItem.DB_CONN_ID.get();
		if (dbConnID !=null) {
			return dbConnID;
		}
		// 如果没有设置数据源，则看一下有没有mainID缓存，有mainID缓存就重新设置数据源
		String mainID = RequestCacheDataItem.MAIN_ID.get();
		if (mainID != null) {
			try {
				DBConnStrUtil.setDataSourceTypeByID(mainID);
				dbConnID = RequestCacheDataItem.DB_CONN_ID.get();// 重新获取
				return dbConnID;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		//如果都没有就使用默认数据源
		return defaultDBConnID;
	}

	public static void clearDataSourceType() {
		RequestCacheDataItem.DB_CONN_ID.remove();
	}
}
