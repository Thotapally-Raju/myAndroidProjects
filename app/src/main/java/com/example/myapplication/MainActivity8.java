package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import android.widget.*;

import android.os.AsyncTask;


public class MainActivity8 extends AppCompatActivity {

    EditText input;
    TextView text;
    ProgressBar progressBar;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main8);
        input=findViewById(R.id.input);
        text=findViewById(R.id.text);
        progressBar=findViewById(R.id.progressbar);
        button=findViewById(R.id.button);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        if(!Python.isStarted())
        {
            Python.start(new AndroidPlatform(MainActivity8.this));
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchData();
            }
        });



    }

    public void fetchData()
    {
       progressBar.setVisibility(View.VISIBLE);
       new FetchTranslatedDataTask().execute();
    }
     private class FetchTranslatedDataTask extends AsyncTask<String,String, String> {

        @Override
        protected String doInBackground(String...a) {
            Python obj=Python.getInstance();
            PyObject file=obj.getModule("function");
            PyObject res=file.callAttr("setString","en","te",input.getText().toString());
            return res.toString();
        }

        @Override
        protected void onPostExecute(String translatedText) {
            super.onPostExecute(translatedText);
            progressBar.setVisibility(ProgressBar.GONE);
            text.setText(translatedText);
        }
    }
}

