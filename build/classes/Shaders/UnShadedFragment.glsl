#version 400 core

layout(location = 0) out vec4 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;


void main(void){

    color = texture(text,fs_in.UV);
}