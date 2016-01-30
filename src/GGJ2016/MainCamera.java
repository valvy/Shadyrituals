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
package GGJ2016;

import PrutEngine.Camera;
import PrutEngine.Core.Math.Quaternion;
import PrutEngine.Core.Math.Vector3;
import PrutEngine.Debug;

/**
 *
 * @author Heiko van der Heijden
 */
public final class MainCamera extends Camera {
    
    private float shakeMagnitude = 0;
    private float shakeDuration = 0;
    private final Quaternion oldQuaternion;
    public MainCamera(Vector3<Float> position) {
        super(position);
        this.oldQuaternion = this.getRotationQuaternion();
    }
    
    @Override
    public void update(float tpf){
        super.update(tpf);
        
        if(this.shakeDuration > 0 && this.shakeMagnitude > 0){
            this.rotate(new Vector3<>(1f,1f,1f), (float) Math.sin(this.shakeDuration - (this.shakeDuration * this.shakeMagnitude)  ));
            this.shakeDuration -= tpf;
        }else{
            this.setRotation(oldQuaternion);
        }
        
    }
    
    
    public void shakeScreen(float magnitude, float duration){
        this.shakeMagnitude = magnitude;
        this.shakeDuration = duration;
    }
}
