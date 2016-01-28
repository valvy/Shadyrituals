package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.Math.Matrix4x4;

 
public class Main{

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        Application.getInstance().loadScene(new ExampleSplashScreen());
    }
 
}