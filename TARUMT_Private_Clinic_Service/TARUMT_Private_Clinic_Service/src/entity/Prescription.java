package entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Prescription.java
 * Entity class representing a prescription/dispense transaction record.
 * 
 * @author ivan
 */
public class Prescription implements Serializable {

    private String prescriptionId;
    private String patientId;
    private String patientName;
    private String medicineId;
    private String medicineName;
    private int quantityDispensed;
    private double totalCost;
    private String pharmacistId;
    private String dateTime;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public Prescription(String prescriptionId, String patientId, String patientName,
                        String medicineId, String medicineName,
                        int quantityDispensed, double unitPrice, String pharmacistId) {
        this.prescriptionId    = prescriptionId;
        this.patientId         = patientId;
        this.patientName       = patientName;
        this.medicineId        = medicineId;
        this.medicineName      = medicineName;
        this.quantityDispensed = quantityDispensed;
        this.totalCost         = quantityDispensed * unitPrice;
        this.pharmacistId      = pharmacistId;
        this.dateTime          = LocalDateTime.now().format(FORMATTER);
    }

    // Getters
    public String getPrescriptionId()   { return prescriptionId; }
    public String getPatientId()        { return patientId; }
    public String getPatientName()      { return patientName; }
    public String getMedicineId()       { return medicineId; }
    public String getMedicineName()     { return medicineName; }
    public int getQuantityDispensed()   { return quantityDispensed; }
    public double getTotalCost()        { return totalCost; }
    public String getPharmacistId()     { return pharmacistId; }
    public String getDateTime()         { return dateTime; }

    @Override
    public String toString() {
        return String.format("%-12s %-10s %-18s %-10s %-22s %-6d RM%-8.2f %-12s %s",
                prescriptionId, patientId, patientName, medicineId,
                medicineName, quantityDispensed, totalCost, pharmacistId, dateTime);
    }
}