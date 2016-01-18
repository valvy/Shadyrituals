#version 410 core

in vec4 position;
uniform mat4 mv_matrix;
uniform mat4 projection_matrix;

void main(){
    gl_Position = projection_matrix * mv_matrix *  position;
}

