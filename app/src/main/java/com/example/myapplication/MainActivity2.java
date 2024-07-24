package com.example.myapplication;

import static android.system.Os.close;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class MainActivity2 extends AppCompatActivity {
    String[] targetLanguage={"Target","English","Telugu","Hindi","Tamil","Bengali","Urdu","Gujarati","Bhojpuri","Malayalam","Marathi","Punjabi","Sanskrit","Kannada","odia (oriya)","Assamese","Konkani","Sindhi","Maithili","Dogri","Meiteilon (manipuri)"};
    String[] sourceLanguage={"Source","English","Telugu","Hindi","Tamil","Bengali","Urdu","Gujarati","Bhojpuri","Malayalam","Marathi","Punjabi","Sanskrit","Kannada","odia (oriya)","Assamese","Konkani","Sindhi","Maithili","Dogri","Meiteilon (manipuri)"};

    Spinner ssource;

    LinearLayout progressBar;
    Spinner starget;
    Button speak;
    ClipboardManager cbm;
    TextToSpeech textToSpeech;
    TextView error;
    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> source;

    TextInputEditText input;
    Button button,stop;
    TextView target;
    String name="";
    String sourceL="";
    LinearLayout copy;
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
        startActivity(intent);
        if(textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(MainActivity2.this));
        }
        ssource=findViewById(R.id.source);
        starget=findViewById(R.id.destination);
        button=findViewById(R.id.button);
        progressBar=findViewById(R.id.load);
        copy=findViewById(R.id.copy);
        target=findViewById(R.id.target);
        speak=findViewById(R.id.speak);
        input=findViewById(R.id.textBox);
        stop=findViewById(R.id.stop);
        error=findViewById(R.id.error);
        source=new ArrayAdapter<>(this,R.layout.spinnerlayout,sourceLanguage);
        ssource.setAdapter(source);
        ssource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceL=parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        arrayAdapter=new ArrayAdapter<>(this,R.layout.spinnerlayout,targetLanguage);
        starget.setAdapter(arrayAdapter);
        starget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                name=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text",target.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        textToSpeech=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR){
                    textToSpeech.setLanguage(Locale.UK);
                }
            }
        });
        speak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String fin = target.getText().toString();
                if(fin.trim().isEmpty())
                {
                    Toast.makeText(MainActivity2.this,"Nothing to speak",Toast.LENGTH_SHORT).show();
                }
                else {
                    textToSpeech.speak(fin,TextToSpeech.QUEUE_FLUSH,null);

                }

            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(textToSpeech.isSpeaking())
                {
                    textToSpeech.stop();
                }
                else{
                    Toast.makeText(MainActivity2.this,"No Ongoing speech",Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fin = input.getText().toString();
                if(fin.trim().isEmpty())
                {
                    error.setText("Nothing to Convert");

                }
                else if(sourceL.equals("Source"))
                {
                    error.setText("Source Language Cannot be Empty");
                }
                else if(name.equals("Target"))
                {
                    error.setText("Target Language Cannot be Empty");
                }
                else
                {
                    error.setText("");
                    Python python = Python.getInstance();
                    PyObject pyModule = python.getModule("function");
                    PyObject src=pyModule.callAttr("get_language_key",sourceL);
                    PyObject key = pyModule.callAttr("get_language_key", name);
                    new Load().execute(src.toString(),key.toString(),fin);
                }
            }

        });
    }
    private class Load extends AsyncTask<String,Void,String>{
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String...a)
        {
            String src=a[0];
            String target=a[1];
            String text=a[2];
            Python instance=Python.getInstance();
            PyObject file=instance.getModule("function");
            PyObject result=file.callAttr("setString",src,target,text);
            try {
                return result.toString();
            }
            catch(Exception e)
            {
                return "Unknown data found";
            }

        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            target.setText(result);
        }
    }
}