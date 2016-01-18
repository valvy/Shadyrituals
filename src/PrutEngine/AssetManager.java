/*
 * Copyright (c) 2016, heikovanderheijden
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

import PrutEngine.Core.Data.GLProgram;
import PrutEngine.Core.Data.Mesh;
import PrutEngine.Core.Data.Resource;
import PrutEngine.Core.Data.Shader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Management of all the resources
 * Making sure a resource is only loaded once
 * @author heikovanderheijden
 */
public final class AssetManager {
    /**
     * Called when the program can't find an asset
     */
    public static class AssetNotFoundException extends Exception{
        
    }
    
    /**
     * An unique number that represents a reference
     */
    private static int uniqueNumber = 0;
    
    /**
     * A list containing all the loaded shaders
     */
    private static final ArrayList<Shader> SHADERS = new ArrayList<>();
    
    /**
     * A list containing all the compiled and linked programs
     */
    private static final ArrayList<GLProgram> PROGRAMS = new ArrayList<>();

    private static final ArrayList<Mesh> MESHES = new ArrayList<>();
    
    public static ArrayList<Integer> allPrograms(){
        final ArrayList<Integer> result = new ArrayList<>();
        AssetManager.PROGRAMS.stream().forEach((pr) -> {
            result.add(pr.getProgram());
        });
        
        return result;
    }
    
    /**
     * Loads and links two shaders into a opengl program.
     * And stores the program into a reference to be used
     * @param vertexShader
     * @param fragmentShader
     * @return  The program reference
     * @throws Exception 
     */
    public static int loadProgram(final String vertexShader, final String fragmentShader) throws Exception{
        
        final String programName = vertexShader + fragmentShader;
        //Check if program already exists
        for(GLProgram program : AssetManager.PROGRAMS){
            if(program.getDataLocation().equals(programName)){
                return program.addRef();
            }
        }
        
        Shader vShader = null;
        Shader fShader = null;
        //Check if the shaders do exist
        for(Shader shader : AssetManager.SHADERS){
            if(shader.getDataLocation().equals(vertexShader)){
                vShader = shader;
            }else if(shader.getDataLocation().equals(fragmentShader)){
                fShader = shader;
            }
            if(vShader != null && fShader != null){
                break;
            }
        }
        
        //Create vertex shader if it does not exists...
        if(vShader == null){
            vShader = new Shader(vertexShader, AssetManager.uniqueNumber,Shader.Type.Vertex_Shader);
            AssetManager.uniqueNumber++;
            AssetManager.SHADERS.add(vShader);
        }
        
        if(fShader == null){
            fShader = new Shader(fragmentShader, AssetManager.uniqueNumber,Shader.Type.Fragment_Shader);
            AssetManager.uniqueNumber++;
            AssetManager.SHADERS.add(fShader);
        }
        
        GLProgram program = new GLProgram(programName, AssetManager.uniqueNumber, vShader.getShader(), fShader.getShader());
        AssetManager.uniqueNumber++;
        AssetManager.PROGRAMS.add(program);
       
        return program.getMemoryPosition();
    }
    
    public static int loadMesh(final String path) throws IOException{
        
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getDataLocation().equals(path)){
                return mesh.addRef();
            }
        }
        
        final Mesh mesh = new Mesh(path,AssetManager.uniqueNumber);
        AssetManager.uniqueNumber++;
        AssetManager.MESHES.add(mesh);
        
        return mesh.getMemoryPosition();
    }
    
    /**
     * Gets the opengl program specified by the reference
     * @param reference
     * @return  the GLProgram
     * @throws PrutEngine.AssetManager.AssetNotFoundException 
     */
    public static int getProgram(final int reference) throws AssetNotFoundException{
        for(GLProgram pr : AssetManager.PROGRAMS){
            if(pr.getMemoryPosition() == reference){
                
                return pr.getProgram();
            }
        }
        throw new AssetNotFoundException();
    }
    

    
    
    
    public static int getMeshSize(final int reference) throws AssetNotFoundException{
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getMemoryPosition() == reference){
                return mesh.getSize();
            }
        }
        throw new AssetNotFoundException();
    }
    
    public static int getMeshVao(final int reference) throws AssetNotFoundException{
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getMemoryPosition() == reference){
                return mesh.getVao();
            }
        }
        throw new AssetNotFoundException();
    }
    
    /**
     * Clears the Shader buffer, and marking it to delete
     * It will remain in memory until the programs using the shaders are removed to
     */
    public static void clearShaderBuffer(){
        AssetManager.SHADERS.stream().forEach((shader) -> {
            shader.destroy();
        });
        SHADERS.clear();
    }
    

    
    /**
     * Removes an program when it exists
     * @param reference 
     */
    public static void removeProgram(final int reference){

        GLProgram tmp = null;
        
        for(GLProgram pr : AssetManager.PROGRAMS){
            if(pr.getMemoryPosition() == reference){
                if(pr.removeRef()){
                    pr.destroy();
                    tmp = pr;
                }
            }
        }
        
        if(tmp != null){
            AssetManager.PROGRAMS.remove(tmp);
        }
        
    }
    
    
    
    
     /**
     * Removes an Mesh when it exists
     * @param reference 
     */
    public static void removeMesh(final int reference){

        Mesh tmp = null;
        
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getMemoryPosition() == reference){
                if(mesh.removeRef()){
                    mesh.destroy();
                    tmp = mesh;
                }
            }
        }
        
        if(tmp != null){
            AssetManager.MESHES.remove(tmp);
        }
        
    }

    
    
    
}
