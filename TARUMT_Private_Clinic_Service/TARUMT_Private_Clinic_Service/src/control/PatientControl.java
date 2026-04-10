package control;

import adt.ArrayList;
import adt.ListInterface;
import boundary.PatientUI;
import dao.AppointmentDAO;
import dao.DoctorDAO;
import dao.PatientDAO;
import entity.Appointment;
import entity.Doctor;
import entity.Patient;
import utility.MessageUI;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author lee seng wai
 * 
 */

public class PatientControl {

    Scanner scan = new Scanner(System.in);

    private ListInterface<Appointment> appointmentList = new ArrayList<>();
    private ListInterface<Doctor> doctorList = new ArrayList<>();
    private ListInterface<Patient> patientList = new ArrayList<>();

    private PatientUI patientUI = new PatientUI();
    private Patient currentPatient;

    private DoctorDAO doctorDAO = new DoctorDAO();
    private PatientDAO patientDAO = new PatientDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public PatientControl() {
        doctorList = doctorDAO.getAllDoctors();
        patientList = patientDAO.getAllPatients();
        appointmentList = appointmentDAO.getAllAppointments();

        linkAppointmentsToPatients();
        linkAppointmentsToDoctors();
    }

    public void runPatientModule() {
        int option = -1;
        do {
            option = patientUI.getPatientAccount();
            switch (option) {
                case 1: createNewAccount(); break;
                case 2: loginPatientAccount(); break;
                case 0: MessageUI.displayReturnPreviousPageMessage(); break;
                default: MessageUI.displayInvalidInputMessage();
            }
        } while (option != 0);
    }

    public void createNewAccount() {
        Patient newAccount = patientUI.newPatientDetails(patientList);
        boolean duplicate = false;

        // Check if ID already exists in the database/list
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            if (newAccount.getPatientID().equalsIgnoreCase(patientList.getEntry(i).getPatientID())) {
                duplicate = true;
                break;
            }
        }

        if (duplicate) {
            MessageUI.displayPatientIDExistsMessage();
        } else {
            patientList.add(newAccount);
            patientDAO.savePatients(patientList);
            MessageUI.displayNewPatientAccountCreatedSuccessfulMessage();
        }
    }

    public void loginPatientAccount() {
        String inputID = patientUI.getPatientID();
        boolean found = false;

        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            Patient patient = patientList.getEntry(i);
            if (inputID.equalsIgnoreCase(patient.getPatientID())) {
                found = true;
                currentPatient = patient;
                MessageUI.displayLoginSuccessfulMessage();
                runPatientMainMenu();
                break;
            }
        }
        if (!found) MessageUI.displayPatientIDNotFoundMessage();
    }

    public void runPatientMainMenu() {
        int choice = -1;
        do {
            choice = patientUI.getPatientMenu(currentPatient);
            switch (choice) {
                case 1: updateProfile(); break;
                case 2: viewProfile(); break;
                case 3: bookAppointment(); break;
                case 4: cancelAppointment(); break;
                case 5: viewAppointment(); break;
                case 0: MessageUI.displayLogoutSuccessfulMessage(); break;
                default: MessageUI.displayInvalidInputMessage();
            }
        } while (choice != 0);
    }

    public void updateProfile() {
        int updateChoice = 0;
        do {
            patientUI.viewPatientProfile(currentPatient);
            updateChoice = patientUI.updatePatientDetails();
            switch (updateChoice) {
                case 1:
                    currentPatient.setPatientName(patientUI.updatePatientName());
                    break;
                case 2:
                    currentPatient.setPatientGender(patientUI.updatePatientGender());
                    break;
                case 3:
                    currentPatient.setPatientContactNumber(patientUI.updatePatientContactNumber());
                    break;
                case 0:
                    break;
                default:
                    MessageUI.displayInvalidInputMessage();
            }
            if (updateChoice >= 1 && updateChoice <= 3) {
                patientDAO.savePatients(patientList);
                System.out.println(" [OK] Profile updated successfully!");
            }
        } while (updateChoice != 0);
    }

    public void viewProfile() {
        patientUI.viewPatientProfile(currentPatient);
    }

    public void bookAppointment() {
        refreshData();
        
        this.doctorList = doctorDAO.getAllDoctors();
        
        boolean hasActive = false;
        for (int i = 1; i <= currentPatient.getAppointmentList().getNumberOfEntries(); i++) {
            String status = currentPatient.getAppointmentList().getEntry(i).getStatus();

            if (!status.equalsIgnoreCase("Completed") && !status.equalsIgnoreCase("Cancelled")) {
                hasActive = true;
                break;
            }
        }
        if (hasActive) {
            MessageUI.displayExistingAppointmentMessage(); 
            return;
        }
        
        // 1. Let patient choose doctor type
        String selectedSpecialty = patientUI.selectDepartment();

        // 2. Find available doctor
        ListInterface<Doctor> matchedDoctor = findAllDoctorsBySpecialty(selectedSpecialty);
        if (matchedDoctor.isEmpty()) {
            MessageUI.displayDoctorNotFoundMessage();
            return;
        }
        
        Doctor selectedDoctor = patientUI.selectSpecificDoctor(matchedDoctor);
        
        if (selectedDoctor == null) {
            return; 
        }

        // 3. Get Symptom
        String symptomInput = patientUI.inputSymptom();
        if (symptomInput.isEmpty()) {
            return;
        }

        // 4. Get System Date and Time
        LocalDateTime now = LocalDateTime.now();
        String systemDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String systemTime = now.format(DateTimeFormatter.ofPattern("hh:mm a"));

        // 5. Confirmation
        patientUI.displayAssignedDoctor(selectedDoctor);
        if (patientUI.confirmAppointment().equalsIgnoreCase("Y")) {
            String newID = generateNextAppointmentID();

            Appointment newAppointment = new Appointment(
                    newID,
                    currentPatient.getPatientID(),
                    currentPatient.getPatientName(),
                    selectedDoctor.getDoctorID(),
                    selectedDoctor.getDoctorName(),
                    selectedDoctor.getProfession(),
                    symptomInput,
                    "Pending",
                    systemDate, // Auto-generated Date
                    systemTime, // Auto-generated Time
                    "" // No feedback yet
            );

            // Sync lists
            appointmentList.add(newAppointment);
            currentPatient.getAppointmentList().add(newAppointment);
            selectedDoctor.getAppointmentList().add(newAppointment);

            appointmentDAO.saveAppointments(appointmentList);
            MessageUI.displayBookAppointmentSuccessfulMessage();
        }
    }

    public void cancelAppointment() {
        refreshData();
        
        if (currentPatient.getAppointmentList().isEmpty()) {
            MessageUI.displayActiveAppointmentNotFoundMessage();
            return;
        }

        patientUI.displayActiveAppointments(currentPatient);

        int choice = patientUI.getChoiceForCancellation(); // Move prompt to UI
        if (choice <= 0) {
            return;
        }

        int count = 0;
        boolean foundMatch = false;

        for (int i = 1; i <= currentPatient.getAppointmentList().getNumberOfEntries(); i++) {
            Appointment a = currentPatient.getAppointmentList().getEntry(i);

            // Matching status logic
            if (a.getStatus().equalsIgnoreCase("Pending") || a.getStatus().equalsIgnoreCase("Booked") || a.getStatus().equalsIgnoreCase("Active")) {
                count++;
                if (count == choice) {
                    foundMatch = true;

                    // Move input prompts to PatientUI
                    String reason = patientUI.inputCancellationReason();

                    if (patientUI.confirmAppointment().equalsIgnoreCase("Y")) {
                        a.setStatus("Pending Cancel");
                        a.setRemarks(reason);

                        // Sync the main list and save
                        appointmentDAO.saveAppointments(appointmentList);
                        MessageUI.displayCancelAppointmentRequestMessage();
                    }
                    return;
                }
            }
        }

        if (!foundMatch) {
            MessageUI.displayInvalidInputMessage();
        }
    }

    public void viewAppointment() {
        refreshData();
        
        int viewOption = patientUI.getAppointmentMenu();

        if (viewOption == 1) {
            if (currentPatient.getAppointmentList().isEmpty()) {
                MessageUI.displayAppointmentHistoryNotFoundMessage();
            } else {
                // Pass the list to the UI to handle the printing/formatting
                patientUI.displayFullAppointmentHistory(currentPatient.getAppointmentList());
            }
        } else if (viewOption == 2) { 
            if (currentPatient.getAppointmentList().isEmpty()) {
                MessageUI.displayAppointmentHistoryNotFoundMessage();
            } else {
                // 1. Ask user which status they want
                String selectedStatus = patientUI.getStatusChoice();
                // 2. Display the filtered results
                patientUI.displayAppointmentsByStatus(currentPatient.getAppointmentList(), selectedStatus);
                patientUI.pause();
            }
        } else if (viewOption == 0) {
            MessageUI.displayReturnPreviousPageMessage();
        }
    }

    // --- HELPER METHODS ---
    private void linkAppointmentsToPatients() {
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment a = appointmentList.getEntry(i);
            Patient p = findPatientById(a.getPatientID().trim());
            if (p != null) {
                p.getAppointmentList().add(a);
            }
        }
    }

    private void linkAppointmentsToDoctors() {
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment a = appointmentList.getEntry(i);
            Doctor d = findDoctorByID(a.getDoctorID());
            if (d != null) d.getAppointmentList().add(a);
        }
    }

    public Patient findPatientById(String id) {
        for (int i = 1; i <= patientList.getNumberOfEntries(); i++) {
            if (patientList.getEntry(i).getPatientID().equalsIgnoreCase(id)) return patientList.getEntry(i);
        }
        return null;
    }

    private Doctor findDoctorByID(String id) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(id)) return doctorList.getEntry(i);
        }
        return null;
    }

    private ListInterface<Doctor> findAllDoctorsBySpecialty(String specialty) {
        ListInterface<Doctor> matches = new ArrayList<>();
        String searchCriteria = specialty.trim().toLowerCase();

        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);

            String doctorProfession = d.getProfession().trim().toLowerCase();

            // Check if they match or if one contains the other (e.g., "GP" inside "GP / Clinic")
            if (doctorProfession.equals(searchCriteria) || doctorProfession.contains(searchCriteria)) {
                matches.add(d);
            }
        }
        return matches;
    }
    
    private String generateNextAppointmentID() {
        if (appointmentList.isEmpty()) {
            return "AP001";
        }

        int maxNumber = 0;
        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            String id = appointmentList.getEntry(i).getAppointmentID();
            try {
                int currentNum = Integer.parseInt(id.substring(2));
                if (currentNum > maxNumber) {
                    maxNumber = currentNum;
                }
            } catch (Exception e) {
            }
        }

        return String.format("AP%03d", maxNumber + 1);
    }
    
    private void refreshData() {
        this.doctorList = doctorDAO.getAllDoctors();
        this.patientList = patientDAO.getAllPatients();
        this.appointmentList = appointmentDAO.getAllAppointments();

        linkAppointmentsToPatients();
        linkAppointmentsToDoctors();

        if (currentPatient != null) {
            currentPatient = findPatientById(currentPatient.getPatientID());
        }
    }
}