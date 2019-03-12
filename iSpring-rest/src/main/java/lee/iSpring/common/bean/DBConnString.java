package lee.iSpring.common.bean;

public class DBConnString {

    private String dbConnID;
    private String userID;
    private String password;
    private String dataBase;
    private String url;
    private String driverName;


    public String getDbConnID() {
        return dbConnID;
    }

    public void setDbConnID(String dbConnID) {
        this.dbConnID = dbConnID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDataBase() {
        return dataBase;
    }

    public void setDataBase(String dataBase) {
        this.dataBase = dataBase;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
