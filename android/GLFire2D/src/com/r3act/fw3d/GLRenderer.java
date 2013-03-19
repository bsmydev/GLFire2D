package com.r3act.fw3d;

import java.lang.reflect.Field;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Iterator;
import java.util.Set;

import com.r3act.glfire2d.Flame;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

public class GLRenderer {
	
	private static String TAG = "fw3d.GLRenderer";
	
	protected static Context context;
	
    private float[] pMatrix = new float[16];
    private float[] vMatrix = new float[16];
    private float[] mMatrix = new float[16];
    
    private int pMatrixId = -1;
    private int vMatrixId = -1;
    private int mMatrixId = -1;
    
    private int[] textureChannels;
    
    private int currentProgram = -1;
	
    private Flame flame = null;
	
	public GLRenderer( Context context )
	{
		this.context = context;

		getTextureChannels();
		
		Matrix.setIdentityM( pMatrix, 0 );
		Matrix.setIdentityM( vMatrix, 0 );
		Matrix.setIdentityM( mMatrix, 0 );
		
	}
	
	public void onCreate()
	{
		GLES20.glDisable( GLES20.GL_CULL_FACE );
		GLES20.glEnable( GLES20.GL_DEPTH_TEST );
		
		Matrix.setLookAtM( vMatrix, 0,
				0.0f, 0.0f, -2.5f,
				0.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f );
		
		flame = new Flame();
	}
	
	public void render()
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.2f, 1.0f);
		GLES20.glClear( GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT );
		
		flame.uniforms.get( 0 ).value =  0.01f + ( Float ) flame.uniforms.get( 0 ).value;
		renderObject( flame );
	}
	
	public void resize( int width, int height )
	{
		GLES20.glViewport( 0, 0, width, height );
		float ratio = ( float ) width / height;
		Matrix.perspectiveM( pMatrix, 0, 45, ratio, 1, 100);
	}
	
	private void getTextureChannels()
	{
		try 
		{
			Class<?> c = Class.forName( "android.opengl.GLES20" );
			Field[] fields = c.getFields();
			for ( int i = 0; i < fields.length; i++ )
			{
				Log.i( TAG, "GLES20 : " + fields[ i ].getName() );
			}
		}
		catch ( ClassNotFoundException e ) 
		{
		    e.printStackTrace();
		} 
	}
	
	private void renderObject( Object3D object )
	{
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE);
		
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
			ErrorUtils.check( ":Link uniform 'projection matrix'" );
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
		for ( int i = 0; i < object.uniforms.size(); i++ )
		{
			Uniform uniform = object.uniforms.get( i );
			
			int location = GLES20.glGetUniformLocation( currentProgram , uniform.alias );
			if ( location != -1 )
			{
				if ( uniform.type == GLES20.GL_FLOAT )
				{
					GLES20.glUniform1f( location, ( Float ) uniform.value );
					ErrorUtils.check( TAG + "glUniform1f : " + uniform.alias );
				}
			}			
			
		}
		
		
			/* Handle textures */
			int count = 0;
			int channelId = GLES20.GL_TEXTURE0;
			
			for ( int i = 0; i < object.textures.size(); i++ )
			{
				int id = object.textures.get( i ).id;
				String alias = object.textures.get( i ).alias;
				
				/* Get uniform location */
				int location = GLES20.glGetUniformLocation( currentProgram, alias );
				if ( location != -1 )
				{
					GLES20.glActiveTexture( channelId + count );
					GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, id );
					ErrorUtils.check( TAG + "glBindTexture texture : " + id );
					
					GLES20.glUniform1i( location, count );
					ErrorUtils.check( TAG + "glUniform1i texture : " + id );
					
					count++;
				}		        
			}
		
		
		/* Initialize object specific attributes */
		
		
		/* Draw object by faces */
		ShortBuffer fBuffer = object.getFacesBuffer();
		fBuffer.position( 0 );
		
//		GLES20.glDrawArrays( GLES20.GL_TRIANGLE_STRIP, 0, 4 );
		GLES20.glDrawElements( GLES20.GL_TRIANGLES,	object.getFacesLength(), GLES20.GL_UNSIGNED_SHORT, fBuffer );
		ErrorUtils.check( "Draw Elements" );		
		
	}

}
