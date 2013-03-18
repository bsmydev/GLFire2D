package Utils;

import android.opengl.GLES20;
import android.util.Log;

public class ShaderProgram {

	private static String TAG = "ShaderProgram";
	
	public static int create( String vertexSource, String fragmentSource ) {
		
        int vertexShader = Shader.create( GLES20.GL_VERTEX_SHADER, vertexSource );
        if ( vertexShader == 0 )
        {
            return -1;
        }

        int fragmentShader = Shader.create( GLES20.GL_FRAGMENT_SHADER, fragmentSource );        
        if ( fragmentShader == 0 )
        {
            return -1;
        }

        int program = GLES20.glCreateProgram();
        
        if ( program != 0 ) {
        	
            GLES20.glAttachShader( program, vertexShader );
            ErrorUtils.check( "glAttachShader" );
            
            GLES20.glAttachShader( program, fragmentShader );
            ErrorUtils.check( "glAttachShader" );
            
            GLES20.glLinkProgram( program );
            int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES20.GL_TRUE)
            {
                Log.e( TAG, "Could not link program : " );
                Log.e( TAG, GLES20.glGetProgramInfoLog( program ) );
                GLES20.glDeleteProgram( program );
                program = -1;
            }
        }
        return program;
    }
	
}
