package com.example.pragyantransition;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glPushMatrix;
import static android.opengl.GLES20.GL_EQUAL;
import static android.opengl.GLES20.GL_FALSE;
import static android.opengl.GLES20.GL_LEQUAL;
import static com.example.pragyantransition.MainActivity.screenWidthRatio;
import static com.example.pragyantransition.PragyanCube.VELOCITY_THRES;
import static com.example.pragyantransition.SplashUtils.cubeSize;
import static java.lang.Math.abs;

class OpenGLRenderer implements GLSurfaceView.Renderer {

    private final static String TAG = "OpenGLRenderer";
    public static float z=10.0f;
    public static boolean dropCubes = false;
    private Cube[] cube = new Cube[36];
    private int rows,columns;
    private PragyanCube pragyanCube=new PragyanCube();
    public static float xLimit=screenWidthRatio*z;
    public static float yLimit=0.35f*z;
    public static float touchX=xLimit*2.0f;
    public static float touchY=yLimit*2.0f;
    ICallback ic;

    public OpenGLRenderer(ICallback ic) {
       this.ic=ic;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT,
                GL10.GL_FASTEST);

        gl.glClearColor(0, 0, 0, 0);
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        rows=3;
        columns=cube.length/rows;

        for(int i=0;i<cube.length;i++){
            Log.v("CHECK",i+" cube");
            cube[i]=new Cube();
            if(i<columns){
                if(i<columns/2){
                    Log.v("CHECK",i+" cube here"+columns/2 );

                    cube[i].xTranslate=-(((columns/2)-i)*3*(cubeSize)+xLimit);
                    Log.v("CHECK",i+" cube "+cube[i].xTranslate);

                }
                else  if(i>columns/2){
                    Log.v("CHECK",i+" cube here 1");
                    cube[i].xTranslate=cube[i-1].xTranslate+3*(cubeSize);
                    Log.v("CHECK",i+" cube "+cube[i].xTranslate);


                }
                else{
                    cube[i].xTranslate=cube[i-1].xTranslate+2.5f*xLimit;
                    Log.v("CHECK",i+" cube "+cube[i].xTranslate);

                }
                cube[i].yTranslate=-3*cubeSize;

            }
            else{
                Log.v("CHECK",i+" cube here2");

                cube[i].xTranslate=cube[i-(cube.length/(rows))].xTranslate;
                cube[i].yTranslate=cube[i-(cube.length/(rows))].yTranslate+3*cubeSize;

            }



        }




    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

        for (int i = 0; i < cube.length; i++) {
            glPushMatrix();
            gl.glTranslatef(cube[i].xTranslate, cube[i].yTranslate, -z);
            gl.glRotatef(cube[i].mCubeRotation, 1.0f, 1.0f, 1.0f);


            if (dropCubes) {
                MainActivity.startPragyanCube=true;
                  if(cube[i].velocity>0){
                      cube[i].updateDrop();
                  }
                  if(cube[i].yTranslate<-yLimit){
                      cube[i].stopCube();
                      cube[i].yTranslate=-yLimit;
                      cube[i].updateDrop();
                  }else{
                      cube[i].updateDrop();
                  }
                  if(cube[i].velocity!=0){
                    isCollidingY(i);
                      cube[i].updateDrop();
                  }
//                if (i > 4) {
//                    cube[i].stopCube();
//                    cube[i].xTranslate = 11f;
//                    cube[i].yTranslate = 11f;
//                }
//
//                //P
//                cube[0].stopCube();
//                cube[0].xTranslate = -xLimit;
//                cube[0].yTranslate = -yLimit;
//                cube[1].stopCube();
//                cube[1].xTranslate = cube[0].xTranslate;
//                cube[1].yTranslate = cube[0].yTranslate + cubeSize;
//                cube[2].stopCube();
//                cube[2].xTranslate = cube[1].xTranslate;
//                cube[2].yTranslate = cube[1].yTranslate + cubeSize;
//                cube[3].stopCube();
//                cube[3].xTranslate = cube[2].xTranslate;
//                cube[3].yTranslate = cube[2].yTranslate + cubeSize;
//                cube[4].stopCube();
//                cube[4].xTranslate = cube[3].xTranslate;
//                cube[4].yTranslate = cube[3].yTranslate + cubeSize;
//                cube[5].stopCube();
//                cube[5].xTranslate = cube[4].xTranslate + cubeSize;
//                cube[5].yTranslate = cube[4].yTranslate;
//                cube[6].stopCube();
//                cube[6].xTranslate = cube[5].xTranslate + cubeSize;
//                cube[6].yTranslate = cube[5].yTranslate - cubeSize;
//                cube[7].stopCube();
//                cube[7].xTranslate = cube[6].xTranslate - cubeSize;
//                cube[7].yTranslate = cube[6].yTranslate - cubeSize;
//
//                //I
//
//                cube[8].stopCube();
//                cube[8].xTranslate = cube[7].xTranslate + 3 * cubeSize;
//                cube[8].yTranslate = -yLimit;
//                cube[9].stopCube();
//                cube[9].xTranslate = cube[8].xTranslate;
//                cube[9].yTranslate = cube[8].yTranslate + cubeSize;
//                cube[10].stopCube();
//                cube[10].xTranslate = cube[9].xTranslate;
//                cube[10].yTranslate = cube[9].yTranslate + cubeSize;
//                cube[11].stopCube();
//                cube[11].xTranslate = cube[10].xTranslate;
//                cube[11].yTranslate = cube[10].yTranslate + cubeSize;
//                cube[12].stopCube();
//                cube[12].xTranslate = cube[11].xTranslate;
//                cube[12].yTranslate = cube[11].yTranslate + 2*cubeSize;
//
//                //X
//
//                cube[13].stopCube();
//                cube[13].xTranslate = cube[12].xTranslate + 2 * cubeSize;
//                cube[13].yTranslate = -yLimit;
//                cube[14].stopCube();
//                cube[14].xTranslate = cube[13].xTranslate+cubeSize;
//                cube[14].yTranslate = cube[13].yTranslate + cubeSize;
//                cube[15].stopCube();
//                cube[15].xTranslate = cube[14].xTranslate-cubeSize;
//                cube[15].yTranslate = cube[14].yTranslate + cubeSize;
//                cube[16].stopCube();
//                cube[16].xTranslate = cube[14].xTranslate+cubeSize;
//                cube[16].yTranslate = cube[14].yTranslate + cubeSize;
//                cube[17].stopCube();
//                cube[17].xTranslate = cube[14].xTranslate+cubeSize;
//                cube[17].yTranslate = cube[14].yTranslate-cubeSize;
//
//                //e
//
//                cube[18].stopCube();
//                cube[18].xTranslate = cube[17].xTranslate + 2 * cubeSize;
//                cube[18].yTranslate = -yLimit;
//                cube[19].stopCube();
//                cube[19].xTranslate = cube[18].xTranslate;
//                cube[19].yTranslate = cube[18].yTranslate + cubeSize;
//                cube[20].stopCube();
//                cube[20].xTranslate = cube[19].xTranslate;
//                cube[20].yTranslate = cube[19].yTranslate + cubeSize;
//                cube[21].stopCube();
//                cube[21].xTranslate = cube[20].xTranslate;
//                cube[21].yTranslate = cube[20].yTranslate + cubeSize;
//                cube[22].stopCube();
//                cube[22].xTranslate = cube[21].xTranslate;
//                cube[22].yTranslate = cube[21].yTranslate + cubeSize;
//                cube[23].stopCube();
//                cube[23].xTranslate = cube[22].xTranslate+cubeSize;
//                cube[23].yTranslate = cube[22].yTranslate;
//
//                cube[24].stopCube();
//                cube[24].xTranslate = cube[20].xTranslate+cubeSize;
//                cube[24].yTranslate = cube[20].yTranslate;
//                cube[25].stopCube();
//                cube[25].xTranslate = cube[18].xTranslate+cubeSize;
//                cube[25].yTranslate = cube[18].yTranslate;
//
//                cube[26].stopCube();
//                cube[26].xTranslate = cube[25].xTranslate + 2 * cubeSize;
//                cube[26].yTranslate = -yLimit;
//                cube[27].stopCube();
//                cube[27].xTranslate = cube[26].xTranslate;
//                cube[27].yTranslate = cube[26].yTranslate + cubeSize;
//                cube[28].stopCube();
//                cube[28].xTranslate = cube[27].xTranslate;
//                cube[28].yTranslate = cube[27].yTranslate + cubeSize;
//                cube[29].stopCube();
//                cube[29].xTranslate = cube[28].xTranslate;
//                cube[29].yTranslate = cube[28].yTranslate + cubeSize;
//                cube[30].stopCube();
//                cube[30].xTranslate = cube[26].xTranslate+cubeSize;
//                cube[30].yTranslate = cube[26].yTranslate;
//                cube[31].stopCube();
//                cube[31].xTranslate = cube[30].xTranslate+cubeSize;
//                cube[31].yTranslate = cube[30].yTranslate;
//
//
//
            }
            else {
                if(cube[i].velocity==0){

                        for(int k=0;k<rows;k++){
                            if(i>=k*columns&&i<(k*columns)+(columns/2)){
                                cube[i].rateOfX=cube[i].initialRateX;
                                break;
                            }
                            else if(k==rows-1){
                                cube[i].rateOfX=-cube[i].initialRateX;

                            }
                        }

                }

                if (isColliding(i)) {
                    if(cube[i].velocity>0) {
                        cube[i].velocity = -2 * cube[i].initialRateX;
                    }
                    else if(cube[i].velocity==0){

                            if(((i)%rows==0)||((i-1)%rows==0)){
                                cube[i].velocity=-2*cube[i].initialRateX;

                            }else{
                                cube[i].velocity=+2*cube[i].initialRateX;

                            }


                    }
                    else{
                        cube[i].velocity=2*cube[i].initialRateX;
                    }
                    cube[i].velocity = -cube[i].velocity;
                    cube[i].rateOfX = -cube[i].rateOfX;
                }

                //touch
                if(touchX<10f){
                    if(isCollidingTouch(touchX,touchY,i)){
                        cube[i].velocity = -cube[i].velocity;
                        cube[i].rateOfX = -cube[i].rateOfX;
                    }
                }
                cube[i].updateRandom();

            }
            cube[i].draw(gl);

            gl.glPopMatrix();

            glPushMatrix();

            gl.glTranslatef(pragyanCube.xTranslate, pragyanCube.yTranslate, -z);
            gl.glRotatef(pragyanCube.mCubeRotation, 1.0f, 1.0f, 1.0f);

            if(MainActivity.startPragyanCube){
                pragyanCube.updateDrop();
                if(pragyanCube.mCubeRotation==0){
                    ic.callback();
                }
            }
            pragyanCube.draw(gl);

            gl.glPopMatrix();








        }
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45.0f, (float) width / (float) height, 0.1f, 100.0f);
        gl.glViewport(0, 0, width, height);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    private boolean isColliding(int idx) {
        for (int i = 0; i < cube.length; i++) {
            float diffX = abs(cube[idx].xTranslate - cube[i].xTranslate);
            float diffY = abs(cube[idx].yTranslate - cube[i].yTranslate);
            if (i != idx && (diffX < cubeSize && diffY < 2*cubeSize)) {
                // Collision
                Log.v(TAG, "Collision " + idx + " " + i);
                Log.v(TAG, "Collision " + diffX + " " + diffY);
                return true;
            }

        }
        return false;
    }
    private boolean isCollidingTouch(float x,float y,int i) {

            float diffX = abs(x - cube[i].xTranslate);
            float diffY = abs(y - cube[i].yTranslate);
            if (diffX < 3*cubeSize && diffY < 2*cubeSize) {
                // Collision
                Log.v(TAG, "Collision " + diffX + " " + diffY);
                return true;
            }else{
                return false;

            }
    }
    private boolean isCollidingY(int idx) {
        for (int i = 0; i < idx; i++) {
            float diffX = abs(cube[idx].xTranslate - cube[i].xTranslate);
            float diffY = abs(cube[idx].yTranslate - cube[i].yTranslate);
            if (diffX < cubeSize && diffY < 1*cubeSize) {
                // Collision
                Log.v(TAG, "Collision " + idx + " " + i);
                Log.v(TAG, "Collision " + diffX + " " + diffY);

                if(cube[i].velocity==0){

                        cube[idx].stopCube();
                        cube[idx].yTranslate=cube[i].yTranslate+cubeSize;


                }else{
                    cube[idx].velocity = -cube[idx].velocity;
                    cube[idx].rateOfX = -cube[idx].rateOfX;
                }


                return true;
            }

        }
        return false;
    }

}
