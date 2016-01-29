/*
 * Copyright (c) 2016, Heiko van der Heijden 
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package Example;

import PrutEngine.Application;
import PrutEngine.AssetManager;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.GuiLabel;
import PrutEngine.Scene;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Heiko van der Heijden
 */
public class ExampleSplashScreen extends Scene{
    private GuiLabel label;
    @Override
    public void awake() {
        Application.getInstance().getWindow().setWindowTitle("Splash Scene");
       this.setCamera(new ExampleCamera(new Vector3<>(0f,0f,-10f)));
       this.addGameObject(new Background());
       this.addGameObject(new ExampleSprite(new Vector3<>(1f,0f,0f)));
       //this.addGameObject(new ExampleSprite(new Vector3<>(-2f,0f,0f)));
       
       try {
            label = new GuiLabel();
       } catch (IOException ex) {
            Logger.getLogger(ExampleSplashScreen.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    @Override
    public void update(float tpf){
         super.update(tpf);
         label.draw();
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_SPACE) == GLFW_PRESS){
             AssetManager.clearProgramsBuffer();
             AssetManager.clearShaderBuffer();
             Application.getInstance().loadScene(new ExampleScene());
         }
         //if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_ESCAPE) == GLFW_PRESS){
             Application.getInstance().quit();
         }
    }

    @Override
    public void onQuit() {
        super.onQuit();
        label.onDestroy();
    }
    
}
