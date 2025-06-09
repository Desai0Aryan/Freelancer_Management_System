package program;
import entity.Client;
import entity.ClientDAO;
import entity.Freelancer;
import entity.FreelancerDAO;
import entity.JobContract;
import entity.JobContractDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

public class MainGUI extends JFrame {
    private FreelancerDAO freelancerDAO = new FreelancerDAO();
    private ClientDAO clientDAO = new ClientDAO();
    private JobContractDAO jobContractDAO = new JobContractDAO();
    private Map<String, Integer> skillMap = new HashMap<>();

    private JTextArea displayArea; // For displaying data
    private JPanel formPanel; // Dynamic panel for forms

    public MainGUI() {
        setTitle("Freelance Job Management System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Display area
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(displayArea);

        // Button panel
        JPanel buttonPanel = new JPanel(new GridLayout(2, 7, 5, 5));
        String[] buttons = {
            "Create Freelancer", "Update Freelancer", "Delete Freelancer", "View Freelancers",
            "Create Client", "Update Client", "Delete Client", "View Clients",
            "Create Contract", "Update Contract", "Delete Contract", "View Contracts",
            "View Skills", "Exit"
        };
        for (String text : buttons) {
            JButton button = new JButton(text);
            button.addActionListener(new ButtonHandler(text));
            buttonPanel.add(button);
        }

        // Form panel for inputs
        formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        add(formPanel, BorderLayout.EAST);
    }

    // Action listener for buttons
    private class ButtonHandler implements ActionListener {
        private String action;

        public ButtonHandler(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            formPanel.removeAll();
            switch (action) {
                case "Create Freelancer":
                    createFreelancerForm();
                    break;
                case "Update Freelancer":
                    updateFreelancerForm();
                    break;
                case "Delete Freelancer":
                    deleteFreelancerForm();
                    break;
                case "View Freelancers":
                    displayFreelancers();
                    break;
                case "Create Client":
                    createClientForm();
                    break;
                case "Update Client":
                    updateClientForm();
                    break;
                case "Delete Client":
                    deleteClientForm();
                    break;
                case "View Clients":
                    displayClients();
                    break;
                case "Create Contract":
                    createContractForm();
                    break;
                case "Update Contract":
                    updateContractForm();
                    break;
                case "Delete Contract":
                    deleteContractForm();
                    break;
                case "View Contracts":
                    displayContracts();
                    break;
                case "View Skills":
                    displaySkills();
                    break;
                case "Exit":
                    System.exit(0);
                    break;
            }
            formPanel.revalidate();
            formPanel.repaint();
        }
    }
    // Create Freelancer Form
private void createFreelancerForm() {
    JTextField idField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField skillField = new JTextField();
    JTextField rateField = new JTextField();

    formPanel.add(new JLabel("Freelancer ID:"));
    formPanel.add(idField);
    formPanel.add(new JLabel("First Name:"));
    formPanel.add(firstNameField);
    formPanel.add(new JLabel("Last Name:"));
    formPanel.add(lastNameField);
    formPanel.add(new JLabel("Skill Set:"));
    formPanel.add(skillField);
    formPanel.add(new JLabel("Hourly Rate:"));
    formPanel.add(rateField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            int id = validatePositiveInteger(idField.getText(), "Freelancer ID");
            String firstName = validateName(firstNameField.getText(), "First Name");
            String lastName = validateName(lastNameField.getText(), "Last Name");
            String skill = validateNonEmpty(skillField.getText(), "Skill Set");
            double rate = validatePositiveDouble(rateField.getText(), "Hourly Rate");

            // Check if freelancer with the given ID already exists
            if (freelancerDAO.get(id).isPresent()) {
                throw new Exception("Freelancer with ID " + id + " already exists.");
            }

            freelancerDAO.insert(new Freelancer(id, firstName, lastName, skill, rate));
            skillMap.put(skill, skillMap.getOrDefault(skill, 0) + 1);
            showMessage("Freelancer created successfully!", "Success");
            displayFreelancers();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
private void updateFreelancerForm() {
    JTextField idField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField skillField = new JTextField();
    JTextField rateField = new JTextField();

    formPanel.add(new JLabel("Freelancer ID:"));
    formPanel.add(idField);
    formPanel.add(new JLabel("New First Name:"));
    formPanel.add(firstNameField);
    formPanel.add(new JLabel("New Last Name:"));
    formPanel.add(lastNameField);
    formPanel.add(new JLabel("New Skill Set:"));
    formPanel.add(skillField);
    formPanel.add(new JLabel("New Hourly Rate:"));
    formPanel.add(rateField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            int id = validatePositiveInteger(idField.getText(), "Freelancer ID");
            String firstName = validateName(firstNameField.getText(), "First Name");
            String lastName = validateName(lastNameField.getText(), "Last Name");
            String skill = validateNonEmpty(skillField.getText(), "Skill Set");
            double rate = validatePositiveDouble(rateField.getText(), "Hourly Rate");

            // Check if freelancer with the given ID exists
            Optional<Freelancer> freelancerOptional = freelancerDAO.get(id);
            if (freelancerOptional.isEmpty()) {
                throw new Exception("Freelancer with ID " + id + " does not exist.");
            }

            freelancerDAO.update(new Freelancer(id, firstName, lastName, skill, rate));
            skillMap.put(skill, skillMap.getOrDefault(skill, 0) + 1);
            showMessage("Freelancer updated successfully!", "Success");
            displayFreelancers();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
private void deleteFreelancerForm() {
    JTextField idField = new JTextField();

    formPanel.add(new JLabel("Freelancer ID:"));
    formPanel.add(idField);

    JButton submitButton = new JButton("Delete");
    submitButton.addActionListener(e -> {
        try {
            int id = validatePositiveInteger(idField.getText(), "Freelancer ID");

            // Check if freelancer with the given ID exists
            Optional<Freelancer> freelancerOptional = freelancerDAO.get(id);
            if (freelancerOptional.isEmpty()) {
                throw new Exception("Freelancer with ID " + id + " does not exist.");
            }

            freelancerDAO.delete(freelancerOptional.get()); // Use the retrieved freelancer
            showMessage("Freelancer deleted successfully!", "Success");
            displayFreelancers();
        } catch (Exception ex) {
            showMessage(ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
// Validates that the input is a positive integer
private int validatePositiveInteger(String input, String fieldName) throws Exception {
    try {
        int value = Integer.parseInt(input);
        if (value <= 0) {
            throw new Exception(fieldName + " must be a positive integer.");
        }
        return value;
    } catch (NumberFormatException ex) {
        throw new Exception(fieldName + " must be a valid integer.");
    }
}
// Validates that the input is a valid name (non-empty and only alphabetic characters)
private String validateName(String input, String fieldName) throws Exception {
    // Check for null or empty input
    if (input == null || input.trim().isEmpty()) {
        throw new Exception(fieldName + " cannot be empty.");
    }
    
    // Check for length constraint (e.g., max 50 characters)
    int maxLength = 50;  // You can adjust the max length as needed
    if (input.length() > maxLength) {
        throw new Exception(fieldName + " cannot be longer than " + maxLength + " characters.");
    }
    
    // Check for only alphabetic characters
    if (!input.matches("[a-zA-Z]+")) {
        throw new Exception(fieldName + " must contain only alphabetic characters.");
    }
    
    return input;
}

// Validates that the input is not empty (used for skills or any non-numeric field)
private String validateNonEmpty(String input, String fieldName) throws Exception {
    if (input == null || input.trim().isEmpty()) {
        throw new Exception(fieldName + " cannot be empty.");
    }
    return input;
}
// Validates that the input is a positive double (for hourly rate)
private double validatePositiveDouble(String input, String fieldName) throws Exception {
    try {
        double value = Double.parseDouble(input);
        if (value <= 0) {
            throw new Exception(fieldName + " must be a positive number.");
        }
        return value;
    } catch (NumberFormatException ex) {
        throw new Exception(fieldName + " must be a valid number.");
    }
}
// Displays a message to the user (can be used for both success and error messages)
private void showMessage(String message, String messageType) {
    JOptionPane.showMessageDialog(null, message, messageType, messageType.equals("Error") ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
}
private void displayFreelancers() {
    // Retrieve the list of freelancers from the database using the DAO
    List<Freelancer> freelancers = freelancerDAO.getAll();  // Assuming freelancerDAO is already initialized
    
    // Create a StringBuilder to hold the formatted text to display
    StringBuilder freelancersText = new StringBuilder();
    freelancersText.append("All Freelancers:\n");
    
    // Check if there are any freelancers
    if (freelancers != null && !freelancers.isEmpty()) {
        // Loop through the list and append each freelancer's details to the StringBuilder
        for (Freelancer freelancer : freelancers) {
            freelancersText.append("ID: ").append(freelancer.getFreelancerID())
                    .append(", Name: ").append(freelancer.getFreelancerFirstName()).append(" ")
                    .append(freelancer.getFreelancerLastName())
                    .append(", Skillset: ").append(freelancer.getSkillSet())
                    .append(", Hourly Rate: $").append(freelancer.getHourlyRate())
                    .append("\n");
        }
    } else {
        freelancersText.append("No freelancers available.");
    }
    
    // Use the existing JTextArea to display the freelancer information
    displayArea.setText(freelancersText.toString());  // Use your JTextArea
    
    // Optionally, you could log the freelancers to the console for debugging
    System.out.println(freelancersText.toString());
}
    // Client 
private void createClientForm() {
    JTextField idField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField companyField = new JTextField();
    JTextField emailField = new JTextField();

    formPanel.add(new JLabel("Client ID:"));
    formPanel.add(idField);
    formPanel.add(new JLabel("First Name:"));
    formPanel.add(firstNameField);
    formPanel.add(new JLabel("Last Name:"));
    formPanel.add(lastNameField);
    formPanel.add(new JLabel("Company Name:"));
    formPanel.add(companyField);
    formPanel.add(new JLabel("Email:"));
    formPanel.add(emailField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            // Validate input fields
            int id = validatePositiveInteger(idField.getText(), "Client ID");
            String firstName = validateName(firstNameField.getText(), "First Name");
            String lastName = validateName(lastNameField.getText(), "Last Name");
            String company = validateNonEmpty(companyField.getText(), "Company Name");
            String email = validateEmail(emailField.getText());

            // Create new Client object
            Client client = new Client(id, firstName, lastName, company, email);

            // Check if ClientID already exists
            if (clientDAO.get(client.getClientID()).isPresent()) {
                throw new IllegalArgumentException("Client with ID " + client.getClientID() + " already exists.");
            }

            // Insert into DB via DAO
            clientDAO.insert(client);
            showMessage("Client created successfully!", "Success");

            // Clear the fields after submission
            clearFormFields(idField, firstNameField, lastNameField, companyField, emailField);
            displayClients();  // Refresh client list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
private void updateClientForm() {
    JTextField idField = new JTextField();
    JTextField firstNameField = new JTextField();
    JTextField lastNameField = new JTextField();
    JTextField companyField = new JTextField();
    JTextField emailField = new JTextField();

    formPanel.add(new JLabel("Client ID:"));
    formPanel.add(idField);
    formPanel.add(new JLabel("New First Name:"));
    formPanel.add(firstNameField);
    formPanel.add(new JLabel("New Last Name:"));
    formPanel.add(lastNameField);
    formPanel.add(new JLabel("New Company Name:"));
    formPanel.add(companyField);
    formPanel.add(new JLabel("New Email:"));
    formPanel.add(emailField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            // Validate input fields
            int id = validatePositiveInteger(idField.getText(), "Client ID");
            String firstName = validateName(firstNameField.getText(), "First Name");
            String lastName = validateName(lastNameField.getText(), "Last Name");
            String company = validateNonEmpty(companyField.getText(), "Company Name");
            String email = validateEmail(emailField.getText());

            // Create updated Client object
            Client updatedClient = new Client(id, firstName, lastName, company, email);

            // Check if the client exists before updating
            if (clientDAO.get(id).isEmpty()) {
                throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
            }

            // Update the client in the DB via DAO
            clientDAO.update(updatedClient);
            showMessage("Client updated successfully!", "Success");

            // Clear the fields after submission
            clearFormFields(idField, firstNameField, lastNameField, companyField, emailField);
            displayClients();  // Refresh client list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
private void deleteClientForm() {
    JTextField idField = new JTextField();

    formPanel.add(new JLabel("Client ID:"));
    formPanel.add(idField);

    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(e -> {
        try {
            // Validate input field
            int id = validatePositiveInteger(idField.getText(), "Client ID");

            // Check if the client exists before deleting
            Optional<Client> clientOptional = clientDAO.get(id);
            if (clientOptional.isEmpty()) {
                throw new IllegalArgumentException("Client with ID " + id + " does not exist.");
            }

            // Create a Client object for deletion
            Client clientToDelete = clientOptional.get();
            clientDAO.delete(clientToDelete);  // Delete the client from DB via DAO
            showMessage("Client deleted successfully!", "Success");

            // Clear the field after deletion
            idField.setText("");
            displayClients();  // Refresh client list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(deleteButton);
}
private String validateEmail(String email) throws Exception {
    String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    if (!Pattern.matches(emailRegex, email)) {
        throw new Exception("Invalid email format.");
    }
    return email;
}
private void clearFormFields(JTextField... fields) {
    for (JTextField field : fields) {
        field.setText("");
    }
}
private void displayClients() {
    try {
        // Retrieve the list of clients from the database using the DAO
        List<Client> clients = clientDAO.getAll();  // Assuming clientDAO is already initialized
        
        // Create a StringBuilder to hold the formatted text to display
        StringBuilder clientsText = new StringBuilder();
        clientsText.append("All Clients:\n");

        // Check if there are any clients
        if (clients != null && !clients.isEmpty()) {
            // Loop through the list and append each client's details to the StringBuilder
            for (Client client : clients) {
                clientsText.append("ID: ").append(client.getClientID())
                        .append(", Name: ").append(client.getClientFirstName()).append(" ")
                        .append(client.getClientLastName())
                        .append(", Company: ").append(client.getCompanyName())
                        .append(", Email: ").append(client.getEmail())
                        .append("\n");
            }
        } else {
            clientsText.append("No clients available.");
        }

        // Use the existing JTextArea to display the client information
        displayArea.setText(clientsText.toString());  // Assuming displayArea is your JTextArea

        // Optionally, you could log the clients to the console for debugging
        System.out.println(clientsText.toString());
    } catch (Exception ex) {
        showMessage("Failed to retrieve clients: " + ex.getMessage(), "Error");
    }
}
    //JobContract
private void createContractForm() {
    JTextField contractIdField = new JTextField();
    JTextField clientIdField = new JTextField();
    JTextField freelancerIdField = new JTextField();
    JTextField jobDescriptionField = new JTextField();
    JTextField paymentStatusField = new JTextField();

    formPanel.add(new JLabel("Contract ID:"));
    formPanel.add(contractIdField);
    formPanel.add(new JLabel("Client ID:"));
    formPanel.add(clientIdField);
    formPanel.add(new JLabel("Freelancer ID:"));
    formPanel.add(freelancerIdField);
    formPanel.add(new JLabel("Job Description:"));
    formPanel.add(jobDescriptionField);
    formPanel.add(new JLabel("Payment Status:"));
    formPanel.add(paymentStatusField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            // Validate input fields
            int contractId = validatePositiveInteger(contractIdField.getText(), "Contract ID");
            int clientId = validatePositiveInteger(clientIdField.getText(), "Client ID");
            int freelancerId = validatePositiveInteger(freelancerIdField.getText(), "Freelancer ID");
            String jobDescription = validateNonEmpty(jobDescriptionField.getText(), "Job Description");
            String paymentStatus = validateNonEmpty(paymentStatusField.getText(), "Payment Status");

            // Create new JobContract object
            JobContract jobContract = new JobContract(contractId, clientId, freelancerId, jobDescription, paymentStatus);

            // Check if ContractID already exists
            if (jobContractDAO.get(contractId).isPresent()) {
                throw new IllegalArgumentException("Job Contract with ID " + contractId + " already exists.");
            }

            // Insert into DB via DAO
            jobContractDAO.insert(jobContract);
            showMessage("Job Contract created successfully!", "Success");

            // Clear the fields after submission
            clearFormFields(contractIdField, clientIdField, freelancerIdField, jobDescriptionField, paymentStatusField);
            displayContracts();  // Refresh job contract list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}
private void updateContractForm() {
    JTextField contractIdField = new JTextField();
    JTextField clientIdField = new JTextField();
    JTextField freelancerIdField = new JTextField();
    JTextField jobDescriptionField = new JTextField();
    JTextField paymentStatusField = new JTextField();

    formPanel.add(new JLabel("Contract ID:"));
    formPanel.add(contractIdField);
    formPanel.add(new JLabel("New Client ID:"));
    formPanel.add(clientIdField);
    formPanel.add(new JLabel("New Freelancer ID:"));
    formPanel.add(freelancerIdField);
    formPanel.add(new JLabel("New Job Description:"));
    formPanel.add(jobDescriptionField);
    formPanel.add(new JLabel("New Payment Status:"));
    formPanel.add(paymentStatusField);

    JButton submitButton = new JButton("Submit");
    submitButton.addActionListener(e -> {
        try {
            // Validate input fields
            int contractId = validatePositiveInteger(contractIdField.getText(), "Contract ID");
            int clientId = validatePositiveInteger(clientIdField.getText(), "Client ID");
            int freelancerId = validatePositiveInteger(freelancerIdField.getText(), "Freelancer ID");
            String jobDescription = validateNonEmpty(jobDescriptionField.getText(), "Job Description");
            String paymentStatus = validateNonEmpty(paymentStatusField.getText(), "Payment Status");

            // Check if the contract exists before updating (PK validation)
            Optional<JobContract> existingContract = jobContractDAO.get(contractId);
            if (existingContract.isEmpty()) {
                throw new IllegalArgumentException("Job Contract with ID " + contractId + " does not exist.");
            }

            // Check if the client exists before updating (FK validation)
            Optional<Client> existingClient = clientDAO.get(clientId);
            if (existingClient.isEmpty()) {
                throw new IllegalArgumentException("Client with ID " + clientId + " does not exist.");
            }

            // Check if the freelancer exists before updating (FK validation)
            Optional<Freelancer> existingFreelancer = freelancerDAO.get(freelancerId);
            if (existingFreelancer.isEmpty()) {
                throw new IllegalArgumentException("Freelancer with ID " + freelancerId + " does not exist.");
            }

            // Create updated JobContract object
            JobContract updatedJobContract = new JobContract(contractId, clientId, freelancerId, jobDescription, paymentStatus);

            // Update the contract in the DB via DAO
            jobContractDAO.update(updatedJobContract);
            showMessage("Job Contract updated successfully!", "Success");

            // Clear the fields after submission
            clearFormFields(contractIdField, clientIdField, freelancerIdField, jobDescriptionField, paymentStatusField);
            displayContracts();  // Refresh job contract list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message for PK/FK validation
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(submitButton);
}

private void deleteContractForm() {
    JTextField contractIdField = new JTextField();

    formPanel.add(new JLabel("Contract ID:"));
    formPanel.add(contractIdField);

    JButton deleteButton = new JButton("Delete");
    deleteButton.addActionListener(e -> {
        try {
            // Validate input field
            int contractId = validatePositiveInteger(contractIdField.getText(), "Contract ID");

            // Check if the contract exists before deleting
            Optional<JobContract> jobContractOptional = jobContractDAO.get(contractId);
            if (jobContractOptional.isEmpty()) {
                throw new IllegalArgumentException("Job Contract with ID " + contractId + " does not exist.");
            }

            // Create a JobContract object for deletion
            JobContract jobContractToDelete = jobContractOptional.get();
            jobContractDAO.delete(jobContractToDelete);  // Delete the job contract from DB via DAO
            showMessage("Job Contract deleted successfully!", "Success");

            // Clear the field after deletion
            contractIdField.setText("");
            displayContracts();  // Refresh job contract list

        } catch (IllegalArgumentException ex) {
            showMessage(ex.getMessage(), "Error");  // Display error message
        } catch (Exception ex) {
            showMessage("An unexpected error occurred: " + ex.getMessage(), "Error");
        }
    });
    formPanel.add(deleteButton);
}
private void displayContracts() {
    try {
        // Retrieve the list of job contracts from the database using the DAO
        List<JobContract> jobContracts = jobContractDAO.getAll();  // Assuming jobContractDAO is already initialized
        
        // Create a StringBuilder to hold the formatted text to display
        StringBuilder contractsText = new StringBuilder();
        contractsText.append("All Job Contracts:\n");

        // Check if there are any contracts
        if (jobContracts != null && !jobContracts.isEmpty()) {
            // Loop through the list and append each contract's details to the StringBuilder
            for (JobContract jobContract : jobContracts) {
                contractsText.append("Contract ID: ").append(jobContract.getContractID())
                        .append(", Client ID: ").append(jobContract.getClientID())
                        .append(", Freelancer ID: ").append(jobContract.getFreelancerID())
                        .append(", Job Description: ").append(jobContract.getJobDescription())
                        .append(", Payment Status: ").append(jobContract.getPaymentStatus())
                        .append("\n");
            }
        } else {
            contractsText.append("No job contracts available.");
        }

        // Use the existing JTextArea to display the job contract information
        displayArea.setText(contractsText.toString());  // Assuming displayArea is your JTextArea

        // Optionally, you could log the contracts to the console for debugging
        System.out.println(contractsText.toString());
    } catch (Exception ex) {
        showMessage("Failed to retrieve job contracts: " + ex.getMessage(), "Error");
    }
}
private void displaySkills() {
    try {
        // Map to store skill counts
        Map<String, Integer> skillsMap = new HashMap<>();
        
        // Retrieve all freelancers (replace this with your actual method to get freelancers)
        List<Freelancer> freelancers = freelancerDAO.getAll(); // Adjust this line based on your existing logic

        // Loop through freelancers to count their skills
        for (Freelancer freelancer : freelancers) {
            String skillsString = freelancer.getSkillSet();  // Get the skills as a comma-separated string

            // Split the string into individual skills and loop through them
            if (skillsString != null && !skillsString.isEmpty()) {
                String[] freelancerSkills = skillsString.split(",\\s*");  // Split by comma, trimming spaces

                // Count each skill's occurrence
                for (String skill : freelancerSkills) {
                    skillsMap.put(skill, skillsMap.getOrDefault(skill, 0) + 1);
                }
            }
        }

        // Find the top skill
        String topSkill = null;
        int topSkillCount = 0;

        for (Map.Entry<String, Integer> entry : skillsMap.entrySet()) {
            if (entry.getValue() > topSkillCount) {
                topSkill = entry.getKey();
                topSkillCount = entry.getValue();
            }
        }

        // Display the top skill in the UI (assuming you have a JTextArea or similar for displaying)
        StringBuilder skillsText = new StringBuilder();
        if (topSkill != null) {
            skillsText.append("Top Skill in Demand: ").append(topSkill)
                      .append(" with ").append(topSkillCount).append(" freelancers.");
        } else {
            skillsText.append("No skills available.");
        }

        // Update the display area (replace displayArea with your actual JTextArea variable)
        displayArea.setText(skillsText.toString());

        // Optionally, log to console for debugging
        System.out.println(skillsText.toString());

    } catch (Exception ex) {
        showMessage("Failed to display skills: " + ex.getMessage(), "Error");
    }
}


       public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainGUI gui = new MainGUI();
            gui.setVisible(true);
        });
    }
}
