package control;

import adt.*;
import boundary.DoctorUI;
import entity.DocAppointment;
import entity.Doctor;

public class DoctorControl {
    private ListInterface<DocAppointment> appointmentList = new ArrayList<>();
    private ListInterface<Doctor> doctorList = new ArrayList<>(); 
    private DoctorUI ui = new DoctorUI();

     public DoctorControl(){
        // Sample doctor data
        doctorList.add(new Doctor("D001", "Tang Le Yi", "0123456789", "General Practitioner"));
        doctorList.add(new Doctor("D002", "Lim Keong", "0129876543", "Dentist"));
        doctorList.add(new Doctor("D003", "Samuel Wong", "0131112233", "Cardiologist"));

        // Sample appointment data
        appointmentList.add(new DocAppointment("A001","D001","Patient A","Pending"));
        appointmentList.add(new DocAppointment("A002","D001","Patient B","Approved"));
        appointmentList.add(new DocAppointment("A003","D002","Patient C","Pending"));
    }

    // Check doctor ID and get name
    public String getDoctorName(String doctorID){
        for(int i = 1; i <= doctorList.getNumberOfEntries(); i++){
            Doctor d = doctorList.getEntry(i);
            if(d.getDoctorID().equals(doctorID)){
                return d.getDoctorName();
            }
        }
        return null;  // ID not found
    }

    public boolean isValidDoctorID(String doctorID){
        for(int i = 1; i <= doctorList.getNumberOfEntries(); i++){
            if(doctorList.getEntry(i).getDoctorID().equals(doctorID)){
                return true;
            }
        }
        return false;
    }

    private void viewAppointment(String doctorID){
    for(int i = 1; i <= appointmentList.getNumberOfEntries(); i++){
        DocAppointment appt = appointmentList.getEntry(i);
        if(appt.getDoctorID().equals(doctorID)){
            System.out.println(appt);
        }
    }
}

    private void updateStatus(String doctorID){
    for(int i = 1; i <= appointmentList.getNumberOfEntries(); i++){
        DocAppointment appt = appointmentList.getEntry(i);

        if(appt.getDoctorID().equals(doctorID)){
            System.out.println("Current Appointment: " + appt);
            System.out.print("Enter new status: ");
            String status = new java.util.Scanner(System.in).nextLine();
            appt.setStatus(status);
            appointmentList.replace(i, appt);
            System.out.println("Status updated.");
        }
    }
    }
    private void updateProfile(String doctorID){

        for(int i = 1; i <= doctorList.getNumberOfEntries(); i++){
            Doctor d = doctorList.getEntry(i);

            if(d.getDoctorID().equals(doctorID)){

                boolean continueUpdating = true;

                while(continueUpdating){
                    // Display Profile
                    ui.displayProfile(doctorID, d);

                    int fieldChoice = ui.chooseUpdateField();

                    switch(fieldChoice){
                        case 1: // Name
                            String newName = ui.inputNewName(d);
                            d.setDoctorName(newName);
                            System.out.println("Name updated successfully!");
                            break;

                        case 2: // Phone
                            String newPhone = ui.inputNewPhone(d);
                            d.setPhone(newPhone);
                            System.out.println("Phone updated successfully!");
                            break;

                        case 3: // Profession
                            String newProf = ui.chooseProfession();
                            d.setProfession(newProf);
                            System.out.println("Profession updated successfully!");
                            break;

                        case 0: // Exit updating
                            continueUpdating = false;
                            System.out.println("Returning to previous menu...");
                            break;

                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }

                    // Update in ADT
                    doctorList.replace(i, d);
                }

                break; // profile found, exit loop
            }
        }
    }
    public void runDoctorModule() {
        String doctorID = ui.enterDoctorID(this);  
        int choice;
            do {
                choice = ui.showDoctorMenu();
                switch(choice){
            case 1:
                viewAppointment(doctorID);
                break;

            case 2:
                updateStatus(doctorID);
                break;

            case 3:
                updateProfile(doctorID); 
                break;
            }
        } while(choice != 0);
    }


}