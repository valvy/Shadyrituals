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
package GGJ2016.Actors;

import PrutEngine.AssetManager;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Background extends GameObject
{
    int time;
    float timer = 0f;
    
    public Background(){
        try {
            this.setRenderer(new Renderer(
                "Assets/Shaders/UnShadedVertex.glsl",
                "Assets/Shaders/UnShadedFragment.glsl",
                "Assets/Textures/SplashScreen.png",
                "Assets/Meshes/Quad.obj"    
            ));
           
           time = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "time");
        } catch (Exception ex) {
            Logger.getLogger(Example.Background.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSize(new Vector3<>(100f,100f,100f));
        this.setPosition(new Vector3<>(0f,0f,-11f));
        this.rotate(new Vector3<>(1f,0f,0f), -90);
    }
    
    @Override
    public void update(float tpf){
        timer += 1 * tpf;
        if(timer % 360 == 0){
            Debug.log("hai");
            timer = 0;
        }
        try{
            glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
            glUniform1f(this.time, (float) Math.sin(timer) * 100);
        }
        catch(AssetManager.AssetNotFoundException ex){
            System.out.println(ex);
        }
    }
}