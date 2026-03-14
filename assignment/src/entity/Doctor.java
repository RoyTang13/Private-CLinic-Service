package entity;

public class Doctor {

    private String doctorID;
    private String doctorName;
    private String phone;
    private String profession;


    public Doctor(String doctorID, String doctorName,String phone, String profession) {
        this.doctorID = doctorID;
        this.doctorName = doctorName;
        this.phone = phone;
        this.profession = profession;
    }
    public String getDoctorID() {
        return doctorID;
    }

     public void setDoctorName(String doctorName){
        this.doctorName = doctorName;
    }
    public String getDoctorName() {
        return "Dr. " + doctorName;
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

    public String toString(){
        return doctorID + " | " + doctorName + " | " +
               profession + " | " + phone;
    }
}