/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Core.Math.Vector3;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Heiko van der Heijden
 */
public final class ExampleObject extends GameObject {
    
    private final float speed = 1000f;
    
    public ExampleObject(){
        try {
            this.setRenderer(new Renderer("Assets/Shaders/UnshadedVertex.glsl", "Assets/Shaders/UnshadedFragment.glsl","Assets/Textures/cube.bmp", "Assets/Meshes/monkey.obj"));
        
        } catch (Exception ex) {
            Logger.getLogger(ExampleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setPosition(new Vector3<>(0f,0f,-10f));
        
       
    }
    @Override
    public void update(float tpf) {
  
        this.rotate(new Vector3<>(0f,1f,0f), speed * tpf);
       // this.setRotation(new Vector3<>(1f,0f,0f), tmp);
      //  Quaternion quat = Quaternion.rotateVector3(new Vector3<>(1f,1f,1f), this.getPosition(),tmp);
    //    this.setRotation(new Vector3<>(10f,0f,0f), speed * tpf);
    //   this.setRotation(quat);
      
          
     
       
       
       
    }
    
}
