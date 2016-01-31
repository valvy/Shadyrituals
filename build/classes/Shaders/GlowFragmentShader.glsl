#version 400 core
out vec4 color;
in VS_OUT{
    vec2 UV;
} fs_in;
uniform sampler2D text;
uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;
void main ()
{
    vec2 pos = ( gl_FragCoord.xy / resolution.xy );
    float three = 1.0 - ((pos.x + pos.y) / 12000.0);
    vec3 newColor = vec3(three, pos.x, pos.y);


    pos = gl_FragCoord.xy / resolution.xy * 2.0 - 1.0;

    for(float i=0.0;i<0.5;i+=0.05){
        newColor *= abs(1.0+i /(sin(pos.y + sin(pos.x + i*time) * 4.) * 7.0));

    }
    color = vec4(newColor, 1) * texture(text,fs_in.UV);;    

}