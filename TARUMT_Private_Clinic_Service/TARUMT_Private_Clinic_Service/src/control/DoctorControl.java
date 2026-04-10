package control;

import adt.ListInterface;
import boundary.DoctorUI;
import dao.DoctorDAO;
import dao.AppointmentDAO;
import entity.*;


/**
 * @author Tang Le Yi
 */
public class DoctorControl {

    private ListInterface<Appointment> appointmentList;
    private ListInterface<Doctor> doctorList;
    private DoctorUI ui = new DoctorUI();

    private DoctorDAO doctorDAO = new DoctorDAO();
    private AppointmentDAO appointmentDAO = new AppointmentDAO();

    public DoctorControl() {
        doctorList = doctorDAO.getAllDoctors();
        appointmentList = appointmentDAO.getAllAppointments();
    }
   

    public String getDoctorName(String doctorID) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            if (d.getDoctorID().equals(doctorID)) {
                return d.getDoctorName();
            }
        }
        return null;
    }

    public boolean isValidDoctorID(String doctorID) {
        // Refresh doctor list to ensure we have the latest data
        this.doctorList = doctorDAO.getAllDoctors();
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equalsIgnoreCase(doctorID.trim())) {
                return true;
            }
        }
        return false;
    }

    private void refreshAppointments() {
        appointmentList = appointmentDAO.getAllAppointments();
    }

    // --- MAIN MODULE RUNNER ---
    public void runDoctorModule() {
        int mainChoice;
        
        do {
            mainChoice = ui.showPortalMenu();
            switch (mainChoice) {
                case 1:
                    handleLoginFlow();
                    break;
                case 2:
                    registerNewDoctor();
                    break;
                case 0:
                    ui.displayMessage("Exiting Doctor Module...");
                    break;
                default:
                    ui.displayMessage("Invalid Input!");
            }
        } while (mainChoice != 0);
    }
    
    private void handleLoginFlow() {
        String doctorID = ui.enterDoctorID(this);
        
        // If login is successful (UI returns a valid ID)
        if (doctorID != null) {
            int choice;
            do {
                String currentName = getDoctorName(doctorID);
                choice = ui.showDoctorMenu(currentName);

                switch (choice) {
                    case 1: callNextPatient(doctorID); break;
                    case 2: handleReportMenu(doctorID); break;
                    case 3: updateProfile(doctorID); break;
                    case 0: ui.displayMessage("Logging out..."); break;
                    default: ui.displayMessage("Invalid choice.");
                }
            } while (choice != 0);
        }
    }
    
    public void registerNewDoctor() {
        // UI collects data, Control saves it
        Doctor newDoctor = ui.inputRegistrationDetails(this);
        
        if (newDoctor != null) {
            doctorList.add(newDoctor);
            doctorDAO.saveDoctors(doctorList);
            ui.displayMessage("Registration Successful! Welcome, Dr. " + newDoctor.getDoctorName());
        }
    }

    public void callNextPatient(String doctorID) {
        refreshAppointments();
        Appointment nextPatient = getNextPatient(doctorID);

        if (nextPatient == null) {
            ui.displayMessage("\n[!] No pending patients in your queue.");
            ui.pressEnterToContinue();
            return;
        }

        ui.displayServingPatient(nextPatient);
        int action = ui.currentPatientActionMenu();

        if (action == 1) { 
            String statusChoice = ui.chooseStatus();
            String finalStatus = "";
            String finalNote = "";

            if ("1".equals(statusChoice)) {
                finalStatus = "To-Collect";
                finalNote = ui.inputDoctorFeedback();
            } else if ("2".equals(statusChoice)){
                finalStatus = "Completed";
                finalNote = ui.inputDoctorFeedback();
            } else if ("3".equals(statusChoice)) {
                finalStatus = "Cancelled";
                finalNote = "CANCEL REASON: " + ui.inputCancellationReason();
            } else {
                return;
            }

            nextPatient.setStatus(finalStatus);
            nextPatient.setRemarks(finalNote);

            boolean foundAndUpdated = false;
            for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                Appointment currentInList = appointmentList.getEntry(i);
                if (currentInList.getAppointmentID().trim().equalsIgnoreCase(nextPatient.getAppointmentID().trim())) {
                    appointmentList.replace(i, nextPatient);
                    foundAndUpdated = true;
                    break;
                }
            }

            // Save to file
            if (foundAndUpdated) {
                appointmentDAO.saveAppointments(appointmentList);
                ui.displayMessage("System: Record updated to " + finalStatus + " and saved to file.");
            }

            ui.pressEnterToContinue();
            callNextPatient(doctorID);
        }
    }

    private Appointment getNextPatient(String doctorID) {
        this.appointmentList = appointmentDAO.getAllAppointments();

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment appt = appointmentList.getEntry(i);

            String fileDocID = appt.getDoctorID().trim();
            String loginDocID = doctorID.trim();
            String status = appt.getStatus().trim();

            if (fileDocID.equalsIgnoreCase(loginDocID) && status.equalsIgnoreCase("Pending")) {
                return appt;
            }
        }
        return null;
    }

    // --- PROFILE MANAGEMENT ---
    private void updateProfile(String doctorID) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);
            if (d.getDoctorID().equals(doctorID)) {
                boolean active = true;
                while (active) {
                    ui.displayProfile(doctorID, d);
                    int fieldChoice = ui.chooseUpdateField();

                    switch (fieldChoice) {
                        case 1: d.setDoctorName(ui.inputNewName(d)); break;
                        case 2: d.setPhone(ui.inputNewPhone(d)); break;
                        case 3: 
                            String g = ui.chooseGender(d);
                            if (g != null) d.setGender(g);
                            break;
                        case 4: d.setProfession(ui.selectProfession(d)); break;
                        case 0: active = false; continue;
                    }
                    
                    if (fieldChoice != 0) {
                        doctorList.replace(i, d);
                        doctorDAO.saveDoctors(doctorList);
                        ui.displayMessage("Profile updated and saved.");
                    }
                }
                break;
            }
        }
    }

    // --- REPORT MANAGEMENT ---
    private void handleReportMenu(String doctorID) {
        int reportChoice;
        do {
            reportChoice = ui.reportMenu();
            refreshAppointments();
            switch (reportChoice) {
                case 1:
                    ui.displayReportTable(appointmentList, doctorID);
                    ui.pressEnterToContinue();
                    break;
                case 2:
                    handleSearch(doctorID);
                    break;
                case 3:
                    calculateTotalReport(doctorID);
                    ui.pressEnterToContinue();
                    break;
            }
        } while (reportChoice != 0);
    }

    private void handleSearch(String doctorID) {
        int searchType = ui.searchReportMenu();
        if (searchType == 0) return;

        String type = "";
        String keyword = "";

        if (searchType == 1) {
            type = "name";
            keyword = ui.inputSearchKeyword("Enter Patient Name >> ");
        } else if (searchType == 2) {
            type = "status";
            keyword = ui.chooseSearchStatus();
            if (keyword == null) return;
        } else if (searchType == 3) {
            type = "id";
            keyword = ui.inputSearchKeyword("Enter Appointment ID >> ");
        }

        ui.displaySearchResultTable(appointmentList, doctorID, type, keyword);
        ui.pressEnterToContinue();
    }

    private void calculateTotalReport(String doctorID) {
        int pending = 0, completed = 0, cancelled = 0, total = 0;

        refreshAppointments();

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            Appointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().trim().equalsIgnoreCase(doctorID.trim())) {
                total++;
                String status = appt.getStatus().trim().toLowerCase();
                if (status.equals("pending")) {
                    pending++;
                } else if (status.equals("completed")) {
                    completed++;
                } else if (status.equals("cancelled")) {
                    cancelled++;
                }
            }
        }
        ui.displayTotalAppointmentReport(pending, completed, cancelled, total);
    }
}