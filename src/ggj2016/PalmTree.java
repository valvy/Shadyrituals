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

import PrutEngine.Core.Math.Vector3;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heiko van der Heijden
 */
public class PalmTree extends GameObject{

    public PalmTree(Vector3<Float> position){
        this.setPosition(position);
        
        try {
            this.setRenderer(new Renderer("Assets/Shaders/testV.glsl", "Assets/Shaders/testF.glsl","Assets/Textures/PalmTree.png", "Assets/Meshes/PalmTree.obj"));
        
        } catch (Exception ex) {
            Logger.getLogger(ExampleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public void update(float tpf) {
        
    }
    
}
