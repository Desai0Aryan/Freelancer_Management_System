/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 * Client class to represent the Client table in the database.
 * 
 * @author desai
 */
public class Client {
    private int clientID;
    private String clientFirstName;
    private String clientLastName;
    private String companyName;
    private String email;

    // Constructor
    public Client(int clientID, String clientFirstName, String clientLastName, String companyName, String email) {
        this.clientID = clientID;
        this.clientFirstName = clientFirstName;
        this.clientLastName = clientLastName;
        this.companyName = companyName;
        this.email = email;
    }

    // Getters and Setters (Make sure these match the column names in your SQL table)
    public int getClientID() {
        return clientID;
    }
    public String getClientFirstName() {
        return clientFirstName;
    }
    public String getClientLastName() {
        return clientLastName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public String getEmail() {
        return email;
    }
 // toString method for displaying client details
    @Override
    public String toString() {
        return "Client{" +
                "clientID=" + clientID +
                ", clientFirstName='" + clientFirstName + '\'' +
                ", clientLastName='" + clientLastName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

