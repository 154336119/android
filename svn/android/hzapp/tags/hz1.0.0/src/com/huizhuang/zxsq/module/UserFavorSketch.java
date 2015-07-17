package com.huizhuang.zxsq.module;

import java.util.List;

import com.huizhuang.zxsq.http.bean.common.Image;

public class UserFavorSketch {

    private int totalRecord;
    private int totalpage;
    private List<ImageInfo> imageInfoList;

    public class ImageInfo {
        private int id;
        private int album_id;
        private String name;
        private String digest;
        private int viewNum;
        private int likeNum;
        private int favorNum;
        private int shareNum;
        private int favored;
        private String room_style;
        private String room_type;
        private String room_space;
        private int store_id;
        private int liked;
        private List<Integer> sharedTypes;
        private Image img;
        private Visitors visitors;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getAlbum_id() {
            return album_id;
        }

        public void setAlbum_id(int album_id) {
            this.album_id = album_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getViewNum() {
            return viewNum;
        }

        public void setViewNum(int viewNum) {
            this.viewNum = viewNum;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getFavorNum() {
            return favorNum;
        }

        public void setFavorNum(int favorNum) {
            this.favorNum = favorNum;
        }

        public int getShareNum() {
            return shareNum;
        }

        public void setShareNum(int shareNum) {
            this.shareNum = shareNum;
        }

        public int getFavored() {
            return favored;
        }

        public void setFavored(int favored) {
            this.favored = favored;
        }

        public int getLiked() {
            return liked;
        }

        public void setLiked(int liked) {
            this.liked = liked;
        }

        public List<Integer> getSharedTypes() {
            return sharedTypes;
        }

        public void setSharedTypes(List<Integer> sharedTypes) {
            this.sharedTypes = sharedTypes;
        }

        public Image getImg() {
            return img;
        }

        public void setImg(Image img) {
            this.img = img;
        }

        public Visitors getVisitor() {
            return visitors;
        }

        public void setVisitors(Visitors visitors) {
            this.visitors = visitors;
        }

        public String getRoom_style() {
            return room_style;
        }

        public void setRoom_style(String room_style) {
            this.room_style = room_style;
        }

        public String getRoom_type() {
            return room_type;
        }

        public void setRoom_type(String room_type) {
            this.room_type = room_type;
        }

        public String getRoom_space() {
            return room_space;
        }

        public void setRoom_space(String room_space) {
            this.room_space = room_space;
        }

        public int getStore_id() {
            return store_id;
        }

        public void setStore_id(int store_id) {
            this.store_id = store_id;
        }


        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        @Override
        public String toString() {
            return "ImageInfo{" +
                    "id=" + id +
                    ", album_id=" + album_id +
                    ", name='" + name + '\'' +
                    ", digest='" + digest + '\'' +
                    ", viewNum=" + viewNum +
                    ", likeNum=" + likeNum +
                    ", favorNum=" + favorNum +
                    ", shareNum=" + shareNum +
                    ", favored=" + favored +
                    ", room_style=" + room_style +
                    ", room_type=" + room_type +
                    ", room_space=" + room_space +
                    ", store_id=" + store_id +
                    ", liked=" + liked +
                    ", sharedTypes=" + sharedTypes +
                    ", img=" + img +
                    ", visitors=" + visitors +
                    '}';
        }
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public List<ImageInfo> getImageInfoList() {
        return imageInfoList;
    }

    public void setImageInfoList(List<ImageInfo> imageInfoList) {
        this.imageInfoList = imageInfoList;
    }

    @Override
    public String toString() {
        return "UserFavorSketch [totalRecord=" + totalRecord + ", totalpage="
                + totalpage + ", imageInfoList=" + imageInfoList + "]";
    }


    public class Visitors {
        private int totalrecord;
        private List<Visitor> visitorList;

        public List<Visitor> getVisitorList() {
            return visitorList;
        }

        public void setVisitorList(List<Visitor> visitorList) {
            this.visitorList = visitorList;
        }

        @Override
        public String toString() {
            return "Visitors [totalreco0rd=" + getTotalrecord() + ", visitorList="
                    + visitorList + "]";
        }

        public int getTotalrecord() {
            return totalrecord;
        }

        public void setTotalrecord(int totalrecord) {
            this.totalrecord = totalrecord;
        }


    }

    public class Visitor {
        private int userId;
        private Image image;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public Image getImage() {
            return image;
        }

        public void setImage(Image image) {
            this.image = image;
        }

        @Override
        public String toString() {
            return "Visitor [userId=" + userId + ", image=" + image + "]";
        }


    }


}
