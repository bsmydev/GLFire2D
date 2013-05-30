GLFire.Flame = function()
{
	var geometry = new THREE.PlaneGeometry( 50, 50 ),
		material = null;

	this.texNoise1x1.wrapS = this.texNoise1x1.wrapT = THREE.RepeatWrapping;
	this.texNoise2x2.wrapS = this.texNoise2x2.wrapT = THREE.RepeatWrapping;
	this.texNoise3x3.wrapS = this.texNoise3x3.wrapT = THREE.RepeatWrapping;

	this.texFire.wrapS = this.texFire.wrapT = THREE.RepeatWrapping;
	
	material = new THREE.ShaderMaterial( {

		uniforms : {

			texNoise1x1 : { type : "t", value : this.texNoise1x1 },
			texNoise2x2 : { type : "t", value : this.texNoise2x2 },
			texNoise3x3 : { type : "t", value : this.texNoise3x3 },
			texFire : { type : "t", value : this.texFire },
			texAlpha : { type : "t", value : this.texAlpha },
			//scrollSpeed : { type : "f", value : 0.3 },
			time : { type : "f", value : 1.0 }

		},
		vertexShader : this.vertexShader,
		fragmentShader : this.fragmentShader

	} );

	material.blendSrc = THREE.OneFactor;
	material.blendDst = THREE.OneFactor;
	material.blendEquation = THREE.AddEquation;

	material.side = THREE.DoubleSide;
	material.transparent = true;
	material.depthTest = false;

	THREE.Mesh.call( this, geometry, material );

	this.doubleSided = true;
};


GLFire.Flame.prototype = new THREE.Mesh();


GLFire.Flame.prototype.animate = function()
{
	this.material.uniforms.time.value += 0.01;
	this.material.uniforms.time.needsUpdate = true;
};


GLFire.Flame.prototype.vertexShader = [

	"varying vec2 vUv;",

	"void main() {",

		"vUv = uv;",
		"gl_Position = projectionMatrix * modelViewMatrix * vec4( position, 1.0 );",

	"}"

].join( "\n" );


GLFire.Flame.prototype.fragmentShader = [

	"uniform sampler2D texNoise1x1;",
	"uniform sampler2D texNoise2x2;",
	"uniform sampler2D texNoise3x3;",
	"uniform sampler2D texFire;",
	"uniform sampler2D texAlpha;",

	"uniform float time;",

	"varying vec2 vUv;",

	"void main() {",

		"/* Scroll the textures upwards */",

		"float y = vUv.y + 1.0 - fract( time );",
		"vec2 scrollcoord = vec2( vUv.x, y );",
		"vec4 noise1 = texture2D( texNoise1x1, scrollcoord );",

		"y = vUv.y + 1.0 - fract( time * 1.3 );",
		"scrollcoord = vec2( vUv.x, y );",
		"vec4 noise2 = texture2D( texNoise2x2, scrollcoord );",

		"y = vUv.y + 1.0 - fract( time * 2.3 );",
		"scrollcoord = vec2( vUv.x, y );",
		"vec4 noise3 = texture2D( texNoise3x3, scrollcoord );",

		"/* Move range of color to -1 / +1 */",
		"noise1.xyz = noise1.xyz * 2.0 - 1.0;",
		"noise2.xyz = noise2.xyz * 2.0 - 1.0;",
		"noise3.xyz = noise3.xyz * 2.0 - 1.0;",

		"/* Distort color */",
		"vec2 distortion = vec2( 0.1, 0.2 );",
		"noise1.xy = noise1.xy * distortion.xy;",
		"distortion = vec2( 0.1, 0.3 );",
		"noise2.xy = noise2.xy * distortion.xy;",
		"distortion = vec2( 0.1, 0.1 );",
		"noise3.xy = noise3.xy * distortion.xy;",

		"/* Get final noise */",
		"vec4 noise = noise1 + noise2 + noise3;",

		"/* Perturb the final noise */",
		"float perturbation = ( ( vUv.y ) * 0.8 ) + 0.5;",
		"noise.xy = ( noise.xy * perturbation ) + vUv.xy;",

		"/* Get color from noise and fire texture */",
		"vec4 color = texture2D( texFire, noise.xy );",
		"color.a = texture2D( texAlpha, noise.xy ).x ;",

		"gl_FragColor = color;",

	"}"

].join( "\n" );

GLFire.Flame.prototype.texNoise1x1 = THREE.ImageUtils.loadTexture( "tex_noise_1x1.gif" );
GLFire.Flame.prototype.texNoise2x2 = THREE.ImageUtils.loadTexture( "tex_noise_2x2.gif" );
GLFire.Flame.prototype.texNoise3x3 = THREE.ImageUtils.loadTexture( "tex_noise_3x3.gif" );
GLFire.Flame.prototype.texFire = THREE.ImageUtils.loadTexture( "tex_fire.gif" );
GLFire.Flame.prototype.texAlpha = THREE.ImageUtils.loadTexture( "tex_alpha.gif" );