package entity;

public class DocAppointment {
    private String appointmentID;
    private String doctorID;
    private String patientName;
    private String status;

    public DocAppointment(String appointmentID, String doctorID,String patientName, String status) {
        this.appointmentID = appointmentID;
        this.doctorID = doctorID;
        this.patientName = patientName;
        this.status = status;
    }

    public String getAppointmentID(){
        return appointmentID;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return appointmentID + " | " + patientName + " | " + status;
    }
}
