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
package nl.globalgamejam.shadyrituals;

import nl.globalgamejam.shadyrituals.actors.Actor;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This is an interface to the game for the connection
 * Manages the connected players. (it's a singleton)
 * @author Heiko van der Heijden 
 */
public abstract class BaseConnection implements Runnable{
    /**
     * Called when the game wants to quit
     */
    private boolean shouldStop = false;
    /**
     * The instance of the singleton
     */
    private static BaseConnection instance;
    /**
     * Connection thread
     */
    private final Thread thread;
    /**
     * Default port to connect
     */
    protected final int PORT = 7000;
    /**
     * Called when te server has nothing to say
     */
    protected final String NOTHING = "Nothing";
    protected final String HANDSHAKE = "Player:";

    /**
     * The name that it receives from the server to identify with
     */
    protected String idName = "NULL";

    /**
     * The player that is currently conencted
     */
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
    
    /**
     * Asks the child for the connected players.
     * @return the list of players
     */
    public abstract ArrayList<ConnectedPlayer> getAllConnections();
    
    /**
     * Gives the child the player data for the other players
     * @param player 
     */
    public abstract void notifyWorld(ConnectedPlayer player);
    
    /**
     * Initializes the connection
     */
    protected BaseConnection(){
        this.thread = new Thread(this);
        this.thread.start();
    }
    
    /**
     * The getter for shouldstop
     * @return 
     */
    protected boolean shouldStop(){
        return this.shouldStop;
    }
    
    /**
     * gets the id name
     * @return 
     */
    public String getIdName()
    {
        return idName;
    }
    
    /**
     * Stops the connection and stops the connection thread
     */
    public void stopConnection(){
        this.shouldStop = true;
        try {
            this.stop();
            this.thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(BaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Stops the child
     */
    protected abstract void stop();
    
    @Override
    public void run() {
        if(this.attemptToConnect()){
            while(!shouldStop){
                this.connected();
            }
        }
    }
    
    /**
     * Attemps to connect. implementation depends on the child
     * @return 
     */
    public abstract boolean attemptToConnect();
    
    /**
     * Creates the singleton depending on what you want
     * @param host if true it wil set itself as server
     */
    public static void create(boolean host){
        if(host){
            instance = new ConnectionServer();
            instance.idName = instance.HANDSHAKE + "Server";
        }else{
            instance = new ConnectionClient();
        }
    }
    
    /**
     * Gets the instance
     * @return 
     */
    public static BaseConnection getInstance(){
        return instance;
    }
    /**
     * Do connection 
     */
    public  abstract void connected();
}