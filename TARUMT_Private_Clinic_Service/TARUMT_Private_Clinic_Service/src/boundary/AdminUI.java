package boundary;

import java.util.Scanner;
import entity.*;
import control.AdminControl;

/**
 * @author Lim SiYu
 */
public class AdminUI {

    private Scanner input = new Scanner(System.in);
    private int selection;
    private String adminID;

    // --- HELPER METHODS ---

    private void printHeader(String title) {
        System.out.println("\n==============================================================");
        System.out.printf("           %-50s\n", title);
        System.out.println("==============================================================");
    }

    private void printLine() {
        System.out.println("--------------------------------------------------------------");
    }

    private void pause() {
        System.out.print("\nPress [ENTER] to continue...");
        input.nextLine();
    }

    public String enterAdminID() {
        printHeader("ADMIN MODULE");
        System.out.println(" [0] Back to Module Selection");
        System.out.print("\nEnter Admin ID (Format AXXX): ");
        adminID = input.nextLine().trim();
        return adminID; // Returns "0" if they want to exit
    }

    public int showAdminMenu(Admin currentAdmin) {
        String welcomeNote = (currentAdmin != null) 
            ? "MAIN MENU [ " + currentAdmin.getName() + " | " + currentAdmin.getAdminID() + " ]"
            : "ADMIN MAIN MENU";
            
        printHeader(welcomeNote);
        System.out.println(" [1] Manage Patient Account");
        System.out.println(" [2] Manage Doctor Account");
        System.out.println(" [3] Manage Appointment");
        System.out.println(" [4] View Report & Summary");
        System.out.println(" [5] Manage Pharmacist Account");
        System.out.println(" [0] Logout");
        printLine();
        System.out.print("\nEnter choice: ");
        selection = getValidInt();
        return selection;
    }

    public int showManagePatientMenu() {
        printHeader("MANAGE PATIENT ACCOUNT");
        System.out.println(" [1] Add New Patient");
        System.out.println(" [2] Display All Patients");
        System.out.println(" [3] Update Patient Account");
        System.out.println(" [4] Remove Patient Account");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    public int showManageDoctorMenu() {
        printHeader("MANAGE DOCTOR ACCOUNT");
        System.out.println(" [1] Create New Doctor Account");
        System.out.println(" [2] View All Doctors");
        System.out.println(" [3] Update Doctor Details");
        System.out.println(" [4] Remove Doctor Account");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    public int showManageAppointmentMenu() {
        printHeader("MANAGE APPOINTMENTS");
        System.out.println(" [1] View Pending Cancellation Requests");
        System.out.println(" [2] Approve/Reject Cancellation");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    public int showReportMenu() {
        printHeader("REPORTS & SUMMARY");
        System.out.println(" [1] Clinic Visit Report");
        System.out.println(" [2] Clinic Summary");
        System.out.println(" [3] Doctor Performance");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }
    

    public int showManagePharmacistMenu() {
        printHeader("MANAGE PHARMACIST ACCOUNT");
        System.out.println(" [1] Add New Pharmacist");
        System.out.println(" [2] Display All Pharmacists");
        System.out.println(" [3] Update Pharmacist Info");
        System.out.println(" [4] Remove Pharmacist");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }
    
    public int showDoctorUpdateFields() {
        System.out.println("\n --- Select Field to Update ---");
        System.out.println(" [1] Name");
        System.out.println(" [2] Phone Number");
        System.out.println(" [3] Profession");
        System.out.println(" [4] Gender");
        System.out.println(" [0] Cancel");
        System.out.print("\nChoice: ");
        return getValidInt();
    }
    

    public int showPharmacistUpdateFields() {
        System.out.println("\n --- Select Field to Update ---");
        System.out.println(" [1] Name");
        System.out.println(" [0] Cancel");
        System.out.print("\nChoice: ");
        return getValidInt();
    }

    public String inputID(String type) {
        System.out.print("\nEnter " + type + " ID: ");
        return input.nextLine().trim();
    }
    
    public int showPatientUpdateFields() {
        System.out.println("\n --- Select Field to Update ---");
        System.out.println(" [1] Name");
        System.out.println(" [2] Gender");
        System.out.println(" [3] IC Number");
        System.out.println(" [4] Phone Number");
        System.out.println(" [0] Cancel");
        System.out.print("\nChoice: ");
        return getValidInt();
    }

    public void displayPatientProfile(Patient p) {
        printHeader("PATIENT PROFILE");
        System.out.printf("  %-15s : %s\n", "Patient ID", p.getPatientID());
        System.out.printf("  %-15s : %s\n", "Name", p.getPatientName());
        System.out.printf("  %-15s : %s\n", "Gender", p.getPatientGender());
        System.out.printf("  %-15s : %s\n", "IC Number", p.getPatientIC());
        System.out.printf("  %-15s : %s\n", "Phone", p.getPatientContactNumber());
        printLine();
    }

    public String inputValidatedIC() {
        return getValidatedICFormat();
    }

    public void displayMessage(String msg) {
        String lower = msg.toLowerCase();
        boolean isError = lower.contains("error") || 
                          lower.contains("invalid") || 
                          lower.contains("denied") || 
                          lower.contains("not found"); 
        
        System.out.println("\n  " + (isError ? "[!]" : "[OK]") + " " + msg);
    }

    public Doctor inputNewDoctorDetails(control.AdminControl control) {
        printHeader("REGISTER NEW DOCTOR");
        String id;
        while (true) {
            System.out.print("\nEnter Doctor ID (DXXX): ");
            id = input.nextLine().trim().toUpperCase();
        
            if (!id.matches("^D\\d{3}$")) {
                System.out.println("  [!] Error: Must be DXXX (e.g., D101).");
            } else if (control.findDoctorById(id) != null) {
                
            System.out.println(" [!] Error: Doctor ID " + id + " already exists!");
            } else {
                break;
            }   
        }

        String name = inputValidatedName("Name");
        String phone = inputValidatedPhone();
        String prof = selectProfession();
        String gender = selectGender();

        return new Doctor(id, name, phone, prof, gender);
    }


    public Patient inputPatientDetails(control.AdminControl control) {
        printHeader("REGISTER NEW PATIENT");
    
        // 1. Validate ID Format & Uniqueness
        String id;
        while (true) {
            System.out.print("Enter Patient ID (PXXX): ");
            id = input.nextLine().trim().toUpperCase();
            if (!id.matches("^P\\d{3}$")) {
                System.out.println(" [!] Error: Format must be P followed by 3 digits (e.g., P001).");
            } else if (control.findPatientById(id) != null) {
                System.out.println(" [!] Error: Patient ID " + id + " already exists!");
            } else {
                break; 
            }
        }

        // 2. Validate Name (Letters and spaces only)
        String name;
        while (true) {
            System.out.print("Enter Patient Name: ");
            name = input.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s.]+$") && !name.isEmpty()) break;
            System.out.println(" [!] Error: Name contains invalid characters.");
        }

        // 3. Gender Selection
        String gender = selectGender(); 

        // 4. Validate IC (12 Digits)
        String ic = getValidatedICFormat();

        // 5. Validate Phone
        String phone = inputValidatedPhone();

        return new Patient(id, name, gender, ic, phone);
    }

    // Standardized Number Input
    public int getValidInt() {
        while (true) {
            try {
                int val = Integer.parseInt(input.nextLine().trim());
                return val;
            } catch (NumberFormatException e) {
                System.out.print(" [!] Please enter a valid number: ");
            }
        }
    }

    public String selectProfession() {
        System.out.println("\n --- Select Profession ---");
        System.out.println(" [1] General Practitioner");
        System.out.println(" [2] Cardiologist");
        System.out.println(" [3] Dermatologist");
        System.out.println(" [4] Pediatrician");
        System.out.println(" [5] Neurologist");
        System.out.println(" [6] Surgeon");
        System.out.print("\nChoice: ");
        int choice = getValidInt();

        return switch(choice) {
            case 1 -> "General Practitioner";
            case 2 -> "Cardiologist";
            case 3 -> "Dermatologist";
            case 4 -> "Pediatrician";
            case 5 -> "Neurologist";
            case 6 -> "Surgeon";
            default -> "General Practitioner";
        };
    }

    public String selectGender() {
        System.out.println("\n --- Select Gender ---");
        System.out.println(" [1] Male");
        System.out.println(" [2] Female");
        System.out.print("\nChoice: ");
        int choice = getValidInt();
        return (choice == 2) ? "Female" : "Male";
    }

    public String inputNewName() {
        return inputValidatedName("New Name");
    }

    public String inputNewPhone() {
        return inputValidatedPhone();
    }

    public int inputApprovalDecision() {
        System.out.println("\n --- Cancellation Decision ---");
        System.out.println(" [1] Approve");
        System.out.println(" [2] Reject");
        System.out.print("\nChoice: ");
        return getValidInt();
    }
    
    public Pharmacist inputPharmacistDetails(control.AdminControl control) {
        printHeader("REGISTER NEW PHARMACIST");
        String id;
    
        while (true) {
            System.out.print("Enter Pharmacist ID (PHXXX): ");
            id = input.nextLine().trim().toUpperCase(); // Auto-convert to uppercase for consistency
        
            // Step 1: Check Format
            if (!id.matches("^PH\\d{3}$")) {
                System.out.println(" [!] Error: Format must be PH followed by 3 digits (e.g., PH001).");
                continue;
            }
        
            // Step 2: Check Existence (using the control object)
            if (control.findPharmacistById(id) != null) {
                System.out.println(" [!] Error: Pharmacist ID " + id + " already exists in the system!");
                continue;
            }
        
            // If it passes both, break the loop
            break;
        }   
    
        System.out.print("Enter Pharmacist Name: ");
        String name = input.nextLine().trim();
    
        return new Pharmacist(id, name);
    }
    
    public String inputNewPharmacistName() {
        return inputValidatedName("New Pharmacist Name");
    }
    
    private String inputValidatedName(String label) {
        String name;
        while (true) {
            System.out.print("Enter " + label + ": ");
            name = input.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s.]+$") && !name.isEmpty()) {
                return name;
            }
            System.out.println(" [!] Invalid format. Characters only (e.g. Ali Abu).");
        }
    }

    private String inputValidatedPhone() {
        String phone;
        while (true) {
            System.out.print("Enter Phone (10-11 digits): ");
            phone = input.nextLine().trim();
            if (phone.matches("^\\d{10,11}$")) {
                return phone;
            }
            System.out.println(" [!] Invalid format. Digits only (e.g. 0123456789).");
        }
    }
    
    public void displayPharmacistProfile(Pharmacist p) {
        printHeader("PHARMACIST PROFILE");
        System.out.printf("  %-15s : %s\n", "Pharmacist ID", p.getPharmacistId());
        System.out.printf("  %-15s : %s\n", "Name", p.getName());
        printLine();
    }

    public void displayDoctorProfile(Doctor d) {
        printHeader("DOCTOR PROFILE");
        System.out.printf("  %-15s : %s\n", "Doctor ID", d.getDoctorID());
        System.out.printf("  %-15s : %s\n", "Name", d.getDoctorName());
        System.out.printf("  %-15s : %s\n", "Phone", d.getPhone());
        System.out.printf("  %-15s : %s\n", "Gender", d.getGender());
        System.out.printf("  %-15s : %s\n", "Profession", d.getProfession());
        printLine();
    }
    
    private String getValidatedICFormat() {
        String ic;
        while (true) {
            System.out.print("Enter IC (XXXXXX-XX-XXXX): ");
            ic = input.nextLine().trim();
            if (ic.matches("^\\d{6}-\\d{2}-\\d{4}$")) {
                return ic;
            }
            System.out.println(" [!] Error: Invalid IC format! Must be XXXXXX-XX-XXXX (e.g., 010101-10-1234).");
        }
    }
    
    
}