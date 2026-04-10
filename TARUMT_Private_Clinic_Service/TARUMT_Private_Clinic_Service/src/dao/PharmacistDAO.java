package dao;

import adt.ArrayList;
import entity.Pharmacist;
import java.io.*;

/**
 * PharmacistDAO.java
 * Data Access Object for Pharmacist.
 * Handles permanent storage to/from "data/pharmacists.dat" using Java serialization.
 * 
 * @author ivan
 */
public class PharmacistDAO {

    private static final String FILE_PATH = "data/pharmacists.dat";

    /**
     * Save the entire pharmacist list to the .dat file.
     */
    public static boolean saveAll(ArrayList<Pharmacist> list) {
        ensureDataFolder();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(list);
            return true;
        } catch (IOException e) {
            System.out.println("  [!] Error saving pharmacist data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load the pharmacist list from the .dat file.
     * Seeds default pharmacists if the file does not exist yet.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Pharmacist> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return null;   // signals caller to seed defaults
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            return (ArrayList<Pharmacist>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [!] Error loading pharmacist data: " + e.getMessage());
            return null;
        }
    }

    /**
     * Export pharmacist list to a human-readable .txt file.
     */
    public static String exportToTxt(ArrayList<Pharmacist> list) {
        ensureDataFolder();
        String path = "data/pharmacists_export.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("========================================");
            pw.println("  TARUMT PRIVATE CLINIC");
            pw.println("  PHARMACIST LIST EXPORT");
            pw.println("========================================");
            pw.printf("  %-12s %-30s%n", "ID", "Name");
            pw.println("----------------------------------------");

            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                Pharmacist p = list.getEntry(i);
                pw.printf("  %-12s %-30s%n", p.getPharmacistId(), p.getName());
            }

            pw.println("----------------------------------------");
            pw.printf("  Total: %d pharmacist(s)%n", list.getNumberOfEntries());
            pw.println("========================================");
            return path;
        } catch (IOException e) {
            System.out.println("  [!] Error exporting pharmacist data: " + e.getMessage());
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