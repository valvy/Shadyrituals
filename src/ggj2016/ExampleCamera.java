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
import PrutEngine.Camera;
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 *
 * @author Heiko van der Heijden
 */
public final class ExampleCamera extends Camera{
    private final float speed =100f;
    private final float rotSpeed = 1000;

    public ExampleCamera(Vector3<Float> position) {
        super(position);
  
    }
    
    @Override
    public void update(float tpf){
        super.update(tpf);
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_RIGHT) == GLFW_PRESS){
            this.rotate(new Vector3<>(0f,1f,0f),-rotSpeed * tpf);
        }
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_LEFT) == GLFW_PRESS){

            this.rotate(new Vector3<Float>(0f,1f,0f), rotSpeed * tpf);
       }
       if(Application.getInstance().getKeyboardKey(GLFW_KEY_UP) == GLFW_PRESS){
            this.rotate(new Vector3<Float>(1f,0f,0f), rotSpeed * tpf);
        

       } 
       if(Application.getInstance().getKeyboardKey(GLFW_KEY_DOWN) == GLFW_PRESS){
    
              this.rotate(new Vector3<Float>(-1f,0f,0f), rotSpeed * tpf);
       } 
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_W) == GLFW_PRESS){
            // this.translate(Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.getRotationQuaternion()), new Vector3<>(0f,0f,1f)), speed * tpf);
            this.translate(this.forward(), speed * tpf);
       } 
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_S) == GLFW_PRESS){
                // this.translate(Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.getRotationQuaternion()), new Vector3<>(0f,0f,-1f)), speed * tpf);
             this.translate(this.back(), speed * tpf);
        } 
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_A) == GLFW_PRESS){
            // this.translate(Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.getRotationQuaternion()), new Vector3<>(0f,0f,1f)), speed * tpf);
            this.translate(this.left(), speed * tpf);
        } 
        
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_D) == GLFW_PRESS){
                // this.translate(Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.getRotationQuaternion()), new Vector3<>(0f,0f,-1f)), speed * tpf);
             this.translate(this.right(), speed * tpf);
        } 
        
        
    }
    
}
