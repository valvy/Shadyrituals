#version 400 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 vertexUV;

float fogdensity = 0.001;

uniform mat4 cam_matrix;
uniform mat4 projection_matrix;
uniform mat4 mv_matrix;


//out vec2 UV;

void main(){
    vec4 P = projection_matrix * cam_matrix * mv_matrix * position;

    gl_Position =  P;
    //UV = vertexUV;

}


