package com.huizhuang.zxsq.http.bean.supervision;

public class ComplaintsInfo {

    private String first_category_name;
    private String category_name;

    public String getFirst_category_name() {
        return first_category_name;
    }

    public void setFirst_category_name(String first_category_name) {
        this.first_category_name = first_category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    @Override
    public String toString() {
        return "ComplaintsInfo [first_category_name=" + first_category_name + ", category_name="
                + category_name + "]";
    }

}
