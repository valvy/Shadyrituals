/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Core.Data.Vector3;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author heikovanderheijden
 */
public final class ExampleObject extends GameObject {
    
    
    public ExampleObject(){
        try {
            this.setRenderer(new Renderer("Assets/Shaders/testV.glsl", "Assets/Shaders/testF.glsl","Assets/Textures/test", "Assets/Meshes/monkey.obj"));
        
        } catch (Exception ex) {
            Logger.getLogger(ExampleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setPosition(new Vector3<>(0f,0f,1.1f));
        
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
}
