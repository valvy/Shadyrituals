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

import PrutEngine.Core.Data.Shader;
import PrutEngine.Core.Data.Vector3;
import PrutEngine.Core.Math.Matrix4x4;
import ggj2016.ExampleScene;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

/**
 *
 * @author heikovanderheijden
 */
public class Renderer {
    
    private final int program;
    private final int mesh;
    private final int texture;
    private int glPos;
    private final Vector3<Float> pos;
    public Renderer(final String vShader,final String fShader,final String texture, final String meshName) throws Exception{
        final HashMap<String, Shader.Type> dat = new HashMap<>();
        dat.put(vShader, Shader.Type.Vertex_Shader);
        dat.put(fShader, Shader.Type.Fragment_Shader);
        this.program = AssetManager.loadProgram(dat);
        
        
       this.texture = AssetManager.loadTexture(texture);
        this.mesh = AssetManager.loadMesh(meshName);
        this.glPos = glGetUniformLocation(AssetManager.getProgram(this.program), "mv_matrix");
        this.pos = new Vector3<>(0f,0f,0f);
    }
    
    public void setPosition(Vector3<Float> pos){        
        this.pos.set(pos);

    }
    
    public void render(){
        try {
            glUseProgram(AssetManager.getProgram(this.program));
            Matrix4x4 mat = new Matrix4x4();
            
            Matrix4x4 rotation = Matrix4x4.rotate(mat, 20, Vector3.Orientation.Z);
           // mat.translate(pos);
            mat = Matrix4x4.multiply(mat,rotation);
            glUniformMatrix4fv(this.glPos,true,mat.getRawData());
            //glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, AssetManager.getTexture(this.texture));
            glBindVertexArray(AssetManager.getMeshVao(this.mesh));
            glEnableVertexAttribArray(0);
            glDrawArrays(GL_TRIANGLES, 0, AssetManager.getMeshSize(this.mesh));
            glDisableVertexAttribArray(0);
            glBindVertexArray(0);
        } catch (AssetManager.AssetNotFoundException ex) {
            Logger.getLogger(ExampleScene.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void destroy(){
        AssetManager.removeMesh(this.mesh);
        AssetManager.removeProgram(this.program);
        AssetManager.removeTexture(this.texture);
    }
}
