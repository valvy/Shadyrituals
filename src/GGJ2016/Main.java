package GGJ2016;

import Example.ExampleSplashScreen;
import PrutEngine.Application;


 
public class Main{

    public static void main(String[] args) {
        //Dirty hack :-(
        ConnectionController con = new ConnectionController(true);
        if(System.getProperty("os.name").equals("Mac OS X")){
            System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        }
        Application.getInstance().loadScene(new GameScene());
   
       ;
    }
 
}