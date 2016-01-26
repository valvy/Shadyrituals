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
package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Renderer;
import PrutEngine.Scene;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author Heiko van der Heijden
 */
public class ExampleSplashScreen extends Scene{

    @Override
    public void awake() {
        Application.getInstance().getWindow().setWindowTitle("Splash Scene");
        this.addGameObject(new Background());
    }
    @Override
    public void update(float tpf){
         super.update(tpf);
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_SPACE) == GLFW_PRESS){
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
    }
    
}
