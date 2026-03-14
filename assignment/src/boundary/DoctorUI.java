/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import java.util.Scanner;

import control.DoctorControl;
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
        public void displayProfile(String doctorID, Doctor doctor) {
        System.out.println("----- Your Current Profile -----");
        System.out.println("Doctor ID : " + doctorID);
        System.out.println("Name      : " + doctor.getDoctorName());
        System.out.println("Phone     : " + doctor.getPhone());
        System.out.println("Profession: " + doctor.getProfession());
        System.out.println("--------------------------------");
    }
        public int chooseUpdateField() {
        System.out.println("Select which detail to update:");
        System.out.println("1. Name");
        System.out.println("2. Phone");
        System.out.println("3. Profession");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter your choice: ");
        
        int choice = input.nextInt();
        input.nextLine(); // clear buffer
        return choice;
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

    public String inputNewProfession(Doctor doctor){
        System.out.println("Your Profession: " + doctor.getProfession());
        System.out.print("Enter new profession: ");
        return input.nextLine();
    }
    public String chooseProfession() {
    while (true) {
        System.out.println("Select your profession:");
        System.out.println("1. General Practitioner");
        System.out.println("2. Dentist");
        System.out.println("3. Pediatrician");
        System.out.println("4. Dermatologist");
        System.out.println("5. Physiotherapist");
        System.out.println("0. Return to previous menu");
        System.out.print("Enter your choice (0-5): ");

        int choice = input.nextInt();
        input.nextLine(); // clear buffer

        switch (choice) {
            case 0:
                return null; // Exit without updating
            case 1:
                return "General Practitioner";
            case 2:
                return "Dentist";
            case 3:
                return "Pediatrician";
            case 4:
                return "Dermatologist";
            case 5:
                return "Physiotherapist";
            default:
                System.out.println("Invalid choice. Please select a number between 0 and 5.");
        }
    }
}
    
    public int showDoctorMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("----------------Doctor Menu----------------");
        System.out.println("-------------------------------------------");
        //show current doctor, need change after
        System.out.println("Your Doctor ID: " + doctorID);
        System.out.println("Your Name: " + doctorName);
        
        System.out.println("1. View My Appointment");
        System.out.println("2. Update Appointment Status");
        System.out.println("3. Update Your Profile");
        System.out.println("0. Exit");
        System.out.println("Enter your choice: ");
        selection = input.nextInt();
        input.nextLine(); //clear buffer
        return selection;
    }

    
}
