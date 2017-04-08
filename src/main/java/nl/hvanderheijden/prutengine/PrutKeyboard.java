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
package nl.hvanderheijden.prutengine;

import static org.lwjgl.glfw.GLFW.*;
import java.util.ArrayList;

public class PrutKeyboard
{
    private ArrayList<PrutKey> prutKeys = new ArrayList<PrutKey>();
    
    void PrutKeyboard(){}
    
    public void addKey(int keyCode)
    {
        prutKeys.add(new PrutKey(keyCode));
    }
    
    public void addKey(PrutKey prutKey)
    {
        prutKeys.add(prutKey);
    }
    
    public void changeState(int keyCode, int action)
    {
        PrutKey pressedKey = null;
        for (PrutKey prutKey : prutKeys)
        {
            if (prutKey.keyCode == keyCode) 
            {
                pressedKey = prutKey;
                break;
            }
        }
        if(pressedKey == null)
        {
            pressedKey = new PrutKey(keyCode);
            addKey(pressedKey);
        }
        pressedKey.action = action;
    }
    
    public int GetState(int keyCode)
    {
        for (PrutKey prutKey : prutKeys)
        {
            if (prutKey.keyCode == keyCode) 
            {
                int oldState = prutKey.action;
                if(prutKey.action == GLFW_RELEASE )
                    prutKey.action = -1;
                else if(prutKey.action == GLFW_PRESS)
                    prutKey.action = GLFW_REPEAT;
                return oldState;
            }
        }
        return -1;
    }
}