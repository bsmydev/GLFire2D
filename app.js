GLFire = {};

GLFire.App = ( function(){

	var scene = null,
		camera = null,
		renderer = null,

		flames = [],
		nbFlames = 6,

	setup = function(){

		scene = new THREE.Scene(),

		/* Setup camera */
		camera = new THREE.PerspectiveCamera( 60, window.innerWidth / window.innerHeight, 1, 10000 );
	    camera.lookAt( new THREE.Vector3( 0, 0, 1 ) );
	    camera.up = new THREE.Vector3( 0, 1, 0 );
	    camera.position = new THREE.Vector3( 0, 0, -100 );
	    camera.rMatrix = ( new THREE.Matrix4() ).makeRotationAxis( new THREE.Vector3( 0, 1, 0 ), Math.PI / 180 );

	    scene.add( camera );

	    /* create fire */
	    ( function(){

	    	var i = 0,
			object = null;

	    	for ( ; i < nbFlames; i++ )
		    {
		        object = new GLFire.Flame();
		        object.rotation.y = i * ( Math.PI / nbFlames );
		        flames.push( object );
		        scene.add( object );
		    }

	    } )();
				

	    /* Setup renderer */
	    ( function(){

	    	var container = document.getElementById( "container" ),
	    		canvas = document.getElementById( "canvas" ),

	    		resize = function(){

	    			var width = container.offsetWidth,
	    				height = container.offsetHeight;
	    				
	    			renderer.setSize( width, height );
	    			camera.aspect = width / height;
					camera.updateProjectionMatrix();
	    		};
	    
		    renderer = new THREE.WebGLRenderer( {

		        canvas : canvas,
		        clearColor : 0x000000,
		        clearAlpha: 1,
		        antialias : true 
		    
		    } );

		    resize();

		    window.onresize = resize;

	    } )();

	    animate();	    

	},

	animate = function(){

		var i = 0;

		for ( ; i < flames.length; i++ )
		{
		    flames[ i ].animate();
		}

		camera.position.applyMatrix4( camera.rMatrix );
    	camera.lookAt( new THREE.Vector3( 0, 0, 1 ) );
	
		render();   	

	},

	render = function(){

		renderer.setDepthTest( false );
		//renderer.setViewport( 0, 0, window.innerWidth, window.innerHeight );
    	renderer.render( scene, camera );

    	requestAnimationFrame( animate );

	};



	return {

		start : function(){

			setup();

		}

	};

} )();