package boundary;

import java.util.Scanner;
import adt.ListInterface;
import control.DoctorControl;
import entity.Appointment;
import entity.Doctor;

/**
 * @author Tang Le Yi
 */

public class DoctorUI {
    private Scanner input = new Scanner(System.in);
    private String doctorID;

    // --- HELPER METHOD ---
    private void printHeader(String title) {
        System.out.println("\n========================================================================");
        System.out.printf("                     %-50s\n", title);
        System.out.println("========================================================================");
    }

    private void printDivider() {
        System.out.println("------------------------------------------------------------------------");
    }
    
    public int showPortalMenu() {
        printHeader("DOCTOR MODULE");
        System.out.println(" [1] Login to Existing Account");
        System.out.println(" [2] Register New Doctor Account");
        printDivider();
        System.out.println(" [0] Exit System");
        System.out.print("\n Selection > ");
        return readInt();
    }

    // 1. Login Interface
    public String enterDoctorID(DoctorControl control) {
        printHeader("DOCTOR MODULE");
        while (true) {
            System.out.print("Enter Doctor ID (e.g., D001) >> ");
            doctorID = input.nextLine().trim();

            if (doctorID.length() > 0 && (doctorID.charAt(0) == 'd' || doctorID.charAt(0) == 'D')) {
                doctorID = "D" + doctorID.substring(1);
            }

            if (!doctorID.matches("D\\d{3}")) {
                System.out.println(" [!] Invalid format. Please use DXXX.");
                continue;
            }

            if (!control.isValidDoctorID(doctorID)) {
                System.out.println(" [!] Doctor ID not found in system.");
                continue;
            }

            String doctorName = control.getDoctorName(doctorID);
            System.out.println("\nWelcome back, Dr. " + doctorName + "!");
            return doctorID;
        }
    }

    // 2. Main Doctor Menu
    public int showDoctorMenu(String currentName) {
        printHeader("DOCTOR MAIN MENU");
        System.out.println("Logged in as : Dr. " + currentName + " [" + doctorID + "]");
        printDivider();
        System.out.println(" [1] View and Manage Appointments");
        System.out.println(" [2] View Performance Reports");
        System.out.println(" [3] Update Personal Profile");
        System.out.println();
        printDivider();
        System.out.println(" [0] Logout and Exit");
        System.out.println("========================================================================");
        System.out.print("\n Selection > ");
        return readInt();
    }

    // 3. Appointment Tables
    public void displayPatientsTable(ListInterface<Appointment> appointmentList, String doctorID, String status) {
        printHeader("PATIENT LIST - " + status.toUpperCase());

        System.out.printf("%-4s %-20s %-15s %-10s\n", "No", "Patient Name", "Appt ID", "Status");
        printDivider();

        int no = 1;
        boolean found = false;
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment appt = appointmentList.getEntry(i);
            if (appt.getDoctorID().trim().equalsIgnoreCase(doctorID.trim()) && appt.getStatus().trim().equalsIgnoreCase(status.trim())) {
            
                System.out.printf("%-4d %-20s %-15s %-10s\n",
                        no, appt.getPatientName(), appt.getAppointmentID(), appt.getStatus());
                no++;
                found = true;
            }
        }
        if (!found) System.out.println("No " + status + " patients found.");
        System.out.println("========================================================================");
    }

    public void displayReportTable(ListInterface<Appointment> appointmentList, String doctorID) {
        printHeader("ALL APPOINTMENT REPORT");
        System.out.printf("%-4s %-18s %-12s %-12s %-20s\n", "No", "Patient Name", "Status", "Appt ID", "Feedback");
        printDivider();

        int no = 1;
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment appt = appointmentList.getEntry(i);
            if (appt.getDoctorID().equals(doctorID)) {
                String feedback = appt.getRemarks();
                if (feedback == null || feedback.isEmpty()) feedback = "-";
                System.out.printf("%-4d %-18s %-12s %-12s %-20s\n",
                        no++, appt.getPatientName(), appt.getStatus(), appt.getAppointmentID(), feedback);
            }
        }
        System.out.println("========================================================================");
    }

    // 4. Search UI
    public int searchReportMenu() {
        printHeader("SEARCH REPORT BY");
        System.out.println(" [1] Patient Name");
        System.out.println(" [2] Appointment Status");
        System.out.println(" [3] Appointment ID");
        printDivider();
        System.out.println(" [0] Return to Menu");
        System.out.print("\nSearch Type > ");
        return readInt();
    }

    public String inputSearchKeyword(String prompt) {
        System.out.print("" + prompt);
        return input.nextLine().trim();
    }

    public String chooseSearchStatus() {
        System.out.println("\nCHOOSE STATUS:");
        System.out.println(" [1] Pending  [2] Completed  [3] Cancelled  [0] Abort");
        System.out.print("Selection > ");
        int choice = readInt();
        switch (choice) {
            case 1: return "Pending";
            case 2: return "Completed";
            case 3: return "Cancelled";
            default: return null;
        }
    }

    public void displaySearchResultTable(ListInterface<Appointment> list, String doctorID, String type, String keyword) {
        printHeader("SEARCH RESULTS");
        System.out.printf("%-4s %-18s %-12s %-12s %-20s\n", "No", "Patient Name", "Status", "Appt ID", "Feedback");
        printDivider();

        int no = 1;
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            Appointment appt = list.getEntry(i);
            if (!appt.getDoctorID().equals(doctorID)) continue;

            boolean match = false;
            if (type.equals("name")) match = appt.getPatientName().toLowerCase().contains(keyword.toLowerCase());
            else if (type.equals("status")) match = appt.getStatus().equalsIgnoreCase(keyword);
            else if (type.equals("id")) match = appt.getAppointmentID().equalsIgnoreCase(keyword);

            if (match) {
                System.out.printf("%-4d %-18s %-12s %-12s %-20s\n",
                        no++, appt.getPatientName(), appt.getStatus(), appt.getAppointmentID(), appt.getRemarks());
            }
        }
    }

    // 5. Action Menus
    public int currentPatientActionMenu() {
        System.out.println("\nACTION MENU:");
        System.out.println(" [1] Edit Patient Status");
        System.out.println(" [0] Return to Menu");
        System.out.print("\nAction > ");
        return readInt();
    }

    public String chooseStatus() {
        System.out.println("\nSET STATUS TO:");
        System.out.println(" [1] To-Collect the Medicine");
        System.out.println(" [2] Completed");
        System.out.println(" [3] Cancelled");
        System.out.print("\nSelection > ");
        int choice = readInt();
        if (choice == 1) return "1";
        if (choice == 2) return "2";
        if (choice == 3) return "3";
        return "0";
    }
    
    public String inputCancellationReason() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Reason for Cancellation: ");
        return scanner.nextLine();
    }

    // 6. Profile Management UI
    public void displayProfile(String doctorID, Doctor doctor) {
        printHeader("MY PROFILE DETAILS");
        System.out.printf("%-15s : %s\n", "Doctor ID", doctorID);
        System.out.printf("%-15s : %s\n", "Name", doctor.getDoctorName());
        System.out.printf("%-15s : %s\n", "Phone", doctor.getPhone());
        System.out.printf("%-15s : %s\n", "Gender", doctor.getGender());
        System.out.printf("%-15s : %s\n", "Profession", doctor.getProfession());
        System.out.println("========================================================================");
    }
    
    // --- UPDATED REGISTRATION METHOD ---
    public Doctor inputRegistrationDetails(control.DoctorControl control) {
        printHeader("DOCTOR ACCOUNT REGISTRATION");
        String id;
        while (true) {
            System.out.print("Create Doctor ID (DXXX) >> ");
            id = input.nextLine().trim().toUpperCase();
            if (!id.matches("D\\d{3}")) {
                System.out.println(" [!] Format: D followed by 3 digits (e.g. D005).");
                continue;
            }
            if (control.isValidDoctorID(id)) {
                System.out.println(" [!] ID already exists. Try another.");
                continue;
            }
            break;
        }

        String name = inputNewName(null); 
        String phone = inputNewPhone(null);
        String profession = selectProfession(null);
        String gender = chooseGender(null);

        return new Doctor(id, name, phone, profession, gender);
    }

    public int chooseUpdateField() {
        System.out.println("\nUPDATE FIELD:");
        System.out.println(" [1] Name  [2] Phone  [3] Gender  [4] Profession  [0] Done");
        System.out.print("Selection > ");
        return readInt();
    }


    public String inputNewName(Doctor d) {
        String name;
        while (true) {
            // If d is null, show simple prompt. If d exists, show "Current"
            String prompt = (d == null) ? "Enter Full Name >> " : "Enter New Name (Current: " + d.getDoctorName() + ") >> ";
            System.out.print(prompt);
            name = input.nextLine().trim();
            if (name.matches("^[a-zA-Z\\s.]+$") && !name.isEmpty()) return name;
            System.out.println(" [!] Invalid name. Use alphabets only.");
        }
    }

    public String inputNewPhone(Doctor d) {
        String phone;
        while (true) {
            String prompt = (d == null) ? "Enter Phone Number >> " : "Enter New Phone (Current: " + d.getPhone() + ") >> ";
            System.out.print(prompt);
            phone = input.nextLine().trim();
            if (phone.matches("^\\d{10,11}$")) return phone;
            System.out.println(" [!] Invalid format. Enter 10-11 digits.");
        }
    }

    public String chooseGender(Doctor d) {
        if (d != null) System.out.println("Current Gender: " + d.getGender());
        System.out.println(" [1] Male  [2] Female" + (d == null ? "" : "  [0] Keep Current"));
        System.out.print("Gender Selection > ");
        int c = readInt();
        if (c == 1) return "Male";
        if (c == 2) return "Female";
        return (d == null) ? "Male" : d.getGender(); // Default for new, Keep for update
    }

    public String selectProfession(Doctor d) {
        if (d != null) System.out.println("\n--- UPDATE PROFESSION (Current: " + d.getProfession() + ") ---");
        else System.out.println("\n--- SELECT PROFESSION ---");
        
        System.out.println(" [1] General Practitioner\n [2] Cardiologist\n [3] Dermatologist\n [4] Pediatrician\n [5] Neurologist\n [6] Surgeon");
        if (d != null) System.out.println(" [0] Keep Current");
        
        System.out.print(" Choice > ");
        int choice = readInt();
        
        return switch(choice) {
            case 1 -> "General Practitioner";
            case 2 -> "Cardiologist";
            case 3 -> "Dermatologist";
            case 4 -> "Pediatrician";
            case 5 -> "Neurologist";
            case 6 -> "Surgeon";
            default -> (d == null) ? "General Practitioner" : d.getProfession();
        };
    }


    // 7. Report & Utilities
    public int reportMenu() {
        printHeader("MY REPORT GENERATOR");
        System.out.println(" [1] Display All Appointments");
        System.out.println(" [2] Filter/Search Reports");
        System.out.println(" [3] View Statistical Summary");
        printDivider();
        System.out.println(" [0] Back to Previous Menu");
        System.out.println("========================================================================");
        System.out.print("\nSelection > ");
        return readInt();
    }

    public void displayTotalAppointmentReport(int pending, int completed, int cancelled, int total) {
        printHeader("APPOINTMENT SUMMARY STATISTICS");
        System.out.printf("%-30s : %d\n", "Total Pending", pending);
        System.out.printf("%-30s : %d\n", "Total Completed", completed);
        System.out.printf("%-30s : %d\n", "Total Cancelled", cancelled);
        printDivider();
        System.out.printf("%-30s : %d\n", "GRAND TOTAL", total);
        System.out.println("========================================================================");
    }

    private int readInt() {
        while (true) {
            try {
                String raw = input.nextLine().trim();
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.print(" [!] Please enter a digit >> ");
            }
        }
    }

    public void displayMessage(String message) {
        System.out.println("\n>> " + message);
    }

    public void pressEnterToContinue() {
        System.out.print("\nPress [Enter] to continue...");
        input.nextLine();
    }

    public String inputDoctorFeedback() {
        System.out.print("Enter Medical Feedback >> ");
        return input.nextLine();
    } 
    
    public void displayServingPatient(Appointment appt) {
        System.out.println("\n=======================================================");
        System.out.println("NOW SERVING PATIENT: " + appt.getPatientName());
        System.out.println("SYMPTOM            : " + appt.getSymptom());
        System.out.println("=======================================================");
    }
    
    
    
}