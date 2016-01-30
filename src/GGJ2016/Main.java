package GGJ2016;

import Example.ExampleSplashScreen;
import PrutEngine.Application;
import java.util.Scanner;


 
public class Main{

    public static void main(String[] args) {
        //Dirty hack :-(
        
        
        boolean isServer = false;

        
        try{
            System.out.println("Run as server?(true/false)");
            if(System.in.read() == 't'){isServer = true;}
        }
        catch(Exception e){}
        
        
        ConnectionController con = new ConnectionController(isServer);
        if(System.getProperty("os.name").equals("Mac OS X")){
            System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        }
        Application.getInstance().loadScene(new GameScene());
    }
 
}