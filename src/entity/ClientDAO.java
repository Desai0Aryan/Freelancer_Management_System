package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import core.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class ClientDAO implements DAO<Client> {

    List<Client> clientList;

    @Override
    public Optional<Client> get(int id) {
        DB db = DB.getInstance();
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM Client WHERE ClientID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            Client client = null;
            while (rs.next()) {
                client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("Client_First_Name"),
                        rs.getString("Client_Last_Name"),
                        rs.getString("CompanyName"),
                        rs.getString("Email")
                );
            }
            return Optional.ofNullable(client);
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return Optional.empty();
        }
    }

    @Override
    public List<Client> getAll() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        clientList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Client";
            rs = db.executeQuery(sql);
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("Client_First_Name"),
                        rs.getString("Client_Last_Name"),
                        rs.getString("CompanyName"),
                        rs.getString("Email")
                );
                clientList.add(client);
            }
            return clientList;
        } catch (SQLException ex) {
            System.err.println(ex.toString());
            return null;
        }
    }

    @Override
public void insert(Client client) {
    DB db = DB.getInstance();
    
    // Check if ClientID already exists
    if (get(client.getClientID()).isPresent()) {
        System.err.println("Error: Client with ID " + client.getClientID() + " already exists.");
        throw new IllegalArgumentException("Client with this ID already exists."); // Throw an exception to halt the process
    }

    try {
        String sql = "INSERT INTO Client (ClientID, Client_First_Name, Client_Last_Name, CompanyName, Email) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, client.getClientID());
        stmt.setString(2, client.getClientFirstName());
        stmt.setString(3, client.getClientLastName());
        stmt.setString(4, client.getCompanyName());
        stmt.setString(5, client.getEmail());

        int rowInserted = stmt.executeUpdate();
        if (rowInserted > 0) {
            System.out.println("A new client was inserted successfully!");
        }
    } catch (SQLException ex) {
        System.err.println("Error while inserting client: " + ex.toString());
    }
}


    @Override
public void update(Client client) {
    DB db = DB.getInstance();
    
    // Check if ClientID exists
    Optional<Client> existingClient = get(client.getClientID());
    if (existingClient.isEmpty()) {
        System.err.println("Error: Client with ID " + client.getClientID() + " does not exist.");
        throw new IllegalArgumentException("Client with this ID does not exist.");
    }

    try {
        String sql = "UPDATE Client SET Client_First_Name=?, Client_Last_Name=?, CompanyName=?, Email=? WHERE ClientID=?";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setString(1, client.getClientFirstName());
        stmt.setString(2, client.getClientLastName());
        stmt.setString(3, client.getCompanyName());
        stmt.setString(4, client.getEmail());
        stmt.setInt(5, client.getClientID());

        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Client with ID " + client.getClientID() + " was updated successfully!");
        }
    } catch (SQLException ex) {
        System.err.println("Error while updating client: " + ex.toString());
    }
}


    @Override
public void delete(Client client) {
    DB db = DB.getInstance();
    
    // Check if ClientID exists
    Optional<Client> existingClient = get(client.getClientID());
    if (existingClient.isEmpty()) {
        System.err.println("Error: Client with ID " + client.getClientID() + " does not exist.");
        throw new IllegalArgumentException("Client with this ID does not exist.");
    }

    try {
        String sql = "DELETE FROM Client WHERE ClientID = ?";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, client.getClientID());

        int rowsDeleted = stmt.executeUpdate();
        if (rowsDeleted > 0) {
            System.out.println("Client with ID " + client.getClientID() + " was deleted successfully!");
        }
    } catch (SQLException ex) {
        System.err.println("Error while deleting client: " + ex.toString());
    }
}


    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        ResultSet rs = null;
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Client WHERE ClientID = -1"; // We just need this query to get the column headers
            rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
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
