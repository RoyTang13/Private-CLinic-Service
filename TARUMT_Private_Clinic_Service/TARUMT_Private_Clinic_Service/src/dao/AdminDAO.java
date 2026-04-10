/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Lim SiYu
 */

import entity.Admin;
import adt.*;
import java.io.*;
import java.util.Scanner;

public class AdminDAO {
    
    private final String FILE_NAME = "data/admins.txt";

    public void saveAdmins(ListInterface<Admin> adminList) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= adminList.getNumberOfEntries(); i++) {
                Admin a = adminList.getEntry(i);
                //format: id | name
                out.println(a.getAdminID() + "|" + a.getName());
            }
        } catch (IOException e) {
            System.err.println("Error saving admins: " + e.getMessage());
        }
    }

    public ListInterface<Admin> getAllAdmins() {
        ListInterface<Admin> adminList = new ArrayList<>();
        File file = new File(FILE_NAME);
        
        if (!file.exists()) {
            return adminList; 
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|"); 
                if (parts.length == 2) {
                    adminList.add(new Admin(parts[0], parts[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        return adminList;
    }
}
