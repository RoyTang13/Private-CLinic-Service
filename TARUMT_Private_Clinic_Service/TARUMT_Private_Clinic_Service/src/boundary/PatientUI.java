package boundary;

import adt.ListInterface;
import java.util.Scanner;
import entity.Doctor;
import entity.Patient;
import entity.Appointment;

/**
 * @author lee seng wai
 */
public class PatientUI {

    private Scanner scan = new Scanner(System.in);

    // --- HELPER METHODS (Matching AdminUI Pattern) ---

    private void printHeader(String title) {
        System.out.println("\n========================================================================");
        System.out.printf("                     %-50s\n", title);
        System.out.println("========================================================================");
    }

    private void printLine() {
        System.out.println("------------------------------------------------------------------------");
    }

    public void pause() {
        System.out.print("\nPress [ENTER] to continue...");
        scan.nextLine();
    }

    public void displayMessage(String msg) {
        System.out.println("\n [MSG] " + msg);
    }

    // --- UI METHODS ---

    public int getPatientAccount() {
        printHeader("PATIENT ENTRANCE");
        System.out.println(" [1] Create New Account");
        System.out.println(" [2] Login Account");
        System.out.println(" [0] Back to Main Menu");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    public Patient newPatientDetails(adt.ListInterface<Patient> patientList) {
        printHeader("REGISTER NEW ACCOUNT");

        String id = "";
        boolean validId = false;

        while (!validId) {
            System.out.print("Create Patient ID (e.g., P001): ");
            id = scan.nextLine().trim().toUpperCase();

            if (!id.matches("^P\\d{3}$")) {
                System.out.println(" [!] Error: Format must be P followed by 3 digits (e.g., P001).");
                continue;
            }

            boolean exists = false;
            for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                if (id.equalsIgnoreCase(patientList.getEntry(i).getPatientID())) {
                    exists = true;
                    break;
                }
            }

            if (exists) {
                System.out.println(" [!] Error: Patient ID " + id + " already exists! Please use a different ID.");
            } else {
                validId = true; 
            }
        }

        String name = newPatientName();
        String gender = newPatientGender();
        String ic = newPatientIC();
        String phone = newPatientContactNumber();

        return new Patient(id, name, gender, ic, phone);
    }

    public String newPatientID() {
        while (true) {
            System.out.print("Create Patient ID (e.g., P001): ");
            String id = scan.nextLine().trim().toUpperCase();
            if (id.matches("P\\d{3}")) {
                return id;
            }
            System.out.println(" [!] Invalid ID! Format must be P followed by 3 digits (e.g., P001).");
        }
    }
    
    public String newPatientName() {
        while (true) {
            System.out.print("Enter Full Name              : ");
            String name = scan.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s]+$") && !name.isEmpty()) {
                return name;
            }
            System.out.println(" [!] Invalid Name! Characters and spaces only (no digits).");
        }
    }

    public String newPatientGender() {
        System.out.println("\n --- Select Gender ---");
        System.out.println(" [1] Male");
        System.out.println(" [2] Female");
        System.out.print("\nChoice: ");
        int choice = getValidInt();
        return (choice == 2) ? "Female" : "Male";
    }

    public String newPatientIC() {
        while (true) {
            System.out.print("Enter IC (XXXXXX-XX-XXXX)    : ");
            String ic = scan.nextLine().trim();
            
            if (ic.matches("\\d{6}-\\d{2}-\\d{4}")) {
                return ic;
            }
            System.out.println(" [!] Invalid IC! Use format XXXXXX-XX-XXXX.");
        }
    }

    public String newPatientContactNumber() {
            while (true) {
                System.out.print("Enter Contact Number (Digits Only): ");
                String phone = scan.nextLine().trim();
                if (phone.matches("\\d{10,11}")) {
                    return phone;
                }
                System.out.println(" [!] Invalid Phone! Enter 10-11 digits without dashes.");
            }
        }

    public String getPatientID() {
        System.out.print("\nEnter your Patient ID to Login: ");
        return scan.nextLine().trim();
    }

    public int getPatientMenu(Patient patient) {
        printHeader("PATIENT MENU [ " + patient.getPatientName() + " ]");
        System.out.println(" [1] Update My Profile");
        System.out.println(" [2] View My Profile");
        System.out.println(" [3] Book Appointment / Consultation");
        System.out.println(" [4] Cancel Appointment");
        System.out.println(" [5] View Appointment History");
        System.out.println(" [0] Logout");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    public int updatePatientDetails() {
        printHeader("UPDATE PROFILE SETTINGS");
        System.out.println(" [1] Update Name");
        System.out.println(" [2] Update Gender");
        System.out.println(" [3] Update Contact Number");
        System.out.println(" [0] Back");
        printLine();
        System.out.print("\nSelect field: ");
        return getValidInt();
    }

    public String updatePatientName() {
        while (true) {
            System.out.print("Enter New Name: ");
            String name = scan.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s.]+$") && !name.isEmpty()) {
                return name;
            }
            System.out.println(" [!] Invalid Name! Please use letters only (no digits).");
        }
    }

    public String updatePatientGender() {
        while (true) {
            System.out.println("\n --- Select New Gender ---");
            System.out.println(" [1] Male");
            System.out.println(" [2] Female");
            System.out.print("Choice: ");
            String choice = scan.nextLine().trim();

            if (choice.equals("1")) {
                return "Male";
            }
            if (choice.equals("2")) {
                return "Female";
            }

            System.out.println(" [!] Invalid choice! Please enter 1 or 2.");
        }
    }

    public String updatePatientContactNumber() {
        while (true) {
            System.out.print("Enter New Contact Number (10-11 digits): ");
            String phone = scan.nextLine().trim();
            if (phone.matches("^\\d{10,11}$")) {
                return phone;
            }
            System.out.println(" [!] Invalid Phone! Enter 10-11 digits only (no characters or dashes).");
        }
    }

    public void viewPatientProfile(Patient patient) {
        printHeader("MY PROFILE");
        System.out.printf("  %-15s : %s\n", "Patient ID", patient.getPatientID());
        System.out.printf("  %-15s : %s\n", "Name", patient.getPatientName());
        System.out.printf("  %-15s : %s\n", "Gender", patient.getPatientGender());
        System.out.printf("  %-15s : %s\n", "IC Number", patient.getPatientIC());
        System.out.printf("  %-15s : %s\n", "Contact", patient.getPatientContactNumber());
        printLine();
    }

    public String inputSymptom() {
        System.out.print("\nDescribe your symptom: ");
        return scan.nextLine().trim();
    }

    public String confirmAppointment() {
        String input = "";
        while (true) {
            System.out.print("Confirm appointment registration? (Y/N): ");
            input = scan.nextLine().trim().toUpperCase(); // Convert to Uppercase for easier checking

            if (input.equals("Y") || input.equals("N")) {
                return input; // Exit loop and return valid input
            } else {
                System.out.println(" [!] Invalid input. Please enter 'Y' for Yes or 'N' for No.");
            }
        }
    }

    public void displayAssignedDoctor(Doctor doctor) {
        printHeader("ASSIGNED DOCTOR");
        System.out.printf("  %-15s : %s\n", "Doctor ID", doctor.getDoctorID());
        System.out.printf("  %-15s : %s\n", "Doctor Name", doctor.getDoctorName());
        System.out.printf("  %-15s : %s\n", "Profession", doctor.getProfession());
        printLine();
    }

    public void displayActiveAppointments(Patient patient) {
        System.out.println("\n======================================================================================");
        System.out.println("|                               PENDING APPOINTMENTS                                 |");
        System.out.println("======================================================================================");
        System.out.printf("| %-3s | %-20s | %-15s | %-25s | %-8s|\n", "No", "Doctor", "Specialty", "Symptom", "Status");
        System.out.println("--------------------------------------------------------------------------------------");

        int count = 0;
        for (int i = 1; i <= patient.getAppointmentList().getNumberOfEntries(); i++) {
            Appointment app = patient.getAppointmentList().getEntry(i);
            if (app.getStatus().equalsIgnoreCase("Pending")) {
                count++;
                System.out.printf("| %-3d | %-20.20s | %-15.15s | %-25.25s | %-10s     |\n", 
                        count, app.getDoctorName(), app.getDoctorSpecialty(), app.getSymptom(), app.getStatus());
            }
        }

        if (count == 0) {
            System.out.println("|                     - No active appointments found -                               |");
        }
        System.out.println("======================================================================================");
    }
    
    public void displayFullAppointmentHistory(ListInterface<Appointment> history) {
        System.out.println("\n======================================================================================");
        System.out.println("|                               FULL APPOINTMENT HISTORY                             |");
        System.out.println("======================================================================================");
        System.out.printf("| %-8s | %-12s | %-20s | %-15s | %-10s      |\n",
                "ID", "Date", "Doctor", "Specialty", "Status");
        System.out.println("--------------------------------------------------------------------------------------");

        for (int i = 1; i <= history.getNumberOfEntries(); i++) {
            Appointment app = history.getEntry(i);
            System.out.printf("| %-8s | %-12s | %-20.20s | %-15.15s | %-10s      |\n",
                    app.getAppointmentID(),
                    app.getDate(),
                    app.getDoctorName(),
                    app.getDoctorSpecialty(),
                    app.getStatus());
        }
        System.out.println("======================================================================================");
        System.out.println("Press ENTER to return to menu...");
        new Scanner(System.in).nextLine(); // Pause so user can read
    }

    public int getAppointmentMenu() {
        printHeader("APPOINTMENT HISTORY REPORT");
        System.out.println(" [1] Full Report");
        System.out.println(" [2] Report by Status");
        System.out.println(" [0] Back");
        printLine();
        System.out.print("\nEnter choice: ");
        return getValidInt();
    }

    // Standardized Number Input helper
    public int getValidInt() {
        while (true) {
            try {
                return Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(" [!] Invalid input. Enter a number: ");
            }
        }
    }
    
    public String selectDepartment() {
        System.out.println("\n --- Select Department ---");
        System.out.println(" [1] General Practitioner");
        System.out.println(" [2] Cardiologist");
        System.out.println(" [3] Dermatologist");
        System.out.println(" [4] Pediatrician");
        System.out.println(" [5] Neurologist");
        System.out.println(" [6] Surgeon");
        System.out.print("\nChoice: ");
        int choice = getValidInt();

        return switch (choice) {
            case 2 ->
                "Cardiologist";
            case 3 ->
                "Dermatologist";
            case 4 ->
                "Pediatrician";
            case 5 ->
                "Neurologist";
            case 6 ->
                "Surgeon";
            default ->
                "General Practitioner";
        };
    }
    
    public int getChoiceForCancellation() {
        System.out.print("Enter choice to cancel (0 to back): ");
        return getValidInt();
    }

    public String inputCancellationReason() {
        System.out.print("Enter reason for cancellation: ");
        return scan.nextLine();
    }
    
    public Doctor selectSpecificDoctor(ListInterface<Doctor> doctors) {
        System.out.println("\n--- Available Doctors ---");
        for (int i = 1; i <= doctors.getNumberOfEntries(); i++) {
            Doctor d = doctors.getEntry(i);
            // Using doctors.getEntry(i) to match your ArrayList implementation
            System.out.printf("[%d] %-20s | %s\n", i, d.getDoctorName(), d.getProfession());
        }

        while (true) {
            System.out.print("\nSelect a doctor by number (0 to cancel): ");
            int choice = getValidInt(); // Use your existing helper for safety

            if (choice == 0) {
                return null;
            }

            if (choice >= 1 && choice <= doctors.getNumberOfEntries()) {
                return doctors.getEntry(choice);
            }

            System.out.println(" [!] Invalid selection. Please choose a number from the list.");
        }
    }
    
    public String getStatusChoice() {
        System.out.println("\n--- Select Status to Filter ---");
        System.out.println(" [1] Pending");
        System.out.println(" [2] Completed");
        System.out.println(" [3] Cancelled");
        System.out.print("\nChoice: ");
        int choice = getValidInt();
        return switch (choice) {
            case 1 ->
                "Pending";
            case 2 ->
                "Completed";
            case 3 ->
                "Cancelled";
            default ->
                "Pending";
        };
    }
    
    public void displayAppointmentsByStatus(ListInterface<Appointment> history, String status) {
        printHeader("APPOINTMENT REPORT - " + status.toUpperCase());
        System.out.printf("| %-8s | %-12s | %-20s | %-15s     |\n", "ID", "Date", "Doctor", "Specialty");
        printLine();

        int count = 0;
        for (int i = 1; i <= history.getNumberOfEntries(); i++) {
            Appointment app = history.getEntry(i);
            if (app.getStatus().equalsIgnoreCase(status)) {
                System.out.printf("| %-8s | %-12s | %-20.20s | %-15.15s     |\n",
                        app.getAppointmentID(), app.getDate(), app.getDoctorName(), app.getDoctorSpecialty());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("            -- No " + status + " appointments found --");
        }
        printLine();
    }
    
    
}