/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boundary;

import java.util.Scanner;

/**
 *
 * @author Lim SiYu, Lee Seng Wai, Tang Le Yi, Ivan Wai Kim Hou
 */

public class PateintUI {
    
    Scanner input = new Scanner(System.in);
    
    private int selection;
    private String patientID;
    
    public int hasPatientID(){
        System.out.println("Do you have patient ID? (1 for yes, 0 for no): ");
        selection = input.nextInt();
        input.nextLine(); //clear buffer
        
        return selection;
    }
    
    public void createPatientAcc(){ //put void first, later will change
        System.out.println("----------Create Patient Account-----------");
        System.out.println("Enter Patient ID (Format PXXX): ");
        String newID = input.nextLine();
        System.out.println("Enter Patient Name: ");
        String patientName = input.nextLine();
        
        System.out.println("Patient account created successfully: ID = " + newID + ", Name = " + patientName);
        
        //return new Patient(newID, patientName);
    }
    
    public String enterPatientID(){
        System.out.println("Enter Patient ID (Format PXXX): ");
        patientID = input.nextLine();
        
        return patientID;
    }
    
    public int showPatientMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("---------------Patient Menu----------------");
        System.out.println("-------------------------------------------");
        //show current patient, need change after
        //System.out.println("Your Patient ID: ");
        //System.out.println("Your Name: ");
        
        System.out.println("1. Book Appointment");
        System.out.println("2. View My Appointment");
        System.out.println("3. Cancel Appointment");
        System.out.println("0. Exit");
        System.out.println("Enter your choice: ");
        selection = input.nextInt();
        input.nextLine(); //clear buffer
        return selection;
    }
}
