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

import PrutEngine.*;
//import org.lwjgl.glfw.GLFW;
import static org.lwjgl.glfw.GLFW.*;
//import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import PrutEngine.Core.Math.Vector3;

 
/**
 *
 * @author quget
 */
public class Player extends Actor
{
    private final Scene gameScene;
    private final float speed = 10f;
    
    public Player(Scene gameScene){
        super(new Vector3<Float>(0f,0f,-10f), 0f, 0f);
        this.initRenderer("sphere.obj");
        this.gameScene = gameScene;
    }
    @Override
    public void update(float tpf) 
    {

        PlayerInput(tpf);

        super.update(tpf);
       
    }
    public void PlayerInput(float tpf)
    {
        Vector3 movePos = new Vector3(0f, 0f, 0f);
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_W) == GLFW_PRESS)
        {
            movePos.y = 1f;
        }
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_A) == GLFW_PRESS)
        {
            movePos.x = -1f;
        }      
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_S) == GLFW_PRESS)
        {
            movePos.y = -1f;
        }             
        if(Application.getInstance().getKeyboardKey(GLFW_KEY_D) == GLFW_PRESS)
        {
             movePos.x = 1f;
        }
        translate(movePos,speed * tpf);
    }
}
