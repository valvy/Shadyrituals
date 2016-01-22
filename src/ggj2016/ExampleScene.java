/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.View;
import PrutEngine.Scene;
import static java.lang.System.exit;
import static org.lwjgl.opengl.ARBImaging.GL_TABLE_TOO_LARGE;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_INVALID_ENUM;
import static org.lwjgl.opengl.GL11.GL_INVALID_OPERATION;
import static org.lwjgl.opengl.GL11.GL_INVALID_VALUE;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_OUT_OF_MEMORY;
import static org.lwjgl.opengl.GL11.GL_STACK_OVERFLOW;
import static org.lwjgl.opengl.GL11.GL_STACK_UNDERFLOW;
import static org.lwjgl.opengl.GL30.GL_INVALID_FRAMEBUFFER_OPERATION;
import static org.lwjgl.system.APIUtil.apiUnknownToken;
//import org.lwjgl.opengl.
/**
 *
 * @author heikovanderheijden
 */
public class ExampleScene extends Scene{

    public ExampleScene(){

    }
    
    @Override
    public void awake(){
        Application.getInstance().getWindow().setWindowTitle("ExampleScene");
        this.addGameObject(new ExampleObject());

    }
    

    
    @Override
    public void update(float tpf) {
        super.update(tpf);
        int error = GL11.glGetError();
     //   Debug.log(tpf);
        while(error != GL_NO_ERROR){
         //   System.out.println(getErrorString(error));
            exit(-1);
        }

    }

    @Override
    public void onQuit() {
   
    }
    
}
