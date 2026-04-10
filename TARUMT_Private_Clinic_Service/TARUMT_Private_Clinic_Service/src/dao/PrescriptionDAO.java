package dao;

import adt.ArrayList;
import entity.Prescription;
import java.io.*;

/**
 * PrescriptionDAO.java
 * Data Access Object for Prescription.
 * Handles permanent storage to/from "data/prescriptions.dat" using Java serialization.
 * 
 * @author ivan
 */
public class PrescriptionDAO {

    private static final String FILE_PATH        = "data/prescriptions.dat";
    private static final String COUNTER_PATH     = "data/presc_counter.dat";

    /**
     * Save the entire prescription list to the .dat file.
     */
    public static boolean saveAll(ArrayList<Prescription> list) {
        ensureDataFolder();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FILE_PATH))) {
            oos.writeObject(list);
            return true;
        } catch (IOException e) {
            System.out.println("  [!] Error saving prescription data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Load the prescription list from the .dat file.
     * Returns an empty list if the file does not exist yet.
     */
    @SuppressWarnings("unchecked")
    public static ArrayList<Prescription> loadAll() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>(100);
        }
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(FILE_PATH))) {
            return (ArrayList<Prescription>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("  [!] Error loading prescription data: " + e.getMessage());
            return new ArrayList<>(100);
        }
    }

    /**
     * Save the prescription auto-increment counter so IDs never repeat across sessions.
     */
    public static void saveCounter(int counter) {
        ensureDataFolder();
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(COUNTER_PATH))) {
            oos.writeInt(counter);
        } catch (IOException e) {
            System.out.println("  [!] Error saving prescription counter: " + e.getMessage());
        }
    }

    /**
     * Load the prescription counter. Returns 1 if no counter file exists.
     */
    public static int loadCounter() {
        File file = new File(COUNTER_PATH);
        if (!file.exists()) return 1;
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(COUNTER_PATH))) {
            return ois.readInt();
        } catch (IOException e) {
            return 1;
        }
    }

    /**
     * Export prescription history to a human-readable .txt file.
     */
    public static String exportToTxt(ArrayList<Prescription> list) {
        ensureDataFolder();
        String path = "data/prescriptions_export.txt";
        try (PrintWriter pw = new PrintWriter(new FileWriter(path))) {
            pw.println("============================================================");
            pw.println("  TARUMT PRIVATE CLINIC");
            pw.println("  PRESCRIPTION HISTORY EXPORT");
            pw.println("============================================================");
            pw.printf("  %-12s %-10s %-18s %-10s %-22s %-6s %-10s %-12s %s%n",
                    "Presc.ID", "PatientID", "Patient Name", "Med ID",
                    "Medicine Name", "Qty", "Total(RM)", "Pharmacist", "Date & Time");
            pw.println("------------------------------------------------------------");

            for (int i = 1; i <= list.getNumberOfEntries(); i++) {
                pw.println("  " + list.getEntry(i));
            }

            pw.println("------------------------------------------------------------");
            pw.printf("  Total Prescriptions: %d%n", list.getNumberOfEntries());
            pw.println("============================================================");
            return path;
        } catch (IOException e) {
            System.out.println("  [!] Error exporting prescription data: " + e.getMessage());
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