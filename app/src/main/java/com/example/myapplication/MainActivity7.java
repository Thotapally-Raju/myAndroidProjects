package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Locale;

public class MainActivity7 extends AppCompatActivity {

    Button speak;
    EditText input;
    Button stop;
    TextToSpeech speech;
    TextView error;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        if(speech.isSpeaking())
        {
            speech.stop();
        }
        Intent intent =new Intent(MainActivity7.this,MainActivity4.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);
        speak=findViewById(R.id.speak);
        input=findViewById(R.id.textBox);
        error=findViewById(R.id.error);
        stop=findViewById(R.id.stop);
        speech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i!=TextToSpeech.ERROR){
                    speech.setLanguage(Locale.US);
                }
            }
        });
        speak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(input.getText().toString().trim().isEmpty())
                {
                    error.setText("Nothing to speak");
                }
                else {
                    error.setText("");
                    speech.speak(input.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(speech.isSpeaking())
                {
                    speech.stop();
                }
                else{
                    Toast.makeText(MainActivity7.this,"No ongoing speech",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}