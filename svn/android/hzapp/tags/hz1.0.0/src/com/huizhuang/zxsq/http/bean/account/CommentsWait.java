package com.huizhuang.zxsq.http.bean.account;

import java.io.Serializable;
import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

/**
 * @ClassName: CommentsWait
 * @Description: 待点评列表
 * @author th
 * @mail 342592622@qq.com
 * @date 2015-2-7 下午2:50:10
 * 
 */
public class CommentsWait implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String record_id;
	private String node_id;
	private String node_name;
	private String report_time;
	private String name;
	private Image image;
	private String content;
	private List<Score> rank;

	public String getRecord_id() {
		return record_id;
	}

	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}

	public String getNode_id() {
		return node_id;
	}

	public void setNode_id(String node_id) {
		this.node_id = node_id;
	}

	public String getNode_name() {
		return node_name;
	}

	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}

	public String getReport_time() {
		return report_time;
	}

	public void setReport_time(String report_time) {
		this.report_time = report_time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<Score> getRank() {
		return rank;
	}

	public void setRank(List<Score> rank) {
		this.rank = rank;
	}

	@Override
	public String toString() {
		return "CommentsWait [record_id=" + record_id + ", node_id=" + node_id
				+ ", node_name=" + node_name + ", report_time=" + report_time
				+ ", name=" + name + ", image=" + image + ", content="
				+ content + ", rank=" + rank + "]";
	}

}
