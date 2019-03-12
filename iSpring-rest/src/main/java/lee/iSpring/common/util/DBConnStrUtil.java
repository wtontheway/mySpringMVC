package lee.iSpring.common.util;

import lee.iSpring.common.bean.DBConnString;
import lee.iSpring.common.constant.StateCode;
import lee.iSpring.common.datasource.DataSourceContextHolder;
import lee.iSpring.common.datasource.MutiDataSourceBean;
import lee.iSpring.common.exception.DataAccessObjectException;
import org.springframework.util.ObjectUtils;


public class DBConnStrUtil {

    public static void setDataSourceTypeByID(String mainID) throws Exception{
        try {
            MutiDataSourceBean mutiSource = SpringContextHolder.getBean("mutiDataSourceBean", MutiDataSourceBean.class);
            DBConnString dbConnString = getDBConnStringByID(mainID);
            if(!mutiSource.isContainDataSource(dbConnString.getDbConnID())){
                mutiSource.addDataSource(dbConnString);
            }
            //动态设置当前线程数据源
            DataSourceContextHolder.setDataSource(dbConnString.getDbConnID());
        }catch (Exception e){
            throw new DataAccessObjectException(StateCode.DATASOURCE_ERROR,e);
        }
    }

    /**
     * 根据关键属性，获取数据库连接字符串
     * @param id
     * @return
     * @throws Exception
     */
    public static DBConnString getDBConnStringByID(String id)throws Exception{

        DBConnString dbConnString = new DBConnString();

        // TODO: 根据id从配置中心获取数据库配置，应加入缓存

        if (id.equals("0")){
            dbConnString.setDbConnID("0");
            dbConnString.setDataBase("TestDB");
            dbConnString.setUserID("sa");
            dbConnString.setPassword("r123456");
            dbConnString.setUrl("jdbc:mysql://127.0.0.1:3066/TestDB?useSSL=false&useUnicode=true&characterEncoding=UTF-8");
            dbConnString.setDriverName("com.mysql.jdbc.Driver");
        }else if (id.equals("1")){
            dbConnString.setDbConnID("1");
            dbConnString.setDataBase("TestDB");
            dbConnString.setUserID("sa");
            dbConnString.setPassword("r123456");
            dbConnString.setUrl("jdbc:mysql://127.0.0.1:1433/TestDB?useSSL=false&useUnicode=true&characterEncoding=UTF-8");
            dbConnString.setDriverName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        }else {
            dbConnString =null;
        }

        if (ObjectUtils.isEmpty(dbConnString)){throw new DataAccessObjectException(StateCode.DATASOURCE_NULL_ERROR);}
        return dbConnString;
    }
}
