package entity;

import java.io.Serializable;

/**
 * Pharmacist.java
 * Entity class representing a pharmacist user in the system.
 * 
 * @author ivan
 */
public class Pharmacist implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String pharmacistId;
    private String name;

    public Pharmacist(String pharmacistId, String name) {
        this.pharmacistId = pharmacistId;
        this.name = name;
    }

    public String getPharmacistId() { return pharmacistId; }
    public String getName()         { return name; }

    public void setPharmacistId(String pharmacistId) { this.pharmacistId = pharmacistId; }
    public void setName(String name)                 { this.name = name; }

    @Override
    public String toString() {
        return String.format("ID: %-10s  Name: %s", pharmacistId, name);
    }
}