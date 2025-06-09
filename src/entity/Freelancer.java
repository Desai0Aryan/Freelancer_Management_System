/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author desai
 */

public class Freelancer {
    private int freelancerID;
    private String freelancerFirstName;
    private String freelancerLastName;
    private String skillSet;
    private double hourlyRate; 
    
    // Constructor
    public Freelancer(int freelancerID, String freelancerFirstName, String freelancerLastName, String skillSet, double hourlyRate) {
        this.freelancerID = freelancerID;
        this.freelancerFirstName = freelancerFirstName;
        this.freelancerLastName = freelancerLastName;
        this.skillSet = skillSet;
        this.hourlyRate = hourlyRate;
    }
    public int getFreelancerID() {
        return freelancerID;
    }
    public String getFreelancerFirstName() {
        return freelancerFirstName;
    }
    public String getFreelancerLastName() {
        return freelancerLastName;
    }
    public String getSkillSet() {
        return skillSet;
    }
    public double getHourlyRate() {
        return hourlyRate;
    }   
    @Override
    public String toString() {
        return "Freelancer{" +
                "freelancerID=" + freelancerID +
                ", freelancerFirstName='" + freelancerFirstName + '\'' +
                ", freelancerLastName='" + freelancerLastName + '\'' +
                ", skillSet='" + skillSet + '\'' +
                ", hourlyRate=" + hourlyRate +
                '}';
    }
}
