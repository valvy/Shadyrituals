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
package PrutEngine.Core;

import PrutEngine.AssetManager;
import PrutEngine.Debug;
import java.nio.FloatBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;


/**
 *
 * @author Heiko van der Heijden
 */
public final class Graphics {
 
    public enum GL_VERSION{
        GL_41,
        GL_ES2
    };
    
    private static GL_VERSION currentVersion = GL_VERSION.GL_41;
    
    public static void initializeGraphics(GL_VERSION version){
        Graphics.currentVersion = version;
    }
    
    public static GL_VERSION getCurrentVersion(){
        return Graphics.currentVersion;
    }
    
    public static int GL_TRIANGLES(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_TRIANGLES;
            case GL_ES2:
                return GLES20.GL_TRIANGLES;
        }
        return -1;
    }
    
    public static void glDrawArrays(int mode, int first, int ref){
        switch(Graphics.currentVersion){
            case GL_41:
                GL11.glDrawArrays(mode, first, ref);
                return;
            case GL_ES2:
                GLES20.glDrawArrays(mode, first, ref);
                return;
        }
     }
    
    public static void glDisableVertexAttribArray(int index){
        switch(Graphics.currentVersion){
            case GL_41:
                GL20.glDisableVertexAttribArray(index);
                return;
            case GL_ES2:
                GLES20.glDisableVertexAttribArray(index);
                return;
        }
        
    }
    
    public static void glBindVertexArray(int index){
        switch(Graphics.currentVersion){
            case GL_41:
                GL30.glBindVertexArray(index);
                return;
            case GL_ES2:
                Debug.log("Not yet implemented function in es");
                return;
        }
    }
    
    public static void  glUseProgram(int index){
        switch(Graphics.currentVersion){
            case GL_41:
               // GL30.glBindVertexArray(index);
                GL20.glUseProgram(index);
                return;
            case GL_ES2:
                GLES20.glUseProgram(index);
                return;
        }
    }
    
    public static void glEnableVertexAttribArray(int index){
        switch(Graphics.currentVersion){
            case GL_41:
                GL20.glEnableVertexAttribArray(index);
                return;
            case GL_ES2:
                GLES20.glEnableVertexAttribArray(index);
                return;
        }
    }
    
    public static void glUniformMatrix4fv(int location, boolean transpose, FloatBuffer value){
        switch(Graphics.currentVersion){
            case GL_41:
               // GL30.glBindVertexArray(index);
                GL20.glUniformMatrix4fv(location, transpose, value);
                return;
            case GL_ES2:
                GLES20.glUniformMatrix4fv(location,transpose,value);
                return;
        }
    }
}
