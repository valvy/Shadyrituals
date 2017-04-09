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
import nl.hvanderheijden.prutengine.exceptions.InitException;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import nl.hvanderheijden.prutengine.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;


/**
 * The base class of each actor
 * 
 * @author Eddy 
 */
public class Actor extends CollideAble
{

    private final static Logger logger = LogManager.getLogger(Actor.class.getName());
    /**
     * The element it currently is
     */
    public enum Element{
        Sphere,
        Cube,
        Torus
    }
    
    /**
     * what is it
     */
    public Element currentElement;
    /**
     * Movement speed
     */
    protected final float speed = 250f;
    
    //Shader
    int time = -1;
    int resolution = -1;
    float timer = 0f;
    
    /**
     * initializes the actor with given startposition
     * Sets the rotation and starts as sphere 
     * @param startPos 
     */
    public Actor(Vector3<Float> startPos)
    {
        this.setPosition(startPos);
        this.rotate(new Vector3<>(1f,0f,0f), -90);
        setupElement(Element.Sphere);
        this.boundingBox = new Vector4<Float>(1f, 1f, 1f, 1f);
    }
    
    @Override
    public void onCollision(CollideAble collideWith) throws PrutEngineException
    {
        if(collideWith instanceof Actor)
        {
            Actor otherActor = (Actor)collideWith;
            switch(this.currentElement)
            {
                case Cube:
                    if(otherActor.currentElement == Element.Sphere)
                        Die();
                    break;
                case Torus:
                    if(otherActor.currentElement == Element.Cube)
                         Die();
                    break;
                case Sphere:
                    if(otherActor.currentElement == Element.Torus)
                         Die();
                    break;
            }
        }
    }
    /**
     * Initializes a random element
     */
    private void initElement()
    {
        changeRandomElement();
    }
    /**
     * Changes to a random element
     */
    protected void changeRandomElement()
    {
        Element chosenElement = this.currentElement;
        double random = Math.random() * 10;
        for(int i = 0; i < (int)random; i++)
        {
            switch(this.currentElement)
            {
                case Sphere:
                    chosenElement = Element.Cube;
                    break;
                case Cube:
                    chosenElement = Element.Torus;
                    break;
                case Torus:
                    chosenElement = Element.Sphere;
                    break;
            }
        }
        this.setupElement(chosenElement);
        AssetManager.getSound("change").PlaySound(0);
    }
    
    
    protected void changeElement()
    {
        switch(this.currentElement)
        {
            case Sphere:
                this.setupElement(Element.Cube);
                break;
            case Cube:
                this.setupElement(Element.Torus);
                break;
            case Torus:
                this.setupElement(Element.Sphere);
                break;
        }
        AssetManager.getSound("change").PlaySound(0);
    }
    /**
     * Kills of the actor
     */
    protected void Die() throws InitException, PrutEngineException {
        AssetManager.getSound("death01").PlaySound(0);
        respawnActor();
    }
    /**
     * Places the actor on a random place
     */
    public void respawnActor() throws InitException {
        final Vector2<Integer> worldSize = SettingsManager.getInstance().getWorld_size();
       this.setPosition(new Vector3<>(PrutMath.random(-worldSize.x, worldSize.x),PrutMath.random(-worldSize.y, worldSize.y), this.getPosition().z));
    }
    
    /**
     * Setups the element with it dedicated resources
     * @param element 
     */
    protected void setupElement(Element element){
        this.currentElement = element;
        switch(this.currentElement){
            case Sphere:
                this.initRenderer("Circle.png","playerShader.glsl");
                break;
            case Cube:
                this.initRenderer("Square.png","LightShowFragment.glsl");
                break;
            case Torus:
                this.initRenderer("Triangle.png","StarsFragment.glsl");
                break;
        }
    }
    
   
    
    /**
     * Initializes the rendered with given fragment shader and texture
     * @param texture
     * @param fragShader 
     */
    protected void initRenderer(String texture,String fragShader){
        try {
            this.setRenderer(new Renderer(
                "/Assets/Shaders/UnShadedVertex.glsl",
                "/Assets/Shaders/" + fragShader,
                "/Assets/Textures/" + texture,
                "/Assets/Meshes/Quad.obj"));
            time = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "time");
            resolution = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "resolution");
            glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
            glUniform2f(resolution,(int)Application.getInstance().getScreenSize().x,(int)Application.getInstance().getScreenSize().y);
        } catch (ResourceNotFoundException e) {
            logger.error(e);
        } catch (PrutEngineException e) {
            logger.error(e);
        }

    }
    

    @Override
    public void update(float tpf) throws PrutEngineException {
        timer += 10 * tpf;

        if(time != -1){
            glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
            glUniform1f(this.time,timer);
        }


        super.update(tpf);
    }
}