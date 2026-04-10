package control;

import adt.ArrayList;
import adt.ListInterface;
import dao.MedicineDAO;
import dao.PharmacistDAO;
import dao.PrescriptionDAO;
import dao.AppointmentDAO;
import entity.Medicine;
import entity.Pharmacist;
import entity.Prescription;
import entity.Appointment;

/**
 * MedicineController.java
 * TARUMT Private Clinic Services - Medicine Module
 * 
 * @author Ivan
 *
 * Control layer: handles all business logic, validation, data operations,
 * and DAO calls. The boundary (UI) layer only calls methods from this class
 * and never touches the ADT collections or DAOs directly.
 */
public class MedicineController {

    // -----------------------------------------------------------------------
    //  ADT Collections
    // -----------------------------------------------------------------------
    private ArrayList<Medicine>     medicineList     = new ArrayList<>(50);
    private ArrayList<Pharmacist>   pharmacistList   = new ArrayList<>(20);
    private ArrayList<Prescription> prescriptionList = new ArrayList<>(100);

    private Pharmacist currentPharmacist = null;
    private int        prescriptionCounter = 1;
    
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    // -----------------------------------------------------------------------
    //  STARTUP — load all data from .dat files, seed defaults on first run
    // -----------------------------------------------------------------------

    public void initialise() {
        medicineList = MedicineDAO.loadAll();

        ArrayList<Pharmacist> loaded = PharmacistDAO.loadAll();
        if (loaded == null) {
            seedDefaultPharmacists();
        } else {
            pharmacistList = loaded;
        }

        prescriptionList    = PrescriptionDAO.loadAll();
        prescriptionCounter = PrescriptionDAO.loadCounter();

        if (medicineList.isEmpty()) {
            seedDefaultMedicines();
        }
    }

    // -----------------------------------------------------------------------
    //  SECTION 1 — USER MANAGEMENT
    // -----------------------------------------------------------------------

    /**
     * Attempts to log in with the given pharmacist ID.
     * Returns the Pharmacist if found, null otherwise.
     */
    public Pharmacist login(String pharmacistId) {
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            Pharmacist p = pharmacistList.getEntry(i);
            if (p.getPharmacistId().equals(pharmacistId)) {
                currentPharmacist = p;
                return p;
            }
        }
        return null;
    }

    public void logout() {
        currentPharmacist = null;
    }

    public Pharmacist getCurrentPharmacist() {
        return currentPharmacist;
    }

    /** Returns true if a pharmacist with this ID already exists. */
    public boolean isPharmacistIdTaken(String id) {
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            if (pharmacistList.getEntry(i).getPharmacistId().equals(id)) return true;
        }
        return false;
    }

    /**
     * Registers a new pharmacist.
     * Returns true on success, false if ID already exists.
     */
    public boolean registerPharmacist(String id, String name) {
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            if (pharmacistList.getEntry(i).getPharmacistId().equals(id)) {
                return false;   // duplicate
            }
        }
        pharmacistList.add(new Pharmacist(id, name));
        PharmacistDAO.saveAll(pharmacistList);
        return true;
    }

    // -----------------------------------------------------------------------
    //  SECTION 2 — MEDICINE DATA MANAGEMENT
    // -----------------------------------------------------------------------

    /** Returns the full medicine list (read-only reference for display). */
    public ArrayList<Medicine> getMedicineList() {
        refreshData();
        
        return medicineList;
    }

    /**
     * Adds a new medicine.
     * Returns true on success, false if the ID already exists.
     */
    public boolean addMedicine(String id, String name, String category,
                               String details, int qty, double price) {
        if (findMedicineById(id) != null) return false;
        medicineList.add(new Medicine(id, name, category, details, qty, price));
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Updates the name of a medicine.
     * Returns true on success, false if ID not found.
     */
    public boolean updateMedicineName(String id, String newName) {
        Medicine m = findMedicineById(id);
        if (m == null) return false;
        m.setName(newName);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Updates the category of a medicine.
     */
    public boolean updateMedicineCategory(String id, String newCategory) {
        Medicine m = findMedicineById(id);
        if (m == null) return false;
        m.setCategory(newCategory);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Updates the details of a medicine.
     */
    public boolean updateMedicineDetails(String id, String newDetails) {
        Medicine m = findMedicineById(id);
        if (m == null) return false;
        m.setDetails(newDetails);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Updates the quantity of a medicine.
     * Returns false if ID not found or qty is negative.
     */
    public boolean updateMedicineQuantity(String id, int newQty) {
        if (newQty < 0) return false;
        Medicine m = findMedicineById(id);
        if (m == null) return false;
        m.setQuantity(newQty);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Updates the price of a medicine.
     * Returns false if ID not found or price is not positive.
     */
    public boolean updateMedicinePrice(String id, double newPrice) {
        if (newPrice <= 0) return false;
        Medicine m = findMedicineById(id);
        if (m == null) return false;
        m.setPrice(newPrice);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Removes a medicine by ID.
     * Returns true on success, false if not found.
     */
    public boolean removeMedicine(String id) {
        int index = findMedicineIndex(id);
        if (index == -1) return false;
        medicineList.remove(index);
        MedicineDAO.saveAll(medicineList);
        return true;
    }

    /**
     * Finds a medicine by exact ID match.
     * Returns the Medicine, or null if not found.
     */
    public Medicine findMedicineById(String id) {
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            if (medicineList.getEntry(i).getMedicineId().equals(id)) {
                return medicineList.getEntry(i);
            }
        }
        return null;
    }

    /**
     * Searches medicines whose name contains the given keyword (case-insensitive).
     * Returns an ArrayList of matching medicines.
     */
    public ArrayList<Medicine> searchByName(String keyword) {
        refreshData();
        
        ArrayList<Medicine> results = new ArrayList<>(20);
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine m = medicineList.getEntry(i);
            if (m.getName().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            }
        }
        return results;
    }

    /**
     * Searches medicines whose category contains the given keyword (case-insensitive).
     */
    public ArrayList<Medicine> searchByCategory(String keyword) {
        refreshData();
        
        ArrayList<Medicine> results = new ArrayList<>(20);
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine m = medicineList.getEntry(i);
            if (m.getCategory().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(m);
            }
        }
        return results;
    }

    // -----------------------------------------------------------------------
    //  SECTION 3 — TRANSACTION: DISPENSE MEDICINE
    // -----------------------------------------------------------------------

    /**
     * Result object returned by dispenseMedicine so the boundary can display
     * the outcome without knowing any business logic internals.
     */
    public static class DispenseResult {
        public final boolean success;
        public final String  message;
        public final String  prescriptionId;
        public final int     remainingStock;
        public final boolean lowStockWarning;

        public DispenseResult(boolean success, String message,
                              String prescriptionId, int remainingStock,
                              boolean lowStockWarning) {
            this.success         = success;
            this.message         = message;
            this.prescriptionId  = prescriptionId;
            this.remainingStock  = remainingStock;
            this.lowStockWarning = lowStockWarning;
        }
    }

    /**
     * Dispenses medicine to a patient.
     * Reduces stock, records prescription, persists both changes.
     */
    public DispenseResult dispenseMedicine(String patientId, String patientName,
                                           String medicineId, int qty) {
        Medicine m = findMedicineById(medicineId);
        if (m == null) {
            return new DispenseResult(false, "Medicine not found.", null, 0, false);
        }
        if (m.getQuantity() == 0) {
            return new DispenseResult(false, "Out of stock.", null, 0, false);
        }
        if (qty <= 0) {
            return new DispenseResult(false, "Quantity must be greater than 0.", null, 0, false);
        }
        if (qty > m.getQuantity()) {
            return new DispenseResult(false,
                    "Insufficient stock. Available: " + m.getQuantity(), null, 0, false);
        }

        // Reduce stock
        m.setQuantity(m.getQuantity() - qty);

        // Create prescription record
        String prescId = String.format("RX%04d", prescriptionCounter++);
        Prescription presc = new Prescription(prescId, patientId, patientName,
                m.getMedicineId(), m.getName(), qty, m.getPrice(),
                currentPharmacist.getPharmacistId());
        prescriptionList.add(presc);

        // Persist both
        MedicineDAO.saveAll(medicineList);
        PrescriptionDAO.saveAll(prescriptionList);
        PrescriptionDAO.saveCounter(prescriptionCounter);

        boolean lowStock = m.getQuantity() < 20;
        return new DispenseResult(true, "[OK] Dispense successful!", prescId,
                m.getQuantity(), lowStock);
    }
    
    // 1. Get only patients waiting for medicine
    public ArrayList<Appointment> getPharmacyQueue() {
        refreshData();
        
        ListInterface<Appointment> allAppts = appointmentDAO.getAllAppointments();
        ArrayList<Appointment> queue = new ArrayList<>();
        for (int i = 1; i <= allAppts.getNumberOfEntries(); i++) {
            Appointment a = allAppts.getEntry(i);
            if ("To-Collect".equalsIgnoreCase(a.getStatus())) {
                queue.add(a);
            }
        }
        return queue;
    }

    // 2. Mark as picked up (Finalizes the appointment)
    public boolean finalizeCollection(String apptId) {
        ListInterface<Appointment> allAppts = appointmentDAO.getAllAppointments();
        boolean found = false;

        for (int i = 1; i <= allAppts.getNumberOfEntries(); i++) {
            Appointment a = allAppts.getEntry(i);
            if (a.getAppointmentID().equalsIgnoreCase(apptId)) {
                a.setStatus("Completed");
                found = true;
                break;
            }
        }

        if (found) {
            appointmentDAO.saveAppointments(allAppts);
            return true;
        }
        return false;
    }
    
    

    // -----------------------------------------------------------------------
    //  SECTION 4 — REPORTING
    // -----------------------------------------------------------------------

    /** Returns total number of medicine types. */
    public int getTotalMedicineTypes() {
        refreshData();
        return medicineList.getNumberOfEntries();
    }

    /** Returns total units across all medicines. */
    public int getTotalUnitsInStock() {
        refreshData();
        int total = 0;
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            total += medicineList.getEntry(i).getQuantity();
        }
        return total;
    }

    /** Returns total inventory value (qty * price for all medicines). */
    public double getTotalInventoryValue() {
        refreshData();
        double total = 0;
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            Medicine m = medicineList.getEntry(i);
            total += m.getQuantity() * m.getPrice();
        }
        return total;
    }

    /** Returns list of medicines with stock below the threshold. */
    public ArrayList<Medicine> getLowStockMedicines(int threshold) {
        refreshData();
        ArrayList<Medicine> results = new ArrayList<>(20);
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            if (medicineList.getEntry(i).getQuantity() < threshold) {
                results.add(medicineList.getEntry(i));
            }
        }
        return results;
    }

    /** Returns the full prescription list for display. */
    public ArrayList<Prescription> getPrescriptionList() {
        refreshData();

        return prescriptionList;
    }

    /**
     * Returns medicine IDs and their total dispensed quantities,
     * sorted descending by total dispensed (most used first).
     * Returned as a parallel pair: index 0 = ids list, index 1 = counts list.
     */
    public ArrayList[] getMostUsedMedicines() {
        refreshData();

        ArrayList<String>  ids    = new ArrayList<>(50);
        ArrayList<Integer> counts = new ArrayList<>(50);

        for (int i = 1; i <= prescriptionList.getNumberOfEntries(); i++) {
            Prescription p = prescriptionList.getEntry(i);
            String mid = p.getMedicineId();
            boolean exists = false;

            for (int j = 1; j <= ids.getNumberOfEntries(); j++) {
                if (ids.getEntry(j).equals(mid)) {
                    counts.replace(j, counts.getEntry(j) + p.getQuantityDispensed());
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                ids.add(mid);
                counts.add(p.getQuantityDispensed());
            }
        }

        // Bubble sort descending by count
        for (int i = 1; i <= ids.getNumberOfEntries() - 1; i++) {
            for (int j = 1; j <= ids.getNumberOfEntries() - i; j++) {
                if (counts.getEntry(j) < counts.getEntry(j + 1)) {
                    String tempId  = ids.getEntry(j);
                    int    tempCnt = counts.getEntry(j);
                    ids.replace(j,     ids.getEntry(j + 1));
                    ids.replace(j + 1, tempId);
                    counts.replace(j,     counts.getEntry(j + 1));
                    counts.replace(j + 1, tempCnt);
                }
            }
        }

        return new ArrayList[]{ ids, counts };
    }

    // -----------------------------------------------------------------------
    //  EXPORT
    // -----------------------------------------------------------------------

    public String exportMedicinesToTxt() {
        return MedicineDAO.exportToTxt(medicineList);
    }

    public String exportPrescriptionsToTxt() {
        return PrescriptionDAO.exportToTxt(prescriptionList);
    }

    public String exportPharmacistsToTxt() {
        return PharmacistDAO.exportToTxt(pharmacistList);
    }

    // -----------------------------------------------------------------------
    //  VALIDATION HELPERS  (used by boundary to validate input before calling)
    // -----------------------------------------------------------------------

    public static boolean isValidMedicineId(String id) {
        return id != null && id.matches("MED\\d{3}");
    }

    public static boolean isValidPharmacistId(String id) {
        return id != null && id.matches("PH\\d{3}");
    }

    public static boolean isValidPatientId(String id) {
        return id != null && id.matches("P\\d{3}");
    }

    // -----------------------------------------------------------------------
    //  PRIVATE HELPERS
    // -----------------------------------------------------------------------

    private int findMedicineIndex(String id) {
        for (int i = 1; i <= medicineList.getNumberOfEntries(); i++) {
            if (medicineList.getEntry(i).getMedicineId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    private void seedDefaultPharmacists() {
        pharmacistList.add(new Pharmacist("PH001", "Ahmad Razif"));
        pharmacistList.add(new Pharmacist("PH002", "Nurul Ain"));
        pharmacistList.add(new Pharmacist("PH003", "Tan Wei Liang"));
        PharmacistDAO.saveAll(pharmacistList);
    }

    private void seedDefaultMedicines() {
        medicineList.add(new Medicine("MED001", "Paracetamol 500mg",  "Analgesic",    "Fever and pain relief tablet",        150, 0.50));
        medicineList.add(new Medicine("MED002", "Amoxicillin 250mg",  "Antibiotic",   "Broad-spectrum antibiotic capsule",    80, 1.20));
        medicineList.add(new Medicine("MED003", "Ibuprofen 400mg",    "Analgesic",    "Anti-inflammatory and pain relief",    60, 0.80));
        medicineList.add(new Medicine("MED004", "Omeprazole 20mg",    "Antacid",      "Proton pump inhibitor for gastric",    45, 2.50));
        medicineList.add(new Medicine("MED005", "Cetirizine 10mg",    "Antihistamine","Allergy and hay fever relief",         90, 0.60));
        medicineList.add(new Medicine("MED006", "Vitamin C 500mg",    "Vitamin",      "Ascorbic acid supplement tablet",    200, 0.30));
        medicineList.add(new Medicine("MED007", "Azithromycin 500mg", "Antibiotic",   "Macrolide antibiotic for infections",  15, 3.80));
        medicineList.add(new Medicine("MED008", "Metformin 500mg",    "Antidiabetic", "Blood glucose control tablet",         10, 1.10));
        medicineList.add(new Medicine("MED009", "Oseltamivir 75mg",   "Antiviral",    "Influenza treatment and prevention",  25, 5.20));
        MedicineDAO.saveAll(medicineList);
    }
    
    public void refreshData() {
        // Reload medicine list from file
        this.medicineList = MedicineDAO.loadAll();

        // Reload pharmacist list
        ArrayList<Pharmacist> loadedPhar = PharmacistDAO.loadAll();
        if (loadedPhar != null) {
            this.pharmacistList = loadedPhar;
        }

        // Reload prescriptions
        this.prescriptionList = PrescriptionDAO.loadAll();

        // Important: Refresh the current pharmacist reference 
        // in case their details were updated elsewhere
        if (currentPharmacist != null) {
            this.currentPharmacist = login(currentPharmacist.getPharmacistId());
        }
    }
}