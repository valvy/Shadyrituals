#version 410 core

out vec4 color;
//out vec3 color;
in vec2 UV;


uniform sampler2D text;

void main(void){
    color = vec4(texture(text,UV).rgb,UV);

}
