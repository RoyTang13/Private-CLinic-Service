package entity;

public class DocAppointment {
    private String appointmentID;
    private String doctorID;
    private String patientName;
    private String status;
    private String date;
    private String time;

    public DocAppointment(String appointmentID, String doctorID, String patientName, String status, String date, String time) {
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.patientName = patientName;
        this.status = status;
        this.date = date;
        this.time = time;
    }

    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
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

    @Override
    public String toString() {
        return "Appointment ID: " + appointmentID +
               " | Patient: " + patientName +
               " | Date: " + date +
               " | Time: " + time +
               " | Status: " + status;
    }
}