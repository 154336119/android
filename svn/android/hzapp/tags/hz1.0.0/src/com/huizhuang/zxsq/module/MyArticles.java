package com.huizhuang.zxsq.module;

import java.util.List;

public class MyArticles {
	
	private int totalRecord;
	private int totalpage;
	
	private List<ArticleDigest> acArticleDigestList;
	
	public class ArticleDigest{
		private String cntType;
		private int cntId;
		private String title;
		private String digest;
		private String thumb;
		private int shareCount;
		private int ffCount;
		private int commentCount;
		private String detailUrl;
		private long addTime;
		private int top;
		public String getCntType() {
			return cntType;
		}
		public void setCntType(String cntType) {
			this.cntType = cntType;
		}
		public int getCntId() {
			return cntId;
		}
		public void setCntId(int cntId) {
			this.cntId = cntId;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}
		public String getDigest() {
			return digest;
		}
		public void setDigest(String digest) {
			this.digest = digest;
		}
		public String getThumb() {
			return thumb;
		}
		public void setThumb(String thumb) {
			this.thumb = thumb;
		}
		public int getShareCount() {
			return shareCount;
		}
		public void setShareCount(int shareCount) {
			this.shareCount = shareCount;
		}
		public int getCommentCount() {
			return commentCount;
		}
		public void setCommentCount(int commentCount) {
			this.commentCount = commentCount;
		}
		public String getDetailUrl() {
			return detailUrl;
		}
		public void setDetailUrl(String detailUrl) {
			this.detailUrl = detailUrl;
		}
		public long getAddTime() {
			return addTime;
		}
		public void setAddTime(long addTime) {
			this.addTime = addTime;
		}
		public int getTop() {
			return top;
		}
		public void setTop(int top) {
			this.top = top;
		}
		@Override
		public String toString() {
			return "articleDigest [cntType=" + cntType + ", cntId=" + cntId
					+ ", title=" + title + ", digest=" + digest + ", thumb="
					+ thumb + ", shareCount=" + shareCount + ", commentCount="
					+ commentCount + ", detailUrl=" + detailUrl + ", addTime="
					+ addTime + ", top=" + top + "]";
		}
		public int getFfCount() {
			return ffCount;
		}
		public void setFfCount(int ffCount) {
			this.ffCount = ffCount;
		}
		
		
		
	}

	public List<ArticleDigest> getAcArticleDigestList() {
		return acArticleDigestList;
	}

	public void setAcArticleDigestList(List<ArticleDigest> acArticleDigestList) {
		this.acArticleDigestList = acArticleDigestList;
	}

	public int getTotalpage() {
		return totalpage;
	}

	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	@Override
	public String toString() {
		return "MyArticles [totalRecord=" + totalRecord + ", totalpage="
				+ totalpage + ", acArticleDigestList=" + acArticleDigestList
				+ "]";
	}
	
	

}
