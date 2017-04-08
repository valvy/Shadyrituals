#version 400 core

out vec4 color;

uniform sampler2D text;

uniform vec3 diffuse_albedo = vec3(0.2, 0.2, 0.2);
uniform vec3 specular_albedo = vec3(0.2);
uniform float specular_power = 18.0;
uniform vec3 ambient = vec3(0.1, 0.2, 0.1);

in VS_OUT{
    vec2 UV;
    vec3 normal;
    vec3 view;
    vec3 light;
} fs_in;

void main(void){
    vec3 N = normalize(fs_in.normal);
    vec3 L = normalize(fs_in.light);
    vec3 V = normalize(fs_in.view);
    vec3 R = reflect(-L, N);
    
    vec3 diffuse = max(dot(N, L), 0.0) * diffuse_albedo;
    vec3 specular = pow(max(dot(R, V), 0.0), specular_power) * specular_albedo;
      color = vec4(ambient + diffuse + specular + texture(text,fs_in.UV).rgb, fs_in.UV);
    
}

