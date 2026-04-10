package dao;

import adt.ArrayList;
import entity.Medicine;
import java.io.*;

/**
 * MedicineDAO.java
 * Data Access Object for Medicine.
 * Handles permanent storage to/from "data/medicines.dat" using Java serialization.
 * 
 * @author ivan
 */
public class MedicineDAO {

    private static final String FILE_PATH = "data/medicines.dat";

    /**
     * Save the entire medicine list to the .dat file.
     * Overwrites any existing file.
     */
    public static boolean saveAll(ArrayList<Medicine> list) {
        ensureDataFolder();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(list);
            return true;
        } catch (IOException e) {
            System.out.println("  [!] Error saving medicine data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load the medicine list from the .dat file.
     * Returns an empty list if the file does not exist yet.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Medicine> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(50);   // fresh start
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            return (ArrayList<Medicine>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [!] Error loading medicine data: " + e.getMessage());
            return new ArrayList<>(50);
        }
    }

    /**
     * Export medicine list to a human-readable .txt file.
     * Returns the path of the exported file, or null on failure.
     */
    public static String exportToTxt(ArrayList<Medicine> list) {
        ensureDataFolder();
        String path = "data/medicines_export.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("========================================");
            pw.println("  TARUMT PRIVATE CLINIC");
            pw.println("  MEDICINE INVENTORY EXPORT");
            pw.println("========================================");
            pw.printf("  %-10s %-25s %-15s %-8s %-10s %s%n",
                    "ID", "Name", "Category", "Qty", "Price(RM)", "Details");
            pw.println("----------------------------------------");

            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                Medicine m = list.getEntry(i);
                pw.printf("  %-10s %-25s %-15s %-8d RM%-8.2f %s%n",
                        m.getMedicineId(), m.getName(), m.getCategory(),
                        m.getQuantity(), m.getPrice(), m.getDetails());
            }

            pw.println("----------------------------------------");
            pw.printf("  Total: %d medicine(s)%n", list.getNumberOfEntries());
            pw.println("========================================");
            return path;
        } catch (IOException e) {
            System.out.println("  [!] Error exporting medicine data: " + e.getMessage());
            return null;
        }
    }

    private static void ensureDataFolder() {
        File folder = new File("data");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
}