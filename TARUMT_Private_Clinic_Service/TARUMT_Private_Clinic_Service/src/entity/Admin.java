/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author Lim SiYu
 */
public class Admin {
    private String adminID;
    private String name;

    public Admin(String adminID, String name) {
        this.adminID = adminID;
        this.name = name;
    }

    public String getAdminID() { 
        return adminID; 
    }
    
    public String getName() { 
        return name; 
    }
    
}
