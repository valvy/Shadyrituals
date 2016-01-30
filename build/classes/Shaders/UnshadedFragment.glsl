#version 400 core

layout(location = 0) out vec4 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;


void main(void){

    //vec4 tex = texture2D ( sampler, uvVarying );
    //gl_FragColor = vec4(text.r, tex.g, tex.b, tex.a);
    //color = vec4(texture(text,fs_in.UV).rgb, fs_in.UV);
    color = texture(text,fs_in.UV);
}