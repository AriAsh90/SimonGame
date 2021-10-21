package azi.ari.simonapp1;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resultView = (TextView) findViewById(R.id.resultView);

    }

    public void startNewGame(View view) {

        Intent startGameIntent = new Intent(this, GameOn.class);
        startActivityForResult(startGameIntent,0);
    }

    @Override
    protected void onActivityResult(int reqCode,int resCode,Intent data){
        super.onActivityResult(reqCode,resCode,data);

        if(resCode == RESULT_OK)
            resultView.setText(String.valueOf(data.getIntExtra("result",0)));
        else
            resultView.setText(String.valueOf(0));
    }

    @Override
    protected void onPause(){
        super.onPause();

    }

    @Override
    protected void onStop(){
        super.onStop();

    }
}
