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
import PrutEngine.Application;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the connection to a server. 
 * Request data and passes it to the server
 * @author Heiko van der Heijden 
 */
public class ConnectionClient extends BaseConnection {
    /**
     * The socket it want's to connect to
     */
    private Socket socket;
    
    /**
     * The server ip adres
     */
    private final String IP = Globals.IP_ADRES;
    
    /**
     * Data stream to read
     */
    private DataInputStream inputStream;
    
    /**
     * stream to write
     */
    private  BufferedWriter bw;
    /**
     * Concurrency mutex to avoid data races
     */
    private final Mutex mutex;
    
    /**
     * The buffer that the client receives from the server
     */
    private final ArrayList<String> from;
    
    /**
     * the last message that it should send to the server
     */
    private String to = NOTHING;
    
    /**
     * Initializes the client 
     */
    protected ConnectionClient(){
        this.mutex = new Mutex();
        this.from = new ArrayList<>();
    }
    
    /**
     * Gets the data from the specific client
     * @return 
     */
    public String getFrom(){
        String dat = NOTHING;
        try{
            mutex.acquire();
            if(this.from.size() > 0){
                dat = from.get(0);
                this.from.remove(0);
            }
        }
        catch (InterruptedException ex) {
            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            mutex.release();
        }
        return dat;
    }
    
    /**
     * Sends a message to the client
     * @param msg 
     */
    public void addToBuffer(String msg){
        try {
            mutex.acquire();
           // Debug.log("addToBuffer: " + msg);
            this.to = msg;
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            mutex.release();
        }
    }
    
    @Override
    protected void stop() {
        try {
            if(this.socket != null &&!this.socket.isClosed()){
                this.socket.close();
                this.bw.close();
                this.inputStream.close();
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean attemptToConnect() {
        try {
            socket = new Socket(this.IP, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public void connected() {
        byte[] buffer = new byte[1024];
        int read;
        try {
            while((read = inputStream.read(buffer)) != -1){
                if(this.shouldStop()){
                    bw.close();
                    socket.close();
                    break;
                }
            try {
                this.mutex.acquire();
                String msg = new String(buffer, 0, read);

                Debug.log(msg);
                if(!msg.equals(NOTHING))
                {
                    if(msg.contains("Player:"))
                    {
                        if(!msg.contains("Server") && idName.equals("NULL")){
                            idName = msg;
                        }
                    }
                    this.from.add(msg);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                this.mutex.release();
                this.send(to, bw);                           
            }
        }   
        } catch (IOException ex) {
            try {
                this.socket.close();
                 this.inputStream.close();
                 this.bw.close();
            } catch (IOException ex1) {
                Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex1);
            }
          Application.getInstance().quit();
            Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sends a message to the server
     * @param msg the message
     * @param bw the writer
     */
    public void send(String msg, BufferedWriter bw){
        try {
            bw.write(msg);
            bw.flush();
        }
        catch (IOException ex) {
            Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public ArrayList<ConnectedPlayer> getAllConnections() {
        String dat = NOTHING;
        ArrayList<ConnectedPlayer> results = new ArrayList<>();
        do{
            dat = getFrom();
            if(dat.equals(NOTHING)){//nothing to do
                return results;
            }
            
            String[] splitedData =  dat.split(";");
            if(splitedData.length == 1){//invalid input
                continue;
            }
  
            String id = splitedData[0];
            //parse it to a vector3
            final Vector3<Float> currentPosition = new Vector3<>(0f,0f,0f);
            try{
                final Scanner fi = new Scanner(splitedData[1]);
           
                currentPosition.x = fi.nextFloat();
                currentPosition.y = fi.nextFloat();
                currentPosition.z = fi.nextFloat();
            }catch(java.util.InputMismatchException ex){
                Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
                continue;
            }
            
            //set the correct data
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
            results.add(player);
            
        }while(!dat.equals(NOTHING));
        return results;
    }

    @Override
    public void notifyWorld(ConnectedPlayer player) {
        this.addToBuffer(player.id + ";" + player.currentPosition.toString() + ";" + player.playerElement.toString() + ";");
    }
}