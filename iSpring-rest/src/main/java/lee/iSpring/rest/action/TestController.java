package lee.iSpring.rest.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lee.iSpring.common.bean.Request;
import lee.iSpring.common.bean.RestResponse;
import lee.iSpring.common.util.DBConnStrUtil;
import lee.iSpring.rest.entity.TestObject;
import lee.iSpring.rest.service.TestBizService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class TestController extends BaseRestController {

	@Resource(name = "testBiz")
	private TestBizService testBiz;

	@RequestMapping(value = "/getTest", method = RequestMethod.GET)
	public RestResponse<List<TestObject>> getTest() throws Exception {

		List<TestObject> result = testBiz.getTestList();

		return success(result);
	}

	@RequestMapping(value = "/postTest", method = RequestMethod.POST)
	public RestResponse<Map<String,Object>> postTest(@RequestBody Request request) throws Exception {

		Map<String,Object> result = (HashMap)request;

		return success(result);
	}

	@RequestMapping(value = "/getSqlTest", method = RequestMethod.GET)
	public RestResponse<Integer> getSqlTest() throws Exception {
		DBConnStrUtil.setDataSourceTypeByID("1");
		return success(testBiz.sqlTest());
	}

	@RequestMapping(value = "/getTest1/{path}", method = RequestMethod.GET)
	public RestResponse<Map<String,Object>> getSTest1(@RequestParam Integer params, @PathVariable String path) throws Exception {

		Map<String,Object>  result = new HashMap<>();
		result.put(path,params);
		return success(result);
	}

	@RequestMapping(value = "/getTest2/{param1}/{param2}", method = RequestMethod.GET)
	public RestResponse<Map<String,Object>> getSTest2(@PathVariable Integer param1,@PathVariable Integer param2) throws Exception {

		Map<String,Object>  result = new HashMap<>();
		result.put("param1",param1);
		result.put("param2",param2);
		return success(result);
	}
}
