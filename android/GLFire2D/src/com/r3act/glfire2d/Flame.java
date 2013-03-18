package com.r3act.glfire2d;

import Utils.Object3D;
import Utils.ShaderProgram;

public class Flame extends Object3D{
	
	public Flame()
	{	
		super();
		
		program = ShaderProgram.create( vShader, fShader );
		
		vertices = new float[]{
				
				-1.0f, -1.0f, 0.0f,
				1.0f, -1.0f, 0.0f,
				-1.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f
				
		};
		
		uvs = new float[]{
				
				0.0f, 0.0f,
				1.0f, 0.0f,
				0.0f, 1.0f,
				1.0f, 1.0f
				
		};
		
		faces = new short[]{
				
			0, 2, 1,
			1, 2, 3
				
		};
		
		init();
	}
	
	
	
	private static String vShader = "\n" +  
		
		"uniform mat4 projectionMatrix; \n" +
		"uniform mat4 viewMatrix; \n" +
		"uniform mat4 modelMatrix; \n" +
		
		"attribute vec3 position; \n" +
		"attribute vec2 uv; \n" +
		
		/*"varying vec2 vUv;\n" +*/ 

		"void main() { \n" +

			/*"vUv = uv; \n" +*/
			"gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4( position, 1.0 ); \n" +

		"} \n";
	
	private static String fShader = "\n" +  
	
		"void main() { \n" +
		
			"gl_FragColor = vec4( 1.0, 1.0, 1.0, 1.0 ); \n" +
		
		"} \n";
	
//	private static String fShader = "\n" +  
//			
//	"uniform sampler2D texNoise1x1; \n" +
//	"uniform sampler2D texNoise2x2; \n" +
//	"uniform sampler2D texNoise3x3; \n" +
//	"uniform sampler2D texFire; \n" +
//	"uniform sampler2D texAlpha; \n" +
//
//	"uniform float time; \n" +
//
//	"varying vec2 vUv; \n" +
//
//	"void main() { \n" +
//
//		/* Scroll the textures upwards */
//		"float y = vUv.y + 1.0 - fract( time ); \n" +
//		"vec2 scrollcoord = vec2( vUv.x, y ); \n" +
//		"vec4 noise1 = texture2D( texNoise1x1, scrollcoord ); \n" +
//
//		"y = vUv.y + 1.0 - fract( time * 1.3 ); \n" +
//		"scrollcoord = vec2( vUv.x, y ); \n" +
//		"vec4 noise2 = texture2D( texNoise2x2, scrollcoord ); \n" +
//
//		"y = vUv.y + 1.0 - fract( time * 2.3 ); \n" +
//		"scrollcoord = vec2( vUv.x, y ); \n" +
//		"vec4 noise3 = texture2D( texNoise3x3, scrollcoord ); \n" +
//
//		/* Move range of color to -1 / +1 */
//		"noise1.xyz = noise1.xyz * 2.0 - 1.0; \n" +
//		"noise2.xyz = noise2.xyz * 2.0 - 1.0; \n" +
//		"noise3.xyz = noise3.xyz * 2.0 - 1.0; \n" +
//
//		/* Distort color */
//		"vec2 distortion = vec2( 0.1, 0.2 ); \n" +
//		"noise1.xy = noise1.xy * distortion.xy; \n" +
//		"distortion = vec2( 0.1, 0.3 ); \n" +
//		"noise2.xy = noise2.xy * distortion.xy; \n" +
//		"distortion = vec2( 0.1, 0.1 ); \n" +
//		"noise3.xy = noise3.xy * distortion.xy; \n" +
//
//		/* Get final noise */
//		"vec4 noise = noise1 + noise2 + noise3; \n" +
//
//		/* Perturb the final noise */
//		"float perturbation = ( ( vUv.y ) * 0.8 ) + 0.5; \n" +
//		"noise.xy = ( noise.xy * perturbation ) + vUv.xy; \n" +
//
//		/* Get color from noise and fire texture */
//		"vec4 color = texture2D( texFire, noise.xy ); \n" +
//		"color.a = texture2D( texAlpha, noise.xy ).x ; \n" +
//
//		"gl_FragColor = color; \n" +
//
//	"}";
	
}
