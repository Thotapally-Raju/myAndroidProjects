package com.example.myapplication;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    TextView text;
    LinearLayout ltext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ltext=findViewById(R.id.ltext);
        text=findViewById(R.id.text);
        Animation anim=AnimationUtils.loadAnimation(MainActivity.this,R.anim.moveandzoom);
        ltext.startAnimation(anim);
        text.startAnimation(anim);
        dialog();
        
    }
    public boolean isConnected( )
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetwork()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    public void dialog()
    {
        if(!isConnected()) {
            View view=LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog,null);
            AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
            dialog.setView(view);
            AlertDialog alertDialog= dialog.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            Button button=alertDialog.findViewById(R.id.buttonOk);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isConnected())
                    {
                        dialog();
                    }
                    else {
                        Intent intent=new Intent(MainActivity.this,MainActivity4.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else
        {

            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent intent=new Intent(MainActivity.this,MainActivity4.class);
                    startActivity(intent);
                    finish();
                }
            },1500);


        }
    }
}