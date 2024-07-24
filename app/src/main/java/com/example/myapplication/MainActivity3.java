package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.pdf.PdfDocument;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.os.Bundle;
import com.chaquo.python.*;
import com.chaquo.python.android.AndroidPlatform;
import com.ironsoftware.ironpdf.edit.PageSelection;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.function.ToIntFunction;
import java.util.zip.Inflater;

public class MainActivity3 extends AppCompatActivity {
    Button inputText;
    Button file;
    Button convert,stop;
    TextView error;
    String parsedText = "";
    LinearLayout copy,load;
    Button speak;
    TextToSpeech textToSpeech;

    String sourceSelected="",targetSelected="";
    ArrayAdapter<String> source,target;
    TextView path1;
    Spinner sorceLanguage,targetLaguage;
    String[] sl={"Source","English","Telugu","Hindi","Tamil","Bengali","Urdu","Gujarati","Bhojpuri","Malayalam","Marathi","Punjabi","Sanskrit","Kannada","odia (oriya)","Assamese","Konkani","Sindhi","Maithili","Dogri","Meiteilon (manipuri)"};
    String[] tl={"Target","English","Telugu","Hindi","Tamil","Bengali","Urdu","Gujarati","Bhojpuri","Malayalam","Marathi","Punjabi","Sanskrit","Kannada","odia (oriya)","Assamese","Konkani","Sindhi","Maithili","Dogri","Meiteilon (manipuri)"};

    public void dialogs()
    {
            View view = LayoutInflater.from(MainActivity3.this).inflate(R.layout.dialog, null);
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity3.this);
            dialog.setView(view);
            AlertDialog alertDialog = dialog.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.show();
            Button button = alertDialog.findViewById(R.id.buttonOk);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        alertDialog.cancel();

                }
            });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(textToSpeech.isSpeaking())
        {
            textToSpeech.stop();
        }
        Intent intent=new Intent(MainActivity3.this,MainActivity4.class);
        startActivity(intent);
        finish();
    }
    public boolean isConnected( )
    {
        ConnectivityManager connectivityManager=(ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetwork()!=null && connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();
    }
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        inputText = findViewById(R.id.textInput);
        file = findViewById(R.id.fileInput);
        stop=findViewById(R.id.stop);
        sorceLanguage=findViewById(R.id.autocomplete1);
        targetLaguage=findViewById(R.id.autocomplete);
        path1 = findViewById(R.id.path);
        error=findViewById(R.id.error);
        speak=findViewById(R.id.speak);
        copy=findViewById(R.id.copy);
        load=findViewById(R.id.load);
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(MainActivity3.this));
        }
        source=new ArrayAdapter<>(this,R.layout.spinnerlayout,sl);
        sorceLanguage.setAdapter(source);
        target=new ArrayAdapter<>(this,R.layout.spinnerlayout,tl);
        convert=findViewById(R.id.convert);
        targetLaguage.setAdapter(target);
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData=ClipData.newPlainText("text copied",path1.getText());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(MainActivity3.this,"Text copied to Clipboard",Toast.LENGTH_SHORT).show();
            }
        });
        sorceLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceSelected=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        textToSpeech=new TextToSpeech(MainActivity3.this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!=TextToSpeech.ERROR)
                {
                    textToSpeech.setLanguage(Locale.ENGLISH);
                }
            }
        });
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(path1.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity3.this,"Nothing to speak",Toast.LENGTH_SHORT).show();
                }
                else {
                    textToSpeech.speak(path1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        targetLaguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetSelected=parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        inputText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 1);
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
                    Toast.makeText(MainActivity3.this,"No ongoing speech", Toast.LENGTH_SHORT).show();
                }
            }
        });
        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected())
                {
                    dialogs();
                }
                else if(sourceSelected.equals("Source"))
                {
                    error.setText("Source Language Cannot be Empty");
                }
                else if(targetSelected.equals("Target"))
                {
                    error.setText("Target Language Cannot be Empty");
                }
                else {
                    error.setText("");
                            new Load().execute(sourceSelected,targetSelected,parsedText);
                }
            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        {
            if (requestCode == 1 && resultCode == RESULT_OK) {

                if (data != null) {
                    Uri file = data.getData();
                    if (file != null) {
                        parsedText="";
                        path1.setText("");
                        readPdfFile(file);

                    }
                }


            }
        }
    }
    public void readPdfFile(Uri uri)
    {

        PdfReader reader = null;

        try {
            // Check if the URI scheme is "file"
            if ("file".equals(uri.getScheme())) {
                reader = new PdfReader(uri.getPath());
            }
            // Check if the URI scheme is "content"
            else if ("content".equals(uri.getScheme())) {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    reader = new PdfReader(inputStream);
                } else {
                    path1.setText("Please try again");
                    // Handle the case where the input stream is null (e.g., show an error message)
                }
            }

            if (reader != null) {
                int n = reader.getNumberOfPages();
                for (int i = 0; i < n; i++) {
                    parsedText = parsedText + PdfTextExtractor.getTextFromPage(reader, i + 1).trim() + "\n";
                }
            }
        } catch (IOException e) {
            path1.setText("This file is not supported");
            // Handle the exception appropriately (e.g., log it or show an error message)
        } finally {
            if (reader != null) {
                reader.close(); // Close the PdfReader in a finally block to ensure it's always closed
            }
        }
        if(parsedText.equals("")==false)
        {
            convert.setVisibility(View.VISIBLE);
        }



    }
    private class Load extends AsyncTask<String,Void,String>
    {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            load.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Python python=Python.getInstance();
            PyObject file=python.getModule("function");
            PyObject src=file.callAttr("get_language_key",strings[0]);
            PyObject target=file.callAttr("get_language_key",strings[1]);
            PyObject trans=file.callAttr("setString",src,target,strings[2]);
            return trans.toString();
        }
        @Override
        protected void onPostExecute(String x)
        {
            super.onPostExecute(x);
            load.setVisibility(View.GONE);
            path1.setText(x);
        }
    }



}






