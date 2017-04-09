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

package nl.hvanderheijden.prutengine.core.data;

import nl.hvanderheijden.prutengine.SettingsManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sound.sampled.AudioInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 *
 * @author quget
 */
public class Sound extends Resource
{
    private final static Logger logger = LogManager.getLogger(Sound.class.getName());
    public int soundID;
    private String name;
    private AudioFormat audioFormat;
    private byte[] data;
    private int buffSize;
    private Clip clip;
    
    public Sound(String fileLocation,int position,String soundName)
    {   
        super(fileLocation,position);
        try
        {
            name = soundName;
            File scrFile = new File(Sound.class.getResource(fileLocation).getPath());
          //  InputStream inputStream = Sound.cl//new FileInputStream(scrFile);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(scrFile);
            buffSize = audioInputStream.available() - 1;
            audioFormat = audioInputStream.getFormat();
            data = new byte[(int)audioInputStream.getFrameLength() * audioFormat.getFrameSize()];
            byte[] buf = new byte[buffSize];
            for (int i=0; i<data.length; i+=buffSize) 
            {
                int r = audioInputStream.read(buf, 0, buffSize);
                if (i+r >= data.length) 
                {
                    r = i + r - data.length;
                }
                System.arraycopy(buf, 0, data, i, r);
            }
            audioInputStream.close();
        }
        catch(Exception e)
        {
             logger.error(e);
        }
    }
    public void PlaySound(int loopCount)
    {
        try
        {
            if(clip == null)
            {
                clip = AudioSystem.getClip();
                clip.open(audioFormat,data, 0, buffSize);
            }
            clip.setFramePosition(0);
            clip.loop(loopCount);
            clip.start();
        }
        catch(Exception e)
        {
            logger.error(e);
        }
    }
    public int getSound()
    {
        return this.soundID;
    }
    public String getSoundName()
    {
        return name;
    }
    @Override
    public void destroy() 
    {
        
    }
}