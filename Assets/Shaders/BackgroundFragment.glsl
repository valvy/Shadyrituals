#version 400 core

layout(location = 0) out vec3 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform float time;
uniform sampler2D text;


void main(void){
    
    vec3 overlay = vec3(sin(time),cos(time) * 10,tan(time) * 10);
   
    color = overlay * texture(text,fs_in.UV).rgb;

}

