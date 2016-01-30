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

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ConnectionController implements Runnable{
    private boolean shouldStop = false;
    private final boolean isServer;
    private final int PORT = 4000;
    private Socket socket;
    private ServerSocket serverSocket;
    private static ConnectionController instance;
    private Thread thread;
    private String IP;
    private ArrayList<Client> clients;
    private int id = 0;
    
    public static ConnectionController getInstance(){
        return instance;
    }
    
    public ConnectionController(boolean host, String ip){
        instance = this;
        isServer = host;
        this.IP = ip;
        thread = new Thread(this);
        thread.start();
    }

    public void stopConnection(){
        this.shouldStop = true;
        try {
            this.thread.join();
        }
        catch(InterruptedException ex) {
            System.out.println("Cant stop thread... WTF");
        }
    }
    
    @Override
    public void run() {
        try
        {
            if(isServer)
            {
                clients = new ArrayList<Client>();
                serverSocket = new ServerSocket(PORT);
                serverSocket.setSoTimeout(3000);
                while(!this.shouldStop)
                {
                    try{
                        clients.add(new Client(id, serverSocket.accept()));
                        id++;
                    }
                    catch(Exception e){
                        //timeout is reached
                        continue;
                    }
                }
            }
            else
            {
                socket = new Socket(this.IP, PORT);
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
    
    private class Client
    {
        public int id;
        public Socket sock;
        private DataInputStream dis;
        
        public Client(int id, Socket sock)
        {
            this.id = id;
            this.sock = sock;
            try{
                dis = new DataInputStream(sock.getInputStream());
            }
            catch(Exception e){System.out.println(e);}
        }
        
        public void write(String s)
        {
            try{
                sock.getOutputStream().write(s.getBytes());
            }
            catch(Exception e) {System.out.println(e);}
        }
        
        public String read()
        {
            try {
                return dis.readLine();
            } catch (IOException ex) {
                System.out.println(ex);
            }
            return null;
        }
    }
}