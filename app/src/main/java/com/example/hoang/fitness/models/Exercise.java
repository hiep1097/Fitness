package com.example.hoang.fitness.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Exercise {

    @SerializedName("sound")
    @Expose
    private String sound;
    @SerializedName("gif_pad")
    @Expose
    private String gifPad;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("body_part")
    @Expose
    private String bodyPart;
    @SerializedName("calorie")
    @Expose
    private Double calorie;
    @SerializedName("pic")
    @Expose
    private String pic;
    @SerializedName("custom")
    @Expose
    private Boolean custom;
    @SerializedName("video_link")
    @Expose
    private String videoLink;
    @SerializedName("gif_phone")
    @Expose
    private String gifPhone;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("description")
    @Expose
    private List<String> description = null;

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getGifPad() {
        return gifPad;
    }

    public void setGifPad(String gifPad) {
        this.gifPad = gifPad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBodyPart() {
        return bodyPart;
    }

    public void setBodyPart(String bodyPart) {
        this.bodyPart = bodyPart;
    }

    public Double getCalorie() {
        return calorie;
    }

    public void setCalorie(Double calorie) {
        this.calorie = calorie;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Boolean getCustom() {
        return custom;
    }

    public void setCustom(Boolean custom) {
        this.custom = custom;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getGifPhone() {
        return gifPhone;
    }

    public void setGifPhone(String gifPhone) {
        this.gifPhone = gifPhone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

}