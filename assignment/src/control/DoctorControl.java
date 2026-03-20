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

    private void viewAppointment(String doctorID) {
        ui.displayDoctorAppointment(appointmentList, doctorID);
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

                if (appt.getDoctorID().equals(doctorID)) {
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
                            System.out.println("Name updated successfully!");
                            break;

                        case 2:
                            String newPhone = ui.inputNewPhone(d);
                            d.setPhone(newPhone);
                            System.out.println("Phone updated successfully!");
                            break;

                        case 3:
                            String newGender = ui.chooseGender(d);
                            if (newGender != null) {
                                d.setGender(newGender);
                                System.out.println("Gender updated successfully!");
                            }
                            break;

                        case 0:
                            continueUpdating = false;
                            System.out.println("Returning to previous menu...");
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }

                    doctorList.replace(i, d);
                }
                break;
            }
        }
    }

    public void runDoctorModule() {
        String doctorID = ui.enterDoctorID(this);
        int choice;
        do {
            choice = ui.showDoctorMenu();
            switch (choice) {
                case 1:
                    int nextChoice;
                    do {
                        viewAppointment(doctorID); 
                        nextChoice = ui.displayOptionsAndGetChoice(
                            "What would you like to do next?",
                            "Update Appointment Status"
                        );

                        if (nextChoice == 1) {
                            updateStatus(doctorID);
                            break;
                        } else if (nextChoice == 0) {
                            break;
                        } else {
                            ui.displayMessage("Invalid choice. Try again.\n");
                        }

                    } while (true);

                    break;
                case 2:
                    updateStatus(doctorID);
                    break;
                case 3:
                    updateProfile(doctorID);
                    break;
            }
        } while (choice != 0);
    }
}