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
package PrutEngine;

import PrutEngine.Core.Data.GLProgram;
import PrutEngine.Core.Data.Mesh;
import PrutEngine.Core.Data.Shader;
import PrutEngine.Core.Data.Texture;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Management of all the resources
 * Making sure a resource is only loaded once
 * @author Heiko van der Heijden
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

    /**
     * A list containing all the textures
     */
    private static final ArrayList<Texture> TEXTURES = new ArrayList<>();
    
    /**
     * A list containing all the 3d meshes
     */
    private static final ArrayList<Mesh> MESHES = new ArrayList<>();
    
    
    /**
     * Loads an shader and passes the reference if the shader does not exists
     * @param shaderName    The relative path of the shader.
     * @param type  the type of the shader (Vertex_shader/FragmentShader/etc)
     * @return  An reference you can use for this shader
     * @throws IOException Calls when the shader could not be found
     */
    private static  int loadShader(String shaderName,Shader.Type type) throws IOException{
        for(Shader shader : AssetManager.SHADERS){
            if(shader.getDataLocation().equals(shaderName)){
                return shader.getMemoryPosition();
            }
        }
        
        Shader result = new Shader(shaderName,AssetManager.uniqueNumber,type);
        AssetManager.uniqueNumber++;
        AssetManager.SHADERS.add(result);
        return result.getMemoryPosition();
    }
    
    
    /**
     * Gets the texture from the stack
     * @param reference The code you received while loading the asset
     * @return  the opengl pointer
     * @throws PrutEngine.AssetManager.AssetNotFoundException When the asset is already deleted
     */
    public static int getTexture(int reference) throws AssetNotFoundException{
        for(Texture tex : AssetManager.TEXTURES){
            if(tex.getMemoryPosition() == reference){
                
                return tex.getTexture();
            }
        }
        throw new AssetNotFoundException();
    }
    
    
    /**
     * Loads an texture on the stack
     * @param texture   the relative path
     * @return  a reference you can use for the texture
     * @throws IOException 
     */
    public static int loadTexture(final String texture) throws IOException{
        for(Texture tex : AssetManager.TEXTURES){
            if(tex.getDataLocation().equals(texture)){
                return tex.addRef();
            }
        }
        
        Texture tex = new Texture(texture,AssetManager.uniqueNumber);
        AssetManager.uniqueNumber++;
        AssetManager.TEXTURES.add(tex);
        
        return tex.getMemoryPosition();
    }
    
    /**
     * Gets the opengl data for a shader
     * @param reference the reference you received while loading the shader
     * @return  the raw shader data
     * @throws PrutEngine.AssetManager.AssetNotFoundException 
     */
    private static int getShader(int reference) throws AssetNotFoundException{
        for(Shader sh : AssetManager.SHADERS){
            if(sh.getMemoryPosition() == reference){
                
                return sh.getShader();
            }
        }
        throw new AssetNotFoundException();
    }
    
    /**
     * Gets a list of all the raw data programs, this is specificly designed for the camera.
     * @return 
     */
    public static ArrayList<Integer> allPrograms(){
        final ArrayList<Integer> result = new ArrayList<>();
        AssetManager.PROGRAMS.stream().forEach((pr) -> {
            result.add(pr.getProgram());
        });
        
        return result;
    }
    
    /**
     * Loads and links shaders into a opengl program.
     * And stores the program into a reference to be used
     * @param shaders
     * @return  The program reference
     * @throws Exception 
     */
    public static int loadProgram(
            final HashMap<String, Shader.Type> shaders
    ) throws Exception{
        
        String programName = "";
        programName = shaders.entrySet().stream().map((entry) -> entry.getKey()).reduce(programName, String::concat);
        //Check if program already exists
        for(GLProgram program : AssetManager.PROGRAMS){
            if(program.getDataLocation().equals(programName)){
                return program.addRef();
            }
        }
        
        ArrayList<Integer> shaderList = new ArrayList<>();
        for (Map.Entry<String, Shader.Type> entry : shaders.entrySet()) {
            String key = entry.getKey();
            Shader.Type value = entry.getValue();
            shaderList.add(AssetManager.getShader(AssetManager.loadShader(key, value)));
        }
        
        GLProgram program = new GLProgram(
                programName,
                AssetManager.uniqueNumber, 
                shaderList);
        AssetManager.uniqueNumber++;
        AssetManager.PROGRAMS.add(program);
       
        return program.getMemoryPosition();
    }
    
    /**
     * Loads an mesh from the specified relative path
     * @param path  the relative path where the 3d mesh is located
     * @return  the reference you can use to use this asset
     * @throws IOException  When the 3d mesh does not exists
     */
    public static int loadMesh(final String path) throws IOException{
        
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getDataLocation().equals(path)){
            //    Debug.log("adsf");
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
    
    /**
     * Get the amount of vertices in a mesh
     * @param reference the reference you received when loading in the mesh
     * @return  the amount of vertices
     * @throws PrutEngine.AssetManager.AssetNotFoundException 
     */
    public static int getMeshSize(final int reference) throws AssetNotFoundException{
        for(Mesh mesh : AssetManager.MESHES){
            if(mesh.getMemoryPosition() == reference){
                return mesh.getSize();
            }
        }
        throw new AssetNotFoundException();
    }
    
    /**
     * Gets the vertex array object from specified mesh
     * @param reference the mesh you wish to have it's vao
     * @return  the vao
     * @throws PrutEngine.AssetManager.AssetNotFoundException 
     */
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
     * Removes an texture from the stack
     * @param reference 
     */
     public static void removeTexture(final int reference){

        Texture tmp = null;
        
        for(Texture tex : AssetManager.TEXTURES){
            if(tex.getMemoryPosition() == reference){
                if(tex.removeRef()){
                    tex.destroy();
                    tmp = tex;
                }
            }
        }
        
        if(tmp != null){
            AssetManager.TEXTURES.remove(tmp);
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
