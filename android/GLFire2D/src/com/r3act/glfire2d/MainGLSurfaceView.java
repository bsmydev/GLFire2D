package com.r3act.glfire2d;

import android.content.Context;
import android.opengl.GLSurfaceView;


public class MainGLSurfaceView extends GLSurfaceView {

	private static String TAG = "MainGLSurfaceView";
	private MainRenderer renderer;
	
	public MainGLSurfaceView( Context context ) {
		
		super( context );
		setEGLContextClientVersion( 2 );
		renderer = new MainRenderer( context );
		setRenderer( renderer );
		
	}
	
}
