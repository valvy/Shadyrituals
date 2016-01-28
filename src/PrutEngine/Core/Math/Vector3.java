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

/**
 *
 * @author Heiko van der Heijden
 */

public class Vector3<T> {

    
    public T x, y ,z;
    
    public Vector3(T x, T y, T z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Vector3(final Vector3<T> other){
        this.set(other);
    }
    
    
    
    public static float norm(final Vector3<Float> vec){
        return (float) Math.sqrt(Math.pow(vec.x, 2) + Math.pow(vec.y, 2) + Math.pow(vec.z, 2));
    }
    
    public static Vector3<Float> unitVector(final Vector3<Float> vec){

        float length = vec.x * vec.x + vec.y * vec.y + vec.z + vec.z;
        if(length != 1f && length != 0f){
            length = (float) (1.0f / Math.sqrt(length));
            return new Vector3<>(vec.x * length,vec.y * length, vec.z * length );
        }
      
        return new Vector3<>(vec);
    }
    
    public void set(final Vector3<T> other){
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }
    
    @Override
    public String toString(){
        return x.toString() + " " + y.toString() + " " + z.toString();
    }
    
}
