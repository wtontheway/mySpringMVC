package lee.iSpring.rest.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import lee.iSpring.rest.dao.MybatisTestDao;
import lee.iSpring.rest.dao.TestDaoService;
import lee.iSpring.rest.entity.TestObject;
import lee.iSpring.rest.service.TestBizService;

public class TestBiz implements TestBizService {

	@Resource(name = "testDao")
	private TestDaoService testDao;

	@Autowired
	private MybatisTestDao mybatisTestDao;

	@Override
	public List<TestObject> getTestList()throws Exception {

		return testDao.getTest();
	}

	public Integer sqlTest()throws Exception{
		return mybatisTestDao.sqlTest(0);
	}
}
