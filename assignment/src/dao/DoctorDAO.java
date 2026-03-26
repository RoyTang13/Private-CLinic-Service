package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;
import java.io.*;
import java.util.Scanner;

public class DoctorDAO {

    private final String FILE_NAME = "assignment/src/dao/doctor.txt";

    // Save all doctors to file
    public void saveDoctors(ListInterface<Doctor> doctorList) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
                Doctor d = doctorList.getEntry(i);
                out.println(d.getDoctorID() + "|" 
                          + d.getDoctorName() + "|" 
                          + d.getPhone() + "|"      
                          + d.getGender());
            }
        } catch (IOException e) {
            System.err.println("Error saving doctors: " + e.getMessage());
        }
    }

    // Read all doctors from file
    public ListInterface<Doctor> getAllDoctors() {
        ListInterface<Doctor> doctorList = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return doctorList;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 4) {
                    doctorList.add(new Doctor(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }

        return doctorList;
    }

    //Debug Use only to find the file (can remove it if no needed)
    //change the isValidDoctorID to another debug code
//     public ListInterface<Doctor> getAllDoctors() {
//     ListInterface<Doctor> doctorList = new ArrayList<>();
//     File file = new File(FILE_NAME);

//     System.out.println("Looking for file at: " + file.getAbsolutePath());
//     System.out.println("File exists: " + file.exists());

//     if (!file.exists()) {
//         return doctorList;
//     }

//     try (Scanner fileScanner = new Scanner(file)) {
//         while (fileScanner.hasNextLine()) {
//             String line = fileScanner.nextLine();
//             System.out.println("Read line: " + line);

//             String[] parts = line.split("\\|");

//             if (parts.length == 4) {
//                 doctorList.add(new Doctor(parts[0].trim(), parts[1].trim(), parts[2].trim(), parts[3].trim()));
//                 System.out.println("Loaded Doctor ID: " + parts[0].trim());
//             } else {
//                 System.out.println("Invalid line format: " + line);
//             }
//         }
//     } catch (FileNotFoundException e) {
//         System.err.println("File not found: " + e.getMessage());
//     }

//     return doctorList;
// }
}