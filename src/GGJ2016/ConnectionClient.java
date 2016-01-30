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
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Heiko van der Heijden
 */
public class ConnectionClient extends BaseConnection {
    private Socket socket;
    private final String IP = "localhost";
    private DataInputStream inputStream;
    private  BufferedWriter bw;
    
    protected ConnectionClient(){
        
    }
    
    @Override
    protected void stop() {
      
    }

    @Override
    public boolean attemptToConnect() {
        try {
            socket = new Socket(this.IP, PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            return true;
        } catch (IOException ex) {
            Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
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
                    return;
                }
                String msg = new String(buffer, 0, read);
                Debug.log(msg);
                this.send("hai", bw);
            }   
        } catch (IOException ex) {
            Logger.getLogger(ConnectionClient.class.getName()).log(Level.SEVERE, null, ex);
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
