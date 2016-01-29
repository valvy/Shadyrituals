//#version 400 core
#version 200 es 
layout(location = 0) out vec4 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;


void main(void){

    
    color = vec4(texture(text,fs_in.UV).rgb, fs_in.UV);
}