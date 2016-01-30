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

import PrutEngine.Core.Math.Vector3;
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector4;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 *
 * @author Heiko van der Heijden
 */
public class Camera extends GameObject{
    private boolean needUpdate = true;
    private final Matrix4x4 projection;
    
        
    public Camera(final Vector3<Float> position){
        this.rotate(new Vector3<>(0f,1f,0f), 180);
        final float vovy = 50f, aspect = 1, near = 0.1f, far = 10000f; //Standard cam settings
        this.projection = this.perspective(vovy, aspect, near, far);
        this.setPosition(position);
    }
    
    
    
    private void setProgramLocations(){
        if(needUpdate){//Only update when the camera has moved.. this is a costly operation
            final Matrix4x4 perspective = Matrix4x4.multiply(Quaternion.quaternionToMatrix(this.getRotationQuaternion()),this.projection);
           
            final Matrix4x4 matPosition = Matrix4x4.identityMatrix();
            matPosition.translate(this.getPosition());
        //Set the projection
       
            for(int dat : AssetManager.allPrograms()){
                glUseProgram(dat);
                glUniformMatrix4fv( glGetUniformLocation(dat, "projection_matrix"),false,perspective.getRawData());
                glUniformMatrix4fv( glGetUniformLocation(dat, "cam_matrix"),true,matPosition.getRawData());
            }

            this.needUpdate = false;
        }
    }
    
    
    public void setPerspective(float vovy, float aspect, float near, float far){
        this.projection.set(this.perspective(vovy, aspect, near, far));
    }

    private Matrix4x4 perspective(float fovy, float aspect, float near, float far){
        float yscale = (float) (1.0 / Math.tan(0.5 * (fovy * (Math.PI / 180))));
        float xscale = yscale / aspect;

        float q = (near + far) / (near - far);
        float c = (2.0f * near * far) / (near - far);
        return new Matrix4x4(
                new Vector4<>(xscale,0f,0f,0f),
                new Vector4<>(0f,yscale,0f,0f),
                new Vector4<>(0f,0f,q,-1f),
                new Vector4<>(0f,0f,c ,0f));
        

    }

    
    @Override
    public void setSize(Vector3<Float> nSize){
        this.needUpdate = true; 
        super.setSize(nSize);
    }
   
    @Override
    public void setRotation (final Quaternion q){
        this.needUpdate = true;
        super.setRotation(q);
    }
    
    public void translate(Vector3<Float> pos, float speed){
        this.needUpdate = true;
        super.translate(pos, speed);
        
    }
    
    @Override
    public void rotate(final Vector3<Float> rot ,final float angle){
        this.needUpdate = true;
        super.rotate(rot, angle);
    }
    
    @Override
    public void setPosition(final Vector3<Float> nposition){
        this.needUpdate = true;
        super.setPosition(nposition);
    }

    @Override
    public void update(float tpf) {
        this.setProgramLocations();
    }    
}
