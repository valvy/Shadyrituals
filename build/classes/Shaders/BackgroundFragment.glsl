#version 400 core

layout(location = 0) out vec3 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform float time;


float reflectColor(float a, float b) {
    if (b == 1.0)
        return 1.;
    else
        return (a * a) / (1. - b);
}

vec3 reflectColor(vec3 a, vec3 b) {
    return vec3(
                reflectColor(a.x, b.x),
                reflectColor(a.y, b.y),
                reflectColor(a.z, b.z));
}

vec3 hsv2rgb(vec3 c)
{
    vec4 K = vec4(1.0, 2.0 / 3.0, 1.0 / 3.0, 3.0);
    vec3 p = abs(fract(c.xxx + K.xyz) * 6.0 - K.www);
    return c.z * mix(K.xxx, clamp(p - K.xxx, 0.0, 1.0), c.y);
}


void main(void){
    
    
    vec2 position = ( gl_FragCoord.xy / vec2(800,800) );
    vec3 overlay = vec3(sin(time),cos(time) * 10,tan(time) * 10);
    vec3 reflection = reflectColor(hsv2rgb(vec3(time * 0.1, 0.5, position.x)), vec3(position.y));
    color = overlay * reflection;
  
}

