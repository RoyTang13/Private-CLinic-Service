/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package control;

/**
 *
 * @author Lim SiYu
 */

import adt.*;
import entity.*;
import dao.*;

public class AdminControl {
    
    private ListInterface<Doctor> doctorList = new ArrayList<>();
    private ListInterface<Patient> patientList = new ArrayList<>();
    private ListInterface<Appointment> appointmentList = new ArrayList<>();
    private ListInterface<Pharmacist> pharmacistList = new ArrayList<>();
    
    private ListInterface<Admin> adminList = new ArrayList<>();
    private AdminDAO adminDAO = new AdminDAO();
    private DoctorDAO doctorDAO = new DoctorDAO();
    private PharmacistDAO pharmacistDAO = new PharmacistDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private Admin currentAdmin;

    public AdminControl() {
        this.patientList = new PatientDAO().getAllPatients();
        
        doctorList = doctorDAO.getAllDoctors();
        
        //DEFAULT ADMIN ACCOUNT
        this.adminList = adminDAO.getAllAdmins();
        
        if (adminList.isEmpty()) {
            adminList.add(new Admin("A001", "Lim SiYu"));
            adminList.add(new Admin("A002", "Ivan Wai Kim Hou"));
            adminList.add(new Admin("A003", "Lee Seng Wai"));
            adminList.add(new Admin("A004", "Tang Le Yi"));
            
            adminDAO.saveAdmins(adminList);
        }
        
        adt.ArrayList<Pharmacist> loadedPharmacists = PharmacistDAO.loadAll();
        if (loadedPharmacists != null) {
            this.pharmacistList = loadedPharmacists;
        }
        
    }

    
    /*
    Manage patient account
    */
    
    public boolean addPatient(Patient newPatient) {
        if (findPatientById(newPatient.getPatientID()) != null) {
            return false; 
        }
   
        boolean isAdded = patientList.add(newPatient);

        if (isAdded) {
            patientDAO.savePatients(patientList);
        }
        return isAdded;
    }

    public String getAllPatients() {
        refreshData();
        
        if (patientList.isEmpty()) {
            return "No patient records found.";
        }
    
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------------------------------------------------------------------------\n");
        sb.append(String.format("%-5s %-30s %-10s %-20s %-20s\n", "ID", "Name", "Gender", "IC", "Phone"));
        sb.append("------------------------------------------------------------------------------------------\n");
    
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            sb.append(patientList.getEntry(i).toString()).append("\n");
        }
        return sb.toString();
    }

    public boolean removePatient(String id) {
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            Patient p = patientList.getEntry(i);
            
            if (p.getPatientID().equalsIgnoreCase(id)) {
                patientList.remove(i); 
                patientDAO.savePatients(patientList);
                return true;
            }
        }
        return false; 
    }
    
    public Patient findPatientById(String id) {
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            if (patientList.getEntry(i).getPatientID().equalsIgnoreCase(id)) {
                return patientList.getEntry(i);
            }
        }
        return null;
    }

    public void updatePatientField(Patient p, int choice, String newValue) {
        switch (choice) {
            case 1 -> p.setPatientName(newValue);
            case 2 -> p.setPatientGender(newValue);
            case 3 -> p.setPatientIC(newValue);
            case 4 -> p.setPatientContactNumber(newValue);
        }
        patientDAO.savePatients(patientList);
    }
    
    /*
    Manage doctor account
    */
    
    public boolean addDoctor(Doctor d) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(d.getDoctorID())) {
                return false; 
            }
        }
        
        doctorList.add(d);
        saveDoctorData();
        return true;
    }

    public String getAllDoctors() {
        refreshData();
        
        if (doctorList.isEmpty()) {
            return "No doctor records found.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("\n==========================================================================================\n");
        sb.append(String.format("%-8s | %-20s | %-12s | %-10s | %-20s\n", 
                  "ID", "Name", "Phone", "Gender", "Profession"));
        sb.append("------------------------------------------------------------------------------------------\n");

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            
            sb.append(String.format("%-8s | %-20s | %-12s | %-10s | %-20s\n",
                    d.getDoctorID(),
                    d.getDoctorName(),
                    d.getPhone(),
                    d.getGender(),
                    d.getProfession()));
        }
        
        sb.append("==========================================================================================\n");
        return sb.toString();
    }

    public boolean removeDoctor(String id) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(id)) {
                doctorList.remove(i);
                saveDoctorData();
            
                return true;
            }
        }
        return false;
    }

    public Doctor findDoctorById(String id) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            if (d.getDoctorID().equalsIgnoreCase(id)) {
                return d;
            }
        }
        return null;
    }

    public void updateDoctorDetails(Doctor oldDoc, String newName, String newPhone, String newProf, String newGender) {
        if (newName != null && !newName.isEmpty()) 
            oldDoc.setDoctorName(newName);
    
        if (newPhone != null && !newPhone.isEmpty()) 
            oldDoc.setPhone(newPhone);
    
        if (newProf != null && !newProf.isEmpty())
            oldDoc.setProfession(newProf);
        
        if (newGender != null && !newGender.isEmpty())
            oldDoc.setGender(newGender);

        doctorDAO.saveDoctors(doctorList);
    }
    
    /*
    Manage Appointment Logic
    */
    
    public String getPendingCancellations() {
        refreshData();
        
        // Refresh the list to get the latest requests from the file
        this.appointmentList = new AppointmentDAO().getAllAppointments();

        StringBuilder sb = new StringBuilder();
        sb.append("\n================= PENDING CANCELLATION REQUESTS =================\n");
        sb.append(String.format("%-10s | %-15s | %-25s | %-15s\n", "Appt ID", "Patient Name", "Reason (Remarks)", "Current Status"));
        sb.append("--------------------------------------------------------------------------------\n");

        boolean found = false;
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment app = appointmentList.getEntry(i);
            // We check for "Pending Cancel" status set by the patient
            if ("Pending Cancel".equalsIgnoreCase(app.getStatus())) {
                sb.append(String.format("%-10s | %-15s | %-25s | %-15s\n",
                        app.getAppointmentID(),
                        app.getPatientName(),
                        app.getRemarks(), // This shows the reason they typed
                        app.getStatus()));
                found = true;
            }
        }

        if (!found) {
            return "\n [!] No pending cancellation requests at the moment.";
        }
        return sb.toString();
    }

    public boolean handleCancellation(String appId, int decision) {
        boolean updated = false;
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment app = appointmentList.getEntry(i);
            if (app.getAppointmentID().equalsIgnoreCase(appId)) {
                if (decision == 1) { // 1 = Approve
                    app.setStatus("Cancelled");
                } else if (decision == 2) { // 2 = Reject
                    app.setStatus("Pending");
                    app.setRemarks(""); // Clear the reason if they must still attend
                }
                updated = true;
                break;
            }
        }

        if (updated) {
            // Save the updated status back to appointment.txt
            new AppointmentDAO().saveAppointments(appointmentList);
            return true;
        }
        return false;
    }
    
    /*
    View Report & Summary
    */

    public String generateDailyVisitReport() {
        refreshData();
        this.appointmentList = new AppointmentDAO().getAllAppointments();

        int booked = 0;    // Active/Confirmed
        int cancelled = 0; // Approved cancellations
        int completed = 0; // Finished visits
        int pending = 0;   // Requests waiting for Admin approval

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            String status = appointmentList.getEntry(i).getStatus();

            if ("Booked".equalsIgnoreCase(status) || "Pending".equalsIgnoreCase(status)) {
                booked++;
            } else if ("Cancelled".equalsIgnoreCase(status)) {
                cancelled++;
            } else if ("Completed".equalsIgnoreCase(status)) {
                completed++;
            } else if ("Pending Cancel".equalsIgnoreCase(status)) {
                pending++;
            }
        }

        return "\n================ CLINIC VISIT REPORT ================\n"
                + " Active Appointments     : " + booked + "\n"
                + " Cancellation Requests   : " + pending + " (Pending Admin)\n"
                + " Approved Cancellations  : " + cancelled + "\n"
                + " Completed Consultations : " + completed + "\n"
                + "----------------------------------------------------\n"
                + " Total Records Found     : " + appointmentList.getNumberOfEntries() + "\n"
                + "====================================================\n";
    }

    public String generateMonthlySummary() {
        refreshData();
        
        int pCount = patientList.getNumberOfEntries();
        int dCount = doctorList.getNumberOfEntries();
        int ratio = (dCount == 0 ? 0 : pCount / dCount);

        StringBuilder sb = new StringBuilder();
        sb.append("\n======================================\n");
        sb.append("            CLINIC SUMMARY          \n");
        sb.append("======================================\n");
        sb.append(String.format(" Total Registered Patients : %d\n", pCount));
        sb.append(String.format(" Total Active Doctors      : %d\n", dCount));
        sb.append(String.format(" Patient-to-Doctor Ratio   : %d:1\n", ratio));
        sb.append("--------------------------------------\n");

        // Logic for Capacity Alert
        if (ratio > 50) {
            sb.append(" STATUS: [!] OVER CAPACITY (Hire more doctors)\n");
        } else {
            sb.append(" STATUS: [OK] Optimal Capacity\n");
        }
        sb.append("======================================\n");

        return sb.toString();
    }

    public String generateDoctorPerformance() {
        refreshData();
        
        if (doctorList.isEmpty()) {
            return "No doctors available to evaluate.";
        }

        // Refresh list to ensure we have the latest approved cancellations
        this.appointmentList = new AppointmentDAO().getAllAppointments();

        StringBuilder sb = new StringBuilder();
        sb.append("\n======================================================================\n");
        sb.append("                      DOCTOR PERFORMANCE REPORT                       \n");
        sb.append("======================================================================\n");
        sb.append(String.format("%-8s | %-20s | %-10s | %-10s | %-10s\n",
                "ID", "Name", "Total", "Done", "Cancelled"));
        sb.append("----------------------------------------------------------------------\n");

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            int total = 0, done = 0, cancelled = 0;

            for (int j = 1; j <= appointmentList.getNumberOfEntries(); j++) {
                Appointment app = appointmentList.getEntry(j);
                if (app.getDoctorID().equals(d.getDoctorID())) {
                    total++;
                    if (app.getStatus().equalsIgnoreCase("Completed")) {
                        done++;
                    } else if (app.getStatus().equalsIgnoreCase("Cancelled")) {
                        cancelled++;
                    }
                }
            }

            sb.append(String.format("%-8s | %-20.20s | %-10d | %-10d | %-10d\n",
                    d.getDoctorID(), d.getDoctorName(), total, done, cancelled));
        }
        sb.append("======================================================================\n");
        return sb.toString();
    }
    
    /*
    Manage Pharmacist Account
    */
    
    public boolean addPharmacist(Pharmacist p) {
        // 1. Check if ID already exists
        if (findPharmacistById(p.getPharmacistId()) != null) 
            return false;
        
        // 2. Add to list and SAVE to permanent storage
        pharmacistList.add(p);
        return PharmacistDAO.saveAll((adt.ArrayList<Pharmacist>) pharmacistList);
    }

    public Pharmacist findPharmacistById(String id) {
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            if (pharmacistList.getEntry(i).getPharmacistId().equalsIgnoreCase(id)) {
                return pharmacistList.getEntry(i);
            }
        }
        return null;
    }

    public String getAllPharmacists() {
        refreshData();
        
        if (pharmacistList.isEmpty()) return "No pharmacist records found.";
        
        StringBuilder sb = new StringBuilder();
        sb.append("\n==================================================\n");
        sb.append(String.format("%-10s %-20s\n", "ID", "Name"));
        sb.append("--------------------------------------------------\n");
        
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            Pharmacist p = pharmacistList.getEntry(i);
            sb.append(String.format("%-10s %-20s\n", p.getPharmacistId(), p.getName()));
        }
        sb.append("==================================================\n");
        return sb.toString();
    }

    public boolean removePharmacist(String id) {
        for (int i = 1; i <= pharmacistList.getNumberOfEntries(); i++) {
            if (pharmacistList.getEntry(i).getPharmacistId().equalsIgnoreCase(id)) {
                pharmacistList.remove(i);
                // Save changes to file after removing
                return PharmacistDAO.saveAll((adt.ArrayList<Pharmacist>) pharmacistList);
            }
        }
        return false;
    }
    
    public void updatePharmacistDetails(Pharmacist oldPhar, String newName) {
        if (newName != null && !newName.isEmpty()) {
            oldPhar.setName(newName);
            // Save changes to file after updating
            PharmacistDAO.saveAll((adt.ArrayList<Pharmacist>) pharmacistList);
        }
    }
    
    public boolean login(String id) {
        for (int i = 1; i <= adminList.getNumberOfEntries(); i++) {
            Admin a = adminList.getEntry(i);
            
            if (a.getAdminID().equalsIgnoreCase(id)) {
                this.currentAdmin = a; 
                return true;
            }
        }
        return false;
    }
    
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }
    
    private void saveDoctorData() {
        doctorDAO.saveDoctors(doctorList);
    }
    
    private void refreshData() {
        this.doctorList = doctorDAO.getAllDoctors();
        this.patientList = patientDAO.getAllPatients();
        this.appointmentList = new AppointmentDAO().getAllAppointments();

        // Also refresh pharmacists
        adt.ArrayList<Pharmacist> loadedPharmacists = PharmacistDAO.loadAll();
        if (loadedPharmacists != null) {
            this.pharmacistList = loadedPharmacists;
        }
    }
    
    
}