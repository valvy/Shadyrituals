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

import GGJ2016.BaseConnection;
import GGJ2016.BaseConnection.ConnectedPlayer;
import GGJ2016.GameScene;
import PrutEngine.*;

import static org.lwjgl.glfw.GLFW.*;

import PrutEngine.Core.Math.Vector3;
import java.util.ArrayList;

public class Player extends Actor
{
    private final GameScene gameScene;
    private ArrayList<ScoreCube> scoreCubes = new ArrayList<ScoreCube>();
    private float scoreCubeXStep = 1.5f;
    private float scoreCubeX;
    private float changeTimer;
    private Vector3 lastPos = new Vector3(0f,0f,0f);
    private Element lastElement;
    public Player(GameScene gameScene)
    {
        super(new Vector3<Float>(0f,0f,-10f));
        this.setSize(new Vector3<Float>(2f, 2f, 2f));
        this.gameScene = gameScene;
        changeTimer = (float)Math.random() * 10f + 1f;
    }
    
    public void AddScore()
    {
        ScoreCube scoreCube = new ScoreCube(new Vector3<Float>(scoreCubeX,0f,0f));
        scoreCubes.add(scoreCube);
        gameScene.addGameObjectRealTime(scoreCube);
        scoreCubeX += scoreCubeXStep;
    }
    
    public void ResetScore()
    {
        for(int i = 0; i < scoreCubes.size(); i++)
        {
            gameScene.destroy(scoreCubes.get(i));
        }
        scoreCubes.removeAll(scoreCubes);
        scoreCubeX = 0;
    }
    
    @Override
    public void update(float tpf) 
    {
        //Notify the fellow players
    
        if(!lastPos.equals(this.getPosition()) || lastElement != this.currentElement)
        {
           //Debug.log(lastPos);
            BaseConnection.getInstance().notifyWorld(
                        new ConnectedPlayer(
                            "Eddy",
                            this.getPosition(),
                            this.currentElement));
            lastPos = this.getPosition();
            lastElement = this.currentElement;
        }  
        Vector3<Float> nPos = new Vector3<>(this.getPosition());
        if(this.getPosition().x > 100){
            nPos.x = 100f;
        }
        if(this.getPosition().x < -100){
            nPos.x = -100f;
        }

        if(this.getPosition().y > 100){
            nPos.y = 100f;
        }
        if(this.getPosition().y < -100){
            nPos.y = -100f;
        }

            this.setPosition(nPos);
        int lineCountY = 0;
        int lineCountX = 1;
        for(int i = 0; i < scoreCubes.size(); i++)
        {
            Vector3<Float> cubePosition = new Vector3(0f,0f,0f);
            if(i % 17 == 0)
            {
                lineCountY ++;
                lineCountX = 0;
            }
            cubePosition.x = (-gameScene.getCamera().getPosition().x - 12f) + (lineCountX * scoreCubeXStep);
            cubePosition.y = -gameScene.getCamera().getPosition().y + 14 - ( scoreCubeXStep * lineCountY);// * lineCount);
            lineCountX ++;
            scoreCubes.get(i).setPosition(cubePosition);
        }

        PlayerInput(tpf);
        super.update(tpf);
    }
    public void PlayerInput(float tpf)
    {
        Vector3 movePos = new Vector3(0f, 0f, 0f);
        int moveKeyCount = 0;

        if(Application.getInstance().prutKeyBoard.GetState(GLFW_KEY_W) == GLFW_REPEAT)
        {
            movePos.y = 1f;
        }
        if(Application.getInstance().prutKeyBoard.GetState(GLFW_KEY_A) == GLFW_REPEAT)
        {
            movePos.x = -1f;
        }      
        if(Application.getInstance().prutKeyBoard.GetState(GLFW_KEY_S) == GLFW_REPEAT)
        {
            movePos.y = -1f;
        }  
        if(Application.getInstance().prutKeyBoard.GetState(GLFW_KEY_D) == GLFW_REPEAT)
        {
            movePos.x = 1f;
        }
        if(Application.getInstance().prutKeyBoard.GetState(GLFW_KEY_F) == GLFW_PRESS)
        {
            changeRandomElement();
        }
        translate(movePos,speed * tpf);
    }
    
    @Override
    public void onCollision(CollideAble collideWith)
    {
        if(collideWith instanceof Actor)
        {
            Actor otherActor = (Actor)collideWith;
            switch(this.currentElement)
            {
                case Sphere:
                    if(otherActor.currentElement == Element.Cube)
                       AddScore();
                    break;
                case Torus:
                    if(otherActor.currentElement == Element.Sphere)
                       AddScore();
                    break;
                case Cube:
                    if(otherActor.currentElement == Element.Torus)
                       AddScore();
                    break;
            }
        }
        super.onCollision(collideWith);
    }
    
    @Override
    protected void Die()
    {
        super.Die();
        ResetScore();
        ((GameScene)this.gameScene).shakeScreen(100, 0.01f);
    }
}