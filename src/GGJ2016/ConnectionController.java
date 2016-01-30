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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.awt.Mutex;

/**
 *
 * @author Heiko van der Heijden
 */
public final class ConnectionController implements Runnable{
    private boolean shouldStop = false;
  //  private final Thread thread;
   // private final Mutex lock;
    private final int PORT = 4000;
    
    private static ConnectionController instance;
    
    public static ConnectionController getInstance(){
        return instance;
    }
    
    
    public ConnectionController(boolean host){
       // this.lock = new Mutex();
        this.shouldStop = false;
       // this.thread = new Thread(this);
        instance = this;
     //   this.thread.start();
        
    }

    public void stopConnection(){
        this.shouldStop = true;/*
        try {
            this.thread.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    @Override
    public void run() {
        for(;;){
            Socket sock;
            BufferedReader br;
            DataInputStream inputStream;
            BufferedWriter bw;
            
            //socket = new Socket(Globals.SERVER, Globals.PORT);
              //  inputStream = new DataInputStream(socket.getInputStream());
                //bw= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            
            if(this.shouldStop){

                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
