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
package nl.hvanderheijden.prutengine.core.utilities;

import nl.hvanderheijden.prutengine.core.math.Vector2;
import nl.hvanderheijden.prutengine.core.math.Vector3;
import nl.hvanderheijden.prutengine.Debug;

import java.io.*;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import nl.hvanderheijden.prutengine.exceptions.PrutEngineException;
import nl.hvanderheijden.prutengine.exceptions.ResourceNotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

public final class WaveFrontLoader {

    private final static Logger logger = LogManager.getLogger(WaveFrontLoader.class.getName());

    //The tokens for parsing
    private static final String COMMENT = "#";
    private static final String VERTEX_TEXTURE = "vt";
    private static final String VERTEX_NORMAL = "vn";
    private static final String VERTEX = "v";
    private static final String FACE = "f";
    
    //The loaded data
    private final List<Vector3<Float>> vertexList;
    private final List<Vector2<Float>> vertexTextureList;
    private final List<Vector3<Float>> normalsList;
    private final List<Vector3<Vector3<Integer>>> facesList;

    private WaveFrontLoader(){
        throw new UnsupportedOperationException();
    }

    public WaveFrontLoader(final String path) throws PrutEngineException{
        this.vertexList = new ArrayList<>();
        this.vertexTextureList = new ArrayList<>();
        this.normalsList = new ArrayList<>();
        this.facesList = new ArrayList<>();
        try {
            this.loadFile(path);
        } catch (IOException e) {
            logger.error(e);
            throw new ResourceNotFoundException(String.format("File %s has not been found", path));
        }
    }
    
    private Vector2<Float> parseVector2f(final String line){
       try(final Scanner sc = new Scanner(line)){
           return new Vector2<>(
                   Float.parseFloat(sc.next()),
                   Float.parseFloat(sc.next())
           );

       }

    }
    
    
    private Vector3<Vector3<Integer>> parseFace(final String line){
        try(final Scanner fi = new Scanner(line)) {
            Vector3<Integer> fst = new Vector3<>(
                    fi.nextInt(),
                    fi.nextInt(),
                    fi.nextInt());

            Vector3<Integer> snd = new Vector3<>(
                    fi.nextInt(),
                    fi.nextInt(),
                    fi.nextInt());
            Vector3<Integer> third = new Vector3<>(
                    fi.nextInt(),
                    fi.nextInt(),
                    fi.nextInt());

            return new Vector3<>(fst, snd, third);
        }
    }
    
    private Vector3<Float> parseVector3f(final String line){
        try(final Scanner fi = new Scanner(line)) {
            final Vector3<Float> result = new Vector3<>(0.0f, 0.0f, 0.0f);
            result.x = Float.parseFloat(fi.next());
            result.y = Float.parseFloat(fi.next());
            result.z = Float.parseFloat(fi.next());


            return result;
        }
    }
    
    public int triangleAmount(){
        return this.facesList.size() * 3;
    }
    
    public FloatBuffer rawNormalData(){
        final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.facesList){
            Vector3<Float> n = this.normalsList.get(face.x.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
            n = this.normalsList.get(face.y.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
            n = this.normalsList.get(face.z.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
        }
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach(result::put);
        result.flip();
        return result;
    }
    
    public FloatBuffer rawVertexData(){
       final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.facesList){
            Vector3<Float> v = this.vertexList.get(face.x.x -1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);
            v = this.vertexList.get(face.y.x - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);
            v = this.vertexList.get(face.z.x - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);        
        }
        
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach(result::put);
        result.flip();
        
        return result;   
    }
    
    public FloatBuffer rawUVData(){
        final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.facesList){
            Vector2<Float> v = this.vertexTextureList.get(face.x.y -1);
            rawData.add(v.x);
            rawData.add(v.y);
            v = this.vertexTextureList.get(face.y.y - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            v = this.vertexTextureList.get(face.z.y - 1);
            rawData.add(v.x);
            rawData.add(v.y);    
        }
        
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach(result::put);
        result.flip();

        return result;
    }
    
    private void loadFile(final String path) throws IOException, PrutEngineException {

        try(final InputStream str = getClass().getResourceAsStream(path)){
            final BufferedReader reader = new BufferedReader(new InputStreamReader(str, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
           //     System.out.println(line);

                if(line.startsWith(COMMENT)){
                    //Comment
                }else if(line.startsWith(VERTEX_TEXTURE)){
                    this.vertexTextureList.add(this.parseVector2f(line.substring(VERTEX_TEXTURE.length())));
                }else if(line.startsWith(VERTEX_NORMAL)) {
                    this.normalsList.add(this.parseVector3f(line.substring(VERTEX_NORMAL.length())));
                } else if(line.startsWith(VERTEX)){
                    this.vertexList.add(this.parseVector3f(line.substring(VERTEX.length())));

                }else if(line.startsWith(FACE)){
                    line = line.replace("/", " ");
                    this.facesList.add(this.parseFace(line.substring(FACE.length())));
                }else{
                    Debug.log("unkown command error ignoring line");
                }
            }
        } catch(IOException ex){
            logger.error(ex);
            throw new PrutEngineException(String.format("Could not load file : %s", ex.getMessage()));
        }

    }
}