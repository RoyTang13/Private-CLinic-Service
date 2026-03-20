package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.DocAppointment;

public class DocAppointmentDAO {
    private ListInterface<DocAppointment> appointmentList;

    public DocAppointmentDAO() {
        appointmentList = new ArrayList<>();
        loadDummyData();
    }

    private void loadDummyData() {
        // Dummy data for appointments (d001)
        appointmentList.add(new DocAppointment("A001", "D001", "Ali Rahman", "Pending", "2026-03-21", "09:00 AM"));
        appointmentList.add(new DocAppointment("A002", "D001", "Siti Aisyah", "Approved", "2026-03-21", "10:30 AM"));
        appointmentList.add(new DocAppointment("A010", "D001", "Chong Wei", "Cancelled", "2026-03-25", "11:30 AM"));
        appointmentList.add(new DocAppointment("A007", "D001", "Daniel Lee", "Pending", "2026-03-24", "03:00 PM"));
        appointmentList.add(new DocAppointment("A011", "D001", "Farhan Aziz", "Pending", "2026-03-26", "09:00 AM"));
        appointmentList.add(new DocAppointment("A012", "D001", "Nur Sabrina", "Approved", "2026-03-26", "10:30 AM"));
        appointmentList.add(new DocAppointment("A013", "D001", "Jason Lim", "Completed", "2026-03-27", "11:00 AM"));
        appointmentList.add(new DocAppointment("A014", "D001", "Amira Sofea", "Cancelled", "2026-03-27", "02:00 PM"));
        appointmentList.add(new DocAppointment("A015", "D001", "Kevin Tan", "Pending", "2026-03-28", "03:30 PM"));
        appointmentList.add(new DocAppointment("A016", "D001", "Alicia Wong", "Approved", "2026-03-28", "04:30 PM"));
        appointmentList.add(new DocAppointment("A017", "D001", "Hafiz Rahman", "Completed", "2026-03-29", "09:15 AM"));
        appointmentList.add(new DocAppointment("A018", "D001", "Mei Yee", "Pending", "2026-03-29", "11:45 AM"));
        
        // Dummy data for appointments (d002)
        appointmentList.add(new DocAppointment("A003", "D002", "John Tan", "Pending", "2026-03-22", "11:00 AM"));
        appointmentList.add(new DocAppointment("A004", "D002", "Mei Ling", "Completed", "2026-03-22", "02:00 PM"));
        appointmentList.add(new DocAppointment("A005", "D003", "Raj Kumar", "Cancelled", "2026-03-23", "09:30 AM"));
        appointmentList.add(new DocAppointment("A006", "D003", "Nurul Huda", "Approved", "2026-03-23", "01:00 PM"));

        // Dummy data for appointments (d003)
        appointmentList.add(new DocAppointment("A008", "D002", "Aina Farah", "Completed", "2026-03-24", "04:30 PM"));
        appointmentList.add(new DocAppointment("A009", "D003", "Kavin Raj", "Approved", "2026-03-25", "10:00 AM"));

    }

    public ListInterface<DocAppointment> getAllAppointments() {
        return appointmentList;
    }
}