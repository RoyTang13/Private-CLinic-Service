package control;

import boundary.MainMenuUI;


public class Main {

    public static void main(String[] args) {

        MainMenuUI menu = new MainMenuUI();
        DoctorControl doctorModule = new DoctorControl();

        int choice;

        do{
            choice = menu.getMainChoice();

            switch(choice){

                case 1:
                    System.out.println("Patient module not ready.");
                    break;

                case 2:
                    doctorModule.runDoctorModule();
                    break;

                case 3:
                    System.out.println("Admin module not ready.");
                    break;

                case 0:
                    System.out.println("Thank you for using system.");
                    break;

                default:
                    System.out.println("Invalid choice.");
            }

        }while(choice != 0);

    }
}