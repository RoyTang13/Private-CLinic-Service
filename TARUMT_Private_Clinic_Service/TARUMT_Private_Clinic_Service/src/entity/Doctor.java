package entity;

import adt.*;

/**
 * @author Tang Le Yi
 */

public class Doctor {

    private String doctorID;
    private String doctorName;
    private String phone;
    private String profession;
    private String gender;
    
    private ListInterface<Appointment> appointmentList = new ArrayList<>();


    public Doctor(String doctorID, String doctorName, String phone, String profession, String gender) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.phone = phone;
        this.profession = profession;
        this.gender = gender;
    }
    public String getDoctorID() {
        return doctorID;
    }

     public void setDoctorName(String doctorName){
        this.doctorName = doctorName;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getProfession(){
        return profession;
    }

    public void setProfession(String profession){
        this.profession = profession;
    }
    
    public String getGender(){
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }
    
    public ListInterface<Appointment> getAppointmentList() {
        return appointmentList;
    }
    
    public String toString(){
        return String.format("ID: %s | Name: %s | Prof: %s | Gender: %s", doctorID, doctorName, profession, gender);
    }
}