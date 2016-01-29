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
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MAJOR;
import static org.lwjgl.glfw.GLFW.GLFW_CONTEXT_VERSION_MINOR;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_CORE_PROFILE;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_ES_API;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_FORWARD_COMPAT;
import static org.lwjgl.glfw.GLFW.GLFW_OPENGL_PROFILE;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengles.GLES20;
import org.lwjgl.opengl.GL20;
import static org.lwjgl.opengl.GL20.GL_INFO_LOG_LENGTH;
import org.lwjgl.opengl.GL30;


/**
 *
 * @author Heiko van der Heijden
 */
public final class Graphics {

    public static void initGL(){
        switch(currentVersion){
            case GL_ES2:
            glfwWindowHint(GLFW_OPENGL_PROFILE,GLFW_OPENGL_ES_API);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR,2);
            return;
            case GL_41:
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); //setup opengl version 4
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1); 
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            return;
        }
    }
 
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
    
    public static void glBindTexture(int target,int texture){
        switch(currentVersion){
            case GL_41:
            GL11.glBindTexture(target, texture);
            return;
            case GL_ES2:
            GLES20.glBindTexture(target, texture);
            return;
        }
    }
    
    
    
    public static void glClearColor(float r, float g, float b, float a){
        switch(currentVersion){
            case GL_41:
            GL11.glClearColor(r, g, b, a);
            return;
            case GL_ES2:
            GLES20.glClearColor(r, g, b, a);
            return;
        }
    }
    
    public static void glCompileShader(int shader){
        switch(currentVersion){
            case GL_41:
            GL20.glCompileShader(shader);
            return;
            case GL_ES2:
            GLES20.glCompileShader(shader);
            return;
        }
        
    }
    
    public static void glDeleteProgram(int program){
        switch(currentVersion){
            case GL_41:
                GL20.glDeleteProgram(program);
                return;
            case GL_ES2:
                GLES20.glDeleteProgram(program);
                return;
        }
    }
    
    public static void glDeleteVertexArrays(int vao){
        switch(currentVersion){
            case GL_41:
            GL30.glDeleteVertexArrays(vao);
            return;
        }
    }
    
    public static void glDeleteTextures(int tex){
        switch(currentVersion){
            case GL_41:
            GL11.glDeleteTextures(tex);
            return;
            case GL_ES2:
            GLES20.glDeleteTextures(tex);
            return;
        }
    }
    
    public static void glTexImage2D(int target, int level, int internalformat, int width, int height, int border,int format,int type, ByteBuffer data){
        switch(currentVersion){
            case GL_41:
            GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, data);
            return;
            case GL_ES2:
            GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type, data);
            return;
                 
        }
    }
    
    public static void glTexParameteri(int target, int pname,int param){
        switch(currentVersion){
            case GL_41:
            GL11.glTexParameteri(target, pname, param);
            return;
            case GL_ES2:
            GLES20.glTexParameteri(target, pname, param);
            return;
        }
    }
    
    public static int GL_RGBA(){
        switch(currentVersion){
            case GL_41:
            return GL11.GL_RGBA;
            case GL_ES2:
            return GLES20.GL_RGBA;
        }
        return -1;
    }
    
    public static int GL_UNSIGNED_BYTE(){
        switch(currentVersion){
            case GL_41:
            return GL11.GL_UNSIGNED_BYTE;
            case GL_ES2:
            return GLES20.GL_UNSIGNED_BYTE;
        }
        return -1;
    }
    
    public static int GL_TEXTURE_WRAP_T(){
        switch(currentVersion){
            case GL_41:
            return GL11.GL_TEXTURE_WRAP_T;
            case GL_ES2:
            return GLES20.GL_TEXTURE_WRAP_T;
        }
        return -1;
    }
    
    public static int GL_CLAMP_TO_EDGE(){
        switch(currentVersion){
            case GL_41:
            return GL12.GL_CLAMP_TO_EDGE;
            case GL_ES2:
            return GLES20.GL_CLAMP_TO_EDGE;
        }
        return -1;
    }
    
    public static int glGenTextures(){
        switch(currentVersion){
            case GL_41:
            return GL11.glGenTextures();
            case GL_ES2:
            return GLES20.glGenTextures();
        }
        return -1;
    }
    
    public static void glDeleteBuffers(int vbo){
        switch(currentVersion){
            case GL_41:
            GL15.glDeleteBuffers(vbo);
            return;
            case GL_ES2:
            GLES20.glDeleteBuffers(vbo);
            return;
        }
    }
    
    public static int GL_STATIC_DRAW(){
        switch(currentVersion){
            case GL_41:
            return GL15.GL_STATIC_DRAW;
            case GL_ES2:
            return GLES20.GL_STATIC_DRAW;
        }
        return -1;
    }
    
    public static void glBufferData(int t, FloatBuffer buf, int d){
        switch(currentVersion){
            case GL_41:
            GL15.glBufferData(t, buf, d);
            return;
            case GL_ES2:
            GLES20.glBufferData(t, buf, d);
            return;
        }
    }
    
    public static void glVertexAttribPointer(int position, int amount, int fl, boolean b, int i, int i0) {
        switch(currentVersion){
            case GL_41:
                GL20.glVertexAttribPointer(position, amount , fl, b, i, i0);
                return;
            case GL_ES2:
                GLES20.glVertexAttribPointer(position,amount, fl, b, i, i0);
                return;
        }
    }
    
    public static void glBindBuffer(int type,int res){
        switch(currentVersion){
            case GL_41:
                GL15.glBindBuffer(type, res);
                return;
            case GL_ES2:
                GLES20.glBindBuffer(type, res);
                return;
        }
    }
    
    public static int GL_FLOAT(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_FLOAT;
            case GL_ES2:
                return GLES20.GL_FLOAT;
        }
        return -1;
    }
    
    public static int GL_ARRAY_BUFFER(){
        switch(currentVersion){
            case GL_41:
                return GL15.GL_ARRAY_BUFFER;
            case GL_ES2:
                return GLES20.GL_ARRAY_BUFFER;
        }
        return -1;
    }
    
    public static int glGenBuffers(){
        switch(currentVersion){
            case GL_41:
                return GL15.glGenBuffers();
            case GL_ES2:
                return GLES20.glGenBuffers();
        }
        return -1;
    }
    
    public static int glGenVertexArrays(){
        switch(currentVersion){
            case GL_41:
                return GL30.glGenVertexArrays();
        }
        return -1;
    }
    
    public static void glVertexAttribPointer(int index,int size,int type,boolean transpose, int stride, FloatBuffer data){
        switch(currentVersion){
            case GL_41:
            GL20.glVertexAttribPointer(index, size, type, transpose,stride , data);
            return;
            case GL_ES2:
            GLES20.glVertexAttribPointer(index, size, type, transpose, stride, data);
            return;
        }
    }
    

    
    public static void  glDeleteShader(int shader){
        switch(currentVersion){
            case GL_41:
                GL20.glDeleteShader(shader);
                return;
            case GL_ES2:
                GLES20.glDeleteShader(shader);
                return;
        }
    }
    
    public static int GL_LINK_STATUS(){
        switch(currentVersion){
            case GL_41:
                return GL20.GL_LINK_STATUS;
            case GL_ES2:
                return GLES20.GL_LINK_STATUS;
        }
        return -1;
    }
    
    public static int GL_FALSE(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_FALSE;
            case GL_ES2:
                return GLES20.GL_FALSE;
        }
        return -1;
    }
    
    public static String glGetProgramInfoLog(int program, int s){
        switch(currentVersion){
            case GL_41:
                return GL20.glGetProgramInfoLog(program,s);
            case GL_ES2:
                return GLES20.glGetProgramInfoLog(program, s);
        }
        return "";
    }
    
    public static int GL_INFO_LOG_LENGTH(){
        switch(currentVersion){
            case GL_41:
            return GL20.GL_INFO_LOG_LENGTH;
            case GL_ES2:
            return GLES20.GL_INFO_LOG_LENGTH;
        }
        return -1;
    }
    
    public static int glGetProgrami(int program, int name){
        switch(currentVersion){
            case GL_41:
            return GL20.glGetProgrami(program, name);
            case GL_ES2:
            return GLES20.glGetProgrami(program, name);
        }
        return -1;
    }
    
    public static void glLinkProgram(int program){
        switch(currentVersion){
            case GL_41:
            GL20.glLinkProgram(program);
            return;
            case GL_ES2:
            GLES20.glLinkProgram(program);
            return;
        }
    }
    
    public static int glCreateProgram(){
        switch(currentVersion){
            case GL_41:
            return GL20.glCreateProgram();
            case GL_ES2:
            return GLES20.glCreateProgram();
        }
        return -1;
    }
    
    public static void glAttachShader(int program,int shader){
        switch(currentVersion){
            case GL_41:
            GL20.glAttachShader(program, shader);
            return;
            case GL_ES2:
            GLES20.glAttachShader(program, shader);
            return;
        }
    }
    
    public static String glGetShaderInfoLog(int shader){
        switch(currentVersion){
            case GL_41:
            return GL20.glGetShaderInfoLog(shader);
            case GL_ES2:
            return GLES20.glGetShaderInfoLog(shader);
        }
        return "";
    }
    
    public static int GL_COMPILE_STATUS(){
        switch(currentVersion){
            case GL_41:
            return GL20.GL_COMPILE_STATUS;
            case GL_ES2:
            return GLES20.GL_COMPILE_STATUS;
        }
        return -1;
    }
    
    public static int glGetShaderi(int shader, int s){
        switch(currentVersion){
            case GL_41:
            return GL20.glGetShaderi(shader,s);
            case GL_ES2:
            return GLES20.glGetShaderi(shader, s);
        }
        return -1;
    }
    
    public static void glShaderSource(int s, String src){
        switch(currentVersion){
            case GL_41:
            GL20.glShaderSource(s, src);
            return;
            case GL_ES2:
            GLES20.glShaderSource(s, src);
            return;
        }
    }
    
    public static int GL_VERTEX_SHADER(){
        switch(currentVersion){
            case GL_41:
            return GL20.GL_VERTEX_SHADER;
            case GL_ES2:
            return GLES20.GL_VERTEX_SHADER;
        }
        return -1;
    }
    
    public static int GL_FRAGMENT_SHADER(){
        switch(currentVersion){
            case GL_41:
            return GL20.GL_FRAGMENT_SHADER;
            case GL_ES2:
            return GLES20.GL_FRAGMENT_SHADER;
        }
        return -1;
    }
    
    public static int glCreateShader(int t){
        switch(currentVersion){
            case GL_41:
                return GL20.glCreateShader(t);
            case GL_ES2:
                return GLES20.glCreateShader(t);
        }
        return -1;
    }
    
    public static int GL_NO_ERROR(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_NO_ERROR;
            case GL_ES2:
                return GLES20.GL_NO_ERROR;
                
        }
        return -1;
    }
    
    public static int glGetError(){
        switch(currentVersion){
            case GL_41:
                return GL11.glGetError();
            case GL_ES2:
                return GLES20.glGetError();
        }
        return -1;
    }
    
    public static void glClear(int c){
        switch(currentVersion){
            case GL_41:
                GL11.glClear(c);
                return;
            case GL_ES2:
                GLES20.glClear(c);
                return;
        }
    }
    
    public static int GL_DEPTH_BUFFER_BIT(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_DEPTH_BUFFER_BIT;
            case GL_ES2:
                return GLES20.GL_DEPTH_BUFFER_BIT;
        }
        return -1;
    }
    
    public static int GL_COLOR_BUFFER_BIT(){
        switch(currentVersion){
            case GL_41:
                return GL11.GL_COLOR_BUFFER_BIT;
            case GL_ES2:
                return GLES20.GL_COLOR_BUFFER_BIT;
               
        }
        return -1;
    }
    
    public static int GL_TEXTURE_2D(){
        switch(currentVersion){
            case GL_41:
            return GL11.GL_TEXTURE_2D;
            case GL_ES2:
            return GLES20.GL_TEXTURE_2D;
        }
        return -1;
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
