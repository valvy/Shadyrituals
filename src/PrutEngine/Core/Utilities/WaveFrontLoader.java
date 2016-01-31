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
package PrutEngine.Core.Utilities;

import PrutEngine.Core.Math.Vector2;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Scanner;
import org.lwjgl.BufferUtils;

public final class WaveFrontLoader {
    //The tokens for parsing
    private static final String COMMENT = "#";
    private static final String VERTEX_TEXTURE = "vt";
    private static final String VERTEX_NORMAL = "vn";
    private static final String VERTEX = "v";
    private static final String FACE = "f";
    
    //The loaded data
    private final ArrayList<Vector3<Float>> vertex;
    private final ArrayList<Vector2<Float>> vertexTexture;
    private final ArrayList<Vector3<Float>> normals;
    private final ArrayList<Vector3<Vector3<Integer>>> faces;
    
    public WaveFrontLoader(final String path) throws IOException{
        this.vertex = new ArrayList<>();
        this.vertexTexture = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.faces = new ArrayList<>();
        this.loadFile(path);
    }
    
    private Vector2<Float> parseVector2f(final String line){
        final Scanner fi = new Scanner(line);
        final Vector2<Float> result = new Vector2<>(fi.nextFloat(),fi.nextFloat());
        return result;
    }
    
    
    private Vector3<Vector3<Integer>> parseFace(final String line){
        final Scanner fi = new Scanner(line);
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
        
        return new Vector3<>(fst,snd,third);
    }
    
    private Vector3<Float> parseVector3f(final String line){
        final Scanner fi = new Scanner(line);
      
        final Vector3<Float> result = new Vector3<>(0.0f,0.0f,0.0f);
        result.x = fi.nextFloat();
        result.y = fi.nextFloat();
        result.z = fi.nextFloat();
        
        return result;
    }
    
    public int triangleAmount(){
        return this.faces.size() * 3;
    }
    
    public FloatBuffer rawNormalData(){
        final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.faces){
            Vector3<Float> n = this.normals.get(face.x.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
            n = this.normals.get(face.y.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
            n = this.normals.get(face.z.z - 1);
            rawData.add(n.x);
            rawData.add(n.y);
            rawData.add(n.z);
        }
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach((d) -> {            
            result.put(d);
        });
        result.flip();
        return result;
    }
    
    public FloatBuffer rawVertexData(){
       final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.faces){
            Vector3<Float> v = this.vertex.get(face.x.x -1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);;
            v = this.vertex.get(face.y.x - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);
            v = this.vertex.get(face.z.x - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            rawData.add(v.z);        
        }
        
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach((d) -> {            
            result.put(d);
        });
        result.flip();
        
        return result;   
    }
    
    public FloatBuffer rawUVData(){
        final ArrayList<Float> rawData = new ArrayList<>();
        for(final Vector3<Vector3<Integer>> face : this.faces){
            Vector2<Float> v = this.vertexTexture.get(face.x.y -1);
            rawData.add(v.x);
            rawData.add(v.y);
            v = this.vertexTexture.get(face.y.y - 1);
            rawData.add(v.x);
            rawData.add(v.y);
            v = this.vertexTexture.get(face.z.y - 1);
            rawData.add(v.x);
            rawData.add(v.y);    
        }
        
        FloatBuffer result = BufferUtils.createFloatBuffer(rawData.size());
        rawData.stream().forEach((d) -> {            
            result.put(d);
        });
        result.flip();

        return result;
    }
    
    private void loadFile(final String path) throws IOException{
        final BufferedReader br;
        br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
     
        try {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.startsWith(COMMENT)){
                    //Comment
                }else if(line.startsWith(VERTEX_TEXTURE)){
                    this.vertexTexture.add(this.parseVector2f(line.substring(VERTEX_TEXTURE.length())));
                }else if(line.startsWith(VERTEX_NORMAL)) {
                    this.normals.add(this.parseVector3f(line.substring(VERTEX_NORMAL.length())));
                } else if(line.startsWith(VERTEX)){
                    this.vertex.add(this.parseVector3f(line.substring(VERTEX.length())));
     
                }else if(line.startsWith(FACE)){
                    line = line.replace("/", " ");
                    this.faces.add(this.parseFace(line.substring(FACE.length())));
                }else{
                    Debug.log("unkown command error ignoring line");
                }
            }
        } finally {
            br.close();
        }
    }
}