package com.example.botondepanico.Pojos;

public class ComplaintModel {

    private String Title;
    private String IdSosAlert;
    private String Name;
    private String LastName;
    private String Location;
    private String Description;
    private String Status;



    public ComplaintModel(String Title, String IdSosAlert, String Name, String LastName, String Description, String Location, String Status){

        this.Title = Title;
        this.IdSosAlert = IdSosAlert;
        this.Name = Name;
        this.LastName = LastName;
        this.Description = Description;
        this.Status = Status;
        this.Location= Location;



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

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        this.LastName = lastName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
