package com.huizhuang.zxsq.http.bean;

import java.io.Serializable;

public class CityOpen implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String name_abridge;
    private int parent_id;
    private int level;

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

    public String getName_abridge() {
        return name_abridge;
    }

    public void setName_abridge(String name_abridge) {
        this.name_abridge = name_abridge;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "CityOpen [id=" + id + ", name=" + name + ", name_abridge=" + name_abridge
                + ", parent_id=" + parent_id + ", level=" + level + "]";
    }

}
