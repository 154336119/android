package com.huizhuang.zxsq.module;

import java.util.List;

public class MyMessage {
	
	private int totalrecord;
	private int totalpage;
	private List<Message> messageList;
	
	public class Message{
		private int id;
		private String detailUrl;
		private String content;
		private String userThumb;
		private int userId;
		private int read;//1 已读 2 未读
		private long addTime;
		private int messageType;
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public String getDetailUrl() {
			return detailUrl;
		}
		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getUserThumb() {
			return userThumb;
		}
		public void setUserThumb(String userThumb) {
			this.userThumb = userThumb;
		}
		public int getUserId() {
			return userId;
		}
		public void setUserId(int userId) {
			this.userId = userId;
		}
		public int getRead() {
			return read;
		}
		public void setRead(int read) {
			this.read = read;
		}
		public long getAddTime() {
			return addTime;
		}
		public void setAddTime(long addTime) {
			this.addTime = addTime;
		}
		public int getMessageType() {
			return messageType;
		}
		public void setMessageType(int messageType) {
			this.messageType = messageType;
		}
		@Override
		public String toString() {
			return "Message [id=" + id + ", detailUrl=" + detailUrl
					+ ", content=" + content + ", userThumb=" + userThumb
					+ ", userId=" + userId + ", read=" + read + ", addTime="
					+ addTime + ", messageType=" + messageType + "]";
		}
		
		
	}

	@Override
	public String toString() {
		return "MyMessage [totalrecord=" + getTotalrecord() + ", totalpage="
				+ getTotalpage() + ", messageList=" + getMessageList() + "]";
	}

	public int getTotalrecord() {
		return totalrecord;
	}

	public void setTotalrecord(int totalrecord) {
		this.totalrecord = totalrecord;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public List<Message> getMessageList() {
		return messageList;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}
	
	

}
