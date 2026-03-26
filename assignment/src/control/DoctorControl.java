package control;

import adt.ListInterface;
import boundary.DoctorUI;
import dao.DocAppointmentDAO;
import dao.DoctorDAO;
import entity.DocAppointment;
import entity.Doctor;

public class DoctorControl {
    private ListInterface<DocAppointment> appointmentList;
    private ListInterface<Doctor> doctorList;
    private DoctorUI ui = new DoctorUI();

    private DoctorDAO doctorDAO = new DoctorDAO();
    private DocAppointmentDAO appointmentDAO = new DocAppointmentDAO();

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
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            if (doctorList.getEntry(i).getDoctorID().equals(doctorID)) {
                return true;
            }
        }
        return false;
    }

// Debug Use only for test file exits
//     public boolean isValidDoctorID(String doctorID) {
//     for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
//         System.out.println("Comparing with: " + doctorList.getEntry(i).getDoctorID());
//         if (doctorList.getEntry(i).getDoctorID().equals(doctorID.trim())) {
//             return true;
//         }
//     }
//     return false;
// }

    private void updateStatusByCategory(String doctorID, String status) {
    while (true) {
        ui.displayPatientsTable(appointmentList, doctorID, status);

        int actionChoice = ui.statusTableActionMenu();

        if (actionChoice == 0) {
            ui.displayMessage("Returning to previous menu...");
            return;
        }

        if (actionChoice != 1) {
            ui.displayMessage("Invalid choice. Try again.");
            continue;
        }

        int choice = ui.chooseAppointmentNumber();

        if (choice == 0) {
            ui.displayMessage("Returning to previous menu...");
            return;
        }

        int displayNo = 0;
        int actualIndex = -1;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID)
                    && appt.getStatus().equalsIgnoreCase(status)) {
                displayNo++;
                if (displayNo == choice) {
                    actualIndex = i;
                    break;
                }
            }
        }

        if (actualIndex == -1) {
            ui.displayMessage("Invalid appointment number.");
            continue;
        }

        DocAppointment selectedAppt = appointmentList.getEntry(actualIndex);

        String newStatus = ui.chooseStatus();

        if (newStatus == null) {
            ui.displayMessage("Status update cancelled.");
            continue;
        }

        selectedAppt.setStatus(newStatus);
        String feedback = ui.inputDoctorFeedback();
        selectedAppt.setDoctorFeedback(feedback);
        appointmentList.replace(actualIndex, selectedAppt);
        appointmentDAO.saveAppointments(appointmentList);

        ui.displayMessage("Patient status updated successfully!");
        ui.displayPatientsTable(appointmentList, doctorID, status);

        int nextChoice = ui.continueUpdateMenu();

        if (nextChoice == 0) {
            ui.displayMessage("Returning to previous menu...");
            return;
        } else if (nextChoice == 1) {
            continue;
        } else {
            ui.displayMessage("Invalid choice. Returning to previous menu...");
            return;
        }
    }
}

    private void updateProfile(String doctorID) {
        for (int i = 1; i <= doctorList.getNumberOfEntries(); i++) {
            Doctor d = doctorList.getEntry(i);

            if (d.getDoctorID().equals(doctorID)) {
                boolean continueUpdating = true;

                while (continueUpdating) {
                    ui.displayProfile(doctorID, d);
                    int fieldChoice = ui.chooseUpdateField();

                    switch (fieldChoice) {
                        case 1:
                            String newName = ui.inputNewName(d);
                            d.setDoctorName(newName);
                            ui.displayMessage("Name updated successfully!");
                            break;

                        case 2:
                            String newPhone = ui.inputNewPhone(d);
                            d.setPhone(newPhone);
                            ui.displayMessage("Phone updated successfully!");
                            break;

                        case 3:
                            String newGender = ui.chooseGender(d);
                            if (newGender != null) {
                                d.setGender(newGender);
                                ui.displayMessage("Gender updated successfully!");
                            }
                            break;

                        case 0:
                            continueUpdating = false;
                            ui.displayMessage("Returning to previous menu...");
                            break;

                        default:
                            ui.displayMessage("Invalid choice. Please try again.");
                            break;
                    }

                    doctorList.replace(i, d);
                    doctorDAO.saveDoctors(doctorList);  //save file to txt
                }
                break;
            }
        }
    }

    private void viewByStatus(String doctorID, String status) {
        ui.displayPatientsTable(appointmentList, doctorID, status);
        }

    private void refreshAppointments() {
        appointmentList = appointmentDAO.getAllAppointments();
    }


    private void callNextPatient(String doctorID) {
    while (true) {
        refreshAppointments(); //check latest txt file
        DocAppointment nextPatient = getNextPatient(doctorID);
        ui.displayNextPatient(nextPatient);

        if (nextPatient == null) {
            ui.pressEnterToContinue();
            return;
        }

        int choice = ui.currentPatientActionMenu();

        switch (choice) {
            case 1:
                String newStatus = ui.chooseStatus();

                if (newStatus == null) {
                    ui.displayMessage("Status update cancelled.");
                    continue;
                }
                String feedback = ui.inputDoctorFeedback();

                nextPatient.setStatus(newStatus);
                 nextPatient.setDoctorFeedback(feedback); 

                for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                    if (appointmentList.getEntry(i) == nextPatient) {
                        appointmentList.replace(i, nextPatient);
                        appointmentDAO.saveAppointments(appointmentList); // save to file
                        break;
                    }
                }

                ui.displayMessage("Patient status updated successfully!");
                ui.displayUpdatedAppointmentDetails(nextPatient, feedback);

                int nextAction = ui.afterStatusUpdatedMenu();

                if (nextAction == 1) {
                    continue; // show next patient
                } else {
                    ui.displayMessage("Returning to main menu...");
                    return;// back to Doctor Menu
                }
                
            case 0:
                ui.displayMessage("Returning to previous menu...");
                return;

            default:
                ui.displayMessage("Invalid choice. Try again.");    
        }
    }
}

        private DocAppointment getNextPatient(String doctorID) {
            DocAppointment nextPatient = null;

            for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                DocAppointment appt = appointmentList.getEntry(i);

                if (appt.getDoctorID().equals(doctorID)
                        && appt.getStatus().equalsIgnoreCase("Pending")) {

                    if (nextPatient == null || appt.getQueueNo() < nextPatient.getQueueNo()) {
                        nextPatient = appt;
                    }
                }
            }

            return nextPatient;
        }
    private void displayAllReport(String doctorID) {
        refreshAppointments(); //check latest txt file
        ui.displayReportTable(appointmentList, doctorID);
    }

    private void searchReport(String doctorID) {
        int choice;

        do {
            refreshAppointments(); //check latest txt file
            //display all report before searching
            ui.displayReportTable(appointmentList, doctorID);
            choice = ui.searchReportMenu();

            switch (choice) {
                case 1:
                    searchByPatientName(doctorID);
                    ui.pressEnterToContinue();
                    break;

                case 2:
                    searchByStatus(doctorID);
                    ui.pressEnterToContinue();
                    break;

                case 3:
                    searchByAppointmentID(doctorID);
                    ui.pressEnterToContinue();
                    break;

                case 0:
                    ui.displayMessage("Returning to previous menu...");
                    break;

                default:
                    ui.displayMessage("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }

    //Search Patient Name in Report
    private void searchByPatientName(String doctorID) {
        String keyword = ui.inputSearchKeyword("Enter patient name: ");

        ui.displaySearchResultTable(appointmentList, doctorID, "name", keyword);
    }

    //Search by status in report
    private void searchByStatus(String doctorID) {
        String status = ui.chooseSearchStatus();

        if (status == null) {
            ui.displayMessage("Search cancelled.");
            return;
        }

        ui.displaySearchResultTable(appointmentList, doctorID, "status", status);
    }

    //Search with Appointment ID
    private void searchByAppointmentID(String doctorID) {
        String id = ui.inputSearchKeyword("Enter Appointment ID: ");

        ui.displaySearchResultTable(appointmentList, doctorID, "id", id);
    }

    //calculate total report (case 3)
    private void calculateTotalReport(String doctorID) {
        int pendingCount = 0;
        int completedCount = 0;
        int cancelledCount = 0;
        int totalCount = 0;

        for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
            DocAppointment appt = appointmentList.getEntry(i);

            if (appt.getDoctorID().equals(doctorID)) {
                totalCount++;

                if (appt.getStatus().equalsIgnoreCase("Pending")) {
                    pendingCount++;
                } else if (appt.getStatus().equalsIgnoreCase("Completed")) {
                    completedCount++;
                } else if (appt.getStatus().equalsIgnoreCase("Cancelled")) {
                    cancelledCount++;
                }
            }
        }

        ui.displayTotalAppointmentReport(pendingCount, completedCount, cancelledCount, totalCount);
    }

    public void runDoctorModule() {
        String doctorID = ui.enterDoctorID(this);
        int choice;

        do {
            choice = ui.showDoctorMenu();

            switch (choice) {
                case 1:
                    callNextPatient(doctorID);
                    break;

                case 2:
                    int reportChoice;
                    do {
                        reportChoice = ui.reportMenu();

                        switch (reportChoice) {
                            case 1:// Display all reports
                            displayAllReport(doctorID);
                            ui.pressEnterToContinue();
                            break;

                            case 2: //Search report
                                searchReport(doctorID);
                                break;

                            case 3: //calculate total appointment report
                                calculateTotalReport(doctorID);
                                ui.pressEnterToContinue();
                                break;

                            case 0:
                                ui.displayMessage("Returning to previous menu...");
                                break;

                            default:
                                ui.displayMessage("Invalid choice. Try again.");
                        }
                    } while (reportChoice != 0);
                    break;

                case 3:
                    updateProfile(doctorID);
                    break;

                case 0:
                    ui.displayMessage("Exiting Doctor Module...");
                    break;

                default:
                    ui.displayMessage("Invalid choice. Try again.");
            }

        } while (choice != 0);
    }
}