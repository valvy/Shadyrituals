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

import PrutEngine.Debug;

/**
 * Manages the data of quaternions
 * This can be used to handle rotations without problems of a gimbal lock
 * @author Heiko van der Heijden
 */
public class Quaternion {


    public final Vector3<Float> imaginary;
    public float real;
 
    public Quaternion(){
        this.imaginary = new Vector3<>(0f,1f,0f);
        this.real = 0;
    }
    
    
    
    public static Quaternion rotate(Quaternion q, Vector3<Float> rot, float angle){
        if(rot.x == 0 && rot.y == 0 && rot.z == 0){
            return new Quaternion();//load quaternion with identity
        }
        Vector3<Float> unitV = Vector3.unitVector(rot);
        final float degrees = (float) ((angle * Math.PI) / 180);
        final float halfSin = (float) Math.sin(degrees / 2);
        return Quaternion.multiply(q,new Quaternion(new Vector4<>(
                (float) Math.cos(degrees / 2),
                halfSin * unitV.x ,
                halfSin * unitV.y,
                halfSin * unitV.z)));
    }
    
    /**
     * Rotate an vector around an other vector with the specified angle
     * @param from
     * @param around
     * @param angle
     * @return 
     */
    public static Quaternion rotateVector3(final Vector3<Float> from, final Vector3<Float> around, final float angle){

        //Calculate the norm
        final Vector3<Float> unitVector = Vector3.unitVector(around);
        //Make a quaternion
        final float degrees = (float) ((angle * Math.PI) / 180);
        final Quaternion quat = new Quaternion(new Vector4<>(
                (float)Math.cos(degrees / 2),
                (float)Math.sin(degrees / 2) * unitVector.x,
                (float)Math.sin((degrees / 2) * unitVector.y),
                (float)Math.sin((degrees / 2) * unitVector.z)     
        ));

   
        return Quaternion.multiply(
                Quaternion.multiply(
                        Quaternion.conjugate(quat),
                        new Quaternion(new Vector4<>(0f,from.x,from.y,from.z)))
                , quat);
    }

    public Quaternion(Quaternion quaternion) {
        this.imaginary = new Vector3<>(quaternion.imaginary);
        this.real = quaternion.real;
    }
    
    

    
    
    public void add(Quaternion quat){
        this.imaginary.x += quat.imaginary.x;
        this.imaginary.y += quat.imaginary.y;
        this.imaginary.z += quat.imaginary.z;
        this.real += quat.real;
    }

    @Override
    public String toString(){
        return real + " " + imaginary.toString();
    }
    
    /**
     * Conjugate the specified quaternion
     * (x, -i,-j,-k)
     * @param quat
     * @return 
     */
    public static Quaternion conjugate(final Quaternion quat){
        return new Quaternion(new Vector4<>(
                quat.real, 
                -quat.imaginary.x,
                -quat.imaginary.y,
                -quat.imaginary.z));
        
    }
    
    /**
     * Converts the quaternion to a matrix
     * @param q
     * @return 
     */
    public static Matrix4x4 quaternionToMatrix(final Quaternion q){
        return new Matrix4x4(
                new Vector4<>(
                        q.real * q.real + q.imaginary.x * q.imaginary.x - q.imaginary.y * q.imaginary.y - q.imaginary.z * q.imaginary.z,
                        2f * (q.imaginary.x * q.imaginary.y + q.real * q.imaginary.z),
                        2f * (q.imaginary.x * q.imaginary.z - q.real * q.imaginary.y),
                        0f),
                new Vector4<>(
                        2f * (q.imaginary.x * q.imaginary.y - q.real * q.imaginary.z),
                        q.real * q.real - q.imaginary.x * q.imaginary.x + q.imaginary.y * q.imaginary.y - q.imaginary.z * q.imaginary.z,
                        2f * (q.imaginary.y * q.imaginary.z + q.real * q.imaginary.x),
                        0f),
                new Vector4<>(
                        2 * (q.imaginary.x * q.imaginary.z + q.real * q.imaginary.y),
                        2 * (q.imaginary.y * q.imaginary.z - q.real * q.imaginary.x),
                        q.real * q.real - q.imaginary.x * q.imaginary.x - q.imaginary.y * q.imaginary.y + q.imaginary.z * q.imaginary.z,
                        0f),
                new Vector4<>(0f,0f,0f,1f)
        );
    }
    
    /**
     * Copy the contents of another quaternion and places it in this one
     * @param quat 
     */
    public void set(Quaternion quat){
        this.real = quat.real;
        this.imaginary.set(quat.imaginary);
    }
    
    /**
     * Copy the contents of a vector4 and places it in this quaternion
     * X = real number
     * Y = imaginary.i
     * Z = imaginary.j
     * W = imaginary.k
     * @param vec 
     */
    public void set(Vector4<Float> vec){
        this.real = vec.x;
        this.imaginary.x = vec.y;
        this.imaginary.y = vec.z;
        this.imaginary.z = vec.w;
    }
    
    
    public static Vector3<Float> multiply(final Quaternion quat, final Vector3<Float> vec){
        final Quaternion vecQuat = new Quaternion();
        vecQuat.real = 0;
        vecQuat.imaginary.set(Vector3.unitVector(vec));
        final Quaternion res = Quaternion.multiply(quat, Quaternion.multiply(vecQuat, Quaternion.conjugate(quat)));
        return new Vector3(res.imaginary.x,res.imaginary.y,res.imaginary.z);
    }
    
    /**
     * Multiplies two quaternions with each other
     * @param quat1
     * @param quat2
     * @return 
     */
    public static Quaternion multiply(final Quaternion quat1, final Quaternion quat2){
        return new Quaternion(new Vector4<>(
                quat1.real * quat2.real - quat1.imaginary.x * quat2.imaginary.x - quat1.imaginary.y * quat2.imaginary.y - quat1.imaginary.z * quat2.imaginary.z,
                quat1.real * quat2.imaginary.x + quat1.imaginary.x * quat2.real + quat1.imaginary.y * quat2.imaginary.z - quat1.imaginary.z * quat2.imaginary.y,
                quat1.real * quat2.imaginary.y - quat1.imaginary.x * quat2.imaginary.z + quat1.imaginary.y * quat2.real + quat1.imaginary.z * quat2.imaginary.x,
                quat1.real * quat2.imaginary.z - quat1.imaginary.x * quat2.imaginary.y + quat1.imaginary.y * quat2.imaginary.x - quat1.imaginary.z * quat2.real
        ));
    }
    
    public Quaternion(final Vector4<Float> other){
        this.imaginary = new Vector3<>(other.y,other.z,other.w);
        this.real = other.x;
    }
    

    
    
}
