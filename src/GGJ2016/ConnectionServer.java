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
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
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

   
    private class Client implements Runnable{
        private final int id;
        private final Socket sock;
        private final Thread thread;
        private boolean shouldStop = false;
        public Client(int id, Socket sock){
            this.id =id;
            this.sock = sock;
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
            while(!shouldStop){
                try {
                    DataInputStream inputStream = new DataInputStream(sock.getInputStream());
                    BufferedWriter bw= new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
                    
                    int read;
                    byte[] buffer = new byte[1024];
                 
                    while((read = inputStream.read(buffer)) != -1){
                        if(shouldStop){
                            inputStream.close();
                            bw.close();
                            this.sock.close();
                            break;
                        }
                        String msg = new String(buffer, 0, read);
                        Debug.log(msg);
                        this.send("hai", bw);
                        
                    }
                    
                } catch (IOException ex) {
                    Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
                }
                 
            }
        }
        
        public void send(String msg, BufferedWriter bw){
            try {
                bw.write(msg);
                bw.flush();
            }   catch (IOException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
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
           Debug.log("ASDFASDF");
            clients.add(new Client(this.uniqueNumber, serverSocket.accept()));
            this.uniqueNumber++;
            
        } catch (IOException ex) {
           
           //Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public boolean attemptToConnect() {
        try {
            serverSocket = new ServerSocket(PORT);
         
            serverSocket.setSoTimeout(3000);
            //serverSocket.accept();
           //;
            return true;
        } catch (IOException ex) {
           // Logger.getLogger(ConnectionServer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    
}
