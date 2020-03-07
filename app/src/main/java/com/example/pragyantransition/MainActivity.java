package com.example.pragyantransition;

import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

public class MainActivity extends AppCompatActivity implements ICallback {
    public static float cube1 = 0.0f;
    public static boolean dropCubes;
    ConstraintLayout constraintLayout;
    float centreX,centreY;
    GLSurfaceView view,pragyanview;
    Button fix;
    public static float screenWidthRatio=0.0f;
    float divisionX;
    float divisionY;
    public static boolean startProjection=false;
    public static boolean startPragyanCube=false;
    int lightHeight;
    ImageView light,logo;
    GLSurfaceView.Renderer renderer;
    Animation fadein,project;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        project=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.project);
         fadein=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fadein);

        constraintLayout = findViewById(R.id.constraint);
        light=findViewById(R.id.light);
        logo=findViewById(R.id.logo);
        light.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED);

        lightHeight=light.getMeasuredHeight();

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);
        constraintSet.connect(R.id.logo,ConstraintSet.BOTTOM,R.id.light,ConstraintSet.BOTTOM,2*lightHeight/5);
        constraintSet.applyTo(constraintLayout);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float height = displayMetrics.heightPixels;
        float width = displayMetrics.widthPixels;
        screenWidthRatio=0.4f*(((float)width)/((float)height));

        renderer = new OpenGLRenderer(this);
        centreX=width/2;
        centreY=height/2;
        divisionX=width/OpenGLRenderer.xLimit;
        divisionY=height/OpenGLRenderer.yLimit;
        project=AnimationUtils.loadAnimation(getApplicationContext(),R.anim.project);

        logo.setVisibility(View.INVISIBLE);
        light.setVisibility(View.INVISIBLE);

        initializeGl();





    }
    private void initializeGl(){
        view = findViewById(R.id.gl);
        view.setZOrderOnTop(true);
        view.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        view.setRenderer(renderer);
        view.getHolder().setFormat(PixelFormat.TRANSPARENT);
        fix = findViewById(R.id.fix);
        fix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OpenGLRenderer.dropCubes = true;


            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        float x = (float) event.getX();
        float y = (float) event.getY();

            float a=(x-centreX)/divisionX;
            float b=-(y-centreY)/divisionY;
            OpenGLRenderer.touchX=a;
            OpenGLRenderer.touchY=b;
        switch (event.getAction()) {

            case MotionEvent.ACTION_UP:OpenGLRenderer.touchX=2*OpenGLRenderer.xLimit;OpenGLRenderer.touchY=2*OpenGLRenderer.yLimit;break;
        }

            Log.v("Touch","x : "+x+" y:"+y);

        return false;
    }
    public void startProjection(){
        light.setVisibility(View.VISIBLE);
        light.startAnimation(project);
        logo.setVisibility(View.VISIBLE);
        logo.startAnimation(fadein);
    }


    @Override
    public void callback() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(!startProjection){
                    startProjection();
                    startProjection=true;
                }
            }
        });

    }
}
