/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import java.util.Scanner;

import adt.ListInterface;
import control.DoctorControl;
import entity.DocAppointment;
import entity.Doctor;

/**
 *
 * @author Lim SiYu, Lee Seng Wai, Tang Le Yi, Ivan Wai Kim Hou
 */

public class DoctorUI {
    Scanner input = new Scanner(System.in);
    
    private int selection;
    private String doctorID;
    private String doctorName;
   
    // Doctor login, validate doctor ID format
    public String enterDoctorID(DoctorControl control){
        while(true){
            System.out.print("Enter Doctor ID (Format DXXX): ");
            doctorID = input.nextLine().trim(); // remove extra spaces

            // Convert lowercase 'd' to uppercase
            if(doctorID.length() > 0 && (doctorID.charAt(0) == 'd' || doctorID.charAt(0) == 'D')){
                doctorID = "D" + doctorID.substring(1);
            }

            // ID format check DXXX
            if(!doctorID.matches("D\\d{3}")){
                System.out.println("Invalid Doctor ID format. Example: D001");
                continue;
            }

            // Existence check
            if(!control.isValidDoctorID(doctorID)){
                System.out.println("Doctor ID not found.");
                continue;
            }

            doctorName = control.getDoctorName(doctorID);
            System.out.println("Welcome, " + doctorName + "!");
            return doctorID;
        }
    }

    public int viewAppointmentMenu() {
    System.out.println("===========================================");
    System.out.println("       VIEW APPOINTMENT OPTIONS");
    System.out.println("===========================================");
    System.out.println("Which Appointment list would you like to view?");
    System.out.println("1. Current Patient Waiting");
    System.out.println("2. Pending Patients");
    System.out.println("3. Follow Up Patients");
    System.out.println("4. Completed Patients");
    System.out.println("5. Cancelled Patients");
    System.out.println("0. Return to Previous Menu");
    System.out.println("===========================================");
    System.out.print("Enter your choice: ");

    return readInt();
    }

    public void displayPatientsByStatus(ListInterface<DocAppointment> appointmentList, String doctorID, String status) {
        System.out.println("PATIENT LIST - " + status.toUpperCase());
        System.out.println("========================================");

        boolean found = false;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID)
                    && appt.getStatus().equalsIgnoreCase(status)) {

                System.out.println("Appointment ID : " + appt.getAppointmentID());
                System.out.println("Patient Name   : " + appt.getPatientName());
                System.out.println("Queue Number   : " + appt.getQueueNo());
                System.out.println("Status         : " + appt.getStatus());
                System.out.println("----------------------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No " + status + " patients found.");
        }
    }

    public void displayUpdatedAppointmentDetails(DocAppointment appt, String feedback) {
        System.out.println("==============================================================");
        System.out.println("              UPDATED APPOINTMENT DETAILS");
        System.out.println("==============================================================");
        System.out.println("Appointment ID   : " + appt.getAppointmentID());
        System.out.println("Patient Name     : " + appt.getPatientName());
        System.out.println("Queue Number     : " + appt.getQueueNo());
        System.out.println("Updated Status   : " + appt.getStatus());
        System.out.println("Doctor Feedback  : " + feedback);
        System.out.println("==============================================================");
    }

    public void displayDoctorAppointment(ListInterface<DocAppointment> appointmentList, String doctorID) {
        System.out.println("MY APPOINTMENT LIST");
        System.out.println("========================================");

        boolean found = false;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID) 
                    && appt.getStatus().equalsIgnoreCase("Pending")) {

                System.out.println("Appointment ID : " + appt.getAppointmentID());
                System.out.println("Patient Name   : " + appt.getPatientName());
                System.out.println("Queue Number   : " + appt.getQueueNo());
                System.out.println("Status         : " + appt.getStatus());
                System.out.println("----------------------------------------");

                found = true;
            }
        }

        if (!found) {
            System.out.println("No pending appointments.");
        }
    }
public void displayPatientsTable(ListInterface<DocAppointment> appointmentList, String doctorID, String status) {
    System.out.println("==============================================================");
    System.out.println("PATIENT LIST - " + status.toUpperCase());
    System.out.println("==============================================================");

    System.out.printf("%-4s %-12s %-18s %-10s %-12s%n",
            "No", "Queue", "Patient Name", "Status", "Appt ID");

    System.out.println("--------------------------------------------------------------");

    int no = 1;
    boolean found = false;

    for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
        DocAppointment appt = appointmentList.getEntry(i);

        if (appt.getDoctorID().equals(doctorID)
                && appt.getStatus().equalsIgnoreCase(status)) {

            System.out.printf("%-4d %-12d %-18s %-10s %-12s%n",
                    no,
                    appt.getQueueNo(),
                    appt.getPatientName(),
                    appt.getStatus(),
                    appt.getAppointmentID());

            no++;
            found = true;
        }
    }

    if (!found) {
        System.out.println("No " + status + " patients found.");
    }

    System.out.println("==============================================================");
}

    // public void displayDoctorAppointment(ListInterface<DocAppointment> appointmentList, String doctorID) {
    //     System.out.println("-------------------------------------------------------------------------");
    //     System.out.println("                    MY APPOINTMENT LIST");
    //     System.out.println("-------------------------------------------------------------------------");
    //     System.out.printf("%-4s %-15s %-16s %-12s %-10s %-10s%n",
    //             "No", "Appointment ID", "Patient Name", "Date", "Time", "Status");
    //     System.out.println("-------------------------------------------------------------------------");

    //     int no = 1;
    //     boolean found = false;

    //     for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
    //         DocAppointment appt = appointmentList.getEntry(i);

    //         if (appt.getDoctorID().equals(doctorID)) {
    //             System.out.printf("%-4d %-15s %-16s %-12s %-10s %-10s%n",
    //                     no,
    //                     appt.getAppointmentID(),
    //                     appt.getPatientName(),
    //                     appt.getDate(),
    //                     appt.getTime(),
    //                     appt.getStatus());
    //             no++;
    //             found = true;
    //         }
    //     }

    //     if (!found) {
    //         System.out.println("No appointments found.");
    //         }

    //         System.out.println("-------------------------------------------------------------------------");
    // }

    public int chooseAppointmentNumber() {
        System.out.print("Select appointment number to update (0 to cancel): ");
        return readInt();
    }

        public void displayNextPatient(DocAppointment appt) {
        System.out.println("==============================================================");
        System.out.println("            CURRENT PATIENT WAITING");
        System.out.println("==============================================================");

        System.out.printf("%-12s %-18s %-10s %-12s%n",
                "Queue", "Patient Name", "Status", "Appt ID");

        System.out.println("--------------------------------------------------------------");

        if (appt == null) {
            System.out.println("No pending patients.");
        } else {
            System.out.printf("%-12d %-18s %-10s %-12s%n",
                    appt.getQueueNo(),
                    appt.getPatientName(),
                    appt.getStatus(),
                    appt.getAppointmentID());
        }

        System.out.println("==============================================================");
    }

    public String chooseStatus() {
    while (true) {
        System.out.println("Edit Patient status:");
        System.out.println("1. Completed");
        System.out.println("2. Cancelled");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter your choice: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return null;
            case 1:
                return "Completed";
            case 2:
                return "Cancelled";
            default:
                System.out.println("Invalid choice. Try again.\n");
            }
        }
    }

    public int currentPatientActionMenu() {
        System.out.println("What would you like to do next?");
        System.out.println("1. Edit Patient Status");
        System.out.println("2. Pass to Next Patient");
        System.out.println("0. Return to Previous Menu");
        System.out.print("Enter your choice: ");
        return readInt();
    }

    public int afterStatusUpdatedMenu() {
        System.out.println("What would you like to do next?");
        System.out.println("1. Pass to Next Patient");
        System.out.println("0. Return to Previous Menu");

        System.out.print("Enter your choice: ");
        return readInt();
    }

    public String inputDoctorFeedback() {
    System.out.print("Enter doctor feedback: ");
    return input.nextLine();
}
    public int statusTableActionMenu() {
        System.out.println("========================================");
        System.out.println("     What would you like to do next?");
        System.out.println("========================================");
        System.out.println("1. Edit Patient Status");
        System.out.println("0. Return to Previous Menu");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
        return readInt();
    }

    public int continueUpdateMenu() {
        System.out.println("========================================");
        System.out.println("     What would you like to do next?");
        System.out.println("========================================");
        System.out.println("1. Continue Updating Appointment Status");
        System.out.println("0. Return to Previous Menu");
        System.out.println("========================================");
        System.out.print("Enter your choice: ");
        return readInt();
    }

    
    //VIEW PROFILE & UPDATE PROFILE METHODS
    public void displayProfile(String doctorID, Doctor doctor) {
        System.out.println("----- Your Current Profile -----");
        System.out.println("Doctor ID : " + doctorID);
        System.out.println("Name      : " + doctor.getDoctorName());
        System.out.println("Phone     : " + doctor.getPhone());
        System.out.println("Gender    : " + doctor.getGender());
        System.out.println("--------------------------------");
    }
    public int chooseUpdateField() {
        System.out.println("Select which detail to update:");
        System.out.println("1. Name");
        System.out.println("2. Phone");
        System.out.println("3. Gender");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter your choice: ");
        
        return readInt();
    }

    public String inputNewName(Doctor doctor){
        System.out.println("Your Name: " + doctor.getDoctorName());
        System.out.print("Enter new name: ");
        return input.nextLine();
    }

    public String inputNewPhone(Doctor doctor){
        System.out.println("Your Phone: " + doctor.getPhone());
        System.out.print("Enter new phone: ");
        return input.nextLine();
    }

    public String chooseGender(Doctor d) {
    while (true) {
        System.out.println("Choose Your Gender: ");
        System.out.println("1.Male");
        System.out.println("2.Female");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter new gender: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return null;
            case 1:
                return "Male";
            case 2:
                return "Female";
            default:
                System.out.println("Invalid choice. Try again.\n");
            }
        }
    }

    //Report Module
    public int reportMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("               MY REPORT MENU              ");
        System.out.println("-------------------------------------------");
        System.out.println("1. Display All Report");
        System.out.println("2. Search Report");
        System.out.println("3. Total Appointment Report");
        System.out.println("0. Return to Previous Menu");
        System.out.print("Enter your choice: ");

        return readInt();
    }

    //Display all report (case 1)
    public void displayReportTable(ListInterface<DocAppointment> appointmentList, String doctorID) {
        System.out.println("==========================================================================");
        System.out.println("                         ALL APPOINTMENT REPORT");
        System.out.println("==========================================================================");

        System.out.printf("%-4s %-18s %-12s %-12s %-20s%n",
                "No", "Patient Name", "Status", "Appt ID", "Doctor Feedback");

        System.out.println("--------------------------------------------------------------------------");

        int no = 1;
        boolean found = false;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID)) {
                String feedback = appt.getDoctorFeedback();

                if (feedback == null || feedback.trim().isEmpty()) {
                    feedback = "-";
                }

                System.out.printf("%-4d %-18s %-12s %-12s %-20s%n",
                        no,
                        appt.getPatientName(),
                        appt.getStatus(),
                        appt.getAppointmentID(),
                        feedback);

                no++;
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
        }

        System.out.println("==========================================================================");
    }

    //Search Report(case 2)
    public int searchReportMenu() {
        System.out.println("-------------------------------------------");
        System.out.println("              SEARCH REPORT MENU           ");
        System.out.println("-------------------------------------------");
        System.out.println("1. Search by Patient Name");
        System.out.println("2. Search by Status");
        System.out.println("3. Search by Appointment ID");
        System.out.println("0. Return to Previous Menu");
        System.out.print("Enter your choice: ");

        return readInt();
    }

    public String inputSearchKeyword(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    public void displaySearchResultTable(ListInterface<DocAppointment> appointmentList, String doctorID, String type, String keyword) {
        System.out.println("==========================================================================");
        System.out.println("                     SEARCH RESULT");
        System.out.println("==========================================================================");

        System.out.printf("%-4s %-18s %-12s %-12s %-20s%n",
                "No", "Patient Name", "Status", "Appt ID", "Doctor Feedback");

        System.out.println("--------------------------------------------------------------------------");

        int no = 1;
        boolean found = false;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (!appt.getDoctorID().equals(doctorID)) continue;

            boolean match = false;

            switch (type) {
                case "name":
                    match = appt.getPatientName().toLowerCase().contains(keyword.toLowerCase());
                    break;

                case "status":
                    match = appt.getStatus().equalsIgnoreCase(keyword);
                    break;

                case "id":
                    match = appt.getAppointmentID().equalsIgnoreCase(keyword);
                    break;
            }

            if (match) {
                String feedback = appt.getDoctorFeedback();
                if (feedback == null || feedback.trim().isEmpty()) {
                    feedback = "-";
                }

                System.out.printf("%-4d %-18s %-12s %-12s %-20s%n",
                        no,
                        appt.getPatientName(),
                        appt.getStatus(),
                        appt.getAppointmentID(),
                        feedback);

                no++;
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching result found.");
        }

        System.out.println("==========================================================================");
    }

    public String chooseSearchStatus() {
        while (true) {
            System.out.println("Choose status to search:");
            System.out.println("1. Pending");
            System.out.println("2. Completed");
            System.out.println("3. Cancelled");
            System.out.println("0. Return to previous menu");
            System.out.print("Enter your choice: ");

            int choice = readInt();

            switch (choice) {
                case 0:
                    return null;
                case 1:
                    return "Pending";
                case 2:
                    return "Completed";
                case 3:
                    return "Cancelled";
                default:
                    System.out.println("Invalid choice. Try again.\n");
            }
        }
    }

    //calculate total report case 3)
    public void displayTotalAppointmentReport(int pendingCount, int completedCount, int cancelledCount, int totalCount) {
        System.out.println("==================================================");
        System.out.println("            TOTAL APPOINTMENT REPORT");
        System.out.println("==================================================");
        System.out.println("Total Pending Appointments   : " + pendingCount);
        System.out.println("Total Completed Appointments : " + completedCount);
        System.out.println("Total Cancelled Appointments : " + cancelledCount);
        System.out.println("--------------------------------------------------");
        System.out.println("Grand Total Appointments     : " + totalCount);
        System.out.println("==================================================");
    }


    // For doctor menu options after viewing appointments
    public int showDoctorMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("                Doctor Menu                ");
        System.out.println("-------------------------------------------");
        System.out.println("Your Doctor ID: " + doctorID);
        System.out.println("Your Name: " + doctorName);
        
        System.out.println("1. View and Manage My Appointment");
        System.out.println("2. View My Report");
        System.out.println("3. Update Your Profile");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
        selection = readInt();
        return selection;
    }

    // For displaying messages to doctor after actions
    public void displayMessage(String message) {
        System.out.println(message);
    }

    public int displayOptionsAndGetChoice(String title, String... options) {
        System.out.println(title);

        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }

        System.out.println("0. Return to Previous Menu");
        System.out.print("Enter your choice: ");

        return readInt();
    }

    // Helper method to read integer input with validation
    private int readInt() {
    while (!input.hasNextInt()) {
        System.out.print("Invalid input. Please enter a number: ");
        input.nextLine(); // clear wrong input
    }

    int choice = input.nextInt();
    input.nextLine(); // clear buffer
    return choice;
    }

    public void pressEnterToContinue() {
        System.out.print("Press Enter to continue...");
        input.nextLine();
    }
}
