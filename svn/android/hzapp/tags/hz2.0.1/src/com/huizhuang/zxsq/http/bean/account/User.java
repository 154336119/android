package com.huizhuang.zxsq.http.bean.account;

import android.os.Parcel;
import android.os.Parcelable;

import com.huizhuang.zxsq.http.bean.base.BaseBean;
import com.huizhuang.zxsq.http.bean.common.Image;

import java.io.Serializable;

/**
 * 登录用户
 * 
 */
/** 
* @ClassName: User 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author lim 
* @mail limshare@gmail.com   
* @date 2014-9-28 下午4:35:07 
*  
*/
public class User implements BaseBean, Serializable, Parcelable {

	private static final long serialVersionUID = 1L;

	// 0无效用户、1正常用户、2未审核、3该账户或则密码不存在
	public static final String STATUS_INVALIDATE = "0";
	public static final String STATUS_NORMAL = "1";
	public static final String STATUS_VERIFY = "2";
	public static final String STATUS_INEXISTENCE = "3";
	
	private String id;
	private String openId;
	private String platName;
	private String username;
	private String password;
	private String phone;
	private String name;
	private String nickname;
	private String screenName;
	private String age;
	private String sex;
	private boolean isOpenLogin;
	private String address;
	private String avatar;//头像地址
	private String email;
	private int state;
	private boolean isCDUser;
	private String registerTime;
	private String lastLoginTime;
	private String remark;
	private String city;
	private boolean isComplete;
	private int orderId;
	private boolean isFollowed;
	private String token;
	
	private String user_id;
	private int gender;
	private Image user_thumb;
	private String screen_name;
	private int is_cd;
	private int is_complete;
	private int if_temp_pwd;
	private int is_local_user;
	private int following_count;
	private int followed_count;
	private String account_amount;
	private int complete_comment;
	private int to_comment;
	private int msg_count;
	private String province;
	private String mobile;
	private int order_changed;
	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		@Override
		public User createFromParcel(Parcel source) {
			// 序列化user对象
			User user = new User();
			user.setId(source.readString());
			user.setUsername(source.readString());
			user.setNickname(source.readString());
			user.setAge(source.readString());
			user.setSex(source.readString());
			user.setAvatar(source.readString());
			return user;
		}

		@Override
		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(user_id);
		dest.writeString(username);
		dest.writeString(nickname);
		dest.writeString(age);
		dest.writeString(sex);
		dest.writeString(avatar);
	}

	public int getOrder_changed() {
		return order_changed;
	}

	public void setOrder_changed(int order_changed) {
		this.order_changed = order_changed;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
	
	public String getPlatName() {
		return platName;
	}

	public void setPlatName(String platName) {
		this.platName = platName;
	}

	public String getUsername() {
		return username;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}
	
	public String getScreenName() {
		return screenName;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}

	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean isCDUser() {
		return isCDUser;
	}

	public void setCDUser(boolean isCDUser) {
		this.isCDUser = isCDUser;
	}

	public String getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(String registerTime) {
		this.registerTime = registerTime;
	}

	public String getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(String lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(boolean iscp) {
		this.isComplete = iscp;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	
	public boolean getIsOpenLogin(){
		return isOpenLogin;
	}

	public void setIsOpenLogin(boolean isOpenLogin) {
		this.isOpenLogin = isOpenLogin;
	}

	public boolean getFollwoed(){
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	
	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public Image getUser_thumb() {
		return user_thumb;
	}

	public void setUser_thumb(Image user_thumb) {
		this.user_thumb = user_thumb;
	}

	public String getScreen_name() {
		return screen_name;
	}

	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}

	public int getIs_cd() {
		return is_cd;
	}

	public void setIs_cd(int is_cd) {
		this.is_cd = is_cd;
	}

	public int getIs_complete() {
		return is_complete;
	}

	public void setIs_complete(int is_complete) {
		this.is_complete = is_complete;
	}

	public int getIs_local_user() {
		return is_local_user;
	}

	public void setIs_local_user(int is_local_user) {
		this.is_local_user = is_local_user;
	}

	public int getFollowing_count() {
		return following_count;
	}

	public void setFollowing_count(int following_count) {
		this.following_count = following_count;
	}

	public int getFollowed_count() {
		return followed_count;
	}

	public void setFollowed_count(int followed_count) {
		this.followed_count = followed_count;
	}

	public String getAccount_amount() {
		return account_amount;
	}

	public void setAccount_amount(String account_amount) {
		this.account_amount = account_amount;
	}

	public int getTo_comment() {
		return to_comment;
	}

	public void setTo_comment(int to_comment) {
		this.to_comment = to_comment;
	}

	public int getMsg_count() {
		return msg_count;
	}

	public void setMsg_count(int msg_count) {
		this.msg_count = msg_count;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setOpenLogin(boolean isOpenLogin) {
		this.isOpenLogin = isOpenLogin;
	}

	public void setComplete(boolean isComplete) {
		this.isComplete = isComplete;
	}

	
	public int getComplete_comment() {
		return complete_comment;
	}

	public void setComplete_comment(int complete_comment) {
		this.complete_comment = complete_comment;
	}

	public int getIf_temp_pwd() {
		return if_temp_pwd;
	}

	public void setIf_temp_pwd(int if_temp_pwd) {
		this.if_temp_pwd = if_temp_pwd;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", openId=" + openId + ", platName="
				+ platName + ", username=" + username + ", password="
				+ password + ", phone=" + phone + ", name=" + name
				+ ", nickname=" + nickname + ", screenName=" + screenName
				+ ", age=" + age + ", sex=" + sex + ", isOpenLogin="
				+ isOpenLogin + ", address=" + address + ", avatar=" + avatar
				+ ", email=" + email + ", state=" + state + ", isCDUser="
				+ isCDUser + ", registerTime=" + registerTime
				+ ", lastLoginTime=" + lastLoginTime + ", remark=" + remark
				+ ", city=" + city + ", isComplete=" + isComplete
				+ ", orderId=" + orderId + ", isFollowed=" + isFollowed
				+ ", token=" + token + ", user_id=" + user_id + ", gender="
				+ gender + ", user_thumb=" + user_thumb + ", screen_name="
				+ screen_name + ", is_cd=" + is_cd + ", is_complete="
				+ is_complete + ", is_local_user=" + is_local_user
				+ ", following_count=" + following_count + ", followed_count="
				+ followed_count + ", account_amount=" + account_amount
				+ ", to_comment=" + to_comment + ", msg_count=" + msg_count
				+ ", province=" + province + "]";
	}


}
