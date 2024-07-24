package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import java.util.Locale;

import javax.annotation.Nullable;

public class MainActivity5 extends AppCompatActivity {
    String[] targetLanguage={"Target","English","Telugu","Hindi","Tamil","Bengali","Urdu","Gujarati","Bhojpuri","Malayalam","Marathi","Punjabi","Sanskrit","Kannada","odia (oriya)","Assamese","Konkani","Sindhi","Maithili","Dogri","Meiteilon (manipuri)"};
    ArrayAdapter<String> target;
    Button speak;
    Button stop;
    Spinner autocomplete1;
    TextView text,error;
    TextToSpeech input,output;
    LinearLayout copy,copy1,load;
    Button speak1;
    String sourceL="";
    TextView trans;
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(output.isSpeaking())
        {
            output.stop();
        }
        Intent intent=new Intent(MainActivity5.this,MainActivity4.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(MainActivity5.this));
        }
        text=findViewById(R.id.source);
        speak=findViewById(R.id.voice);
        stop=findViewById(R.id.stop);
        error=findViewById(R.id.error);
        copy=findViewById(R.id.copy);
        load=findViewById(R.id.load);
        copy1=findViewById(R.id.copy2);
        speak1=findViewById(R.id.speak1);
        autocomplete1=findViewById(R.id.autocomplete);
        target=new ArrayAdapter<>(this,R.layout.spinnerlayout,targetLanguage);
        autocomplete1.setAdapter(target);
        trans=findViewById(R.id.target);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(output.isSpeaking())
                {
                    output.stop();
                }
                else{
                    Toast.makeText(MainActivity5.this,"No ongoing speech",Toast.LENGTH_SHORT).show();
                }
            }
        });
        autocomplete1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceL=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        output=new TextToSpeech(getApplicationContext(),new TextToSpeech.OnInitListener()
        {
            @Override
            public void onInit(int status)
            {
                if(status!=TextToSpeech.ERROR)
                {
                    output.setLanguage(Locale.US);
                }
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("Copied",text.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity5.this,"Copied to Clipboard",Toast.LENGTH_LONG).show();
            }
        });
        speak1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t=trans.getText().toString();
                if(t.trim().isEmpty())
                {
                    Toast.makeText(MainActivity5.this,"Nothing to speak",Toast.LENGTH_LONG).show();
                }
                else {
                    output.speak(t,TextToSpeech.QUEUE_FLUSH,null);
                }
            }
        });
        copy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("Copied",trans.getText().toString());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity5.this,"Copied to Clipboard",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void speak()
    {
        if(sourceL.equals("Target"))
        {
            error.setText("Target language cannot be Empty!!");
        }
        else {
            error.setText("");
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
            startActivityForResult(intent, 100);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==100 && resultCode==RESULT_OK)
        {
            String x=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            text.setText(x);
            new Load().execute(x);
        }
    }
    private class Load extends AsyncTask<String,Void,String> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(String...a)
        {
            Python python = Python.getInstance();
            PyObject pyModule = python.getModule("function");
            PyObject src=pyModule.callAttr("get_language_key",sourceL);
            PyObject pythonString = pyModule.callAttr("setString","en",src,a[0]);
            return pythonString.toString();
        }
        @Override
        protected void onPostExecute(String a){
            super.onPostExecute(a);
            trans.setText(a);
            load.setVisibility(View.GONE);
        }
    }
}