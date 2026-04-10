package entity;

import adt.ArrayList;
import adt.ListInterface;
import entity.Appointment;

/**
 * 
 * @author lee seng wai
 */

public class Patient {

    private String patientID;
    private String patientName;
    private String patientGender;
    private String patientIC;
    private String patientContactNumber;
    private ListInterface<Appointment> appointmentList = new ArrayList<>();

    public ListInterface<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public Patient(String patientID, String patientName, String patientGender, String patientIC, String patientContactNumber) {
        this.patientID = patientID;
        this.patientName = patientName;
        this.patientGender = patientGender;
        this.patientIC = patientIC;
        this.patientContactNumber = patientContactNumber;
    }
    
    public String getPatientID() {
        return patientID;
    }

    public void setPatientID(String patientID) {
        this.patientID = patientID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientGender() {
        return patientGender;
    }

    public void setPatientGender(String patientGender) {
        this.patientGender = patientGender;
    }

    public String getPatientIC() {
        return patientIC;
    }

    public void setPatientIC(String patientIC) {
        this.patientIC = patientIC;
    }

    public String getPatientContactNumber() {
        return patientContactNumber;
    }

    public void setPatientContactNumber(String patientContactNumber) {
        this.patientContactNumber = patientContactNumber;
    }

    public boolean compareTo(Patient otherPatient) {
        if (this.patientID.equalsIgnoreCase(otherPatient.patientID) || this.patientName.equalsIgnoreCase(otherPatient.patientName)) {
            return true;
        } else {
            return false;
        }
    }
    

    @Override
    public String toString() {
        return String.format("%-5s %-30s %-10s %-20s %-20s", patientID, patientName, patientGender, patientIC, patientContactNumber);
    }
}
