//package program; 
import entity.Client;
import entity.ClientDAO;
import entity.Freelancer;
import entity.FreelancerDAO;
import entity.JobContract;
import entity.JobContractDAO;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;

public class main {
    public static void main(String[] args) {
        FreelancerDAO freelancerDAO = new FreelancerDAO();
        ClientDAO clientDAO = new ClientDAO();
        JobContractDAO jobContractDAO = new JobContractDAO();
        Scanner scanner = new Scanner(System.in);
        Map<String, Integer> skillMap = new HashMap<>();

        boolean running = true;

        while (running) {
            try {
                System.out.println("Choose an option:");
                System.out.println("1. Create a Freelancer");
                System.out.println("2. Update a Freelancer");
                System.out.println("3. Delete a Freelancer");
                System.out.println("4. View All Freelancers");
                System.out.println("5. Create a Client");
                System.out.println("6. Update a Client");
                System.out.println("7. Delete a Client");
                System.out.println("8. View All Clients");
                System.out.println("9. Create a Job Contract");
                System.out.println("10. Update a Job Contract");
                System.out.println("11. Delete a Job Contract");
                System.out.println("12. View All Job Contracts");
                System.out.println("13. View Most Common Skills");
                System.out.println("14. Exit Application");

                int option = getValidPositiveIntegerInput("Choose an option (1-14): "); // Correct method name


                switch (option) {
                    // Freelancer CRUD operations
                    case 1: // Create a Freelancer
                        int freelancerId = getValidPositiveIntegerInput("Enter freelancer ID: ");
                        String freelancerFirstName = getValidNameInput("Enter first name: ");
                        String freelancerLastName = getValidNameInput("Enter last name: ");
                        String skillSet = getValidStringInput("Enter skill set: ");
                        double hourlyRate = getValidDoubleInput("Enter hourly rate: ");
                        freelancerDAO.insert(new Freelancer(freelancerId, freelancerFirstName, freelancerLastName, skillSet, hourlyRate));

                        // Update the skill map
                        skillMap.put(skillSet, skillMap.getOrDefault(skillSet, 0) + 1);
                        System.out.println("New freelancer created: " + freelancerFirstName + " " + freelancerLastName);
                        break;

                    case 2: // Update a Freelancer
                        int updateFreelancerId = getValidPositiveIntegerInput("Enter freelancer ID to update: ");
                        String newFreelancerFirstName = getValidNameInput("Enter new first name: ");
                        String newFreelancerLastName = getValidNameInput("Enter new last name: ");
                        String newSkillSet = getValidStringInput("Enter new skill set: ");
                        double newHourlyRate = getValidDoubleInput("Enter new hourly rate: ");
                        freelancerDAO.update(new Freelancer(updateFreelancerId, newFreelancerFirstName, newFreelancerLastName, newSkillSet, newHourlyRate));

                        // Update the skill map
                        skillMap.put(newSkillSet, skillMap.getOrDefault(newSkillSet, 0) + 1);
                        System.out.println("Updated freelancer: " + newFreelancerFirstName + " " + newFreelancerLastName);
                        break;

                    case 3: // Delete a Freelancer
                        int deleteFreelancerId = getValidPositiveIntegerInput("Enter freelancer ID to delete: ");
                        freelancerDAO.delete(new Freelancer(deleteFreelancerId, "", "", "", 0.0));
                        System.out.println("Deleted freelancer with ID: " + deleteFreelancerId);
                        break;

                    case 4: // View All Freelancers
                        List<Freelancer> allFreelancers = freelancerDAO.getAll();
                        System.out.println("All Freelancers:");
                        allFreelancers.forEach(System.out::println);
                        break;

                    // Client CRUD operations
                    case 5: // Create a Client
                        int clientId = getValidPositiveIntegerInput("Enter client ID: ");
                        String clientFirstName = getValidNameInput("Enter first name: ");
                        String clientLastName = getValidNameInput("Enter last name: ");
                        String companyName = getValidStringInput("Enter company name: ");
                        String email = getValidEmailInput("Enter email: ");
                        clientDAO.insert(new Client(clientId, clientFirstName, clientLastName, companyName, email));
                        System.out.println("New client created: " + clientFirstName + " " + clientLastName);
                        break;

                    case 6: // Update a Client
                        int updateClientId = getValidPositiveIntegerInput("Enter client ID to update: ");
                        String newClientFirstName = getValidNameInput("Enter new first name: ");
                        String newClientLastName = getValidNameInput("Enter new last name: ");
                        String newCompanyName = getValidStringInput("Enter new company name: ");
                        String newEmail = getValidEmailInput("Enter new email: ");
                        clientDAO.update(new Client(updateClientId, newClientFirstName, newClientLastName, newCompanyName, newEmail));
                        System.out.println("Updated client: " + newClientFirstName + " " + newClientLastName);
                        break;

                    case 7: // Delete a Client
                        int deleteClientId = getValidPositiveIntegerInput("Enter client ID to delete: ");
                        clientDAO.delete(new Client(deleteClientId, "", "", "", ""));
                        System.out.println("Deleted client with ID: " + deleteClientId);
                        break;

                    case 8: // View All Clients
                        List<Client> allClients = clientDAO.getAll();
                        System.out.println("All Clients:");
                        allClients.forEach(System.out::println);
                        break;

                    // Job Contract CRUD operations
                    case 9: // Create a Job Contract
                        int contractId = getValidPositiveIntegerInput("Enter contract ID: ");
                        int clientID = getValidPositiveIntegerInput("Enter client ID: ");
                        int freelancerID = getValidPositiveIntegerInput("Enter freelancer ID: ");
                        String jobDescription = getValidStringInput("Enter job description: ");
                        String paymentStatus = getValidStringInput("Enter payment status: ");
                        jobContractDAO.insert(new JobContract(contractId, clientID, freelancerID, jobDescription, paymentStatus));
                        System.out.println("New job contract created.");
                        break;

                    case 10: // Update a Job Contract
                        int updateContractId = getValidPositiveIntegerInput("Enter contract ID to update: ");
                        int newClientID = getValidPositiveIntegerInput("Enter new client ID: ");
                        int newFreelancerID = getValidPositiveIntegerInput("Enter new freelancer ID: ");
                        String newJobDescription = getValidStringInput("Enter new job description: ");
                        String newPaymentStatus = getValidStringInput("Enter new payment status: ");
                        jobContractDAO.update(new JobContract(updateContractId, newClientID, newFreelancerID, newJobDescription, newPaymentStatus));
                        System.out.println("Updated job contract.");
                        break;

                    case 11: // Delete a Job Contract
                        int deleteContractId = getValidPositiveIntegerInput("Enter contract ID to delete: ");
                        jobContractDAO.delete(new JobContract(deleteContractId, 0, 0, "", ""));
                        System.out.println("Deleted job contract with ID: " + deleteContractId);
                        break;

                    case 12: // View All Job Contracts
                        List<JobContract> allJobContracts = jobContractDAO.getAll();
                        System.out.println("All Job Contracts:");
                        allJobContracts.forEach(System.out::println);
                        break;

                    // View Most Common Skills
                    case 13: // View Most Common Skills
                        System.out.println("Most Common Skills Among Freelancers:");
                        skillMap.forEach((skill, count) -> {
                            System.out.println(skill + ": " + count);
                        });
                        break;

                    case 14: // Exit Application
                        System.out.println("Exiting application. Goodbye!");
                        running = false;
                        break;

                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        scanner.close();
    }

    // Helper methods for input validation

    public static int getValidPositiveIntegerInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                if (value <= 0) {
                    System.out.println("Invalid input. ID must be a positive integer greater than zero.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid positive integer.");
            }
        }
    }

    public static double getValidDoubleInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.print(prompt);
                double value = Double.parseDouble(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    public static String getValidNameInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (value.length() >= 3) {
                return value;
            } else {
                System.out.println("Name must be at least 3 characters long. Please enter a valid name.");
            }
        }
    }

    public static String getValidStringInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            } else {
                System.out.println("Input cannot be blank. Please enter a valid value.");
            }
        }
    }

    public static String getValidEmailInput(String prompt) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(prompt);
            String email = scanner.nextLine().trim();
            if (isValidEmail(email)) {
                return email;
            } else {
                System.out.println("Invalid email format. Please enter a valid email.");
            }
        }
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
