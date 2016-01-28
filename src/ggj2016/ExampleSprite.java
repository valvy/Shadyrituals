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
package ggj2016;

import PrutEngine.Application;
import PrutEngine.Core.Math.Matrix4x4;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

/**
 *
 * @author Heiko van der Heijden
 */
public class ExampleSprite extends GameObject{
    public ExampleSprite(Vector3<Float> position){
        try {
            this.setRenderer(new Renderer("Assets/Shaders/UnshadedVertex.glsl", "Assets/Shaders/ToonFragment.glsl","Assets/Textures/cube.bmp", "Assets/Meshes/Quad.obj"));
        
        } catch (Exception ex) {
            Logger.getLogger(ExampleSprite.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.setSize(new Vector3<>(0.5f,0.5f,0.5f));
        this.rotate(new Vector3<>(1f,0f,0f), -90);
        this.setPosition(position);
    }
    

    @Override
    public void update(float tpf) {
        

           this.rotate(new Vector3<>(0f,0f,1f), 100f * tpf);

    }
    
}
