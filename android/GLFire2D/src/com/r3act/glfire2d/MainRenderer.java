package com.r3act.glfire2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.r3act.fw3d.GLRenderer;
import android.content.Context;
import android.opengl.GLSurfaceView;

public class MainRenderer implements GLSurfaceView.Renderer {

	private static String TAG = "MainRenderer";
	
	private GLRenderer renderer = null;
	
	public MainRenderer( Context context )
	{
		renderer = new GLRenderer( context );
	}
	
	@Override
	public void onDrawFrame( GL10 unused )
	{		
		renderer.render();		
	}

	@Override
	public void onSurfaceChanged( GL10 unused, int width, int height )
	{		
		renderer.resize( width, height );
	}

	@Override
	public void onSurfaceCreated( GL10 unused, EGLConfig config )
	{		
		renderer.onCreate();
	}
	
}
