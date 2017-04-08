#version 400 core

layout(location = 0) out vec4 mainColor;

in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;
uniform vec2 resolution;
vec2 mouse = vec2(100,100);
  
vec2 iSphere( in vec3 ro, in vec3 rd, in vec4 sph )//from iq
{
    vec3 oc = ro - sph.xyz;
    float b = dot( oc, rd );
    float c = dot( oc, oc ) - sph.w * sph.w;
    float h = b*b - c;
    if( h<0.0 ) return vec2(-1.0);
    h = sqrt(h);
    return vec2(-b-h, -b+h );
}

vec3 orbit(vec3 p)
{
    return abs(p)/dot(p,p);
}

float map(in vec3 position)
{
    const float iterations 	= 12.;
    vec3 origin 		= position;
    float mass		= 4.;
    
    float energy 		= 0.;
    for (float i = 0.; i < iterations; ++i)
    {
        
        position		= abs(position-.1)-.13;
        position 		= orbit(position);
        position		= abs(position.yzx*8.)-2.;
        
        float velocity 		= abs(dot(position, origin));
        energy 			+= exp(mass*-velocity)*1.5;
    }
    
    return energy*energy/iterations;
}

vec3 derivative(vec3 position, float delta)
{
    vec2 offset 	= vec2(delta, 0.);
    vec3 normal 	= vec3(0.);
    normal.x 	= map(position+offset.xyy)-map(position-offset.xyy);
    normal.y 	= map(position+offset.yxy)-map(position-offset.yxy);
    normal.z 	= map(position+offset.yyx)-map(position-offset.yyx);
    return normalize(normal);
}

mat2 rmat(float r)
{
    float c = cos(r);
    float s = sin(r);
    return mat2(c, s, -s, c);
}


vec3 hsv(float h,float s,float v)
{
    return mix(vec3(1.),clamp((abs(fract(h+vec3(3.,2.,1.)/3.)*6.-3.)-1.),0.,1.),s)*v;
}


vec3 view(vec2 pixel, vec3 origin)
{
    vec3 w = normalize( origin );
    vec3 u = normalize( cross(w,vec3(0.0,1.0,0.0) ) );
    vec3 v = normalize( cross(u,w));
    
    float fov = 1.124;
    
    return normalize(pixel.x*u + pixel.y*v + fov * w);
}


void main(void){
    // screen
    vec2 aspect 		= resolution/min(resolution.x, resolution.y);
    vec2 pixel 		= gl_FragCoord.xy/resolution.xy;
    vec2 uv	 		= (pixel - .5) * aspect * 2.;
    vec2 mouse 		= (mouse - .5) * aspect * 2.;
    
    
    // view origin
    vec3 origin 		= vec3(3.);
    const float tau		= 8.*atan(1.);
    origin.yz		*= rmat(mouse.y*tau);
    origin.xz		*= rmat(mouse.x*tau);
    
    
    //ray position
    vec3 position		= origin;
    
    
    // view direction
    vec3 direction		= view(uv, origin);

    
    
    
    // raymarch
    vec2 bound = iSphere( origin, direction, vec4(0.,0.,0.,	3.5) );
    float range 		= 0.;
    float total_range	= bound.x;
    vec3 light		= vec3(0.);
    vec3 normal 		= vec3(0.);
    
    vec3 color 		= vec3(0.);
    
    float decay		= .9;
    float steps		= 0.;
    for(float i = 1.; i < 64.; i++)
    {
        total_range	+= exp(range);
        
        if (total_range > bound.y) break;
        
        position 	= origin + direction * total_range;
        range 		= abs(map(position));
        
        
        color		+= hsv(range+steps, range, range*.5)*length(position)*.5;
        color 		*= decay;
        
        steps++;
        normal 		= derivative(position, range);
        color 		+= max(0., dot(normal, normalize(position-light)))/steps*.05;
    }
    
    mainColor = vec4(color,1) * texture(text,fs_in.UV);
}