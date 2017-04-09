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

import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;

/**
 * Represents an connected player.
 * Can kill the player when it's a corresponding element
 * @author Heiko van der Heijden 
 */
public final class Enemy extends Actor
{
    private final String name;



    /**
     * Initializes the enemy with the corresponding variables
     * @param startPos The starting position
     * @param name The enemy name
     * @param elem The element the enemy is.
     */
    public Enemy(Vector3<Float> startPos, String name, Element elem)
    {
        super(startPos);
        this.name = name;
        this.currentElement = null;
        this.setSize(new Vector3<>(2f, 2f, 2f));
        this.setPosition(startPos);
    }
    
    public String getName(){
        return this.name;
    }
    
    public void setElementen(Element elem){
        
        if(this.currentElement != elem)
        {
            this.setupElement(elem);
            this.currentElement = elem;
        }
    }
    
    @Override
    public void update(float tpf) throws PrutEngineException {
        super.update(tpf);
    }
    
    @Override
    public void onCollision(CollideAble collideWith) throws PrutEngineException {
        super.onCollision(collideWith);
    }
}