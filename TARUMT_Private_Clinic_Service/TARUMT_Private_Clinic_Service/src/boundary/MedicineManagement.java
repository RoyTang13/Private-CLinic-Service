package boundary;

import adt.ArrayList;
import adt.ListInterface;
import control.MedicineController;
import control.MedicineController.DispenseResult;
import dao.AppointmentDAO;
import entity.Appointment;
import entity.Medicine;
import entity.Pharmacist;
import entity.Prescription;
import java.util.Scanner;

/**
 * MedicineManagement.java  (Boundary Layer)
 * TARUMT Private Clinic Services - Medicine Module
 *
 * Responsibility: User interface ONLY.
 *   - Displays menus, prompts, and results
 *   - Reads and validates raw user input
 *   - Delegates ALL business logic to MedicineController
 * 
 * @author Ivan
 *
 * This class does NOT touch ADT collections, DAOs, or entity fields directly.
 */
public class MedicineManagement {

    private static final MedicineController ctrl    = new MedicineController();
    private static final Scanner            scanner = new Scanner(System.in);
    
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    // -----------------------------------------------------------------------
    //  ENTRY POINT
    // -----------------------------------------------------------------------
    public void start() {
        ctrl.initialise();
        
        boolean shouldContinue = loginMenu();
        if (shouldContinue) {
            mainMenu();
        }
    }

    // -----------------------------------------------------------------------
    //  SECTION 1 — USER MANAGEMENT (Login / Register)
    // -----------------------------------------------------------------------

    private boolean loginMenu() {
        boolean loggedIn = false;
        
        while (true) {
            printHeader("TARUMT PRIVATE CLINIC - PHARMACY MODULE");
            System.out.println(" [1] Login as Pharmacist");
            System.out.println(" [2] Register New Pharmacist");
            System.out.println(" [0] Exit");
            printLine();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> {
                    if (showLogin()) return true;
                }
                case 2 -> showRegisterPharmacist();
                case 0 -> { return false; }
                default -> System.out.println(" [!] Invalid option. Please try again.");
            }
        }
    }

    private static boolean showLogin() {
        System.out.print("\nEnter Pharmacist ID: ");
        String id = scanner.nextLine().trim().toUpperCase();

        Pharmacist p = ctrl.login(id);
        if (p != null) {
            System.out.println("\n [OK] Welcome, " + p.getName() + "!\n");
            pause();
            return true;
        }
        System.out.println(" [!] Pharmacist ID not found. Please try again or register.");
        return false;
    }

    private static void showRegisterPharmacist() {
        printHeader("REGISTER NEW PHARMACIST");

        String id;
        while (true) {
            id = readPharmacistId("Enter Pharmacist ID (e.g. PH004): ");
            if (!ctrl.isPharmacistIdTaken(id)) break;
            System.out.println(" [!] That ID already exists. Please use a different ID.");
        }

        String name = readRequiredText("  Enter Full Name          : ");
        ctrl.registerPharmacist(id, name);
        System.out.println(" [OK] Pharmacist registered: " + name + " (" + id + ")");
        pause();
    }

    // -----------------------------------------------------------------------
    //  MAIN MENU
    // -----------------------------------------------------------------------

    private static void mainMenu() {
        boolean running = true;
        while (running) {
            Pharmacist current = ctrl.getCurrentPharmacist();
            printHeader("MAIN MENU  [ " + current.getName() + " | " + current.getPharmacistId() + " ]");
            System.out.println(" [1] Medicine Management");
            System.out.println(" [2] Dispense Medicine");
            System.out.println(" [3] Reports & Display");
            System.out.println(" [4] Export Data");
            System.out.println(" [0] Logout");
            printLine();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> medicineManagementMenu();
                case 2 -> showDispenseMenu();
                case 3 -> reportMenu();
                case 4 -> exportMenu();
                case 0 -> {
                    ctrl.logout();
                    running = false;
                    System.out.println("\nLogged out. Goodbye!\n");
                }
                default -> System.out.println(" [!] Invalid option.");
            }
        }
    }

    // -----------------------------------------------------------------------
    //  SECTION 2 — MEDICINE DATA MANAGEMENT
    // -----------------------------------------------------------------------

    private static void medicineManagementMenu() {
        boolean back = false;
        while (!back) {
            printHeader("MEDICINE MANAGEMENT");
            System.out.println(" [1] View All Medicines");
            System.out.println(" [2] Add New Medicine");
            System.out.println(" [3] Update Medicine");
            System.out.println(" [4] Remove Medicine");
            System.out.println(" [5] Search Medicine");
            System.out.println(" [0] Back to Main Menu");
            printLine();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> showAllMedicines();
                case 2 -> showAddMedicine();
                case 3 -> showUpdateMedicine();
                case 4 -> showRemoveMedicine();
                case 5 -> showSearchMedicine();
                case 0 -> back = true;
                default -> System.out.println(" [!] Invalid option.");
            }
        }
    }

    /** 2.1 View All Medicines */
    private static void showAllMedicines() {
        printHeader("MEDICINE LIST");
        ArrayList<Medicine> list = ctrl.getMedicineList();
        if (list.isEmpty()) {
            System.out.println("No medicines in inventory.");
        } else {
            printMedicineTableHeader();
            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                System.out.printf("%-4d %s%n", i, list.getEntry(i));
            }
            System.out.println("\nTotal: " + list.getNumberOfEntries() + " medicine(s)");
        }
        pause();
    }

    /** 2.2 Add New Medicine */
    private static void showAddMedicine() {
        printHeader("ADD NEW MEDICINE");

        String id;
        while (true) {
            id = readMedicineId("Medicine ID   (e.g. MED010): ");
            if (ctrl.findMedicineById(id) == null) break;
            System.out.println(" [!] Medicine ID already exists. Please use a different ID.");
        }

        String name     = readRequiredText("  Medicine Name            : ");
        System.out.println("Categories: Antibiotic | Analgesic | Antacid | Vitamin | Antiviral | Other");
        String category = readRequiredText("Category                 : ");
        String details  = readRequiredText("Details/Description      : ");

        int qty;
        while (true) {
            qty = readInt("Initial Quantity         : ");
            if (qty >= 0) break;
            System.out.println(" [!] Quantity cannot be negative.");
        }

        double price;
        while (true) {
            price = readDouble("Price per unit (RM)      : ");
            if (price > 0) break;
            System.out.println(" [!] Price must be greater than 0.");
        }

        boolean success = ctrl.addMedicine(id, name, category, details, qty, price);
        System.out.println(success
                ? "\n [OK] Medicine added: " + name + " (" + id + ")"
                : " [!] Failed to add medicine. ID may already exist.");
        pause();
    }

    /** 2.3 Update Medicine */
    private static void showUpdateMedicine() {
        printHeader("UPDATE MEDICINE");
        ArrayList<Medicine> list = ctrl.getMedicineList();

        if (list.isEmpty()) {
            System.out.println("No medicines in inventory.");
            pause();
            return;
        }

        // Show full list so pharmacist can see IDs before choosing
        printMedicineTableHeader();
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.printf("%-4d %s%n", i, list.getEntry(i));
        }
        System.out.println();

        String id = readMedicineId("Enter Medicine ID to update: ");
        Medicine m = ctrl.findMedicineById(id);

        if (m == null) {
            System.out.println(" [!] Medicine not found.");
            pause();
            return;
        }

        System.out.println("\nSelected: " + m);
        System.out.println();
        System.out.println("What would you like to update?");
        System.out.println(" [1] Name");
        System.out.println(" [2] Category");
        System.out.println(" [3] Details");
        System.out.println(" [4] Quantity");
        System.out.println(" [5] Price");
        System.out.println(" [0] Cancel");
        printLine();
        int choice = readInt("Choice: ");

        boolean success;
        switch (choice) {
            case 1 -> {
                success = ctrl.updateMedicineName(id, readRequiredText("  New Name: "));
                System.out.println(success ? " [OK] Name updated." : "  [!] Update failed.");
            }
            case 2 -> {
                success = ctrl.updateMedicineCategory(id, readRequiredText("  New Category: "));
                System.out.println(success ? " [OK] Category updated." : "  [!] Update failed.");
            }
            case 3 -> {
                success = ctrl.updateMedicineDetails(id, readRequiredText("  New Details: "));
                System.out.println(success ? " [OK] Details updated." : "  [!] Update failed.");
            }
            case 4 -> {
                int newQty;
                while (true) {
                    newQty = readInt("New Quantity: ");
                    if (newQty >= 0) break;
                    System.out.println(" [!] Quantity cannot be negative.");
                }
                success = ctrl.updateMedicineQuantity(id, newQty);
                System.out.println(success ? " [OK] Quantity updated." : "  [!] Update failed.");
            }
            case 5 -> {
                double newPrice;
                while (true) {
                    newPrice = readDouble("New Price (RM): ");
                    if (newPrice > 0) break;
                    System.out.println(" [!] Price must be greater than 0.");
                }
                success = ctrl.updateMedicinePrice(id, newPrice);
                System.out.println(success ? " [OK] Price updated." : "  [!] Update failed.");
            }
            case 0 -> System.out.println("Update cancelled.");
            default -> System.out.println(" [!] Invalid option.");
        }
        pause();
    }

    /** 2.4 Remove Medicine */
    private static void showRemoveMedicine() {
        printHeader("REMOVE MEDICINE");
        ArrayList<Medicine> list = ctrl.getMedicineList();

        if (list.isEmpty()) {
            System.out.println("No medicines in inventory.");
            pause();
            return;
        }

        printMedicineTableHeader();
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.printf("%-4d %s%n", i, list.getEntry(i));
        }
        System.out.println();

        String id = readMedicineId("Enter Medicine ID to remove: ");
        Medicine m = ctrl.findMedicineById(id);

        if (m == null) {
            System.out.println(" [!] Medicine not found.");
            pause();
            return;
        }

        System.out.println("Found: " + m.getName() + " (" + m.getMedicineId() + ")");
        System.out.print("Confirm remove? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("Y")) {
            boolean success = ctrl.removeMedicine(id);
            System.out.println(success
                    ? " [OK] Medicine removed successfully."
                    : " [!] Removal failed.");
        } else {
            System.out.println("Removal cancelled.");
        }
        pause();
    }

    /** 2.5 Search Medicine */
    private static void showSearchMedicine() {
        printHeader("SEARCH MEDICINE");
        System.out.println(" [1] Search by ID");
        System.out.println(" [2] Search by Name (keyword)");
        System.out.println(" [3] Search by Category");
        printLine();
        int choice = readInt("Choice: ");

        switch (choice) {
            case 1 -> {
                System.out.print("Enter Medicine ID: ");
                String id = scanner.nextLine().trim().toUpperCase();
                Medicine m = ctrl.findMedicineById(id);
                if (m != null) {
                    printMedicineTableHeader();
                    System.out.printf("%-4s %s%n", "--", m);
                } else {
                    System.out.println(" [!] No medicine found with ID: " + id);
                }
            }
            case 2 -> {
                String keyword = readRequiredText("Enter keyword: ");
                ArrayList<Medicine> results = ctrl.searchByName(keyword);
                if (results.isEmpty()) {
                    System.out.println(" [!] No medicine found with keyword: " + keyword);
                } else {
                    printMedicineTableHeader();
                    for (int i = 1; i <= results.getNumberOfEntries(); i++) {
                        System.out.printf("%-4d %s%n", i, results.getEntry(i));
                    }
                }
            }
            case 3 -> {
                String cat = readRequiredText("Enter category: ");
                ArrayList<Medicine> results = ctrl.searchByCategory(cat);
                if (results.isEmpty()) {
                    System.out.println(" [!] No medicine found in category: " + cat);
                } else {
                    printMedicineTableHeader();
                    for (int i = 1; i <= results.getNumberOfEntries(); i++) {
                        System.out.printf("%-4d %s%n", i, results.getEntry(i));
                    }
                }
            }
            default -> System.out.println(" [!] Invalid option.");
        }
        pause();
    }

    // -----------------------------------------------------------------------
    //  SECTION 3 — DISPENSE MEDICINE (Transaction)
    // -----------------------------------------------------------------------

    private static void showDispenseMenu() {
        printHeader("DISPENSE MEDICINE (PHARMACY QUEUE)");

        // 1. Fetch and Display Queue
        ArrayList<Appointment> queue = ctrl.getPharmacyQueue();
        if (queue.isEmpty()) {
            System.out.println(" [!] No patients waiting for medicine.");
            pause();
            return;
        }

        System.out.printf("%-10s | %-15s | %-20s\n", "Appt ID", "Patient Name", "Doctor Remarks");
        printLine();
        for (int i = 1; i <= queue.getNumberOfEntries(); i++) {
            Appointment a = queue.getEntry(i);
            System.out.printf("%-10s | %-15s | %-20s\n",
                    a.getAppointmentID(), a.getPatientName(), a.getRemarks());
        }
        System.out.println();

        // 2. Select Appointment
        String apptId = readRequiredText("Enter Appointment ID to process: ");
        Appointment target = null;
        for (int i = 1; i <= queue.getNumberOfEntries(); i++) {
            if (queue.getEntry(i).getAppointmentID().equalsIgnoreCase(apptId)) {
                target = queue.getEntry(i);
                break;
            }
        }

        if (target == null) {
            System.out.println(" [!] Appointment ID not found in queue.");
            pause();
            return;
        }

        // 3. Select Medicine (Showing the list as you did before)
        ArrayList<Medicine> medList = ctrl.getMedicineList();
        printMedicineTableHeader();
        for (int i = 1; i <= medList.getNumberOfEntries(); i++) {
            System.out.printf("%-4d %s%n", i, medList.getEntry(i));
        }

        String medId = readMedicineId("\nEnter Medicine ID to dispense: ");
        Medicine m = ctrl.findMedicineById(medId);

        if (m == null || m.getQuantity() == 0) {
            System.out.println(" [!] Medicine unavailable or out of stock.");
            pause();
            return;
        }

        int qty = readInt("Quantity to Dispense: ");
        if (qty <= 0 || qty > m.getQuantity()) {
            System.out.println(" [!] Invalid quantity (Insufficient stock or invalid input).");
            pause();
            return;
        }

        // 4. YOUR SUMMARY & CONFIRMATION (Maintained as requested)
        System.out.println();
        System.out.println("------------------ Dispense Summary ------------------");
        System.out.printf(" Appointment : %s%n", target.getAppointmentID());
        System.out.printf(" Patient     : %s (%s)%n", target.getPatientName(), target.getPatientID());
        System.out.printf(" Medicine    : %s (%s)%n", m.getName(), m.getMedicineId());
        System.out.printf(" Quantity    : %d%n", qty);
        System.out.printf(" Unit Price  : RM%.2f%n", m.getPrice());
        System.out.printf(" Total Cost  : RM%.2f%n", qty * m.getPrice());
        System.out.println("------------------------------------------------------");
        System.out.print("Confirm dispense? (Y/N): ");
        String confirm = scanner.nextLine().trim();

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Dispense cancelled.");
            pause();
            return;
        }

        // 5. Finalize Action
        DispenseResult result = ctrl.dispenseMedicine(target.getPatientID(), target.getPatientName(), medId, qty);

        if (result.success) {
            // Update Appointment status to 'Completed' and save
            ctrl.finalizeCollection(apptId);

            System.out.println("\n" + result.message + " Prescription ID: " + result.prescriptionId);
            System.out.printf("Remaining stock for %s: %d%n", m.getName(), result.remainingStock);

            if (result.lowStockWarning) {
                System.out.printf(" [!] WARNING: %s stock is low (%d remaining).%n",
                        m.getName(), result.remainingStock);
            }
        } else {
            System.out.println(" [!] " + result.message);
        }
        pause();
    }
    

    // -----------------------------------------------------------------------
    //  SECTION 4 — REPORTS & DISPLAY
    // -----------------------------------------------------------------------

    private static void reportMenu() {
        boolean back = false;
        while (!back) {
            printHeader("REPORTS & DISPLAY");
            System.out.println(" [1] Medicine Stock Summary");
            System.out.println(" [2] Low Stock Alert (< 20 units)");
            System.out.println(" [3] Prescription History");
            System.out.println(" [4] Most Used Medicine Report");
            System.out.println(" [0] Back to Main Menu");
            printLine();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> showStockSummary();
                case 2 -> showLowStockAlert();
                case 3 -> showPrescriptionHistory();
                case 4 -> showMostUsedReport();
                case 0 -> back = true;
                default -> System.out.println(" [!] Invalid option.");
            }
        }
    }

    /** 4.1 Medicine Stock Summary */
    private static void showStockSummary() {
        printHeader("MEDICINE STOCK SUMMARY");
        ArrayList<Medicine> list = ctrl.getMedicineList();

        if (list.isEmpty()) {
            System.out.println("No medicines in inventory.");
            pause();
            return;
        }

        printMedicineTableHeader();
        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.printf("%-4d %s%n", i, list.getEntry(i));
        }
        System.out.println();
        System.out.printf("Total Medicine Types  : %d%n",   ctrl.getTotalMedicineTypes());
        System.out.printf("Total Units in Stock  : %d%n",   ctrl.getTotalUnitsInStock());
        System.out.printf("Total Inventory Value : RM%.2f%n", ctrl.getTotalInventoryValue());
        pause();
    }

    /** 4.2 Low Stock Alert */
    private static void showLowStockAlert() {
        printHeader("LOW STOCK ALERT  (< 20 units)");
        ArrayList<Medicine> lowStock = ctrl.getLowStockMedicines(20);

        System.out.printf("%-10s %-25s %-15s %-10s%n", "ID", "Name", "Category", "Qty");
        printLine();

        if (lowStock.isEmpty()) {
            System.out.println(" [OK] All medicines have sufficient stock (>= 20 units).");
        } else {
            for (int i = 1; i <= lowStock.getNumberOfEntries(); i++) {
                Medicine m = lowStock.getEntry(i);
                System.out.printf("%-10s %-25s %-15s %-10d  [!] REORDER NEEDED%n",
                        m.getMedicineId(), m.getName(), m.getCategory(), m.getQuantity());
            }
        }
        pause();
    }

    /** 4.3 Prescription History */
    private static void showPrescriptionHistory() {
        printHeader("PRESCRIPTION HISTORY");
        ArrayList<Prescription> list = ctrl.getPrescriptionList();

        if (list.isEmpty()) {
            System.out.println("No prescriptions recorded yet.");
            pause();
            return;
        }

        System.out.printf("%-12s %-10s %-18s %-10s %-22s %-6s %-10s %-12s %s%n",
                "Presc. ID", "PatientID", "Patient Name", "Med ID",
                "Medicine Name", "Qty", "Total(RM)", "Pharmacist", "Date & Time");
        printLine();

        for (int i = 1; i <= list.getNumberOfEntries(); i++) {
            System.out.println(" " + list.getEntry(i));
        }
        System.out.println("\nTotal Prescriptions: " + list.getNumberOfEntries());
        pause();
    }

    /** 4.4 Most Used Medicine Report */
    private static void showMostUsedReport() {
        printHeader("MOST USED MEDICINE REPORT");

        if (ctrl.getPrescriptionList().isEmpty()) {
            System.out.println("No prescription data available.");
            pause();
            return;
        }

        ArrayList[] result  = ctrl.getMostUsedMedicines();
        ArrayList<String>  ids    = result[0];
        ArrayList<Integer> counts = result[1];

        System.out.printf("%-6s %-12s %-25s %-15s %-10s%n",
                "Rank", "Medicine ID", "Medicine Name", "Category", "Total Dispensed");
        printLine();

        for (int i = 1; i <= ids.getNumberOfEntries(); i++) {
            Medicine m   = ctrl.findMedicineById(ids.getEntry(i));
            String name  = (m != null) ? m.getName()     : "(removed)";
            String cat   = (m != null) ? m.getCategory() : "-";
            System.out.printf("%-6d %-12s %-25s %-15s %-10d%n",
                    i, ids.getEntry(i), name, cat, counts.getEntry(i));
        }
        pause();
    }

    // -----------------------------------------------------------------------
    //  EXPORT MENU
    // -----------------------------------------------------------------------

    private static void exportMenu() {
        boolean back = false;
        while (!back) {
            printHeader("EXPORT DATA TO TXT FILE");
            System.out.println(" [1] Export Medicine List");
            System.out.println(" [2] Export Prescription History");
            System.out.println(" [3] Export Pharmacist List");
            System.out.println(" [4] Export All");
            System.out.println(" [0] Back to Main Menu");
            printLine();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> printExportResult(ctrl.exportMedicinesToTxt());
                case 2 -> printExportResult(ctrl.exportPrescriptionsToTxt());
                case 3 -> printExportResult(ctrl.exportPharmacistsToTxt());
                case 4 -> {
                    String p1 = ctrl.exportMedicinesToTxt();
                    String p2 = ctrl.exportPrescriptionsToTxt();
                    String p3 = ctrl.exportPharmacistsToTxt();
                    if (p1 != null) System.out.println(" [OK] Exported: " + p1);
                    if (p2 != null) System.out.println(" [OK] Exported: " + p2);
                    if (p3 != null) System.out.println(" [OK] Exported: " + p3);
                    pause();
                }
                case 0 -> back = true;
                default -> System.out.println(" [!] Invalid option.");
            }
        }
    }

    private static void printExportResult(String path) {
        if (path != null) {
            System.out.println(" [OK] Export successful! File saved at: " + path);
        } else {
            System.out.println(" [!] Export failed. Please check write permissions.");
        }
        pause();
    }

    // -----------------------------------------------------------------------
    //  INPUT HELPERS  (boundary-only: read + validate raw user input)
    // -----------------------------------------------------------------------

    private static String readMedicineId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = scanner.nextLine().trim().toUpperCase();
            if (MedicineController.isValidMedicineId(id)) return id;
            System.out.println(" [!] Invalid format. Must be MED + 3 digits (e.g. MED001).");
        }
    }

    private static String readPharmacistId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = scanner.nextLine().trim().toUpperCase();
            if (MedicineController.isValidPharmacistId(id)) return id;
            System.out.println(" [!] Invalid format. Must be PH + 3 digits (e.g. PH001).");
        }
    }

    private static String readPatientId(String prompt) {
        while (true) {
            System.out.print(prompt);
            String id = scanner.nextLine().trim().toUpperCase();
            if (MedicineController.isValidPatientId(id)) return id;
            System.out.println(" [!] Invalid format. Must be P + 3 digits (e.g. P001).");
        }
    }

    private static String readRequiredText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) return val;
            System.out.println(" [!] This field cannot be empty.");
        }
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(" [!] Please enter a valid integer: ");
            }
        }
    }

    private static double readDouble(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.print(" [!] Please enter a valid number: ");
            }
        }
    }

    // -----------------------------------------------------------------------
    //  DISPLAY HELPERS  (boundary-only: print formatting)
    // -----------------------------------------------------------------------

    private static void printHeader(String title) {
        System.out.println("\n========================================================================");
        System.out.printf("                 %-50s\n", title);
        System.out.println("========================================================================");
    }

    private static void printLine() {
        System.out.println("------------------------------------------------------------------------");
    }

    private static void printMedicineTableHeader() {
        System.out.printf("%-4s %-10s %-25s %-15s %-8s %-10s %s%n",
                "No.", "ID", "Name", "Category", "Qty", "Price(RM)", "Details");
        printLine();
    }

    private static void pause() {
        System.out.print("\nPress ENTER to continue...");
        scanner.nextLine();
    }

}