/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 * JobContract class to represent the JobContract table in the database.
 * 
 * @author desai
 */
public class JobContract {
    private int contractID;
    private int clientID;
    private int freelancerID;
    private String jobDescription;
    private String paymentStatus;

    // Constructor
    public JobContract(int contractID, int clientID, int freelancerID, String jobDescription, String paymentStatus) {
        this.contractID = contractID;
        this.clientID = clientID;
        this.freelancerID = freelancerID;
        this.jobDescription = jobDescription;
        this.paymentStatus = paymentStatus;
    }

    // Getters and Setters
    public int getContractID() {
        return contractID;
    }
    public int getClientID() {
        return clientID;
    }
    public int getFreelancerID() {
        return freelancerID;
    }
    public String getJobDescription() {
        return jobDescription;
    }
    public String getPaymentStatus() {
        return paymentStatus;
    }
    // toString method for displaying job contract details
    @Override
    public String toString() {
        return "JobContract{" +
                "contractID=" + contractID +
                ", clientID=" + clientID +
                ", freelancerID=" + freelancerID +
                ", jobDescription='" + jobDescription + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}

