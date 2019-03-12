package lee.iSpring.common.bean;

import java.util.List;

/**
 * 分页实体类
 */
public class Page<T> {

	private static final int DEFAULT_PAGE_SIZE = 20;

	private int pageSize = DEFAULT_PAGE_SIZE;// 每页记录数
	private int start = 0;// 当前页第一条数据在List中的位置，从0开始
	private int page = 1;// 当前页
	private int totalPage = 0;// 总计有多少页
	private int totalCount = 0;// 总记录数
	private List<T> result;// 查询结果

	public Page() {
	}

	public Page(int page) {
		
		if (page > 0) {
			this.page = page;
		}
	}

	public Page(int page, int pageSize) {
		
		if (page > 0 && pageSize > 0) {
			this.page = page;
			this.pageSize = pageSize;
		}
	}

	public int getPageSize() {
		
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		
		this.pageSize = pageSize;
	}

	public int getStart() {
		
		return start;
	}

	public int getPage() {
		
		return page;
	}

	public void setPage(int page) {
		
		if (page > 0) {
			start = (page - 1) * pageSize;
			this.page = page;
		}
	}

	public int getTotalPage() {
		
		return totalPage;
	}

	public int getTotalCount() {
		
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		
		this.totalCount = totalCount;
		totalPage = (totalCount + pageSize - 1) / pageSize;
		start = (page - 1) * pageSize;
	}

	/**
	 * 获取上一页页数
	 */
	public int getLastPage() {
		
		if (hasLastPage()) {
			return page - 1;
		}
		return page;
	}

	/**
	 * 获取下一页页数
	 */
	public int getNextPage() {
		
		if (hasNextPage()) {
			return page + 1;
		}
		return page;
	}

	/**
	 * 判断该页是否有下一页
	 */
	public boolean hasNextPage() {
		
		return page < totalPage;
	}

	/**
	 * 判断该页是否有上一页
	 */
	public boolean hasLastPage() {
		
		return page > 1;
	}

	public List<T> getResult() {
		
		return result;
	}

	public void setResult(List<T> result) {
		
		this.result = result;
	}
}
