package com.r3act.glfire2d;
import android.app.Activity;
import android.os.Bundle;


public class MainActivity extends Activity {
	
	private static String TAG = "MainActivity";
	
    private MainGLSurfaceView mView;

    @Override
    protected void onCreate(Bundle icicle) {
    	
        super.onCreate(icicle);
        mView = new MainGLSurfaceView( getApplicationContext() );
        setContentView( mView );
        
    }

    @Override
    protected void onPause() {
    	
        super.onPause();
        mView.onPause();
        
    }

    @Override
    protected void onResume() {
    	
        super.onResume();
        mView.onResume();
        
    }

}
