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
import nl.hvanderheijden.prutengine.Debug;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Manages the server, and notifies all the clients connected to the server
 * @author Heiko van der Heijden 
 */
public class ConnectionServer extends BaseConnection {
    private final static Logger logger = LogManager.getLogger(ConnectionServer.class.getName());

    /**
     * An unique number to identify the clients
     */
    private int uniqueNumber = 0;

    /**
     * The buffer containing all the data from the connected players
     * send to the connected players
     */
    private final List<String> globalBuffer;
    
    protected ConnectionServer(){
        this.globalBuffer = new ArrayList<>();
    }

    /**
     * An connected client
     * Manages it's own thread and loop
     */
    private class Client implements Runnable{
        /**
         * The id of a client
         */
        private final int id;
        /**
         * The connection socket
         */
        private final Socket sock;
        /**
         * the thread of the client
         */
        private final Thread thread;
        
        /**
         * if true the client will close itself
         */
        private boolean shouldStop = false;
        /**
         * the mutex lock to avoid data races
         */
        private final Mutex mutex;
        
        /**
         * the data what it did last time
         */
        private String from = NOTHING;
        
        /**
         * The buffer that it should send
         */
        private final List<String> to;
        
        
        /**
         * Gets the the data it should get
         * @return 
         */
        public String getFrom(){
            String dat = NOTHING;
            try{
                mutex.acquire();
                dat = from;
            }
            catch (InterruptedException ex) {
                logger.warn(ex);
            }finally{
                mutex.release();
            }
            return dat;
        }
        
        /**
         * Adds it on the buffer to send to the dedicated player
         * @param msg 
         */
        public void addToBuffer(String msg){
            try {
                mutex.acquire();
                this.to.add(msg);
            } catch (InterruptedException ex) {
                logger.warn(ex);
            }finally{
                mutex.release();
            }
        }
        
        /**
         * initializes the client with specified id and socket
         * @param id the player id
         * @param sock the socket of the player
         * @throws Exception 
         */
        public Client(int id, Socket sock){
            this.id =id;
            this.sock = sock;
            this.to = new ArrayList<>();
            this.mutex = new Mutex();
            this.thread = new Thread(this);
            this.thread.start();
        }
        
        /**
         * Stops the connection 
         * And joins the connection thread
         */
        public void stopConnection(){
            shouldStop = true;
            try {
                thread.join();
            } catch (InterruptedException ex) {
                logger.warn(ex);
            }
        }
        
        @Override
        public void run() {
            try {
                DataInputStream inputStream = new DataInputStream(sock.getInputStream());
                BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));

                int read;
                byte[] buffer = new byte[1024];
                this.send(HANDSHAKE + Integer.toString(this.id), bw);
                while((read = inputStream.read(buffer)) != -1){
                    if(shouldStop){
                        inputStream.close();
                        bw.close();
                        this.sock.close();
                        break;
                    }
                    try {
                        this.mutex.acquire();
                        String msg = new String(buffer, 0, read);
                        if(!msg.equals(NOTHING)){
                            this.from = msg;
                        }
                    } catch (InterruptedException ex) {
                        logger.warn(ex);
                    }finally{
                        String dat = NOTHING;
                        if(this.to.size() > 0){
                            dat = to.get(0);
                            //Debug.log(dat);
                            this.to.remove(0);
                        }
                        this.mutex.release();
                        this.send(dat, bw);                           
                    }
                }
            } catch (IOException ex) {
                logger.warn(ex);
            }
        }
        
        /**
         * Sends an message to the client
         * @param msg
         * @param bw 
         */
        public void send(String msg, BufferedWriter bw){
            try {
                bw.write(msg);
                bw.flush();
            }   catch (IOException ex) {
                logger.warn(ex);
            }
        }
    }
    
    /**
     * The list containing all the clients
     */
    private List<Client> clients;
    
    /**
     * The socket of the server
     */
    private ServerSocket serverSocket;
    
    @Override
    protected void stop() {
        if(this.clients != null){
            for(Client cl : this.clients){
                cl.stopConnection();
            }
        }
    }

    @Override
    public void connected() {
        if(this.clients == null){
            this.clients = new ArrayList<>();
        }
        try {
           clients.add(new Client(this.uniqueNumber, serverSocket.accept()));
            this.uniqueNumber++;
        } catch (Exception ex) {
            logger.warn(ex);
        }
    }
    
    @Override
    public List<ConnectedPlayer> getAllConnections() {
        //create a temporary list with all the players 
        final List<ConnectedPlayer> result = new ArrayList<>();
        
        //check the buffer for data
        for(String str : this.globalBuffer){
            if(str.equals(NOTHING)){//do nothing.. the buffer is empty
                return result;
            }
            //split the data with semicolon
            String[] splitedData =  str.split(";");
            
            //the data is invalid :-(
            if(splitedData.length == 1){
                continue;
            }
            String id = splitedData[0]; //parse it to an vector3
            final Vector3<Float> currentPosition = new Vector3<>(0f,0f,0f);
            try{
                final Scanner fi = new Scanner(splitedData[1]);
                Debug.log(str);
                currentPosition.x = fi.nextFloat();
                currentPosition.y = fi.nextFloat();
                currentPosition.z = fi.nextFloat();
            }catch(java.util.InputMismatchException ex){
                logger.info(ex);
                continue;
            }
          
            //set the type of the actor
            Actor.Element playerElement = Actor.Element.Cube;
            String playerElementString = splitedData[2];
            if(playerElementString.equals(Actor.Element.Cube.toString()))
            {
                playerElement = Actor.Element.Cube;
            }
            else if(playerElementString.equals(Actor.Element.Sphere.toString()))
            {
                 playerElement = Actor.Element.Sphere;
            }
            else if(playerElementString.equals(Actor.Element.Torus.toString()))
            {
                 playerElement = Actor.Element.Torus;
            }
            ConnectedPlayer player = new ConnectedPlayer(id, currentPosition, playerElement);
            result.add(player);
        }
        //The globalbuffer is interpreted.
        this.globalBuffer.clear();
        return result;
    }

    @Override
    public void notifyWorld(ConnectedPlayer player) {
        if(clients == null){//nothing todo
            return;
        }
        ArrayList<String> localBuffer = new ArrayList<>();
        this.globalBuffer.clear();
        //foreach client add it to the buffer
        for(Client cl : clients){
            //Pass the server data
            String tmp = player.id + ";" + player.currentPosition.toString() + ";" + player.playerElement.toString() + ";";
            cl.addToBuffer(
                    tmp
            );
            this.globalBuffer.add(tmp);
            localBuffer.add(cl.getFrom());
        }
        
        //send everything to everybody
        for(Client cl : clients){
            for(String str : localBuffer){
                cl.addToBuffer(str);
                Debug.log(str);
            }
        }
        
        for(String tr : localBuffer){
            this.globalBuffer.add(tr);
        }
    }

    @Override
    public boolean attemptToConnect() {
        try {
            serverSocket = new ServerSocket(PORT);
            /*
      How long to wait for a connection
     */
            int TIME_OUT = 3000;
            serverSocket.setSoTimeout(TIME_OUT);
            return true;
        } catch (IOException ex) {
            logger.info(ex);
            return false;
        }
    }
}