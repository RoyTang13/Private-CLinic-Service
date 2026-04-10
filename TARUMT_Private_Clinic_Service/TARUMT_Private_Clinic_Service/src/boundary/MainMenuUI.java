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
    private Scanner input = new Scanner(System.in);

    public int getMainChoice() {
        System.out.println("\n\n");
        printBanner();
        System.out.println("    ==========================================    ");
        System.out.println("               S Y S T E M   M E N U             ");
        System.out.println("    ------------------------------------------    ");
        System.out.println();
        
        printOption(1, "Patient Module");
        printOption(2, "Doctor Module");
        printOption(3, "Admin Module");
        printOption(4, "Pharmacy Module");
        
        System.out.println();
        System.out.println("    ------------------------------------------    ");
        printOption(0, "Exit System");
        System.out.println("    ==========================================    ");
        
        System.out.print("\n    Selection > ");
        return getValidInt();
    }

    private void printOption(int num, String title) {
        // Aligns the options neatly
        System.out.printf("          [%d]  %-20s\n", num, title);
    }

    private int getValidInt() {
        while (true) {
            try {
                String raw = input.nextLine().trim();
                return Integer.parseInt(raw);
            } catch (NumberFormatException e) {
                System.out.print("    [!] Please enter a valid number: ");
            }
        }
    }
    
    private void printBanner() {
        System.out.println("    _______ ___   ___  __ __  __  __  _______ ");
        System.out.println("   |__   __|   \\ |   \\|  |  ||  \\/  ||__   __|");
        System.out.println("      | |  | |  || |  ||  |  || \\  / |   | |   ");
        System.out.println("      | |  | |__|  |__|  |__|  ||  | |   | |   ");
        System.out.println("      |_|  |_|  ||_|  ||_____||_|  |_|   |_|   ");
        System.out.println("             PRIVATE CLINIC SERVICES           ");
    }
}
