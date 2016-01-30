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
package GGJ2016;

import GGJ2016.Actors.Actor;
import PrutEngine.Core.Math.Vector3;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heiko van der Heijden
 */
public abstract class BaseConnection implements Runnable{
    private boolean shouldStop = false;
    private static BaseConnection instance;
    private final Thread thread;
    protected final int PORT = 7000;
    protected final String NOTHING = "Nothing";
    protected String idName = "NULL";
    public static class ConnectedPlayer{
        
        public String id;
        public Vector3<Float> currentPosition;
        public Actor.Element playerElement;
        public ConnectedPlayer(String id, Vector3<Float> currentPosition, Actor.Element playerElement){
            this.id = id;
            this.currentPosition = currentPosition;
            this.playerElement = playerElement;
        }
    }
    
    public abstract ArrayList<ConnectedPlayer> getAllConnections();
    
    public abstract void notifyWorld(ConnectedPlayer player);
    
    protected BaseConnection(){
        this.thread = new Thread(this);
        this.thread.start();
    }
    
    protected boolean shouldStop(){
        return this.shouldStop;
    }
    
    public String getIdName()
    {
        return idName;
    }
    
    public void stopConnection(){
        this.shouldStop = true;
        try {
            
            this.stop();
            this.thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(BaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    protected abstract void stop();
    
    @Override
    public void run() {
        if(this.attemptToConnect()){
            while(!shouldStop){
            
                this.connected();
            
            }
        }
    }
    
    public abstract boolean attemptToConnect();
    
    public static void create(boolean host){
        if(host){
            instance = new ConnectionServer();
        }else{
            instance = new ConnectionClient();
        }
    }
    
    public static BaseConnection getInstance(){
        return instance;
    }
    
    public  abstract void connected();
}
