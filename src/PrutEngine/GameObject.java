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

/**
 * An abstract representation a visible and invisible object in game.
 * @author heikovanderheijden
 */
public abstract class GameObject{
    private final Vector3<Float> position;
    private Renderer renderer;
    
    public GameObject(){
        this.renderer = null;
        this.position = new Vector3<>(0f,0f,0f);
    }
    
    public void translate(Vector3<Float> pos){
        this.position.x += pos.x;
        this.position.y += pos.y;
        this.position.z += pos.z;
        if(renderer != null){
            this.renderer.setPosition(this.position);
        }
        
    }
    
    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
        this.renderer.setPosition(this.position);
    }
    
    /**
     * Gets the position of the gameobject
     * @return 
     */
    public Vector3<Float> getPosition(){
        return new Vector3<>(this.position);
    }
    
    /**
     * Sets the position of the gameObject
     * @param nposition 
     */
    public void setPosition(final Vector3<Float> nposition){
        this.position.set(nposition);
        if(renderer != null){
            this.renderer.setPosition(this.position);
        }
    }
    
    public void draw(){
        if(this.renderer != null){
            this.renderer.render();
           
        }
    }
    
    /**
     * Updates the gameobject
     * @param tpf 
     */
    public abstract void update(float tpf);
    
    public void destroy(){
        if(this.renderer != null){
            this.renderer.destroy();
        }
    }
    
    
}
