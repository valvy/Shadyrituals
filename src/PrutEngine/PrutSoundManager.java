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
package PrutEngine;

import PrutEngine.Core.Data.Sound;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author quget
 */
public class PrutSoundManager 
{
    public PrutSoundManager()
    {
        
    }
    public void PlaySound(Sound sound)
    {
        sound.PlaySound();
        /*
        new Thread(new Runnable() 
        {
            @Override
            public void run() 
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    clip.open(sound.audioFormat, sound.data, 0, sound.buffSize);
                    clip.start();
                }
                catch(Exception e)
                {
                    System.err.println(e.getMessage());
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }).start();*/
    }
    /*
    public synchronized void playSound(final String soundPath) 
    {
        
        new Thread(new Runnable() 
        {
        // The wrapper thread is unnecessary, unless it blocks on the
        // Clip finishing; see comments.
            public void run() 
            {
                try
                {
                    Clip clip = AudioSystem.getClip();
                    //AssetManager.loadSound(soundPath);
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(soundPath));
                    clip.open(inputStream);
                    clip.start(); 
                } 
                catch (Exception e) 
                {
                  System.err.println(e.getMessage());
                }
            }
          }).start();
  }*/
}
