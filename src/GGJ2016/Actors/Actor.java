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
package GGJ2016.Actors;

import GGJ2016.GameScene;
import PrutEngine.AssetManager;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Core.Math.Vector4;
import PrutEngine.Debug;
import PrutEngine.GameObject;
import PrutEngine.Renderer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Actor extends CollideAble
{
    protected enum Element{
        Sphere,
        Cube,
        Torus
    }
    
    protected Element currentElement;
    
    protected final float speed = 800;
    
    public Actor(Vector3<Float> startPos)
    {
        this.setPosition(startPos);
        this.rotate(new Vector3<>(1f,0f,0f), -90);

        setupElement(Element.Sphere);

        this.boundingBox = new Vector4<Float>(1f, 1f, 1f, 1f);
    }
    @Override
    public void onCollision(CollideAble collideWith)
    {
        if((collideWith instanceof Actor))
        {
            Actor otherActor = (Actor)collideWith;
            switch(this.currentElement)
            {
                case Sphere:
                    if(otherActor.currentElement == Element.Cube)
                        Die();
                    break;
                case Cube:
                    if(otherActor.currentElement == Element.Torus)
                         Die();
                    break;
                case Torus:
                    if(otherActor.currentElement == Element.Sphere)
                         Die();
                    break;
            }
        }
    }
    protected void Die()
    {
        //Fix this
        AssetManager.getSound("death01").PlaySound(0);
        respawnActor(new Vector4(10,10,10,10));
    }
    public void respawnActor(Vector4 bounds)
    {
       Random r = new Random();
       position.y = (float)(r.nextInt((int)bounds.w + (int)bounds.y)-(int)bounds.y);
       position.x = (float)(r.nextInt((int)bounds.x + (int)bounds.z)-(int)bounds.z);
    }
    
    protected void setupElement(Element element){
        this.currentElement = element;
        switch(this.currentElement){
            case Sphere:
            this.initRenderer("Circle.png");
            return;
            case Cube:
            this.initRenderer("Square.png");
            return;
            case Torus:           
            this.initRenderer("Triangle.png");
            return;
        }
    }
    protected void initRenderer(String texture){
        try {
            this.setRenderer(new Renderer(
                "Assets/Shaders/UnShadedVertex.glsl",
                "Assets/Shaders/UnShadedFragment.glsl",
                "Assets/Textures/" + texture,
                "Assets/Meshes/Quad.obj")); 
        }
         catch(Exception e ){
                  
                 }
    }

    
    @Override
    public void update(float tpf) 
    {
        super.update(tpf);
    }
}