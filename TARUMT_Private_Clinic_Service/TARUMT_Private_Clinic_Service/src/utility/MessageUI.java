/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

/**
 *
 * @author lee seng wai
 */
public class MessageUI {

    // Prompt number input only message
    public static void displayNumberInputOnlyMessage() {
        System.out.println("\u001B[31m" + "Invalid input! Please enter numbers only!" + "\u001B[0m");
    }

    // Prompt return to previous page message
    public static void displayReturnPreviousPageMessage() {
        System.out.print("Returning to previous page...\n");
    }

    // Prompt invalid input message
    public static void displayInvalidInputMessage() {
        System.out.println("\u001B[31m" + "Invalid input! Please enter again! " + "\u001B[0m");
    }

    // Prompt patient ID exists message
    public static void displayPatientIDExistsMessage() {
        System.out.println("\u001B[31m" + "Patient ID or patient name already exists." + "\u001B[0m");
        System.out.println("\u001B[31m" + "Failed to create new patient account." + "\u001B[0m");
    }

    // Prompt create new patient account successful message
    public static void displayNewPatientAccountCreatedSuccessfulMessage() {
        System.out.println("\u001B[32m" + "New patient account created successful." + "\u001B[0m");
    }

    // Prompt patient ID Not Found message
    public static void displayPatientIDNotFoundMessage() {
        System.out.println("\u001B[31m" + "Patient ID not found! Please enter again!" + "\u001B[0m\n");
    }

    // Prompt login patient account successful message
    public static void displayLoginSuccessfulMessage() {
        System.out.println("\u001B[32m" + "Login successful!" + "\u001B[0m");
    }

    // Prompt login patient account successful message
    public static void displayLogoutSuccessfulMessage() {
        System.out.println("\u001B[32m" + "Logout successful!" + "\u001B[0m");
    }

    // Prompt edit patient profile by name message part
    public static void displayEmptyNameMessage() {
        System.out.println("\u001B[31m" + "Your name cannot be empty!" + "\u001B[0m\n");
    }

    public static void displayInvalidNameMessage() {
        System.out.println("\u001B[31m" + "Your name can only contain letters and spaces!" + "\u001B[0m\n");
    }

    public static void displayUpdateNameSuccessfulMessage() {
        System.out.println("\u001B[32m" + "Your name has updated successful!" + "\u001B[0m\n");
    }

    // Prompt edit patient profile by gender message part
    public static void displayEmptyGenderMessage() {
        System.out.println("\u001B[31m" + "Gender cannot be empty!" + "\u001B[0m\n");
    }

    public static void displayInvalidGenderMessage() {
        System.out.println("\u001B[31m" + "Invalid gender! Please enter Male or Female only!" + "\u001B[0m\n");
    }

    public static void displayUpdateGenderSuccessfulMessage() {
        System.out.println("\u001B[32m" + "Your gender has updated successful!" + "\u001B[0m\n");
    }

    // Prompt edit patient profile by contact number message part
    public static void displayEmptyContactNumberMessage() {
        System.out.println("\u001B[31m" + "Contact number cannot be empty!" + "\u001B[0m\n");
    }

    public static void displayInvalidContactNumberMessage() {
        System.out.println("\u001B[31m" + "Invalid contact number format! Please enter again!" + "\u001B[0m\n");
    }

    public static void displayUpdateContactNumberSuccessfulMessage() {
        System.out.println("\u001B[32m" + "Your contact number has updated successful!" + "\u001B[0m\n");
    }

    public static void displayInvalidConfirmInputMessage() {
        System.out.println("\u001B[31m" + "Invalid input!. Please enter Y/Yes or N/No only!" + "\u001B[0m\n");
    }

    // Prompt book appointment message part
    public static void displayEmptySymptomInputMessage() {
        System.out.println("\u001B[31m" + "Symptoms cannot be empty!" + "\u001B[0m");
    }
    
    public static void displayDoctorNotFoundMessage(String specialty) {
        System.out.println("\u001B[31m" + "No available doctor found in " + specialty + "!" + "\u001B[0m");
    }
    
    public static void displayExistingAppointmentMessage() {
        System.out.println("\n [!] Access Denied: You already have an active appointment.");
        System.out.println("Please complete or cancel your current appointment first.");
    }

    public static void displayActiveAppointmentExistsMessage() {
        System.out.println("You already have an active appointment. ");
        System.out.println("Please wait for doctor consultation or cancel it before booking a new one. ");
    }

    public static void displayGeneralDoctorAssignMessage() {
        System.out.println("Unable to determine specific symptom.");
        System.out.println("You will be assigned to General Doctor.");
    }

    public static void displayDoctorNotFoundMessage() {
        System.out.println("No available doctor found!");
    }

    public static void displayBookAppointmentSuccessfulMessage() {
        System.out.println("\n\u001B[32m" + "Appointment registered successful!" + "\u001B[0m");
    }
    
    public static void displayBookAppointmentFailMessage() {
        System.out.println("Appointment registration cancelled.");
    }
    
    // Prompt cancel appointment message part
    public static void displayAppointmentHistoryNotFoundMessage() {
        System.out.println("No appointment history found!");
        System.out.println("Press ENTER to continue...");
    }

    public static void displayActiveAppointmentNotFoundMessage() {
        System.out.println("No pending appointment found!");
        System.out.println("Press ENTER to continue...");
    }

    public static void displayCancelAppointmentRequestMessage() {
        System.out.println("\u001B[32m" + "Pending cancel! Need to wait for admin approval!" + "\u001B[0m\n");
    }

    public static void displayCancelAppointmentAbortMessage() {
        System.out.println("Cancel appointment aborted!");
    }

    public static void displayInvalidAppointmentSelectionMessage() {
        System.out.println("\u001B[31m" + "Invalid appointment selection!" + "\u001B[0m\n");
    }

}
