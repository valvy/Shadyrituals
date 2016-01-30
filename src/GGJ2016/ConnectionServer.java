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


import PrutEngine.Debug;
import com.sun.corba.se.impl.orbutil.concurrent.Mutex;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import static java.lang.System.exit;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heiko van der Heijden
 */
public class ConnectionServer extends BaseConnection {

    private int uniqueNumber = 0;
    private final int TIME_OUT = 3000;
    private final ArrayList<String> globalBuffer;
    
    protected ConnectionServer(){
        this.globalBuffer = new ArrayList<>();
        
    }

   
    private class Client implements Runnable{
        private final int id;
        private final Socket sock;
        private final Thread thread;
        private boolean shouldStop = false;
        private final Mutex mutex;
        private final ArrayList<String> from;
        private final ArrayList<String> to;
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
        
        public void addToBuffer(String msg){
            try {
                mutex.acquire();
                this.to.add(msg);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            }finally{
                mutex.release();
            }
        }
        
        public Client(int id, Socket sock) throws Exception{
            this.id =id;
            
            this.sock = sock;
           
            this.from = new ArrayList<>();
            this.to = new ArrayList<>();
            this.mutex = new Mutex();
            this.thread = new Thread(this);
            this.thread.start();
        }
        
        public void stopConnection(){
 
            shouldStop = true;
            
            try {
                thread.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
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
                                this.from.add(msg);
                            }
                             
                        } catch (InterruptedException ex) {
                            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                        }finally{
                            String dat = NOTHING;
                            if(this.to.size() > 0){
                                dat = to.get(0);
                                this.to.remove(0);
                            }
                            this.mutex.release();
                            this.send(dat, bw);                           
                        }
              
                        
                        
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        
        public void send(String msg, BufferedWriter bw){
            try {
                bw.write(msg);
                bw.flush();
            }   catch (IOException ex) {
                Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
       
    }
    private ArrayList<Client> clients;
    private ServerSocket serverSocket;
  
    
    @Override
    protected void stop() {
        
        for(Client cl : this.clients){
            cl.stopConnection();
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
            
        } catch (IOException ex) {
           
           Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    @Override
    public ArrayList<ConnectedPlayer> getAllConnections() {
      return null;
    }

    @Override
    public void notifyWorld(ConnectedPlayer player) {
        if(clients == null){
            return;
        }
        ArrayList<String> localBuffer = new ArrayList<>();
        
        for(Client cl : clients){
            localBuffer.add(cl.getFrom());
            cl.addToBuffer(
                    player.id + ";" + player.currentPosition.toString() + ";" + player.playerElement.toString() + ";"
            );
        }
        
        for(Client cl : clients){
            for(String t : localBuffer){
                if(!t.equals(NOTHING)){
                    cl.addToBuffer(t);
                }
            }
        }
        
        for(String t : localBuffer){
            if(!t.equals(NOTHING)){
                Debug.log(t);
                this.globalBuffer.add(t);
            }
        }
        
        
    }

    @Override
    public boolean attemptToConnect() {
        try {
            serverSocket = new ServerSocket(PORT);
            serverSocket.setSoTimeout(TIME_OUT);
            
            return true;
        } catch (IOException ex) {
          //  Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
