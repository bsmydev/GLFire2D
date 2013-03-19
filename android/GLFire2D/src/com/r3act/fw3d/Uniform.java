package com.r3act.fw3d;

import android.opengl.GLES20;

public class Uniform 
{
	protected int type = -1;
	
	protected String alias = null;
	
	protected Object value = null;
	
	public Uniform( int type, String alias )
	{
		this.type = type;
		this.alias = alias;
	}
	
	public Uniform( int type, String alias, Object value )
	{
		this.type = type;
		this.alias = alias;
		this.value = value;
	}
}
