package com.huizhuang.zxsq.module;

import java.io.Serializable;
import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class ZxbdAnswer implements Serializable, Parcelable {
	/** 
		*/
	private static final long serialVersionUID = 1L;
	public int type;
	public String title;
	public String content;
	public int needSubmit;
	public ArrayList<ZxbdAnswer> childs;

	public static final Parcelable.Creator<ZxbdAnswer> CREATOR = new Parcelable.Creator<ZxbdAnswer>() {
		@Override
		public ZxbdAnswer createFromParcel(Parcel source) {
			ZxbdAnswer user = new ZxbdAnswer();
			user.type = (source.readInt());
			user.content = (source.readString());
			user.needSubmit=source.readInt();
			return user;
		}

		@Override
		public ZxbdAnswer[] newArray(int size) {
			return new ZxbdAnswer[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		out.writeInt(type);
		out.writeString(content);
		out.writeInt(needSubmit);
	}

	@Override
	public String toString() {
		return "ZxbdAnswer [type=" + type + ", title=" + title + ", content="
				+ content + ", needSubmit=" + needSubmit + ", childs=" + childs
				+ "]";
	}
	
	
}
