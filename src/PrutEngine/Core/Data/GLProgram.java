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
import java.util.ArrayList;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glDeleteProgram;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;
import static org.lwjgl.opengl.GL20.glGetProgrami;
import static org.lwjgl.opengl.GL20.glGetProgramiv;
import static org.lwjgl.opengl.GL20.glLinkProgram;

/**
 * Manages the opengl program, 
 * making sure it is destroyed when it needs to be destroyed
 * @author Heiko van der Heijden
 */
public final class GLProgram extends Resource{
    
    private final int program;
    private final String errors;
  //  private final int position;
    
    public GLProgram(final String fileLocation, final int position, final ArrayList<Integer> shaders){
        super(fileLocation, position);
        this.program = glCreateProgram();
        shaders.stream().forEach((i) -> {
           glAttachShader(this.program, i);
        });
        glLinkProgram(this.program);
        this.errors = glGetProgramInfoLog(program, glGetProgrami(program, GL_INFO_LOG_LENGTH));
        if(this.containErrors()){
           if(glGetProgrami(program,GL20.GL_LINK_STATUS) == GL_FALSE){
                   Debug.log("Program : " + fileLocation + " could not be linked");
           }
            
        
            
            
            
            Debug.log(this.errors);
        }
    }

    
    
    public boolean containErrors(){
        return errors.length() > 0;
    }
    
    public String getError(){
        return errors;
    }
    
    public int getProgram(){
        return this.program;
    }
    
    
    @Override
    public void destroy() {
        glDeleteProgram(this.program);
    }
    
}
