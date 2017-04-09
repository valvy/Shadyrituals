package nl.globalgamejam.shadyrituals;

import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.SettingsManager;
import nl.hvanderheijden.prutengine.exceptions.InitException;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;


public class Main{

    private final static Logger logger = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        System.out.println("Welcome to Shady ritual");
        System.out.println("To set the server set Server:<127.0.IP.adress>");
        
        for(String str : args){
            if(str.equals("about")){
                System.out.println("Created by Jeffrey Verbeek, Wander van der Wal");
                System.out.println("Heiko van der Heijden and Eddy Meivogel");
                return;
            }else if(str.startsWith("Server:")){
                System.out.println("Connecting to server : " + str.replace("Server:", ""));
                try {
                    SettingsManager.getInstance().setIp(str.replace("Server:",""));
                } catch (InitException e) {
                    logger.error(e);
                }
            }
        }
        
       
        if(System.getProperty("os.name").equals("Mac OS X")){
             System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        }
        try {
            Application.getInstance().loadScene(new SplashScreen());
        } catch (Exception e) {
            logger.error(e);
        }
    }
}