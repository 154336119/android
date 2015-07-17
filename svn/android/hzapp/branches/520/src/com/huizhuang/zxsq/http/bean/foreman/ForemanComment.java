package com.huizhuang.zxsq.http.bean.foreman;

import com.huizhuang.zxsq.http.bean.common.Image;
/**
 * 工长口碑
 * @author admin
 *
 */
public class ForemanComment {
	
	private int id;
	private String name;
	private Image image;
	private String content;
	private Float rank;
	private String time;
	private int useful_count;
	private int useless_count;
	private int count;
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
    @Override
    public String toString() {
        return "ForemanComment [id=" + id + ", name=" + name + ", image=" + image + ", content="
                + content + ", rank=" + rank + ", time=" + time + ", useful_count=" + useful_count
                + ", useless_count=" + useless_count + ", count=" + count + "]";
    }
	
}
