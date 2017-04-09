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
package nl.globalgamejam.shadyrituals.actors;

import nl.hvanderheijden.prutengine.Application;
import nl.hvanderheijden.prutengine.AssetManager;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.GameObject;
import nl.hvanderheijden.prutengine.Renderer;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform2f;
import static org.lwjgl.opengl.GL20.glUseProgram;

/**
 * 
 * @author Wander
 */
public final class InfoBackground extends GameObject
{

    private final static Logger logger = LogManager.getLogger(InfoBackground.class.getName());
    private int time;
    private float timer = 0f;

    private static final String VERTEX_SHADER = "/Assets/Shaders/UnShadedVertex.glsl";

    private static final String FRAGMENT_SHADER = "/Assets/Shaders/LightShowFragment.glsl";

    private static final String TEXTURE = "/Assets/Textures/infoscreen.png";

    private static final String MESH = "/Assets/Meshes/Quad.obj";


    private InfoBackground(){
        throw new UnsupportedOperationException();
    }



    public InfoBackground(Vector3<Float> startPos)
    {
        initRenderer();
        this.setPosition(startPos);
        this.setSize(new Vector3<>(2f, 2f, 2f));
        this.rotate(new Vector3<>(1f, 0f, 0f), -90);
    }
    
    protected void initRenderer()
    {
        try {
            this.setRenderer(new Renderer(VERTEX_SHADER, FRAGMENT_SHADER, TEXTURE, MESH));
            time = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "time");
            int resolution = glGetUniformLocation(AssetManager.getProgram(this.getRenderer().getProgram()), "resolution");
            glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
            glUniform2f(resolution,(int)Application.getInstance().getScreenSize().x,(int)Application.getInstance().getScreenSize().y);

        }
        catch(PrutEngineException e ){
            logger.warn("Could not load Infobackground.. ", e);
        }
    }
    
    @Override
    public void update(float tpf)  throws PrutEngineException
    {
        timer += 10 * tpf;


       // this.rotate(new Vector3<>(0f,0f,100f), 100 * tpf);
        glUseProgram(AssetManager.getProgram(this.getRenderer().getProgram()));
        glUniform1f(this.time,timer);


    }
}