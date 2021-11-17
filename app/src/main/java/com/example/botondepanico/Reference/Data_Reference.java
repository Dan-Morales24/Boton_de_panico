package com.example.botondepanico.Reference;

import com.example.botondepanico.Pojos.UserModel;

public class Data_Reference {

    public static final String Client_Info_reference ="Usuarios";
    public static final String Incidents = "Incidents";
    public static final String SOS= "SOS";
    public static UserModel currentClient;

    public static  String builderWelcomeMessage(){
        if(Data_Reference.currentClient != null){
            return  new StringBuilder("")
                    .append(Data_Reference.currentClient.getName())
                    .append(" ")
                    .append(Data_Reference.currentClient.getLastName()).toString();
        }
        else
            return "";
    }

    public static String formatAdress(String start_adress) {
        int firstIndexOfComma = start_adress.indexOf(".,");
        return start_adress.substring(0,firstIndexOfComma); // Obtiene solo la direccion

    }

}
