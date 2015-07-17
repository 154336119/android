package com.huizhuang.zxsq.http.bean.solution;

import java.io.Serializable;
import java.util.List;

/**
 * 施工现场列表
 * 
 * @author th
 * 
 */
public class ConstructionSiteList implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private int id;
    private String housing_name;
    private List<ConstructionSite> subcase;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHousing_name() {
        return housing_name;
    }

    public void setHousing_name(String housing_name) {
        this.housing_name = housing_name;
    }

    public List<ConstructionSite> getSubcase() {
        return subcase;
    }

    public void setSubcase(List<ConstructionSite> subcase) {
        this.subcase = subcase;
    }

    @Override
    public String toString() {
        return "ConstructionSiteList [id=" + id + ", housing_name=" + housing_name + ", subcase="
                + subcase + "]";
    }

}
