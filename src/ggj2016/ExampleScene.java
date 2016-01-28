/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Application;
import PrutEngine.AssetManager;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Core.Math.Vector4;
import PrutEngine.Debug;
import PrutEngine.Scene;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_BACKSPACE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
//import org.lwjgl.opengl.
/**
 *
 * @author Heiko van der Heijden
 */
public class ExampleScene extends Scene{

    public ExampleScene(){
        Quaternion tmp = Quaternion.rotateVector3(new Vector3<>(2f,0f,3f), new Vector3<>(0f,0f,4f), 90);
       // tmp = Quaternion.rotate(tmp, new Vector3<>(0f,0f,-4f), 90);
        Debug.log(Quaternion.multiply(new Quaternion(new Vector4<>(9f,2f,3f,3f)), new Quaternion(new Vector4<>(4f,6f,3f,10f))));

    }
    
    @Override
    public void awake(){
        Application.getInstance().getWindow().setWindowTitle("ExampleScene");
        this.setCamera(new ExampleCamera(new Vector3<>(0f,0f,-4f)));
        this.addGameObject(new ExampleObject());
        for(int x = 0; x < 10; x++){
            for(int y = 1; y < 30; y++){
                 this.addGameObject(new PalmTree(new Vector3<>((float)-x * 3,0f,(float)-y * 3)));
            }
        }
       
        

    }
    

    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_BACKSPACE) == GLFW_PRESS){
                         AssetManager.clearProgramsBuffer();
             AssetManager.clearShaderBuffer(); 
            Application.getInstance().loadScene(new ExampleSplashScreen());
        }
    }

    @Override
    public void onQuit() {
        super.onQuit();
    }
    
}
