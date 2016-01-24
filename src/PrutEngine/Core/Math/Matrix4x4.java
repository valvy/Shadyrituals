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
package PrutEngine.Core.Math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;

/**
 * An 4 by 4 matrix, 
 * containing various mathematical tools to calculate difficult 3d math
 * @author Heiko van der Heijden
 */
public final class Matrix4x4 {
    
    private final Vector4<Vector4<Float>> mat;
    /**
     * Loads the matrix with an identity matrix
     */
    public Matrix4x4(){
        this.mat = Matrix4x4.identityMatrix().mat;
    }
    
    public Matrix4x4(Matrix4x4 other){
        this.mat = Matrix4x4.identityMatrix().mat;
        this.set(other);
    }
    
    /**
     * Initializes a matrix with specified rows
     * @param row1
     * @param row2
     * @param row3
     * @param row4 
     */
    public Matrix4x4(Vector4<Float> row1, Vector4<Float> row2, Vector4<Float> row3, Vector4<Float> row4){
        this.mat = new Vector4<Vector4<Float>> (row1,row2,row3,row4);
    }
    
    /**
     * Translates an matrix
     * @param vector 
     */
    public void translate(Vector3<Float> vector){
        this.mat.x.w += vector.x;
        this.mat.y.w += vector.y;
        this.mat.z.w += vector.z;
    }
    
    @Override
    public String toString(){
        return mat.x.toString() + '\n' +  mat.y.toString() + '\n' + mat.z.toString() + '\n'+ mat.w.toString();
    }
    
    
    /**
     * Scales an matrix with the specified values
     * @param mat The Matrix you wish to scale
     * @param vec   The values of each axis you wish to scale
     * @return  the scaled matrix
     */
    public static Matrix4x4 scale(Matrix4x4 mat, Vector3<Float> vec){
        Matrix4x4 result = new Matrix4x4(mat);
        result.mat.x.x *= vec.x;
        result.mat.y.y *= vec.y;
        result.mat.z.z *= vec.z;
        return result;
    }
    
    /**
     * Transposes a matrix, (Which is more or less mirroring on the diagonal line)
     * @param mat the matrix you wish to transpose
     * @return the transposed matrix
     */
    public static Matrix4x4 transpose(Matrix4x4 mat){
        Matrix4x4 result = Matrix4x4.identityMatrix();
        result.mat.x = new Vector4<>(mat.mat.x.x, mat.mat.y.x, mat.mat.z.x, mat.mat.w.x);
        result.mat.y = new Vector4<>(mat.mat.x.y, mat.mat.y.y, mat.mat.z.y, mat.mat.w.y);
        result.mat.z = new Vector4<>(mat.mat.x.z,mat.mat.y.z, mat.mat.z.z, mat.mat.w.z);
        result.mat.w = new Vector4<>(mat.mat.x.w,mat.mat.y.w, mat.mat.z.w, mat.mat.w.w);
        return result;
    }
    
    /**
     * Places all the values of the matrix for use in opengl
     * @return 
     */
    public FloatBuffer getRawData(){
        float buffer[] = {
            mat.x.x,mat.x.y,mat.x.z,mat.x.w,
            mat.y.x,mat.y.y,mat.y.z,mat.y.w,
            mat.z.x,mat.z.y,mat.z.z,mat.z.w,
            mat.w.x,mat.w.y,mat.w.z,mat.w.w
        };
        FloatBuffer result = BufferUtils.createFloatBuffer(16);
        result.put(buffer);
        result.flip();
        return result;
    }


    /**
     * Gets an rotation matrix from a vector and angle
     * @param angle the angle in degrees
     * @param around   The position to rotate around
     * @return rotation matrix
     */
    public static Matrix4x4 rotate(final float angle, final Vector3<Float> around){
    //    Matrix4x4 rotation = new Matrix4x4();
        //get radians
        float rad = (float) Math.toRadians(angle);
        final float x2 = around.x * around.x;
        final float y2 = around.y * around.y;
        final float z2 = around.z * around.z;
        final float c = (float) Math.cos(rad);
        final float s = (float)Math.sin(rad);
        final float omc = 1.0f - c;
        
        return new Matrix4x4(
                new Vector4<>(x2 * omc + c,around.y * around.x * omc + around.z * s,around.x * around.z * omc - around.y * s,0f),
                new Vector4<>(around.x * around.y * omc - around.z * s,y2 * omc + c,around.y * around.z  * omc + around.x * s,0f),
                new Vector4<>(around.x * around.z * omc + around.y * s,around.y * around.z * omc - around.x * s,z2 * omc + c,0f),
                new Vector4<>(0f,0f,0f,1f)
        );
    }
    
    /**
     * Multiplies two different matrices with each other
     * @param mat1 the first matrix
     * @param mat2 the second matrix
     * @return the multiplied matrix
     */
    public static Matrix4x4 multiply(final Matrix4x4 mat1, final Matrix4x4 mat2){
        
        Matrix4x4 result = new Matrix4x4();
        //Row 1
        result.mat.x.x = mat1.mat.x.x * mat2.mat.x.x + mat1.mat.x.y * mat2.mat.y.x + mat1.mat.x.z * mat2.mat.z.x + mat1.mat.x.w * mat2.mat.w.x;        
        result.mat.x.y = mat1.mat.x.x * mat2.mat.x.y + mat1.mat.x.y * mat2.mat.y.y + mat1.mat.x.z * mat2.mat.z.y + mat1.mat.x.w * mat2.mat.w.y;
        result.mat.x.z = mat1.mat.x.x * mat2.mat.x.z + mat1.mat.x.y * mat2.mat.y.z + mat1.mat.x.z * mat2.mat.z.z + mat1.mat.x.w * mat2.mat.w.z;
        result.mat.x.w = mat1.mat.x.x * mat2.mat.x.w +mat1.mat.x.y * mat2.mat.y.w +mat1.mat.x.z * mat2.mat.z.w + mat1.mat.x.w * mat2.mat.w.w;
        
        //row2
        result.mat.y.x = mat1.mat.y.x * mat2.mat.x.x + mat1.mat.y.y * mat2.mat.y.x + mat1.mat.y.z * mat2.mat.z.x + mat1.mat.y.w * mat2.mat.w.x;
        result.mat.y.y =  mat1.mat.y.x * mat2.mat.x.y + mat1.mat.y.y * mat2.mat.y.y + mat1.mat.y.z * mat2.mat.z.y + mat1.mat.y.w * mat2.mat.w.y;
        result.mat.y.z = mat1.mat.y.x * mat2.mat.x.z + mat1.mat.y.y * mat2.mat.y.z + mat1.mat.y.z * mat2.mat.z.z +mat1.mat.y.w * mat2.mat.w.z;
        result.mat.y.w = mat1.mat.y.x * mat2.mat.x.w + mat1.mat.y.y * mat2.mat.y.w + mat1.mat.y.z * mat2.mat.z.w + mat1.mat.y.w * mat2.mat.w.w;
        
        //row3
        result.mat.z.x = mat1.mat.z.x * mat2.mat.x.x + mat1.mat.z.y * mat2.mat.y.x + mat1.mat.z.z * mat2.mat.z.x +  mat1.mat.z.w * mat2.mat.w.x;
        result.mat.z.y = mat1.mat.z.x * mat2.mat.x.y + mat1.mat.z.y * mat2.mat.y.y + mat1.mat.z.z * mat2.mat.z.y + mat1.mat.z.w * mat2.mat.w.y;
        result.mat.z.z = mat1.mat.z.x * mat2.mat.x.z +mat1.mat.z.y * mat2.mat.y.z +mat1.mat.z.z * mat2.mat.z.z +mat1.mat.z.w * mat2.mat.w.z;
        result.mat.z.w = mat1.mat.z.x * mat2.mat.x.w + mat1.mat.z.y * mat2.mat.y.w +mat1.mat.z.z * mat2.mat.z.w +mat1.mat.z.w * mat2.mat.w.w;
        
        //row4
        result.mat.w.x = mat1.mat.w.x * mat2.mat.x.x +  mat1.mat.w.y * mat2.mat.y.x +  mat1.mat.w.z * mat2.mat.z.x +  mat1.mat.w.w * mat2.mat.w.x;
        result.mat.w.y = mat1.mat.w.x * mat2.mat.x.y + mat1.mat.w.y * mat2.mat.y.y + mat1.mat.w.z * mat2.mat.z.y + mat1.mat.w.w * mat2.mat.w.y;
        result.mat.w.z = mat1.mat.w.x * mat2.mat.x.z +mat1.mat.w.y * mat2.mat.y.z + mat1.mat.w.z * mat2.mat.z.z +mat1.mat.w.w * mat2.mat.w.z;
        result.mat.w.w =  mat1.mat.w.x * mat2.mat.x.w +mat1.mat.w.y * mat2.mat.y.w + mat1.mat.w.z * mat2.mat.z.w +mat1.mat.w.w * mat2.mat.w.w;
        
        
        
        return result;
    }
    
    /**
     * Sets the matrix values in this one
     * @param other the matrix you wish to copy
     */
    public void set(Matrix4x4 other){
        this.mat.x = new Vector4<>(other.mat.x);
        this.mat.y = new Vector4<>(other.mat.y);
        this.mat.z = new Vector4<>(other.mat.z);
        this.mat.w = new Vector4<>(other.mat.w);
    }
    /**
     * returns an identity matrix
     * @return Matrix
     */
    public static Matrix4x4 identityMatrix(){
      return new Matrix4x4(
              new Vector4<>(1f,0f,0f,0f),
              new Vector4<>(0f,1f,0f,0f),
              new Vector4<>(0f,0f,1f,0f),
              new Vector4<>(0f,0f,0f,1f)
      );
        
    }
}
