/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Appointment;
import java.io.*;
import java.util.Scanner;

/**
 *
 * @author lee seng wai
 */
public class AppointmentDAO {

    public final String FILE_NAME = "data/appointment.txt";

    public void saveAppointments(ListInterface<Appointment> appointmentList) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                Appointment appointment = appointmentList.getEntry(i);
                out.println(appointment.getAppointmentID() + "|"
                        + appointment.getPatientID() + "|"
                        + appointment.getPatientName() + "|"
                        + appointment.getDoctorID() + "|"
                        + appointment.getDoctorName() + "|"
                        + appointment.getDoctorSpecialty() + "|"
                        + appointment.getSymptom() + "|"
                        + appointment.getStatus() + "|"
                        + appointment.getDate() + "|"
                        + appointment.getTime() + "|"
                        + appointment.getRemarks()
                );
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    public ListInterface<Appointment> getAllAppointments() {
        ListInterface<Appointment> appointmentList = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return appointmentList;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                // The -1 limit ensures empty trailing fields (like feedback) are kept
                String[] parts = line.split("\\|", -1);

                // CHANGED: >= 11 to allow for the trailing pipe in your text file
                if (parts.length >= 11) {
                    appointmentList.add(new Appointment(
                            parts[0].trim(), // ID
                            parts[1].trim(), // Patient ID
                            parts[2].trim(), // Name
                            parts[3].trim(), // Doctor ID
                            parts[4].trim(), // Doctor Name
                            parts[5].trim(), // Specialty
                            parts[6].trim(), // Symptom
                            parts[7].trim(), // Status
                            parts[8].trim(), // Date
                            parts[9].trim(), // Time
                            parts[10].trim() // Remarks (the 11th part)
                    ));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
        return appointmentList;
    }
}
