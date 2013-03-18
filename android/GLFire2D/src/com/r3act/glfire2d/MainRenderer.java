package com.r3act.glfire2d;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import Utils.ErrorUtils;
import Utils.Object3D;
import Utils.Shader;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

public class MainRenderer implements GLSurfaceView.Renderer {

	private static String TAG = "MainRenderer";
	
	private Context mContext;
	
    private float[] pMatrix = new float[16];
    private float[] vMatrix = new float[16];
    private float[] mMatrix = new float[16];
    
    private int pMatrixId = -1;
    private int vMatrixId = -1;
    private int mMatrixId = -1;
    
    
    private int currentProgram = -1;
	
    private Flame flame = null;    
    
	public MainRenderer( Context context )
	{
		mContext = context;
		Matrix.setIdentityM( pMatrix, 0 );
		Matrix.setIdentityM( vMatrix, 0 );
		Matrix.setIdentityM( mMatrix, 0 );
	}
	
	@Override
	public void onDrawFrame( GL10 unused ) {
		
		GLES20.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);
		GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT );
		render( flame );
		
	}

	@Override
	public void onSurfaceChanged( GL10 unused, int width, int height )
	{		
		GLES20.glViewport( 0, 0, width, height );
		float ratio = ( float ) width / height;
		Matrix.frustumM( pMatrix, 0, -ratio, ratio, -1, 1, 1, 150 );
	}

	@Override
	public void onSurfaceCreated( GL10 unused, EGLConfig config ) {
		
		GLES20.glDisable( GLES20.GL_CULL_FACE );
		GLES20.glDisable( GLES20.GL_DEPTH_TEST );
		
		Matrix.setLookAtM( vMatrix, 0,
				0.0f, 0.0f, -10.0f,
				0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f );
		
		flame = new Flame();
		
	}
	
	private void render( Object3D object )
	{
		/* Link shader program */
		int program = object.getProgram();
		if ( program != currentProgram )
		{
			GLES20.glUseProgram( program );
			ErrorUtils.check( "Use Program" );
			currentProgram = program;
		}		
		
		/* Initialize general uniforms */
		pMatrixId = GLES20.glGetUniformLocation( currentProgram, "projectionMatrix" );
		if ( pMatrixId != -1 )
		{
			GLES20.glUniformMatrix4fv( pMatrixId, 1, false, pMatrix, 0 );
		}		
		
		vMatrixId = GLES20.glGetUniformLocation( currentProgram, "viewMatrix" );
		if ( vMatrixId != -1 )
		{
			GLES20.glUniformMatrix4fv( vMatrixId, 1, false, vMatrix, 0 );
		}
		
		mMatrixId = GLES20.glGetUniformLocation( currentProgram, "modelMatrix" );
		if ( mMatrixId != -1 )
		{
			GLES20.glUniformMatrix4fv( mMatrixId, 1, false, mMatrix, 0 );
		}
		
		
		/* Initialize general attributes */
		
		int positionId = GLES20.glGetAttribLocation( currentProgram, "position" );
		if ( positionId != -1 )
		{
			FloatBuffer vBuffer = object.getVerticesBuffer();
			vBuffer.position( 0 );
			GLES20.glVertexAttribPointer( positionId, 3, GLES20.GL_FLOAT, false, 0, vBuffer );      
	        GLES20.glEnableVertexAttribArray( positionId );        
	        ErrorUtils.check( "Link Attribute 'position'" );
		}
		
		int uvId = GLES20.glGetAttribLocation( currentProgram, "uv" );
		if ( uvId != -1 )
		{
			GLES20.glVertexAttribPointer( uvId, 2, GLES20.GL_FLOAT, false,
	                0, object.getUvsBuffer() );      
	        GLES20.glEnableVertexAttribArray( uvId );        
	        ErrorUtils.check( "Link Attribute 'uv'" );
		}
		
		
		
		/* Initialize object specific uniforms */
		
		
		/* Initialize object specific attributes */
		
		
		/* Draw object by faces */
		ShortBuffer fBuffer = object.getFacesBuffer();
		fBuffer.position( 0 );
		
		GLES20.glDrawElements( GLES20.GL_TRIANGLES,	object.getFacesLength(), GLES20.GL_UNSIGNED_SHORT, fBuffer );
		ErrorUtils.check( "Draw Elements" );		
		
	}
	
}
