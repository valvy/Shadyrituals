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
package PrutEngine;

import GGJ2016.Actors.Actor;
import GGJ2016.Actors.CollideAble;
import GGJ2016.Actors.Enemy;
import GGJ2016.Actors.Player;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Core.View;
import java.util.ArrayList;

/**
 * An abstract Model to be used for all game logic.
 * @author Heiko van der Heijden
 */
public abstract class Scene {
    /**
     * The list with all the different gameobjects
     */
    private final ArrayList<GameObject> gameObjects;
    private final ArrayList<GameObject> toDestroy;
    
    public Scene(){
        this.gameObjects = new ArrayList<>();
        this.camera = new Camera(new Vector3<>(0f,0f,-4f));
        this.toDestroy = new ArrayList<>();
    }
    
    /**
     * The main camera
     */
    protected Camera camera;
    
    public void destroy(GameObject gameobject){
        GameObject obj = null;
        for(GameObject ga : this.gameObjects){
            if(gameobject == ga){
                
                obj = ga;
            }
        }
        if(obj != null){
            this.toDestroy.add(obj);
        }
    }
    

    
    public void setCamera(Camera cam){
        this.camera = cam;
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
        
        
        for(GameObject obj : this.gameObjects)
        {
            if(!(obj instanceof CollideAble)) continue;
            // Collision
            for(GameObject obj2 : this.gameObjects)
            {
                if(obj == obj2 || !(obj2 instanceof Actor)) continue;
                if(
<<<<<<< HEAD
                   ((((Actor)obj2).boundingBox.w <= ((Actor)obj).boundingBox.w && ((Actor)obj2).boundingBox.w >= ((Actor)obj).boundingBox.y)  ||
                    (((Actor)obj2).boundingBox.y <= ((Actor)obj).boundingBox.w && ((Actor)obj2).boundingBox.y >= ((Actor)obj).boundingBox.y)) &&
                   ((((Actor)obj2).boundingBox.x <= ((Actor)obj).boundingBox.x && ((Actor)obj2).boundingBox.x >= ((Actor)obj).boundingBox.z)  ||
                    (((Actor)obj2).boundingBox.z <= ((Actor)obj).boundingBox.x && ((Actor)obj2).boundingBox.z >= ((Actor)obj).boundingBox.z))
=======
                   ((((CollideAble)obj2).boundingBox.w < ((CollideAble)obj).boundingBox.w && ((CollideAble)obj2).boundingBox.w > ((CollideAble)obj).boundingBox.y)  ||
                    (((CollideAble)obj2).boundingBox.y < ((CollideAble)obj).boundingBox.w && ((CollideAble)obj2).boundingBox.y > ((CollideAble)obj).boundingBox.y)) &&
                   ((((CollideAble)obj2).boundingBox.x < ((CollideAble)obj).boundingBox.x && ((CollideAble)obj2).boundingBox.x > ((CollideAble)obj).boundingBox.z)  ||
                    (((CollideAble)obj2).boundingBox.z < ((CollideAble)obj).boundingBox.x && ((CollideAble)obj2).boundingBox.z > ((CollideAble)obj).boundingBox.z))
>>>>>>> 5da867401f364d3cfc6c802545f869564547fb41
                  )
                {
                    ((CollideAble)obj).onCollision((Actor)obj2);
                }
            }
        }
        for(GameObject des : this.toDestroy){
            des.destroy();
            this.gameObjects.remove(des);
        }
        this.toDestroy.clear();
        
        camera.update(tpf);
    }
  
    public void onQuit(){
        for(GameObject obj : this.gameObjects){
            obj.destroy();
        }
        this.gameObjects.clear();
    }
    
    public void updateCollision()
    {
        for(GameObject go : this.gameObjects)
        {
            if(go instanceof Actor)
                ((Actor)go).updateBoundingBox();
        }
        
    }
    
}
