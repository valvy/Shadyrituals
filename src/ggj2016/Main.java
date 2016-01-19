package ggj2016;

import PrutEngine.Application;

 
public class Main{

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");//Otherwise freezes on os x :-(
        
        Application.getInstance().loadScene(new ExampleScene());
    }
 
}