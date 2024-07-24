package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity6 extends AppCompatActivity {
    Button detect;
    EditText inputText;
    LinearLayout load;
    TextView error,language;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(MainActivity6.this,MainActivity4.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main6);
        detect=findViewById(R.id.detect);
        load=findViewById(R.id.load);
        if(!Python.isStarted())
        {
            Python.start(new AndroidPlatform(MainActivity6.this));
        }
        inputText=findViewById(R.id.textBox);
        error=findViewById(R.id.error);
        language=findViewById(R.id.language);

        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sinput=inputText.getText().toString();
                if(sinput.trim().isEmpty())
                {
                   error.setText("Input cannot be Empty");
                }
                else{
                    error.setText("");
                    new LoadTask().execute(sinput);
                }

            }
        });
    }
    private class LoadTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String...a)
        {
            Python python=Python.getInstance();
            PyObject file=python.getModule("function");
            PyObject target=file.callAttr("language",a[0]);
            return target.toString();
        }
        protected  void onPostExecute(String x)
        {
            super.onPostExecute(x);
            load.setVisibility(View.GONE);
            language.setText(x);
        }
    }
}