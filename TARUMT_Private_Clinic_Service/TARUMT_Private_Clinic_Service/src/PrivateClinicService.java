/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Lim SiYu, Lee Seng Wai, Tang Le Yi, Ivan Wai Kim Hou
 */

import boundary.*;
import control.*;
import entity.*;
import utility.MessageUI;


public class PrivateClinicService {

    public static void main(String[] args) {

        MainMenuUI menu = new MainMenuUI();
        AdminUI adminUI = new AdminUI();
        AdminControl adminControl = new AdminControl();
        DoctorControl doctorControl = new DoctorControl();
        MedicineManagement medicineUI = new MedicineManagement();
        PatientControl patientControl = new PatientControl();
   
        
        int choice;

        do{
            choice = menu.getMainChoice();

            switch(choice){

                case 1:
                    patientControl.runPatientModule();
                    break;

                case 2:
                    doctorControl.runDoctorModule();
                    break;

                case 3:
                    String adminID = adminUI.enterAdminID();
                    
                    if (adminID.equals("0")) {
                        System.out.println("  Returning to Module Selection...");
                        break;
                    }
                    
                    if (adminControl.login(adminID)) { 
                        adminUI.displayMessage("Login Successful!");
                        
                        runAdminModule(adminUI, adminControl);
                        
                    } else {
                        adminUI.displayMessage("Invalid Admin ID! Access Denied.");
                    }
                    break;
                    
                case 4:
                    medicineUI.start();
                    break;

                case 0:
                    System.out.println("Thank you for using system.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        }while(choice != 0);

    }
    
    private static void runAdminModule(AdminUI ui, AdminControl control) {
        int adminChoice;
        do {
            adminChoice = ui.showAdminMenu(control.getCurrentAdmin());

            switch (adminChoice) {
                case 1: // Manage Patient Account
                    handlePatientSection(ui, control);
                    break;

                case 2: // Manage Doctor Account
                    handleDoctorSection(ui, control);
                    break;

                case 3: // Manage Appointment
                    handleAppointmentSection(ui, control);
                    break;

                case 4: // View Report
                    handleReportSection(ui, control);
                    break;
                    
                case 5: // Manage Pharmacist Account
                    handlePharmacistSection(ui, control);
                    break;

                case 0:
                    ui.displayMessage("Returning to Main Module...");
                    break;
            }
        } while (adminChoice != 0);
    }
    

    private static void handlePatientSection(AdminUI ui, AdminControl control) {
        int pChoice;
        do {
            pChoice = ui.showManagePatientMenu();
            switch (pChoice) {
                case 1: // Add New Patient
                    Patient p = ui.inputPatientDetails(control);
                    if (control.addPatient(p)) {
                        ui.displayMessage("Patient added successfully!");
                    } else {
                        ui.displayMessage("Error: Invalid ID format (must be PXXX)");
                    }
                    break;

                case 2: // Display All
                    String allData = control.getAllPatients();
                    System.out.println(allData);
                    break;

                case 3: // Update Patient
                    String updateID = ui.inputID("Patient");
                    Patient foundPatient = control.findPatientById(updateID);

                    if (foundPatient != null) {
                        boolean updatingPatient = true;
                        while (updatingPatient) {
            
                        ui.displayPatientProfile(foundPatient); 
                        int fieldChoice = ui.showPatientUpdateFields(); 
            
                        if (fieldChoice == 0) {
                            updatingPatient = false;
                            continue;
                        }

                        String newValue = "";
                        switch (fieldChoice) {
                            case 1 -> newValue = ui.inputNewName();        // Validates: Letters only
                            case 2 -> newValue = ui.selectGender();       // Validates: Choice 1/2
                            case 3 -> newValue = ui.inputValidatedIC();    // Validates: 12 digits
                            case 4 -> newValue = ui.inputNewPhone();       // Validates: 10-11 digits
                            default -> ui.displayMessage("Invalid choice.");
                        }

                        if (!newValue.isEmpty()) {
                
                            control.updatePatientField(foundPatient, fieldChoice, newValue);
                            ui.displayMessage("Field updated and saved to file.");
                        }
                        }
                } else {
                    ui.displayMessage("Patient ID not found.");
                }
                break;

                case 4: // Remove Patient
                    String removeID = ui.inputID("Patient");
                    boolean removed = control.removePatient(removeID);
                    if (removed) {
                        ui.displayMessage("Patient account deleted.");
                    } else {
                        ui.displayMessage("Error: Patient ID not found.");
                    }
                    break;
            }
        } while (pChoice != 0);
    }
    
    private static void handleDoctorSection(AdminUI ui, AdminControl control) {
        int dChoice;
        do {
            dChoice = ui.showManageDoctorMenu();
            switch (dChoice) {
                case 1: // Create New Doctor
                    Doctor newD = ui.inputNewDoctorDetails(control);
                    if (control.addDoctor(newD)) {
                        ui.displayMessage("Doctor account created!");
                    } else {
                        ui.displayMessage("Error: Doctor ID " + newD.getDoctorID() + " already exists in the system.");
                    }
                    break;

                case 2: // View All Doctors
                    System.out.println(control.getAllDoctors());
                    break;

                case 3: // Update Doctor
                    String id = ui.inputID("Doctor");
                    Doctor doc = control.findDoctorById(id);

                    if (doc != null) {
                        boolean updating = true;
                        
                        while (updating) {
                            ui.displayDoctorProfile(doc);
                            int fieldChoice = ui.showDoctorUpdateFields();
                            String name = null, phone = null, prof = null, gender = null;
                            
                            switch (fieldChoice) {
                                case 1 -> name = ui.inputNewName();
                                case 2 -> phone = ui.inputNewPhone();
                                case 3 -> prof = ui.selectProfession();
                                case 4 -> gender = ui.selectGender();
                                case 0 -> { updating = false; continue; }
                            }    
                            control.updateDoctorDetails(doc, name, phone, prof, gender);
                            ui.displayMessage("Field updated successfully!");
                       }
                    } else {
                        ui.displayMessage("Doctor not found!");
                    }
                    break;
                   

                case 4: // Remove Doctor
                    String removeID = ui.inputID("Doctor");
                    if (control.removeDoctor(removeID)) {
                        ui.displayMessage("Doctor account removed.");
                    } else {
                        ui.displayMessage("Error: Doctor ID not found.");
                    }
                    break;
            }
        } while (dChoice != 0);
    }
    
    private static void handleAppointmentSection(AdminUI ui, AdminControl control) {
        int aChoice;
        do {
            aChoice = ui.showManageAppointmentMenu();
            switch (aChoice) {
                case 1: // View Pending Requests
                    System.out.println(control.getPendingCancellations());
                    break;

                case 2: // Approve/Reject Cancellation
                    String appId = ui.inputID("Appointment"); 
                    int decision = ui.inputApprovalDecision();
                    
                    if (control.handleCancellation(appId, decision)) {
                        ui.displayMessage("Process completed successfully.");
                    } else {
                        ui.displayMessage("Error: Appointment ID not found.");
                    }
                    break;
            }
        } while (aChoice != 0);
    }
    
    private static void handleReportSection(AdminUI ui, AdminControl control) {
        int rChoice;
        do {
            rChoice = ui.showReportMenu();
            switch (rChoice) {
                case 1: // Daily Visit
                    System.out.println(control.generateDailyVisitReport());
                    break;
                case 2: // Monthly Summary
                    System.out.println(control.generateMonthlySummary());
                    break;
                case 3: // Doctor Performance
                    System.out.println(control.generateDoctorPerformance());
                    break;
            }
        } while (rChoice != 0);
    }
    
    private static void handlePharmacistSection(AdminUI ui, AdminControl control) {
        int pchoice;
        do {
            pchoice = ui.showManagePharmacistMenu();
            switch (pchoice) {
                case 1:
                    Pharmacist newP = ui.inputPharmacistDetails(control);
                    if (control.addPharmacist(newP)) 
                        ui.displayMessage("Pharmacist added!");
                    else ui.displayMessage("Error: Pharmacist ID already exists!");
                    break;
                case 2:
                    System.out.println(control.getAllPharmacists());
                    break;
                case 3:
                    String id = ui.inputID("Pharmacist");
                    Pharmacist p = control.findPharmacistById(id);
                    if (p != null) {
                        boolean updating = true;
                        while (updating) {
                            ui.displayPharmacistProfile(p);
                            System.out.println("\n [1] Update Name");
                            System.out.println(" [0] Back");
                            System.out.print("Choice: ");
                            int fieldChoice = ui.getValidInt();

                            if (fieldChoice == 1) {
                                String newName = ui.inputNewPharmacistName();
                                control.updatePharmacistDetails(p, newName);
                                ui.displayMessage("Name updated!");
                            } else {
                                updating = false;
                            }
                        }
                    } else ui.displayMessage("Pharmacist Not found!");
                    break;
                case 4:
                    String removeId = ui.inputID("Pharmacist");
                    if (control.removePharmacist(removeId)){
                        ui.displayMessage("Pharmacist Removed!");
                    }
                    else {
                        ui.displayMessage("Pharmacist Not found!");
                    }
                    break;
            }
        } while (pchoice != 0);
    }
}
