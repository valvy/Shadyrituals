#version 400 core

layout(location = 0) out vec3 color;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;
uniform float time;
float random (vec2 st) {
    return fract(sin(dot(st.xy,
                         vec2(12.9898,78.233+0.0001)))*
                 43758.5453123);
}

void main(void){
    const vec2 size = vec2(50,50);
    vec2 pos = ( gl_FragCoord.xy / size.xy -0.5 )*vec2(size.x/size.y,1.0);
    vec2 position = vec2(fract(atan(pos.y,pos.x)/6.2831853),length(pos));
    position.x *= 100.*(sin(time/10.0));
    position.y *= 5.*((time/20.0));
    float line = floor(position.y);
    position.x += time*40.*(mod(line,2.)*2. -1.)*random(vec2(line));
    vec2 mouse = vec2(100,100);
    vec2 ipos = floor(position);
    
    color = vec3(step(mouse.y*random(vec2(line)), mouse.x*random(ipos)));
   // color = vec4(texture(text,fs_in.UV).rgb, fs_in.UV);
}