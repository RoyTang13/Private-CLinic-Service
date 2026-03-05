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

public class AdminUI {
    
    Scanner input = new Scanner(System.in);
    
    private int selection;
    private String adminID;
    
    public String enterAdminID(){
        System.out.println("Enter Admin ID (Format AXXX): ");
        adminID = input.nextLine();
        
        return adminID;
    }
    
    public int showAdminMenu(){
        System.out.println("-------------------------------------------");
        System.out.println("----------------Admin Menu-----------------");
        System.out.println("-------------------------------------------");
        //show current admin, need change after
        //System.out.println("Your Admin ID: ");
        //System.out.println("Your Name: ");
        
        System.out.println("1. Manage Patient Account");
        System.out.println("2. Manage Doctor Account");
        System.out.println("3. Manage Appointment");
        System.out.println("4. View Report & Summary");
        System.out.println("0. Exit");
        System.out.println("Enter your choice: ");
        selection = input.nextInt();
        input.nextLine(); //clear buffer
        return selection;
    }
}
