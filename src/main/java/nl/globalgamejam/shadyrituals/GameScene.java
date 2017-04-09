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
package nl.globalgamejam.shadyrituals;

import nl.globalgamejam.shadyrituals.actors.*;
import nl.hvanderheijden.prutengine.*;
import nl.hvanderheijden.prutengine.core.math.PrutMath;
import nl.hvanderheijden.prutengine.core.math.Vector2;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import java.io.IOException;
import java.util.ArrayList;

public class GameScene extends Scene
{
    private final static Logger logger = LogManager.getLogger(GameScene.class.getName());
    private final ArrayList<Enemy> otherPlayers;

    public GameScene(){
        this.otherPlayers = new ArrayList<>();
    }
    
    @Override
    public void awake() throws PrutEngineException
    {
        Application.getInstance().getWindow().setWindowTitle("game");
        this.setCamera(new MainCamera(new Vector3<>(0f,0f,0f)));
        Player pl = new Player(this);

        ((MainCamera)this.camera).followObject(pl);
        
        this.addGameObject(new Background());
        this.addGameObject(pl);
        final Vector2<Integer> worldSize = SettingsManager.getInstance().getWorld_size();
        for(int i = 0; i < worldSize.x / 2; i++){
            this.addGameObject(new ChangeObject(new Vector3<>(PrutMath.random(-worldSize.x, worldSize.x),PrutMath.random(-worldSize.y, worldSize.y),-5f),this));
        }
        
        for(int i = 0; i < worldSize.x / 3; i++){
             this.addGameObject(new DeathWall(new Vector3<>(PrutMath.random(-worldSize.x, worldSize.x),PrutMath.random(-worldSize.y, worldSize.y),0f)));
        }
        
        try
        {
            AssetManager.loadSound("/Assets/Sounds/change01.wav","change");
            AssetManager.loadSound("/Assets/Sounds/death01.wav","death01");
         //   AssetManager.loadSound("Assets/Sounds/background.wav","bgm01");
        }
        catch(final IOException e)
        {
            logger.info("could not find sound ",e);
        }
       // AssetManager.getSound("bgm01").PlaySound(-1);
    }
    
    public Camera getCamera()
    {
        return this.camera;
    }
    
    public void shakeScreen(){
        ((MainCamera)this.camera).shakeScreen(1000f, 0.05f);
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException {
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_ESCAPE) == GLFW_PRESS){
            AssetManager.clearProgramsBuffer();
            AssetManager.clearShaderBuffer();
            Application.getInstance().loadScene(new MenuScene());
            return;
        }
        int index = 0;
        
        BaseConnection.getInstance().getAllConnections().stream().forEach((pl) -> {
            boolean inList = false;
            for(Enemy e : this.otherPlayers){
                if(e.getName().equals(pl.id)){
                    Vector3<Float> nPos= new Vector3<>(
                            PrutMath.lerp(e.getPosition().x, pl.currentPosition.x, 0.0001f * tpf),
                            PrutMath.lerp(e.getPosition().y, pl.currentPosition.y, 0.0001f * tpf),
                            pl.currentPosition.z
                    );
                    e.setPosition(pl.currentPosition);
                    e.setElementen(pl.playerElement);
                    inList = true;
                }
            }
            if (!inList) {
                if (!pl.id.equals(BaseConnection.getInstance().idName)) {
                    Enemy a = new Enemy(pl.currentPosition,pl.id,pl.playerElement);
                    otherPlayers.add(a);
                    this.addGameObject(a);
                }
            }
        });
        super.update(tpf);
    }
    
    @Override
    public void onQuit()
    {
        super.onQuit();
    }
}