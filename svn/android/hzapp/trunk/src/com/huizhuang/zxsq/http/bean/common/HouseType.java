package com.huizhuang.zxsq.http.bean.common;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class HouseType implements Serializable, Parcelable {

    /**
     * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么)
     */
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private float area;

    public static final Parcelable.Creator<HouseType> CREATOR = new Parcelable.Creator<HouseType>() {
        @Override
        public HouseType createFromParcel(Parcel source) {
            // 序列化HouseType对象
            HouseType houseType = new HouseType();
            houseType.setId(source.readInt());
            houseType.setName(source.readString());
            houseType.setArea(source.readFloat());
            return houseType;
        }

        @Override
        public HouseType[] newArray(int size) {
            return new HouseType[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeFloat(area);
    }
    
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

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

    @Override
    public String toString() {
        return "HouseType [id=" + id + ", name=" + name + ", area=" + area + "]";
    }

}
