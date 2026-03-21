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

    private void updateStatus(String doctorID) {
        while (true) {
            ui.displayDoctorAppointment(appointmentList, doctorID);

            int choice = ui.chooseAppointmentNumber();

            if (choice == 0) {
                ui.displayMessage("Returning to previous menu...");
                return;
            }

            int displayNo = 0;
            int actualIndex = -1;

            for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                DocAppointment appt = appointmentList.getEntry(i);

                if (appt.getDoctorID().equals(doctorID) && appt.getStatus().equalsIgnoreCase("Pending")) {
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
            appointmentList.replace(actualIndex, selectedAppt);

            ui.displayMessage("Status updated successfully!");
            ui.displayDoctorAppointment(appointmentList, doctorID);

            int nextChoice = ui.displayOptionsAndGetChoice(
                "What would you like to do next?",
                "Continue Updating Appointment Status"
            );

            if (nextChoice == 0) {
                ui.displayMessage("Returning to previous menu...");
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
                }
                break;
            }
        }
    }

        private void viewByStatus(String doctorID, String status) {
        ui.displayPatientsTable(appointmentList, doctorID, status);
        }


       private void callNextPatient(String doctorID) {
            while (true) {
                DocAppointment nextPatient = getNextPatient(doctorID);
                ui.displayNextPatient(nextPatient);

                if (nextPatient == null) {
                    ui.pressEnterToContinue();
                    return;
                }

                int choice = ui.currentPatientActionMenu();

                if (choice == 0) {
                    ui.displayMessage("Returning to previous menu...");
                    return;
                }

                if (choice == 1) {
                    String newStatus = ui.chooseStatus();

                    if (newStatus == null) {
                        ui.displayMessage("Status update cancelled.");
                        continue;
                    }

                    nextPatient.setStatus(newStatus);

                    for (int i = 1; i <= appointmentList.getNumberOfEntries(); i++) {
                        if (appointmentList.getEntry(i) == nextPatient) {
                            appointmentList.replace(i, nextPatient);
                            break;
                        }
                    }

                    ui.displayMessage("Patient status updated successfully!");
                    ui.displayNextPatient(nextPatient);

                    int nextChoice = ui.afterStatusUpdatedMenu();

                    if (nextChoice == 0) {
                        ui.displayMessage("Returning to previous menu...");
                        return;
                    } else if (nextChoice == 1) {
                        continue;
                    } else {
                        ui.displayMessage("Invalid choice. Returning to previous menu...");
                        return;
                    }
                } else {
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

    public void runDoctorModule() {
        String doctorID = ui.enterDoctorID(this);
        int choice;

        do {
            choice = ui.showDoctorMenu();

            switch (choice) {
                case 1:
                    int subChoice;
                    do {
                        subChoice = ui.viewAppointmentMenu();

                        switch (subChoice) {
                            case 1:
                                callNextPatient(doctorID);
                                break;

                            case 2:
                                viewByStatus(doctorID, "Pending");
                                break;

                            case 3:
                                viewByStatus(doctorID, "Follow Up");
                                break;

                            case 4:
                                viewByStatus(doctorID, "Completed");
                                break;

                            case 5:
                                viewByStatus(doctorID, "Cancelled");
                                break;

                            case 0:
                                ui.displayMessage("Returning to previous menu...");
                                break;

                            default:
                                ui.displayMessage("Invalid choice. Try again.");
                        }

                    } while (subChoice != 0);
                    break;

                case 2:
                    updateStatus(doctorID);
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