package GGJ2016;

import Example.ExampleSplashScreen;
import PrutEngine.Application;


 
public class Main{

    public static void main(String[] args) {
        //Dirty hack :-(
        //Graphics.initializeGraphics(Graphics.GL_VERSION.GL_41);
        if(System.getProperty("os.name").equals("Mac OS X")){
            System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        }
        Application.getInstance().loadScene(new GameScene());
    }
 
}