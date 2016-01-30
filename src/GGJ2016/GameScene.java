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
package GGJ2016;

import GGJ2016.Actors.*;
import PrutEngine.Application;
import PrutEngine.AssetManager;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Scene;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_9;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;

/**
 *
 * @author Heiko van der Heijden
 */
public class GameScene extends Scene{
    
    
    @Override
    public void awake() {

         Application.getInstance().getWindow().setWindowTitle("game");
         this.setCamera(new MainCamera(new Vector3<>(0f,0f,0f)));
         Player pl = new Player(this);
         ((MainCamera)this.camera).followObject(pl);
         
         this.addGameObject(new Background());
         this.addGameObject(new Enemy(new Vector3<>(-5f,-1f,-10f)));
         this.addGameObject(new Enemy(new Vector3<>(5f,-1f,-10f)));
         this.addGameObject(pl);

         this.addGameObject(new Actor(new Vector3<>(0f,3f,-10f)));
         this.addGameObject(new ChangeObject(new Vector3<>(0f,3f,-5f),this));

         
         
        try
        {
            AssetManager.loadSound("Assets/Sounds/change01.wav","change");
            AssetManager.loadSound("Assets/Sounds/mmmm.wav","mmm");
            AssetManager.loadSound("Assets/Sounds/death01.wav","death01");
            AssetManager.loadSound("Assets/Sounds/wanderMusic.wav","bgm01");
        }
        catch(Exception e)
        {
            System.err.println(e.getMessage());
        }
        AssetManager.getSound("bgm01").PlaySound(-1);

    }
    
    public void shakeScreen(float magnitude, float duration){
        ((MainCamera)this.camera).shakeScreen(1000f, 0.05f);
    }
    
    @Override
    public void update(float tpf){
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_ESCAPE) == GLFW_PRESS){
             Application.getInstance().quit();
             return;
         }


        
        super.update(tpf);
    }
    
    @Override
    public void onQuit(){
        ConnectionController.getInstance().stopConnection();
        super.onQuit();
    }
}