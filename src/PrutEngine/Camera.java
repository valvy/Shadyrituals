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
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.Core.Math.Vector4;
import java.util.HashMap;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;

/**
 *
 * @author heikovanderheijden
 */
public class Camera extends GameObject{
    
    private void setProgramLocations(){
        for(int dat : AssetManager.allPrograms()){
            int pos = glGetUniformLocation(dat, "projection_matrix");
            Matrix4x4 mat = new Matrix4x4();
          //  mat.translate(this.getPosition());
            //mat = Matrix4x4.transpose(mat);
            
            mat = Matrix4x4.multiply(mat, this.getOrthographic(50, 700 / 700, 0.1f, 10000));
            glUniformMatrix4fv(pos,false,mat.getRawData());
        }
        
    }
    
    private Matrix4x4 getOrthographic(float fovy, float aspect, float near, float far){

        float q = (float) (1.0 / Math.tan(0.5 * fovy) * Math.PI / 180);
        float a = q / aspect;
        float b = (near + far) / (near - far);
        float c = (2.0f * near * far) / (near - far);
        
        return new Matrix4x4(
                new Vector4<>(a,0f,0f,0f),
                new Vector4<>(0f,q,0f,0f),
                new Vector4<>(0f,0f,b,-1f),
                new Vector4<>(0f,0f,c,0f));
                
        
    }
    
    public Camera(final Vector3<Float> position){
        super();
        
   
        this.setPosition(position);
    }
    
    @Override
    public void update(float tpf) {
      this.setProgramLocations();
    }
    
}
