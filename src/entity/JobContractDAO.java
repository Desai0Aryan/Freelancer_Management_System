package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import core.DB;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class JobContractDAO implements DAO<JobContract> {

    // Constructor
    public JobContractDAO() {
    }

    // List to hold JobContract objects
    private List<JobContract> jobContractList;

    /**
     * Get a single job contract entity as a JobContract object
     * @param id
     * @return Optional<JobContract>
     */
    @Override
    public Optional<JobContract> get(int id) {
        DB db = DB.getInstance();
        try {
            String sql = "SELECT * FROM JobContract WHERE ContractID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                JobContract jobContract = new JobContract(
                    rs.getInt("ContractID"),
                    rs.getInt("ClientID"),
                    rs.getInt("FreelancerID"),
                    rs.getString("JobDescription"),
                    rs.getString("PaymentStatus")
                );
                return Optional.of(jobContract);
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving job contract: " + ex);
        }
        return Optional.empty();
    }

    /**
     * Get all job contract entities as a List
     * @return List<JobContract>
     */
    @Override
    public List<JobContract> getAll() {
        DB db = DB.getInstance();
        jobContractList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM JobContract";
            ResultSet rs = db.executeQuery(sql);
            while (rs.next()) {
                JobContract jobContract = new JobContract(
                    rs.getInt("ContractID"),
                    rs.getInt("ClientID"),
                    rs.getInt("FreelancerID"),
                    rs.getString("JobDescription"),
                    rs.getString("PaymentStatus")
                );
                jobContractList.add(jobContract);
            }
        } catch (SQLException ex) {
            System.err.println("Error retrieving all job contracts: " + ex);
        }
        return jobContractList;
    }
private void validateForeignKeys(JobContract jobContract) throws Exception {
    if (!clientExists(jobContract.getClientID())) {
        throw new Exception("Validation Error: Client ID " + jobContract.getClientID() + " does not exist.");
    }
    if (!freelancerExists(jobContract.getFreelancerID())) {
        throw new Exception("Validation Error: Freelancer ID " + jobContract.getFreelancerID() + " does not exist.");
    }
}

@Override
public void insert(JobContract jobContract) {
    DB db = DB.getInstance();
    try {
        // Validate foreign keys
        validateForeignKeys(jobContract);

        // Check if contract already exists
        if (contractExists(jobContract.getContractID())) {
            throw new Exception("Contract ID " + jobContract.getContractID() + " already exists.");
        }

        String sql = "INSERT INTO JobContract (ContractID, ClientID, FreelancerID, JobDescription, PaymentStatus) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, jobContract.getContractID());
        stmt.setInt(2, jobContract.getClientID());
        stmt.setInt(3, jobContract.getFreelancerID());
        stmt.setString(4, jobContract.getJobDescription());
        stmt.setString(5, jobContract.getPaymentStatus());
        int rowsInserted = stmt.executeUpdate();

        if (rowsInserted > 0) {
            System.out.println("A new job contract was inserted successfully!");
        }
    } catch (SQLException ex) {
        handleSQLException(ex);
    } catch (Exception ex) {
        System.err.println("Error: " + ex.getMessage());
    }
}

    /**
     * Update an existing job contract
     * @param jobContract
     */
    @Override
    public void update(JobContract jobContract) {
        DB db = DB.getInstance();
        try {
            if (!contractExists(jobContract.getContractID())) {
                throw new Exception("Contract ID does not exist.");
            }
            validateForeignKeys(jobContract);

            String sql = "UPDATE JobContract SET ClientID=?, FreelancerID=?, JobDescription=?, PaymentStatus=? WHERE ContractID=?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, jobContract.getClientID());
            stmt.setInt(2, jobContract.getFreelancerID());
            stmt.setString(3, jobContract.getJobDescription());
            stmt.setString(4, jobContract.getPaymentStatus());
            stmt.setInt(5, jobContract.getContractID());
            stmt.executeUpdate();

            System.out.println("Job contract updated successfully!");
        } catch (SQLException ex) {
            handleSQLException(ex);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Delete a job contract
     * @param jobContract
     */
    @Override
    public void delete(JobContract jobContract) {
        DB db = DB.getInstance();
        try {
            if (!contractExists(jobContract.getContractID())) {
                throw new Exception("Contract ID does not exist.");
            }

            String sql = "DELETE FROM JobContract WHERE ContractID = ?";
            PreparedStatement stmt = db.getPreparedStatement(sql);
            stmt.setInt(1, jobContract.getContractID());
            stmt.executeUpdate();

            System.out.println("Job contract deleted successfully!");
        } catch (SQLException ex) {
            handleSQLException(ex);
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }

    /**
     * Get all column names in a list array
     * @return List<String>
     */
    @Override
    public List<String> getColumnNames() {
        DB db = DB.getInstance();
        List<String> headers = new ArrayList<>();
        try {
            String sql = "SELECT * FROM JobContract WHERE ContractID = -1";
            ResultSet rs = db.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();

            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                headers.add(rsmd.getColumnLabel(i));
            }
        } catch (SQLException ex) {
            System.err.println("Error getting column names: " + ex);
        }
        return headers;
    }

    // Helper methods
    private boolean clientExists(int clientId) throws SQLException {
        return entityExists("Client", "ClientID", clientId);
    }

    private boolean freelancerExists(int freelancerId) throws SQLException {
        return entityExists("Freelancer", "FreelancerID", freelancerId);
    }

    private boolean contractExists(int contractId) throws SQLException {
        return entityExists("JobContract", "ContractID", contractId);
    }

    private boolean entityExists(String tableName, String columnName, int value) throws SQLException {
        DB db = DB.getInstance();
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE " + columnName + " = ?";
        PreparedStatement stmt = db.getPreparedStatement(sql);
        stmt.setInt(1, value);
        ResultSet rs = stmt.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }


    private void handleSQLException(SQLException ex) {
        if ("23000".equals(ex.getSQLState())) {
            System.err.println("Foreign key violation: Ensure referenced ClientID and FreelancerID exist.");
        } else {
            System.err.println("SQL Error: " + ex);
        }
    }
}
