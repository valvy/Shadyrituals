#version 400 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec2 vertexUV;
layout(location = 2) in vec3 normals;

uniform mat4 cam_matrix;
uniform mat4 projection_matrix;
uniform mat4 mv_matrix;
out VS_OUT{
    vec2 UV;
    vec3 normal;
    vec3 view;
} vs_out;

void main(){
    vec4 P = projection_matrix * cam_matrix * mv_matrix * position;

    
    gl_Position =  P;
    vs_out.view = P.xyz;
    vs_out.UV = vertexUV;
    vs_out.normal = mat3(mv_matrix) * normals;

}


