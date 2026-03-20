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

    public void displayDoctorAppointment(ListInterface<DocAppointment> appointmentList, String doctorID) {
        System.out.println("-------------------------------------------------------------------------");
        System.out.println("                    MY APPOINTMENT LIST");
        System.out.println("-------------------------------------------------------------------------");
        System.out.printf("%-4s %-15s %-16s %-12s %-10s %-10s%n",
                "No", "Appointment ID", "Patient Name", "Date", "Time", "Status");
        System.out.println("-------------------------------------------------------------------------");

        int no = 1;
        boolean found = false;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID)) {
                System.out.printf("%-4d %-15s %-16s %-12s %-10s %-10s%n",
                        no,
                        appt.getAppointmentID(),
                        appt.getPatientName(),
                        appt.getDate(),
                        appt.getTime(),
                        appt.getStatus());
                no++;
                found = true;
            }
        }

        if (!found) {
            System.out.println("No appointments found.");
            }

            System.out.println("-------------------------------------------------------------------------");
    }

    public int chooseAppointmentNumber() {
        System.out.print("Select appointment number to update (0 to cancel): ");
        return readInt();
    }

    public String chooseStatus() {
    while (true) {
        System.out.println("Choose new status:");
        System.out.println("1. Pending");
        System.out.println("2. Approved");
        System.out.println("3. Completed");
        System.out.println("4. Cancelled");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter your choice: ");

        int choice = readInt();

        switch (choice) {
            case 0:
                return null;
            case 1:
                return "Pending";
            case 2:
                return "Approved";
            case 3:
                return "Completed";
            case 4:
                return "Cancelled";
            default:
                System.out.println("Invalid choice. Try again.\n");
            }
        }
    }

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

    // For doctor menu options after viewing appointments
    public int showDoctorMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("                Doctor Menu                ");
        System.out.println("-------------------------------------------");
        System.out.println("Your Doctor ID: " + doctorID);
        System.out.println("Your Name: " + doctorName);
        
        System.out.println("1. View My Appointment");
        System.out.println("2. Update Appointment Status");
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

    // For doctor menu options after viewing appointments
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
}
