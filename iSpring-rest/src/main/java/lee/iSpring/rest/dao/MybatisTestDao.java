package lee.iSpring.rest.dao;

import org.apache.ibatis.annotations.Param;


/**
 * Created by user on 2018/4/20.
 */
public interface MybatisTestDao {

    Integer sqlTest(@Param("id")Integer id)throws Exception;
}
