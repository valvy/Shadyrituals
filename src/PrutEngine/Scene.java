/*
 * Copyright (c) 2016, heikovanderheijden
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
package PrutEngine;

import PrutEngine.Core.Data.Vector3;
import PrutEngine.Core.View;
import java.util.ArrayList;

/**
 * An abstract Model to be used for all game logic.
 * @author heikovanderheijden
 */
public abstract class Scene {
    /**
     * The list with all the different gameobjects
     */
    private final ArrayList<GameObject> gameObjects;
    /**
     * The main camera
     */
    protected final Camera camera;
    
    public Scene(){
        this.gameObjects = new ArrayList<>();
        this.camera = new Camera(new Vector3<>(0f,0f,-10f));
    }
    
    /**
     * Adds an gameobject in the scene
     * @param gameObject 
     */
    protected void addGameObject(GameObject gameObject){
        this.gameObjects.add(gameObject);
    }
    
    /**
     * Removes an GameObject in the scene
     * @param gameObject
     * @return 
     */
    protected boolean removeGameObject(GameObject gameObject){
        return this.gameObjects.remove(gameObject);
    }
    
    /**
     * Draws all the gameobjects
     * @param view 
     */
    public void draw(View view){
        view.draw(gameObjects);
    }
    
    /**
     * Called when everything is setup in the Application
     */
    public abstract void awake();
    
    /**
     * Called by every frame in the game
     * @param tpf 
     */
    public void update(float tpf){
        for(GameObject obj : this.gameObjects){
            obj.update(tpf);
        }
        camera.update(tpf);
    }
  
    public abstract void onQuit();
    
}
