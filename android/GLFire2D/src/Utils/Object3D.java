package Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Object3D {

	private static final int SHORT_SIZE_BYTES = 4;
	private static final int FLOAT_SIZE_BYTES = 4;
	
	private static String vShader = null;
	private static String fShader = null;
	
	protected float[] vertices = null;
	protected FloatBuffer verticesBuffer = null;
	
	protected float[] uvs = null;
	protected FloatBuffer uvsBuffer = null;
	
	protected short[] faces = null;
	protected ShortBuffer facesBuffer = null;
	
	protected int program;
	
	public Object3D()
	{
		vShader = "";
		fShader = "";
		
		vertices = new float[]{};
		uvs = new float[]{};
		faces = new short[]{};
		
		init();
	}
	
	public int getProgram()
	{
		return program;
	}
	
	public void init()
	{
		verticesBuffer = ByteBuffer.allocateDirect( vertices.length * FLOAT_SIZE_BYTES )
		                .order( ByteOrder.nativeOrder() )
		                .asFloatBuffer();
		verticesBuffer.put( vertices ).position( 0 );
		
		uvsBuffer = ByteBuffer.allocateDirect( uvs.length * FLOAT_SIZE_BYTES )
		                .order( ByteOrder.nativeOrder() )
		                .asFloatBuffer();
		uvsBuffer.put( uvs ).position( 0 );
		
		facesBuffer = ByteBuffer.allocateDirect( faces.length * SHORT_SIZE_BYTES )
						.order( ByteOrder.nativeOrder() )
						.asShortBuffer();		
    	facesBuffer.put( faces ).position( 0 );
	}
	
	public FloatBuffer getVerticesBuffer()
	{
		return verticesBuffer;
	}
	
	public FloatBuffer getUvsBuffer()
	{
		return uvsBuffer;
	}
	
	public ShortBuffer getFacesBuffer()
	{
		return facesBuffer;
	}
	
	public int getFacesLength()
	{
		return faces.length;
	}
}
