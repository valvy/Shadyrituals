#version 400 core

layout(location = 0) out vec4 color;
in vec2 UV;

uniform sampler2D text;


void main(void){
    color = vec4(texture(text,UV).rgb, UV);
}