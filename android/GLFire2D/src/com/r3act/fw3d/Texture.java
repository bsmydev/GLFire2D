package com.r3act.fw3d;

import java.io.IOException;
import java.io.InputStream;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

public class Texture
{

	private static String TAG = "fw3d.Texture";
	
	/* Maximum of 10 textures for the moment */
	private static int[] ids = new int[ 10 ];
	private static int count = 0;
	
	protected int id;
	protected String alias;
	protected int resource;
	
	public Texture( int resource, String alias )
	{
		this.resource = resource;
		this.alias = alias;
		
		create();
		load();
	}
	
	private void create()
	{
		/* Create texture id */
		
		GLES20.glGenTextures( 1, ids, count );
		id = ids[ count ];
		count++;
		
		/* Adjust texture parameters */
		GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, id );
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST );
        GLES20.glTexParameterf( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT );
        GLES20.glTexParameteri( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT );
        
	}
	
	/* Load the texture */
	public void load()
	{
		InputStream is = GLRenderer.context.getResources().openRawResource( resource );
		
		Bitmap bitmap = BitmapFactory.decodeStream( is );
		
		try
        {
            is.close();
        }
        catch( IOException e )
        {
            // Ignore.
        }
		
		if ( bitmap != null )
		{
			GLES20.glBindTexture( GLES20.GL_TEXTURE_2D, id );
			GLUtils.texImage2D( GLES20.GL_TEXTURE_2D, 0, bitmap, 0 );
	        bitmap.recycle();
		}
		else
		{
			Log.e( TAG, "Could'nt load texture..." );
		}       
	}
}
