import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run(){

                // display our weather app gui
                new AppInterface().setVisible(true);

                // Here are some cod for make some tests after finalise each Class
                // Each code correspond to one class
//              System.out.println(AppInterface.getLocationData("Tokyo"));
//              System.out.println(AppFunctionality.getCurrentTime());
            }
        });
    }
}