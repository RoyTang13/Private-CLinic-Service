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

public class DoctorUI {
    Scanner input = new Scanner(System.in);
    
    private int selection;
    private String doctorID;
   
    public String enterDoctorID(){
        System.out.println("Enter Doctor ID (Format DXXX): ");
        doctorID = input.nextLine();
        
        return doctorID;
    }
    
    public int showDoctorMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("----------------Doctor Menu----------------");
        System.out.println("-------------------------------------------");
        //show current doctor, need change after
        //System.out.println("Your Doctor ID: ");
        //System.out.println("Your Name: ");
        
        System.out.println("1. View My Appointment");
        System.out.println("2. Update Appointment Status");
        System.out.println("0. Exit");
        System.out.println("Enter your choice: ");
        selection = input.nextInt();
        input.nextLine(); //clear buffer
        return selection;
    }
    
    
}
