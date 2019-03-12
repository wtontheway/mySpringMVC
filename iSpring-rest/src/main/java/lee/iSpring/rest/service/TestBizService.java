package lee.iSpring.rest.service;

import java.util.List;
import java.util.Map;

import lee.iSpring.rest.entity.TestObject;


public interface TestBizService {

	List<TestObject> getTestList()throws Exception;

	Integer sqlTest()throws Exception;
}
