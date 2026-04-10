package entity;

import java.io.Serializable;

/**
 * Medicine.java
 * Entity class representing a medicine in the TARUMT Private Clinic inventory.
 * 
 * @author ivan
 */

public class Medicine implements Serializable {

    private String medicineId;
    private String name;
    private String category;
    private String details;
    private int quantity;
    private double price;

    public Medicine(String medicineId, String name, String category, String details, int quantity, double price) {
        this.medicineId = medicineId;
        this.name = name;
        this.category = category;
        this.details = details;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters
    public String getMedicineId()  { return medicineId; }
    public String getName()        { return name; }
    public String getCategory()    { return category; }
    public String getDetails()     { return details; }
    public int getQuantity()       { return quantity; }
    public double getPrice()       { return price; }

    // Setters
    public void setMedicineId(String medicineId) { this.medicineId = medicineId; }
    public void setName(String name)             { this.name = name; }
    public void setCategory(String category)     { this.category = category; }
    public void setDetails(String details)       { this.details = details; }
    public void setQuantity(int quantity)        { this.quantity = quantity; }
    public void setPrice(double price)           { this.price = price; }

    @Override
    public String toString() {
        return String.format("%-10s %-25s %-15s %-8d RM%-8.2f %s",
                medicineId, name, category, quantity, price, details);
    }
}