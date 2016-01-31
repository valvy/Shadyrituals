#version 400 core

layout(location = 0) out vec3 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform float time;



void main(void){
    
    
  
    vec3 overlay = vec3(sin(time),cos(time) * 10,tan(time) * 10);
    color = overlay;

}

