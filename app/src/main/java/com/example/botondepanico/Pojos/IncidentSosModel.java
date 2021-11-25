package com.example.botondepanico.Pojos;

public class IncidentSosModel {

    private String IdSosAlert;
    private String Location;
    private String Status;
    private String Hour;




    public IncidentSosModel(String idSosAlert, String location, String status, String hour){

        this.IdSosAlert = idSosAlert;
        this.Status = status;
        this.Location= location;
        this.Hour= hour;
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

    public String getIdSosAlert() { return IdSosAlert; }

    public void setIdSosAlert(String idSosAlert) {
        IdSosAlert = idSosAlert;
    }

    public String getHour() { return Hour; }

    public void setHour(String hour) { Hour = hour; }
}
