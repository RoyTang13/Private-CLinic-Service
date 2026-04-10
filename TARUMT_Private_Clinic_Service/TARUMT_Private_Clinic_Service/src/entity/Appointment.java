/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author lee seng wai
 */
public class Appointment {
    private String appointmentID;
    private String patientID;
    private String patientName;
    private String doctorID;
    private String doctorName;
    private String doctorSpecialty;
    private String symptom;
    private String status;
    private String date;
    private String time;
    private String remarks;
    
    public Appointment(String appointmentID, String patientID, String patientName, String doctorID, String doctorName, String doctorSpecialty, String symptom, String status, String date, String time, String remarks) {
        this.appointmentID = appointmentID;
        this.patientID = patientID;
        this.patientName = patientName;
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.doctorSpecialty = doctorSpecialty;
        this.symptom = symptom;
        this.status = status;
        this.date = date;
        this.time = time;
        this.remarks = remarks;
    }
    
    public String getAppointmentID() {
        return appointmentID;
    }
    
    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }
    
    public String getPatientID() {
        return patientID;
    }
    
    public String getPatientName() {
        return patientName;
    }
    
    public String getDoctorID() {
        return doctorID;
    }
    
    public String getDoctorName() {
        return doctorName;
    }
    
    public String getDoctorSpecialty() {
        return doctorSpecialty;
    }
    
    public String getSymptom() {
        return symptom;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public String getTime() {
        return time;
    }
    
    public void setTime(String time) {
        this.time = time;
    }
    
    public String getRemarks() {
        return remarks;
    }
    
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    
    @Override
    public String toString() {
        return String.format("%-5s %-5s %-30s %-5s %-30s %-15s %-30s %-15s %-10s %-10s %-10s", appointmentID, patientID, patientName, doctorID, doctorName, doctorSpecialty, symptom, status, date, time, remarks);
    }
}
