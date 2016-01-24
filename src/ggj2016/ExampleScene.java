/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import PrutEngine.Scene;
//import org.lwjgl.opengl.
/**
 *
 * @author Heiko van der Heijden
 */
public class ExampleScene extends Scene{

    public ExampleScene(){
        Debug.log(Quaternion.quaternionToMatrix(Quaternion.rotateVector3(new Vector3<>(0f,1f,-1f),new Vector3<>(1f,0f,0f),90f)));
    }
    
    @Override
    public void awake(){
        Application.getInstance().getWindow().setWindowTitle("ExampleScene");
        this.addGameObject(new ExampleObject());

    }
    

    
    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    @Override
    public void onQuit() {
   
    }
    
}
