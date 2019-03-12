package lee.iSpring.rest.dao.imp;

import java.util.ArrayList;
import java.util.List;

import lee.iSpring.rest.dao.TestDaoService;
import lee.iSpring.rest.entity.TestObject;

/**
 * 测试用 DAO
 */
public class TestDao implements TestDaoService {

	@Override
	public List<TestObject> getTest() {

		List<TestObject> lstTest = new ArrayList<TestObject>();
		TestObject test = new TestObject();
		TestObject test2 = new TestObject();
		TestObject test3 = new TestObject();

		test.setName("这是一条测试消息1！！！");
		test.setCode("1");
		lstTest.add(test);
		test2.setName("这是一条测试消息2！！！");
		test2.setCode("2");
		lstTest.add(test2);
		test3.setName("这是一条测试消息3！！！");
		test3.setCode("3");
		lstTest.add(test3);
		return lstTest;
	}
}
