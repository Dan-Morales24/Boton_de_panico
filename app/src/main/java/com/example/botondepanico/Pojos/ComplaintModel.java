package com.example.botondepanico.Pojos;

public class ComplaintModel {

    private String Title;
    private String IdSosAlert;
    private String Location;
    private String Status;
    private String Hour;



    public ComplaintModel(String title, String idSosAlert, String location, String status, String hour){

        this.Title = title;
        this.IdSosAlert = idSosAlert;
        this.Status = status;
        this.Location= location;
        this.Hour=hour;

    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getIdSosAlert() {
        return IdSosAlert;
    }

    public void setIdSosAlert(String idSosAlert) {
        IdSosAlert = idSosAlert;
    }


    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getHour() {
        return Hour;
    }

    public void setHour(String hour) {
        Hour = hour;
    }
}
