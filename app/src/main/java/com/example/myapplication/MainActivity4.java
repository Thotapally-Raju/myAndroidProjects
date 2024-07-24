package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity4 extends AppCompatActivity {

    LinearLayout text,image,voice,pdf,findtype,texttovoice;
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        View view = LayoutInflater.from(MainActivity4.this).inflate(R.layout.exitdialog, null);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity4.this);
        dialog.setView(view);
        AlertDialog alertDialog = dialog.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        Button cancelButton = alertDialog.findViewById(R.id.cancelButton);
        Button okButton = alertDialog.findViewById(R.id.okButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main4);
        pdf=findViewById(R.id.pdf);
        voice=findViewById(R.id.voice);
        image=findViewById(R.id.image);
        text=findViewById(R.id.text);
        texttovoice=findViewById(R.id.texttovoice);
        findtype=findViewById(R.id.findtype);
        texttovoice.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity4.this, MainActivity7.class);
                startActivity(intent);
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(MainActivity4.this,MainActivity8.class);
                startActivity(intent);
            }
        });
        findtype.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity4.this,MainActivity6.class);
                startActivity(intent);
                finish();
            }
        });
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity4.this,MainActivity5.class);
                startActivity(intent);
                finish();

            }
        });
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(MainActivity4.this,MainActivity2.class);
                startActivity(intent);
                finish();

            }
        });
        pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity4.this,MainActivity3.class);
                startActivity(intent);
                finish();
            }
        });

    }
}