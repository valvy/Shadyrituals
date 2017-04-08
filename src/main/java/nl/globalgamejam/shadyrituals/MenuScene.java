/*
 * Copyright (c) 2016, wander
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
package nl.globalgamejam.shadyrituals;

import nl.globalgamejam.shadyrituals.actors.Arrow;
import nl.globalgamejam.shadyrituals.actors.MenuBackground;
import nl.hvanderheijden.prutengine.Scene;
import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.AssetManager;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

/**
 * Shows the menu
 * @author Wander
 */
public class MenuScene extends Scene {
    private int simpleState = 0;
    private int view = 0;
    private int up,down,space,quit = 1;
    private final Arrow cursor = new Arrow(new Vector3<>(-0.40f,0.9f,0f));

    private final MenuBackground background;

    public  MenuScene() throws PrutEngineException {
        this.background = new MenuBackground(new Vector3<>(0f,0f,0f));
    }

    @Override
    public void awake() {

        this.addGameObject(background);
        this.addGameObject(cursor);
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException {
        super.update(tpf);
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_W) == GLFW_PRESS | Application.getInstance().getKeyboardKey(GLFW_KEY_UP) == GLFW_PRESS  && simpleState > 0 && up == 0){
             simpleState--;
             up = 1;
             cursor.translate(new Vector3(0f,0.8f,0f), 1);
         }
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_S) == GLFW_PRESS | Application.getInstance().getKeyboardKey(GLFW_KEY_DOWN) == GLFW_PRESS  && simpleState < 2 && down == 0){
             simpleState++;
             down = 1;
             cursor.translate(new Vector3(0f,-0.8f,0f), 1);
         }
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_W) == GLFW_RELEASE && Application.getInstance().getKeyboardKey(GLFW_KEY_UP) == GLFW_RELEASE){
             up = 0;
         }
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_S) == GLFW_RELEASE && Application.getInstance().getKeyboardKey(GLFW_KEY_DOWN) == GLFW_RELEASE){
             down = 0;
         }
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_ESCAPE) == GLFW_PRESS && quit == 0)
             Application.getInstance().quit();
         
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_ESCAPE) == GLFW_RELEASE)
             quit = 0;
         
         if(Application.getInstance().getKeyboardKey(GLFW_KEY_ENTER) == GLFW_PRESS && space == 1||
            Application.getInstance().getKeyboardKey(GLFW_KEY_SPACE) == GLFW_PRESS && space == 1 ){
            switch(simpleState)
            {
                case 0:
                    if(view > 0)
                    {
                        BaseConnection.create(false);
                        LoadGame();
                        break; 
                    }
                    background.initRenderer("multyscreen.png");
                    view++;
                    space = 0;
                    break;
                case 1:
                    if(view > 0)
                    {
                        BaseConnection.create(true);
                        LoadGame();
                        break; 
                    }
                    AssetManager.clearProgramsBuffer();
                    AssetManager.clearShaderBuffer();
                    Application.getInstance().loadScene(new InfoScreen());
                    break;
                case 2:
                    if(view > 0)
                    {
                        background.initRenderer("menuscreen.png");
                        view--;
                        space=0; 
                        break;
                    }
                    Application.getInstance().quit();
                    break;
            }
        }
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_ENTER) == GLFW_RELEASE &&
           Application.getInstance().getKeyboardKey(GLFW_KEY_SPACE) == GLFW_RELEASE ){
            space = 1;
        }
    }
    
    private void LoadGame()
    {

        AssetManager.clearProgramsBuffer();
        AssetManager.clearShaderBuffer();
        Application.getInstance().loadScene(new GameScene());
    }
}