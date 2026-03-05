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

public class MainMenuUI {
    
    Scanner input = new Scanner(System.in);
    private int selection;
    
    public int getMainChoice(){
        
        System.out.println("Welcome to TARUMT Private Clinic Service!");
        System.out.println("-------------------------------------------");
        System.out.println("-------------SELECT YOUR ROLE--------------");
        System.out.println("-------------------------------------------");
        System.out.println("1. Patient");
        System.out.println("2. Doctor");
        System.out.println("3. Admin");
        System.out.println("0. Exit");
        System.out.println("Enter your choice: ");
        selection = input.nextInt();
        return selection;
    }
}
