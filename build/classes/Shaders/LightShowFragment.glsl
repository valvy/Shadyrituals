#version 400 core
// By @paulofalcao
//
// Blobs
out vec4 color;
in VS_OUT{
    vec2 UV;
} fs_in;

uniform sampler2D text;

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

void main( void ) {
	//vec2 uPos = ( gl_FragCoord.xy / resolution.xy );//normalize wrt y axis
	//suPos -= vec2((resolution.x/resolution.y)/2.0, 0.0);//shift origin to center
	vec2 uPos = (( gl_FragCoord.xy / resolution.xy ) * 2.0 - 1.0)*1.;
	uPos*=normalize(resolution).xy;
	uPos.x -= 0.5;
	
	
	vec3 newColor = vec3(0.0);
	float vertColor = 9.8;
	for( float i = 0.0; i < 10.0; ++i )
	{
		float t = time * (0.9);
	
		uPos.y += sin( uPos.x*(i+1.0) + t+i/5.0 ) * 0.1;
		float fTemp = abs(21.0 / uPos.y / 1000.0);
		//vertColor += fTemp;
		newColor += vec3( (sin(t)+1.)/5.*fTemp*(2.0-i)/5.0, (sin(.5*t)+1.)/2.*fTemp*i/6.0, (sin(t)+1.)/2.*pow(fTemp,0.99)*1.5 );
	}
	
	vec4 color_final = vec4(newColor, 7.0);
	color = color_final * texture(text,fs_in.UV);
}