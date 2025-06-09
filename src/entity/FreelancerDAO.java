/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import core.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 *
 * @author desai
 */
public class FreelancerDAO implements DAO<Freelancer> {
    public FreelancerDAO() {
    }
    List<Freelancer> freelancerList;

    /**
     * Get a single passenger entity as a Passenger object
     * @param id
     * @return 
     */
    @Override
    public Optional<Freelancer> get(int id) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Freelancer WHERE FreelancerID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Freelancer freelancer = null;
            while (rs.next()) {
                freelancer = new Freelancer(
                    rs.getInt("FreelancerID"),
                    rs.getString("Freelancer_First_Name"),
                    rs.getString("Freelancer_Last_Name"),
                    rs.getString("Skillset"),
                    rs.getDouble("HourlyRate")
                    
                );
            }
            return Optional.ofNullable(freelancer);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return Optional.empty();
        }
    }
    
    /**
     * Get all passenger entities as a List
     * @return 
     */
    @Override
    public List<Freelancer> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        freelancerList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Freelancer";
            rs = db.executeQuery(sql);
            Freelancer freelancer = null;
            while (rs.next()) {
                freelancer = new Freelancer(
                    rs.getInt("FreelancerID"),
                    rs.getString("Freelancer_First_Name"),
                    rs.getString("Freelancer_Last_Name"),
                    rs.getString("Skillset"),
                    rs.getDouble("HourlyRate")
                );
                freelancerList.add(freelancer);
            }
            return freelancerList;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }
    
    /**
     * Insert a passenger object into the passenger table
     * @param freelancer 
     */
    @Override
    public void insert(Freelancer freelancer) {
    DB db = DB.getInstance();
    // Check if FreelancerID already exists
    if (get(freelancer.getFreelancerID()).isPresent()) {
        // Handle the case where the ID already exists
        System.err.println("Error: Freelancer with ID " + freelancer.getFreelancerID() + " already exists.");
        return; // Exit the method early to prevent the insertion
    }
    
    // Proceed with insertion if FreelancerID does not exist
    try {
        String sql = "INSERT INTO Freelancer (FreelancerID, Freelancer_First_Name, Freelancer_Last_Name, Skillset, HourlyRate) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, freelancer.getFreelancerID());
        stmt.setString(2, freelancer.getFreelancerFirstName());
        stmt.setString(3, freelancer.getFreelancerLastName());
        stmt.setString(4, freelancer.getSkillSet());
        stmt.setDouble(5, freelancer.getHourlyRate());
        
        int rowInserted = stmt.executeUpdate();
        if (rowInserted > 0) {
            System.out.println("A new freelancer was inserted successfully!");
        }
    } catch (SQLException ex) {
        System.err.println("Error while inserting freelancer: " + ex.toString());
    }
}
 
    /**
     * Update a passenger entity in the database if it exists using a passenger object
     * @param freelancer
     */
    @Override
public void update(Freelancer freelancer) {
    DB db = DB.getInstance();
    
    // Check if FreelancerID exists
    if (get(freelancer.getFreelancerID()).isEmpty()) {
        // Handle the case where the ID does not exist
        System.err.println("Error: Freelancer with ID " + freelancer.getFreelancerID() + " does not exist.");
        return; // Exit the method early to prevent the update
    }

    // Proceed with the update if FreelancerID exists
    try {
        String sql = "UPDATE Freelancer SET Freelancer_First_Name=?, Freelancer_Last_Name=?, Skillset=?, HourlyRate=? WHERE FreelancerID=?";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setString(1, freelancer.getFreelancerFirstName());
        stmt.setString(2, freelancer.getFreelancerLastName());
        stmt.setString(3, freelancer.getSkillSet());
        stmt.setDouble(4, freelancer.getHourlyRate());
        stmt.setInt(5, freelancer.getFreelancerID());

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Freelancer with ID " + freelancer.getFreelancerID() + " was updated successfully!");
        } else {
            System.err.println("Error: Freelancer with ID " + freelancer.getFreelancerID() + " was not updated.");
        }
    } catch (SQLException ex) {
        System.err.println("Error while updating freelancer: " + ex.toString());
    }
}
    /**
     * Delete a passenger from the passenger table if the entity exists
     * @param freelancer 
     */
    @Override
public void delete(Freelancer freelancer) {
    DB db = DB.getInstance();
    
    // Check if FreelancerID exists
    if (get(freelancer.getFreelancerID()).isEmpty()) {
        // Handle the case where the ID does not exist
        System.err.println("Error: Freelancer with ID " + freelancer.getFreelancerID() + " does not exist.");
        return; // Exit the method early to prevent the deletion
    }

    // Proceed with the deletion if FreelancerID exists
    try {
        String sql = "DELETE FROM Freelancer WHERE FreelancerID = ?";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, freelancer.getFreelancerID());

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Freelancer with ID " + freelancer.getFreelancerID() + " was deleted successfully!");
        } else {
            System.err.println("Error: Freelancer with ID " + freelancer.getFreelancerID() + " was not deleted.");
        }
    } catch (SQLException ex) {
        System.err.println("Error while deleting freelancer: " + ex.toString());
    }
} 
    /**
     * Get all column names in a list array
     * @return 
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Freelancer WHERE FreelancerID = -1"; // We just need this SQL query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            // Get number of columns in the result set
            int numberCols = rsmd.getColumnCount();
            for (int i = 1; i <= numberCols; i++) {
                headers.add(rsmd.getColumnLabel(i)); // Add column headers to the list
            }
            return headers;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        } 
    }
}