package com.example.botondepanico.Pojos;

public class NotificationsModel {
    String Id;
    String Title;
    String BriefDescription;
    String ImageNotification;
    String HourPublished;


    public NotificationsModel(String id, String title, String briefDescription, String imageNotification, String hourPublished){

        this.Id=id;
        this.Title=title;
        this.BriefDescription=briefDescription;
        this.ImageNotification=imageNotification;
        this.HourPublished=hourPublished;

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getBriefDescription() {
        return BriefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        BriefDescription = briefDescription;
    }


    public String getHourPublished() {
        return HourPublished;
    }

    public void setHourPublished(String hourPublished) {
        HourPublished = hourPublished;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getImageNotification() {
        return ImageNotification;
    }

    public void setImageNotification(String imageNotification) {
        ImageNotification = imageNotification;
    }
}
