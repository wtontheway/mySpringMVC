package lee.iSpring.common.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoUtil {

//    @Autowired
    private MongoTemplate mongo;

    /**
     * 查询query数据
     *
     * @param query
     * @param clazz
     * @param tableName
     */
    public  <T> List<T> find(Query query, Class<T> clazz, String tableName) {
        List<T> result = mongo.find(query, clazz, tableName);
        return result;
    }

    /**
     * 更新query的First数据
     *
     * @param query
     * @param update
     * @param clazz
     * @param tableName
     */
    public <T> void updateFirst(Query query, Update update, Class<T> clazz, String tableName) {
        mongo.updateFirst(query, update, clazz, tableName);
    }

    /**
     * 写入data数据
     *
     * @param tableName
     */
    public <T> void insert(T data, String tableName) {
        mongo.insert(data, tableName);
    }

    /**
     * 清理query数据
     *
     * @param tableName
     */
    public void remove(Query query, String tableName){
        mongo.remove(query, tableName);
    }

    /**
     * 写入data列表数据
     *
     * @param tableName
     */
    public <T> void insert(List<T> data, String tableName) {
        mongo.insert(data, tableName);
    }

}
