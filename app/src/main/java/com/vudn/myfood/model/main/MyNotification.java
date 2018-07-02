package com.vudn.myfood.model.main;

public class MyNotification {
    private int id;
    private byte[] imgLiker;
    private String owner;
    private int idGoods;
    private int status;
    private long createTime;

    public MyNotification() {
    }

    public MyNotification(byte[] imgLiker, String owner, int idGoods, int status, long createTime) {
        this.imgLiker = imgLiker;
        this.owner = owner;
        this.idGoods = idGoods;
        this.status = status;
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImgLiker() {
        return imgLiker;
    }

    public void setImgLiker(byte[] imgLiker) {
        this.imgLiker = imgLiker;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getIdGoods() {
        return idGoods;
    }

    public void setIdGoods(int idGoods) {
        this.idGoods = idGoods;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
