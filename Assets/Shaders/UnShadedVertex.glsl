#version 400 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 vertexUV;
layout(location = 2) in vec3 normals;


uniform mat4 cam_matrix;
uniform mat4 projection_matrix;
uniform mat4 mv_matrix;

out VS_OUT{
    vec2 UV;
} vs_out;


void main(){
    vs_out.UV = vertexUV;
    vec4 P = projection_matrix * cam_matrix * mv_matrix * position;

    gl_Position =  P;
  

}


