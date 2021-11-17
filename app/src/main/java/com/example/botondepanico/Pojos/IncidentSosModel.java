package com.example.botondepanico.Pojos;

public class IncidentSosModel {

    private String IdSosAlert;
    private String Name;
    private String Location;
    private String Status;

    public IncidentSosModel() {

    }



    public IncidentSosModel(String IdSosAleet,String texto, String location, String status){

        this.IdSosAlert = IdSosAleet;
        this.Status = status;
        this.Location= location;
        this.Name = texto;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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

    public String getIdSosAlert() {
        return IdSosAlert;
    }

    public void setIdSosAlert(String idSosAlert) {
        IdSosAlert = idSosAlert;
    }
}
