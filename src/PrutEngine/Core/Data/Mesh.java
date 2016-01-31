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

package PrutEngine.Core.Data;

import PrutEngine.Core.Utilities.Primitives;
import PrutEngine.Core.Utilities.WaveFrontLoader;
import java.io.IOException;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

/**
 * Manages the data of a single 3d mesh
 * It loads all the data in opengl data
 * Such as an vertex array object.
 * @author Heiko van der Heijden
 */
public final class Mesh extends Resource{
    private final FloatBuffer rawVertexdata;
    private final FloatBuffer rawUVData;
    private final FloatBuffer rawNormalData;
    private final int vao;
    private final int size;
    private final int vertex_vbo;
    private final int uv_vbo;
    private final int normal_vbo;
    
    /**
     * @param fileLocation
     * @param position
     * @throws java.io.IOException
     */
    public Mesh(final String fileLocation,final int position) throws IOException {
        super(fileLocation, position);
        if(!fileLocation.equals("Cube")){
            final WaveFrontLoader loader = new WaveFrontLoader(fileLocation);
            this.size = loader.triangleAmount();
            this.vao = glGenVertexArrays();
            this.rawVertexdata = loader.rawVertexData();
            this.rawUVData = loader.rawUVData();
            this.rawNormalData = loader.rawNormalData();
            this.vertex_vbo = this.bindVBO(0, 3, rawVertexdata);
            this.uv_vbo = this.bindVBO(1, 2,rawUVData);
            this.normal_vbo = this.bindVBO(2, 3, rawNormalData);
        }else{
            this.vao = glGenVertexArrays();
            this.rawVertexdata =  Primitives.Cube.rawVertexData();
            this.rawUVData = Primitives.Cube.rawUVData();
            this.rawNormalData = null;
            this.vertex_vbo = this.bindVBO(0, 3, Primitives.Cube.rawVertexData());
            this.uv_vbo = this.bindVBO(1,2,Primitives.Cube.rawUVData());
            this.size = Primitives.Cube.triangleAmount();
            this.normal_vbo = 0;
        }
    }
    
    /**
     * Binds an vertex buffer object to it's position in a vertex array object (VAO)
     * @param position the position of array
     * @param amount   the size of an item (Vertex = 3) (UV = 2)
     * @param buffer   The actual data you wish to store
     * @return the vertex buffer object
     */
    private int bindVBO(int position, int amount, final FloatBuffer buffer){
        glBindVertexArray(vao);
        int res = glGenBuffers();
        glBindBuffer(GL15.GL_ARRAY_BUFFER,res);
        glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        glEnableVertexAttribArray(position);
        glVertexAttribPointer(position, amount, GL11.GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
        return res;
    }
    
    /**
     * Gets the vertex amount
     * @return 
     */
    public int getSize(){
        return this.size;
    }
    
    /**
     * Gets the vertex array object 
     * @return 
     */
    public int getVao(){
        return this.vao;
    }
    
    @Override
    public void destroy() {
        glDeleteVertexArrays(this.vao);
        glDeleteBuffers(this.vertex_vbo);
        glDeleteBuffers(this.uv_vbo);
    }
}