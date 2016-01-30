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

import GGJ2016.BaseConnection;
import PrutEngine.Core.View;
import java.util.Date;
import java.util.HashMap;
import org.lwjgl.glfw.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

/**
 * The class that regulates all the input.
 * (It's a singleton) 
 * @author Heiko van der Heijden
 */
public final class Application {
    
    private static Application instance;
    public PrutKeyboard prutKeyBoard;
    private GLFWErrorCallback errorCallback;
    private GLFWKeyCallback   keyCallback;
    private long window;
    private boolean shouldStop = false;
    private Scene currentModel;
    private final View view;
    /**
     * Gets the instance of the application
     * if the instance does not exists, it makes a new instance
     * @return 
     */
    public static Application getInstance(){
        if(Application.instance == null){
            Application.instance = new Application();
        }
        return Application.instance;
    }
    
    
    
    private Application(){
        this.init();
        this.view = new View(this.window);
 
    }
    
    
    private void init(){
        
        prutKeyBoard = new PrutKeyboard();
        glfwSetErrorCallback(errorCallback = GLFWErrorCallback.createPrint(System.err));
 
        // Initialize GLFW. Most GLFW functions will not work before doing this.
        if ( glfwInit() != GLFW_TRUE )
            throw new IllegalStateException("Unable to initialize GLFW");
 
     
        this.setWindowConfiguration();
        int WIDTH = 700;
        int HEIGHT = 700;

        
        window = glfwCreateWindow(WIDTH, HEIGHT, "", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");
 
        
        
        // Setup a key callback. It will be called every time a key is pressed, repeated or released.
        glfwSetKeyCallback(window, keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
               // Debug.log(action);
                prutKeyBoard.changeState(key, action);
                
              //  if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
              //      glfwSetWindowShouldClose(window, GLFW_TRUE); // We will detect this in our rendering loop
            }
        });
 
        // Get the resolution of the primary monitor
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        // Center our window
        glfwSetWindowPos(
            window,
            (vidmode.width() - WIDTH) / 2,
            (vidmode.height() - HEIGHT) / 2
        );
 
        // Make the OpenGL context current
        glfwMakeContextCurrent(window);
        // Enable v-sync
        glfwSwapInterval(1);
 
        // Make the window visible
        glfwShowWindow(window);
    }
    
    private void setWindowConfiguration(){
     
        glfwDefaultWindowHints(); // optional, the current window hints are already the default
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
                    glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4); //setup opengl version 4
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1); 
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
    }
    
    private void destroy(){
        BaseConnection.getInstance().stopConnection();
        
        this.currentModel.onQuit();
        this.view.destroy();
        glfwSetWindowShouldClose(window, GLFW_TRUE);
    }
    
    public void quit(){
        this.shouldStop = true;
 
    }
    
    public View getWindow(){
        return this.view;
    }
    
    /**
     * Gets an key from the keyboard
     * @param key
     * @return glfw keycode
     */
    public int getKeyboardKey(int key){
        return glfwGetKey(window, key);
    }
    
    /**
     * Loads a scene new scene
     * This will call the Awake method and destroys the old scene
     * @param scene 
     */
    public void loadScene(Scene scene){
        if(scene == null){
            return;
        }
    
        scene.awake();
        if(this.currentModel!= null){
            this.currentModel.onQuit();
            this.currentModel = scene;
        }else{
            this.currentModel = scene;
            this.run();
        }
        //    System.gc();
    }    
    
    private Thread thread ;
    
    private void loop(){
         long lastTime = 0;
         while ( glfwWindowShouldClose(window) == GLFW_FALSE ) {
             Date date = new Date();
             long time = date.getTime();
             if(this.shouldStop){
                 this.destroy();
             }
             
             glfwSwapBuffers(window); // swap the color buffers
             if(this.currentModel != null){
                this.currentModel.draw(this.view);
                
                if(lastTime > 0){
                    float tmp = (float)lastTime / 10000;
                    this.currentModel.update(tmp);
                }
                 
                date = new Date();
                lastTime = date.getTime() - time;
                
                        
             }
             glfwPollEvents();
         }
    }
    
    private void run(){
        try{
            this.loop();
            if(this.currentModel != null){
                this.currentModel.onQuit();
            }
            AssetManager.clearShaderBuffer();
            AssetManager.clearProgramsBuffer();
            AssetManager.clearTextureBuffer();
            AssetManager.clearMeshBuffer();
            glfwDestroyWindow(window);
            keyCallback.release();
        }finally{
            glfwTerminate();
            errorCallback.release();
            
        }
    }
    
  
}
