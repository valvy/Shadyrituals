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


import PrutEngine.Debug;
import java.io.IOException;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glShaderSource;

/**
 * Manages the various opengl shaders in the program
 * @author Heiko van der Heijden
 */
public final class Shader extends Resource {
    
    public enum Type{
        Vertex_Shader,
        Fragment_Shader
    }
    
    private final int shader;
    
    public Shader(final String fileLocation,final int position, final Type type) throws IOException {
        super(fileLocation, position);
        this.shader = loadShader(fileLocation, type);
    }
    
    /**
     * Get the opengl shader
     * @return 
     */
    public int getShader(){
        return this.shader;
    }
    
    /**
     * Loads an shader
     * @param fileLocation
     * @param type
     * @return
     * @throws IOException 
     */
    private int loadShader(final String fileLocation,final Type type) throws IOException{
        final String src = this.loadFile(fileLocation);
        int result = 0;
        switch(type){
            case Fragment_Shader:
               result = glCreateShader(GL_FRAGMENT_SHADER);
               break;
            case Vertex_Shader:
                result = glCreateShader(GL_VERTEX_SHADER);
                break;
        }
        
        glShaderSource(result,src);
        glCompileShader(result);
        if(glGetShaderi(result, GL_COMPILE_STATUS) == 0){
            Debug.log("Compile error in : " + fileLocation);
            Debug.log(glGetShaderInfoLog(result));
        }
        return result;
    }
    
    @Override
    public void destroy() {
        glDeleteShader(this.shader);
    }
}