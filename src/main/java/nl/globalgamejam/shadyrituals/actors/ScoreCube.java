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

import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.GameObject;
import nl.hvanderheijden.prutengine.Renderer;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the score of the player.
 * Each cube represents one kill
 * @author Jeffrey Verbeek
 */
public final class ScoreCube extends GameObject
{
    private final static Logger logger = LogManager.getLogger(ScoreCube.class.getName());

    private static final String VERTEX_SHADER = "/Assets/Shaders/UnShadedVertex.glsl";

    private static final String FRAGMENT_SHADER = "/Assets/Shaders/UnShadedFragment.glsl";

    private static final String TEXTURE = "/Assets/Textures/cube.bmp";

    private static final String MESH = "/Assets/Meshes/cube.obj";


    private ScoreCube(){
        throw new UnsupportedOperationException();
    }

    public ScoreCube(Vector3<Float> startPos)
    {
        initRenderer();
        this.setPosition(startPos);
        this.setSize(new Vector3<>(0.25f, 0.25f, 0.25f));
    }
    
    protected void initRenderer()
    {
        try {
            this.setRenderer(new Renderer(VERTEX_SHADER,FRAGMENT_SHADER,TEXTURE,MESH));
        }
        catch(PrutEngineException e)
        {
            logger.warn("Could not load score cube..", e);
        }
    }
    
    @Override
    public void update(float tpf) 
    {
        
    }
}