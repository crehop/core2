#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;
varying vec2 v_texCoordActual;
varying float intensity;

uniform sampler2D u_texture0;
uniform vec4 u_ambientColor;
uniform vec4 u_diffuseColor;
uniform vec4 u_specularColor;


const vec4 fog_colour = vec4(0.21, 0.22, 0.20, 1.);
vec4 add_fog(vec4 fragColour){
 	float perspective_far = 100000.0;
 	float fog_coord = (gl_FragCoord.z / gl_FragCoord.w) / perspective_far;
  	float fog_density = 6.0;
  	float fog = fog_coord * fog_density;
  	return mix(fog_colour, fragColour, clamp(1.0 - fog, 0., 1.) );
}

void main(void) {
	vec4 water = texture2D(u_texture0, v_texCoord0.st);// * v_color;
	vec4 premix = mix(water,  vec4(-intensity,-intensity,-intensity,-intensity), .25);
	gl_FragColor = add_fog(mix(premix,v_color, 0.5));
}