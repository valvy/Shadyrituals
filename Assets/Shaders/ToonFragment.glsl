#version 400 core

out vec4 color;
//in vec2 UV;

uniform sampler2D text;


void main(void){
    vec4 tmp =  vec4(sin(gl_FragCoord.x * 0.25) * 0.5 + 0.5,
    cos(gl_FragCoord.y * 0.25) * 0.5 + 0.5,sin(gl_FragCoord.x * 0.15) * cos(gl_FragCoord.y * 0.1),1.0);
    color = tmp;//vec4(texture(text,UV).rgb, UV) * tmp;
}