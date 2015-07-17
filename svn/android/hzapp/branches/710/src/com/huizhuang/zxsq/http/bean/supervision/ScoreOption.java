package com.huizhuang.zxsq.http.bean.supervision;

public class ScoreOption {
    private int item_id;
    private String name;

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ScoreOption [item_id=" + item_id + ", name=" + name + "]";
    }

}
