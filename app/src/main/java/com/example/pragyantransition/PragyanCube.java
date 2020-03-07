package com.example.pragyantransition;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import static com.example.pragyantransition.OpenGLRenderer.yLimit;
import static com.example.pragyantransition.OpenGLRenderer.z;
import static com.example.pragyantransition.SplashUtils.cubeSize;
import static java.lang.StrictMath.abs;

class PragyanCube {


    //    public final CubePoint VELOCITY_INIT = new CubePoint(0f, 3.5f);
    public final static float VELOCITY_THRES = 0.01f * 3.5f;
    private final static String TAG = "Cube";
    public boolean firstBounce = false;
    public float yTranslate = -2*yLimit;
    public float xTranslate = 0.0f;
    public float velocity = 0.003f;
    public float rateOfX;
    public float rateofDrop;
    public float rotationRate;
    public float mCubeRotation;
    float r = 2*cubeSize;
    private FloatBuffer mVertexBuffer;
    private FloatBuffer mColorBuffer;
    private ByteBuffer mIndexBuffer;
    private float[] vertices = {
            -r, -r, -r,
            r, -r, -r,
            r, r, -r,
            -r, r, -r,
            -r, -r, r,
            r, -r, r,
            r, r, r,
            -r, r, r
    };
    private float[] colors = {
            0.583f, 0.771f, 0.014f,
            0.609f, 0.115f, 0.436f,
            0.327f, 0.483f, 0.844f,
            0.822f, 0.569f, 0.201f,
            0.435f, 0.602f, 0.223f,
            0.310f, 0.747f, 0.185f,
            0.597f, 0.770f, 0.761f,
            0.559f, 0.436f, 0.730f,
            0.359f, 0.583f, 0.152f,
            0.483f, 0.596f, 0.789f,
            0.559f, 0.861f, 0.639f,
            0.195f, 0.548f, 0.859f,
            0.014f, 0.184f, 0.576f,
            0.771f, 0.328f, 0.970f,
            0.406f, 0.615f, 0.116f,
            0.676f, 0.977f, 0.133f,
            0.971f, 0.572f, 0.833f,
            0.140f, 0.616f, 0.489f,
            0.997f, 0.513f, 0.064f,
            0.945f, 0.719f, 0.592f,
            0.543f, 0.021f, 0.978f,
            0.279f, 0.317f, 0.505f,
            0.167f, 0.620f, 0.077f,
            0.347f, 0.857f, 0.137f,
            0.055f, 0.953f, 0.042f,
            0.714f, 0.505f, 0.345f,
            0.783f, 0.290f, 0.734f,
            0.722f, 0.645f, 0.174f,
            0.302f, 0.455f, 0.848f,
            0.225f, 0.587f, 0.040f,
            0.517f, 0.713f, 0.338f,
            0.053f, 0.959f, 0.120f,
            0.393f, 0.621f, 0.362f,
            0.673f, 0.211f, 0.457f,
            0.820f, 0.883f, 0.371f,
            0.982f, 0.099f, 0.879f
    };
    private byte[] indices = {
            0, 4, 5, 0, 5, 1,
            1, 5, 6, 1, 6, 2,
            2, 6, 7, 2, 7, 3,
            3, 7, 4, 3, 4, 0,
            4, 7, 6, 4, 6, 5,
            3, 0, 1, 3, 1, 2
    };

    public PragyanCube() {
        rotationRate = 0.07f;
        ByteBuffer byteBuf = ByteBuffer.allocateDirect(vertices.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mVertexBuffer = byteBuf.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);

        byteBuf = ByteBuffer.allocateDirect(colors.length * 4);
        byteBuf.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuf.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);

        mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
        mIndexBuffer.put(indices);
        mIndexBuffer.position(0);
        final int min = 0;
        final int max = 1;
        final int random = new Random().nextInt((max - min) + 1) + min;
        Log.d(TAG, String.valueOf(random));

    }

    public void draw(GL10 gl) {
        gl.glFrontFace(GL10.GL_CW);

        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, mVertexBuffer);
        gl.glColorPointer(4, GL10.GL_FLOAT, 0, mColorBuffer);

        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

        gl.glDrawElements(GL10.GL_TRIANGLES, 36, GL10.GL_UNSIGNED_BYTE,
                mIndexBuffer);

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
    }

    public void updateDrop() {
        if(MainActivity.startPragyanCube){
            Log.d(TAG, "yTranslate: " + yTranslate);
            if(yTranslate>=0.0-2*cubeSize){
                stopCube();
            }
            yTranslate += velocity;
            xTranslate += rateOfX;

            Log.d(TAG, rateofDrop + "," + velocity + "," + yTranslate);
            mCubeRotation -= rotationRate;
        }

    }


    public void stopCube() {
        yTranslate=-2*cubeSize;
        rateofDrop = 0;
        velocity = 0;
        rateOfX = 0;
        if(mCubeRotation>0){
            mCubeRotation=0;
            rotationRate = 0;


        }else{
            mCubeRotation = mCubeRotation+(3.0f*rotationRate);

        }
    }


}