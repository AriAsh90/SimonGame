package azi.ari.simonapp1;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class GameOn extends AppCompatActivity implements SensorEventListener {
    private double pitchX, rollY, yawZ;
    private int curLength, curInd, maxLength=200, curOrientation;
    private int[] seqArray = new int[maxLength];
    private float[] rotationMatrix = new float[16];
    private float[] orientationValues=new float[3];
    private Sensor rotSensor, magnetSensor;
    private SensorManager sensorManager;
    private TextView commandView,seqLengthView,curIndView,curLengthView, upView, leftView, rightView, downView;
    public static final int CENTER=0, UP=1, LEFT=2, RIGHT=3,DOWN=4;
    MediaPlayer centerSound, upSound, leftSound, rightSound, downSound;
    private boolean isPlaying,isCenterRequired;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_on);

        centerSound = MediaPlayer.create(getApplicationContext(), R.raw.center_sound);
        upSound = MediaPlayer.create(getApplicationContext(), R.raw.up_sound);
        leftSound = MediaPlayer.create(getApplicationContext(), R.raw.left_sound);
        rightSound = MediaPlayer.create(getApplicationContext(), R.raw.right_sound);
        downSound = MediaPlayer.create(getApplicationContext(), R.raw.down_sound);

        commandView = findViewById(R.id.commandView);
        seqLengthView = findViewById(R.id.seqLengthView);
        curIndView = findViewById(R.id.curIndView);
        curLengthView = findViewById(R.id.curLengthView);
        upView = findViewById(R.id.upView);
        leftView = findViewById(R.id.leftView);
        rightView = findViewById(R.id.rightView);
        downView = findViewById(R.id.downView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        rotSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        upView.setBackgroundColor(getResources().getColor(R.color.upNormal));
        leftView.setBackgroundColor(getResources().getColor(R.color.leftNormal));
        rightView.setBackgroundColor(getResources().getColor(R.color.rightNormal));
        downView.setBackgroundColor(getResources().getColor(R.color.downNormal));

        Random rand = new Random();
        for(int i=0; i<maxLength; i++){
            seqArray[i]= rand.nextInt(4)+1;
        }

        Toast.makeText(getApplicationContext(),"GAME READY \n PRESS START", Toast.LENGTH_SHORT).show();
        curLength=0;
    }

    public void startButtonClick(View view) {
        view.setVisibility(View.INVISIBLE);
        computerSeq();
        sensorManager.registerListener(this,rotSensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void computerSeq(){
        //Toast.makeText(getApplicationContext(),"MEMORIZE THE FOLLOWING SEQUENCE!", Toast.LENGTH_SHORT).show();
        final Toast toast1 = Toast.makeText(getApplicationContext(), "MEMORIZE THE FOLLOWING SEQUENCE!", Toast.LENGTH_SHORT);
        toast1.show();
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast1.cancel();
            }
        }, 1500);
        isPlaying=false;
        curLength++;
        commandView.setText("MEMORIZE!");
        seqLengthView.setText(String.valueOf(curLength));
        curLengthView.setText(String.valueOf(curLength));
        upView.setBackgroundColor(getResources().getColor(R.color.upNormal));
        leftView.setBackgroundColor(getResources().getColor(R.color.leftNormal));
        rightView.setBackgroundColor(getResources().getColor(R.color.rightNormal));
        downView.setBackgroundColor(getResources().getColor(R.color.downNormal));
        Thread computerSeqThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for(curInd=0; curInd<curLength; curInd++){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            curIndView.setText(String.valueOf(curInd+1));
                            switch(seqArray[curInd]){
                                case UP:
                                    upView.setBackgroundColor(getResources().getColor(R.color.upPressed));
                                    if (upSound.isPlaying()) {
                                        upSound.stop();
                                        upSound.release();
                                        upSound = MediaPlayer.create(getApplicationContext(), R.raw.up_sound);
                                    }
                                    upSound.start();
                                    break;
                                case LEFT:
                                    leftView.setBackgroundColor(getResources().getColor(R.color.leftPressed));
                                    if (leftSound.isPlaying()) {
                                        leftSound.stop();
                                        leftSound.release();
                                        leftSound = MediaPlayer.create(getApplicationContext(), R.raw.left_sound);
                                    }
                                    leftSound.start();
                                    break;
                                case RIGHT:
                                    rightView.setBackgroundColor(getResources().getColor(R.color.rightPressed));
                                    if (rightSound.isPlaying()) {
                                        rightSound.stop();
                                        rightSound.release();
                                        rightSound = MediaPlayer.create(getApplicationContext(), R.raw.right_sound);
                                    }
                                    rightSound.start();
                                    break;
                                case DOWN:
                                    downView.setBackgroundColor(getResources().getColor(R.color.downPressed));
                                    if (downSound.isPlaying()) {
                                        downSound.stop();
                                        downSound.release();
                                        downSound = MediaPlayer.create(getApplicationContext(), R.raw.down_sound);
                                    }
                                    downSound.start();
                                    break;
                            }
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            upView.setBackgroundColor(getResources().getColor(R.color.upNormal));
                            leftView.setBackgroundColor(getResources().getColor(R.color.leftNormal));
                            rightView.setBackgroundColor(getResources().getColor(R.color.rightNormal));
                            downView.setBackgroundColor(getResources().getColor(R.color.downNormal));
                        }
                    });
                }
                curInd=0;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(getApplicationContext(),"IT'S YOUR TURN NOW!", Toast.LENGTH_SHORT).show();
                        final Toast toast2 = Toast.makeText(getApplicationContext(), "IT'S YOUR TURN NOW!", Toast.LENGTH_SHORT);
                        toast2.show();
                        Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                toast2.cancel();
                            }
                        }, 1000);
                        curIndView.setText(String.valueOf(curInd+1));
                        commandView.setText("CENTER!");
                    }
                });
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                isPlaying=true;
                isCenterRequired=true;
            }
        });
        computerSeqThread.start();
    }

  @Override
  public void onSensorChanged(SensorEvent sensorEvent) {
      if(isPlaying) {
          SensorManager.getRotationMatrixFromVector(rotationMatrix,sensorEvent.values);
          curOrientation = determineOrientation(rotationMatrix);
          if (isCenterRequired) {
              if (curOrientation == CENTER) {
                  if (centerSound.isPlaying()) {
                      centerSound.stop();
                      centerSound.release();
                      centerSound = MediaPlayer.create(getApplicationContext(), R.raw.center_sound);
                  }
                  centerSound.start();
                  upView.setBackgroundColor(getResources().getColor(R.color.upNormal));
                  leftView.setBackgroundColor(getResources().getColor(R.color.leftNormal));
                  rightView.setBackgroundColor(getResources().getColor(R.color.rightNormal));
                  downView.setBackgroundColor(getResources().getColor(R.color.downNormal));
                  isCenterRequired = false;
                  commandView.setText("PLAY!");
              }
          }
          else
              if (curOrientation == seqArray[curInd]) {
                  if (curInd == curLength - 1)
                      computerSeq();
                  else {
                      curInd++;
                      curIndView.setText(String.valueOf(curInd+1));
                      isCenterRequired = true;
                      commandView.setText("CENTER!");
                  }
              }
              else
                  if(curOrientation != CENTER)
                    gameOver();
          }
      }

    private int determineOrientation(float[] rotationMatrix) {

        SensorManager.getOrientation(rotationMatrix,orientationValues);

        pitchX = Math.toDegrees(orientationValues[1]);
        rollY = Math.toDegrees(orientationValues[2]);

        if(pitchX>30)
           curOrientation = UP;
        if(pitchX<-30)
           curOrientation = DOWN;
        if(rollY>30)
            curOrientation = RIGHT;
        if(rollY<-30)
            curOrientation = LEFT;
        if((pitchX<30) && (pitchX>-30) && (rollY<30) && (rollY>-30))
            curOrientation = CENTER;
        if(!isCenterRequired) {
            switch(curOrientation){
                case UP:
                    upView.setBackgroundColor(getResources().getColor(R.color.upPressed));
                    if (upSound.isPlaying()) {
                        upSound.stop();
                        upSound.release();
                        upSound = MediaPlayer.create(getApplicationContext(), R.raw.up_sound);
                    }
                    upSound.start();
                    break;
                case LEFT:
                    leftView.setBackgroundColor(getResources().getColor(R.color.leftPressed));
                    if (leftSound.isPlaying()) {
                        leftSound.stop();
                        leftSound.release();
                        leftSound = MediaPlayer.create(getApplicationContext(), R.raw.left_sound);
                    }
                    leftSound.start();
                    break;
                case RIGHT:
                    rightView.setBackgroundColor(getResources().getColor(R.color.rightPressed));
                    if (rightSound.isPlaying()) {
                        rightSound.stop();
                        rightSound.release();
                        rightSound = MediaPlayer.create(getApplicationContext(), R.raw.right_sound);
                    }
                    rightSound.start();
                    break;
                case DOWN:
                    downView.setBackgroundColor(getResources().getColor(R.color.downPressed));
                    if (downSound.isPlaying()) {
                        downSound.stop();
                        downSound.release();
                        downSound = MediaPlayer.create(getApplicationContext(), R.raw.down_sound);
                    }
                    downSound.start();
                    break;
            }
        }
        return curOrientation;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    public void gameOver(){
        Toast.makeText(getApplicationContext(),"GAME OVER!", Toast.LENGTH_SHORT).show();
        sensorManager.unregisterListener(this);
        centerSound.release();
        upSound.release();
        leftSound.release();
        rightSound.release();
        downSound.release();
        Intent finishGameIntent = new Intent();
        finishGameIntent.putExtra("result",curLength-1);
        setResult(Activity.RESULT_OK,finishGameIntent);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this);
        centerSound.release();
        upSound.release();
        leftSound.release();
        rightSound.release();
        downSound.release();
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
        centerSound.release();
        upSound.release();
        leftSound.release();
        rightSound.release();
        downSound.release();
    }
}
