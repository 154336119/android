package com.huizhuang.zxsq.http.bean.foreman;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;
/**
 * 工长口碑
 * @author admin
 *
 */
public class ForemanComment {
	
	private int id;//id
	private String name;//评论人的姓名（电话）
	private Image image;
	private String content;//评论的内容
	private Float rank;//工长的分数
	private String time;//时间
	private int useful_count;
	private int useless_count;
	private int count;
	private String node_name;//评论的节点名称
	private String operator_id;//评论人id
	private List<String> problems;//存在的问题列表
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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
    public Float getRank() {
        return rank;
    }
    public void setRank(Float rank) {
        this.rank = rank;
    }
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public int getUseful_count() {
        return useful_count;
    }
    public void setUseful_count(int useful_count) {
        this.useful_count = useful_count;
    }
    public int getUseless_count() {
        return useless_count;
    }
    public void setUseless_count(int useless_count) {
        this.useless_count = useless_count;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }
    
    public String getNode_name() {
		return node_name;
	}
	public void setNode_name(String node_name) {
		this.node_name = node_name;
	}
	public List<String> getProblems() {
		return problems;
	}
	public void setProblems(List<String> problems) {
		this.problems = problems;
	}
	public String getOperator_id() {
		return operator_id;
	}
	public void setOperator_id(String operator_id) {
		this.operator_id = operator_id;
	}
	@Override
    public String toString() {
        return "ForemanComment [id=" + id + ", name=" + name + ", image=" + image + ", content="
                + content + ", rank=" + rank + ", time=" + time + ", useful_count=" + useful_count
                + ", useless_count=" + useless_count + ", count=" + count + "]";
    }
	
}
