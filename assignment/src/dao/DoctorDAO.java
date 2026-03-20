package dao;

import adt.ArrayList;
import adt.ListInterface;
import entity.Doctor;

public class DoctorDAO {
    private ListInterface<Doctor> doctorList;

    public DoctorDAO() {
        doctorList = new ArrayList<>();
        loadDummyData();
    }

    private void loadDummyData() {
        doctorList.add(new Doctor("D001", "Tang Le Yi", "0123456789", "Male"));
        doctorList.add(new Doctor("D002", "Lim Sim", "0129876543", "Female"));
        doctorList.add(new Doctor("D003", "Samuel Wong", "0131112233", "Male"));
    }

    public ListInterface<Doctor> getAllDoctors() {
        return doctorList;
    }
}