package com.huizhuang.zxsq.http.bean.base;

import java.util.ArrayList;
import java.util.Collection;


/**
 * @ClassName: Group
 * @Package com.huizhuang.zxsq.module
 * @Description: 
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014年6月17日  下午5:54:07
 * @param <T>
 */
public class Group<T> extends ArrayList<T> implements BaseBean {

    private static final long serialVersionUID = 1L;

    private int pageTotal;
    private int pageIndex;
    private int totalSize;

    public Group() {
        super();
    }

    public Group(Collection<T> collection) {
        super(collection);
    }

	public int getPageTotal() {
		return pageTotal;
	}

	public void setPageTotal(int pageTotal) {
		this.pageTotal = pageTotal;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(int totalSize) {
		this.totalSize = totalSize;
	}
}
