#version 410 core

layout(location = 0) in vec4 position;
layout(location = 1) in vec4 vertexUV;

uniform mat4 mv_matrix;
uniform mat4 projection_matrix;

out VS_OUT{
	vec4 color;
} vs_out;


void main(){
    gl_Position = projection_matrix * mv_matrix *  position;
    vs_out.color = position * 2.0 + vec4(0.5, 0.5, 0.5, 0.0);
}


