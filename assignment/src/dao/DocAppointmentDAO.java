package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.DocAppointment;
import java.io.*;
import java.util.Scanner;

public class DocAppointmentDAO {

    private final String FILE_NAME = "assignment/src/dao/appointment.txt";

    // Save all appointments to file
    public void saveAppointments(ListInterface<DocAppointment> appointmentList) {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                DocAppointment a = appointmentList.getEntry(i);

                out.println(a.getAppointmentID() + "|" 
                        + a.getDoctorID() + "|" 
                        + a.getPatientName() + "|" 
                        + a.getStatus() + "|" 
                        + a.getDate() + "|" 
                        + a.getTime() + "|" 
                        + a.getQueueNo() + "|" 
                        + (a.getDoctorFeedback() == null ? "" : a.getDoctorFeedback()));
            }
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    // Read all appointments from file
    public ListInterface<DocAppointment> getAllAppointments() {
        ListInterface<DocAppointment> appointmentList = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return appointmentList;
        }

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("\\|");

                if (parts.length >= 7) {
                    String feedback = (parts.length == 8) ? parts[7] : "";

                    appointmentList.add(new DocAppointment(
                            parts[0], // ID
                            parts[1], // Doctor ID
                            parts[2], // Patient Name
                            parts[3], // Status
                            parts[4], // Date
                            parts[5], // Time
                            Integer.parseInt(parts[6]), // Queue
                            feedback // Feedback
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading appointments: " + e.getMessage());
        }

        return appointmentList;
    }
}