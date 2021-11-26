package com.example.botondepanico.Pojos;

public class NotificationsModel {
    String Title;
    String BriefDescription;
    String ImageNotification;
    String HourPublished;


    public NotificationsModel(String title, String briefDescription, String imageNotification, String hourPublished){

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

    public String getImageDescription() {
        return ImageNotification;
    }

    public void setImageDescription(String imageDescription) {
        ImageNotification = imageDescription;
    }

    public String getHourPublished() {
        return HourPublished;
    }

    public void setHourPublished(String hourPublished) {
        HourPublished = hourPublished;
    }
}
