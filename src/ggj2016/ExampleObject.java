/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import static java.awt.SystemColor.window;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;


/**
 *
 * @author heikovanderheijden
 */
public final class ExampleObject extends GameObject {
    
    private final float speed = 100f;
    
    public ExampleObject(){
        try {
            this.setRenderer(new Renderer("Assets/Shaders/testV.glsl", "Assets/Shaders/testF.glsl","Assets/Textures/cube.bmp", "Assets/Meshes/monkey.obj"));
        
        } catch (Exception ex) {
            Logger.getLogger(ExampleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setPosition(new Vector3<>(0f,0f,-10f));
        
    }
    float tmp = 0;
    @Override
    public void update(float tpf) {
        tmp += speed * tpf;
       
        this.rotate(new Vector3<>(10f,10f,10f), tmp, Vector3.Orientation.Y);
         this.rotate(new Vector3<>(10f,10f,10f), tmp * 10, Vector3.Orientation.X);
      //   this.rotate(new Vector3<>(10f,10f,10f), tmp, Vector3.Orientation.Z);
       if(Application.getInstance().getKey(GLFW_KEY_RIGHT) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
               speed * tpf,
                0f,
                0f
        
            ));
       }
        if(Application.getInstance().getKey(GLFW_KEY_LEFT) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
               -speed * tpf,
                0f,
                0f
        
            ));
       }
       if(Application.getInstance().getKey(GLFW_KEY_UP) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
                0f,
                speed * tpf,
                0f
        
            ));
       } 
        if(Application.getInstance().getKey(GLFW_KEY_DOWN) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
                0f,
                -speed * tpf,
                0f
        
            ));
       } 
        
        if(Application.getInstance().getKey(GLFW_KEY_W) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
                0f,
                0f,
                speed * tpf
        
            ));
        }
            
        if(Application.getInstance().getKey(GLFW_KEY_S) == GLFW_PRESS){
            this.translate(new Vector3<Float>(
                0f,
                0f,
                -speed * tpf
        
            ));
       }
       
       
       
    }
    
}
