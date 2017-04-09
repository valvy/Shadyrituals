/*
 * Copyright (c) 2016, quget
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
package nl.globalgamejam.shadyrituals.actors;

import nl.globalgamejam.shadyrituals.GameScene;
import nl.globalgamejam.shadyrituals.Globals;
import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.AssetManager;
import nl.hvanderheijden.prutengine.SettingsManager;
import nl.hvanderheijden.prutengine.core.math.PrutMath;
import nl.hvanderheijden.prutengine.core.math.Vector2;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.core.math.Vector4;
import nl.hvanderheijden.prutengine.Renderer;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * the gameobject (wizard/witch) that changes the players element
 * it despawn when collided with player
 * @author Eddy
 */
public class ChangeObject extends CollideAble
{

    private final static Logger logger = LogManager.getLogger(ChangeObject.class.getName());
    private final GameScene scene;
        //Shader
    int time = -1;
    int resolution = -1;
    float timer = 0f;
    public ChangeObject(Vector3<Float> startPos, GameScene scene)
    {
        this.scene = scene;
        this.setPosition(startPos);
        this.rotate(new Vector3<>(1f,0f,0f), -90);
        this.setSize(new Vector3<>(1.4f,1.4f,1.4f));
        this.boundingBox = new Vector4<Float>(1f, 1f, 1f, 1f);
        initRenderer();
    }
    
    public void initRenderer()
    {
        try {
            this.setRenderer(new Renderer(
                "/Assets/Shaders/UnShadedVertex.glsl",
                //"Assets/Shaders/UnShadedFragment.glsl",
                "/Assets/Shaders/GlowFragmentShader.glsl",
                "/Assets/Textures/Witch.png",
                "/Assets/Meshes/Quad.obj"));
            
            time = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "time");
            resolution = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "resolution");
            glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
            //glUniform2f(resolution,1280,800);
            glUniform2f(resolution,(int)Application.getInstance().getScreenSize().x,(int)Application.getInstance().getScreenSize().y);
        }
        catch (PrutEngineException ex) {
            logger.warn(ex);
        }
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException
    {
        timer += 10 * tpf;

            if(time != -1){
                //this.rotate(new Vector3<>(0f,0f,100f), 100 * tpf);
                glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
                glUniform1f(this.time,timer);
            }
            

        super.update(tpf);
    }
    
    @Override
    public void onCollision(CollideAble collideWith) throws PrutEngineException
    {
        if(collideWith instanceof Player)
        {
            Actor otherActor = (Actor)collideWith;
            switch(otherActor.currentElement)
            {
                case Sphere:
                    otherActor.setupElement(Actor.Element.Torus);
                    break;
                case Cube:
                    otherActor.setupElement(Actor.Element.Sphere);
                    break;
                case Torus:
                    otherActor.setupElement(Actor.Element.Cube);
                    break;
            }
        }

        final Vector2<Integer> worldSize = SettingsManager.getInstance().getWorld_size();
        this.setPosition(new Vector3<>(PrutMath.random(-worldSize.x, worldSize.x),PrutMath.random(-worldSize.y, worldSize.y),-5f));
        super.onCollision(collideWith);
    }
}