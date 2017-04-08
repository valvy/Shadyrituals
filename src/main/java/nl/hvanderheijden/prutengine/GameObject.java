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
package nl.hvanderheijden.prutengine;

import nl.hvanderheijden.prutengine.core.math.Matrix4x4;
import nl.hvanderheijden.prutengine.core.math.Quaternion;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import nl.hvanderheijden.prutengine.exceptions.ResourceNotFoundException;

/**
 * An abstract representation a visible and invisible object in game.
 * @author Heiko van der Heijden
 */
public abstract class GameObject
{
    protected final Vector3<Float> position;
    private final Quaternion quaternion;
    protected final Vector3<Float> size;
    private Renderer renderer;
    
    public GameObject(){
        this.renderer = null;
        this.position = new Vector3<>(0f,0f,0f);
        this.quaternion = new Quaternion();
        this.size = new Vector3<>(1f,1f,1f);
    }
    
    public Vector3<Float> getSize(){
        return new Vector3<>(this.size);
    }

    public void setSize(Vector3<Float> nSize){
        this.size.set(nSize);
    }
    
    public void translate(Vector3<Float> pos, float speed){
        this.position.x += pos.x * speed;
        this.position.y += pos.y * speed;
        this.position.z += pos.z * speed;
    }
    
    /**
     * sets a new quaternion
     * @param q 
     */
    public void setRotation (final Quaternion q){
        this.quaternion.set(q);
    }
    
    /**
     * Rotates the gameobject relative to the quaternion
     * @param rot
     * @param angle 
     */
    public void rotate(final Vector3<Float> rot ,final float angle){
        this.quaternion.set(Quaternion.rotate(quaternion,rot,angle));
    }
    
    /**
     * Get the quaternion of the gameobject
     * @return 
     */
    public Quaternion getRotationQuaternion(){
        return new Quaternion(this.quaternion);
    }
    
    /**
     * Sets a new renderer
     * @param renderer 
     */
    public void setRenderer(Renderer renderer){
        this.renderer = renderer;
    }
     
    /**
     * Gets the position of the gameobject
     * @return 
     */
    public Vector3<Float> getPosition(){
        return new Vector3<>(this.position);
    }
    
    public Renderer getRenderer(){
        return this.renderer;
    }
    
    /**
     * Sets the position of the gameObject
     * @param nposition 
     */
    public void setPosition(final Vector3<Float> nposition){
        this.position.set(nposition);
    }
    
    /**
     * Draws the gameobject 
     */
    public void draw(){
        if(this.renderer != null){
            this.renderer.render(
                this.size,
                this.position,
                this.quaternion);
        }
    }
    
    /**
     * Updates the gameobject
     * @param tpf 
     */
    public abstract void update(float tpf) throws PrutEngineException;
    
    /**
     * Gets the forward position relative to the rotation
     * @return 
     */
    public Vector3<Float> forward(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(0f,0f,1f));
    }
    
    /**
     * Gets the back position relative to the rotation
     * @return 
     */
    public Vector3<Float> back(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(0f,0f,-1f));
    }
    
    /**
     * Gets the left position relative to the rotation
     * @return 
     */
    public Vector3<Float> left(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(1f,0f,0f));
    }
    
    /**
     * Gets the right position relative to the rotation
     * @return 
     */
    public Vector3<Float> right(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(-1f,0f,0f));
    }
    
    /**
     * Gets the up position relative to the rotation
     * @return 
     */
    public Vector3<Float> up(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(0f,-1f,0f));
    }
    
    /**
     * Gets the down position relative to the position
     * @return 
     */
    public Vector3<Float> down(){
        return Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.quaternion), new Vector3<>(0f,1f,0f));
    }
    
    /**
     * Cleans the resources that this gameobject uses
     */
    public void destroy(){
        if(this.renderer != null){
            this.renderer.destroy();
        }
    }
}