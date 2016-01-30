#version 400 core

out vec4 color;

uniform sampler2D text;

uniform sampler1D tex_toon;

uniform vec3 light_pos = vec3(0.0, 0.0, 0.0);

in VS_OUT{
    vec2 UV;
    vec3 normal;
    vec3 view;
} fs_in;

void main(void){
    vec3 N = normalize(fs_in.normal);
    vec3 L = normalize(light_pos - fs_in.view);
    float tc = pow(max(0.0, dot(N, L)), 10.0);
    //color = vec4(texture(text,fs_in.UV).rgb, fs_in.UV);
    //color =  texture(text, tc) * (tc * 0.8 + 0.2) ;
    color = vec4(texture(text,fs_in.UV).rgb, tc) * (tc * 0.9 + 0.5);
    
}

// vec4 tmp =  vec4(sin(gl_FragCoord.x * 0.25) * 0.5 + 0.5,
// cos(gl_FragCoord.y * 0.25) * 0.5 + 0.5,sin(gl_FragCoord.x * 0.15) * cos(gl_FragCoord.y * 0.1),1.0);
