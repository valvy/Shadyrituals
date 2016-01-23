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
package PrutEngine.Core.Math;

import PrutEngine.Debug;

/**
 *
 * @author heikovanderheijden
 */
public class Quaternion {
    public final Vector3<Float> imaginary;
    public float real;
 
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
    

    @Override
    public String toString(){
        return real + " " + imaginary.toString();
    }
    
    public static Quaternion conjugate(final Quaternion quat){
        return new Quaternion(new Vector4<>(
                quat.real, 
                -quat.imaginary.x,
                -quat.imaginary.y,
                -quat.imaginary.z));
        
    }
    
    public void set(Quaternion quat){
        this.real = quat.real;
        this.imaginary.set(quat.imaginary);
    }
    
    public void set(Vector4<Float> vec){
        this.real = vec.x;
        this.imaginary.x = vec.y;
        this.imaginary.y = vec.z;
        this.imaginary.z = vec.w;
    }
    
    public static Quaternion multiply(final Quaternion quat1, final Quaternion quat2){
        return new Quaternion(new Vector4<>(
                quat1.real * quat2.real - quat1.imaginary.x * quat2.imaginary.x - quat1.imaginary.y * quat2.imaginary.y - quat1.imaginary.z * quat2.imaginary.z,
                quat1.real * quat2.imaginary.x + quat1.imaginary.x * quat2.real - quat1.imaginary.y * quat2.imaginary.z + quat1.imaginary.z * quat2.imaginary.y,
                quat1.real * quat2.imaginary.y + quat1.imaginary.x * quat2.imaginary.z + quat1.imaginary.y * quat2.real - quat1.imaginary.z * quat2.imaginary.x,
                quat1.real * quat2.imaginary.z - quat1.imaginary.x * quat2.imaginary.y + quat1.imaginary.y * quat2.imaginary.x + quat1.imaginary.z * quat2.real
        ));
    }
    
    public Quaternion(final Vector4<Float> other){
        this.imaginary = new Vector3<>(other.y,other.z,other.w);
        this.real = other.x;
    }
    

    
    
}
