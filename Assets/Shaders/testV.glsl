#version 410 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 vertexUV;

uniform mat4 mv_matrix;
uniform mat4 projection_matrix;

out vec2 UV;


void main(){
    gl_Position =projection_matrix * mv_matrix *  position;
    UV = vertexUV;
}


