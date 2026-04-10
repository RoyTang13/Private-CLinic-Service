/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Patient;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author lee seng wai
 */

public class PatientDAO {

    public final String FILE_NAME = "data/patient.txt";

    public void savePatients(ListInterface<Patient> patientList) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
                Patient patient = patientList.getEntry(i);
                out.println(patient.getPatientID() + "|"
                        + patient.getPatientName() + "|"
                        + patient.getPatientGender() + "|"
                        + patient.getPatientIC() + "|"
                        + patient.getPatientContactNumber()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving patients: " + e.getMessage());
        }
    }

    public ListInterface<Patient> getAllPatients() {
        ListInterface<Patient> patientList = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return patientList;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    patientList.add(new Patient(
                            parts[0], // patientID
                            parts[1], // patientName
                            parts[2], // patientGender
                            parts[3], // patientIC
                            parts[4]  // patientContactNumber
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        
        return patientList;
    }
}
