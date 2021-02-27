package com.example.broadcastreceiverytelefonarespuestaautomtica;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText txtMensaje, txtNumero;
    Context _ctx;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtMensaje=findViewById(R.id.txtMensaje);
        txtNumero=findViewById(R.id.txtNumero);
        button=findViewById(R.id.button);
        _ctx = this;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ENVIAMOS UN BROADCAST CON EL MENSAJE DE LA CAJA DE TEXTO QUE INGRESO EL USUARIO PARA GUARDARLO EN LA PROXIMA LLAMADA QUE ENTRE.
                try{
                    //Escribir datos en el archivo
                    String cad =txtNumero.getText().toString()+","+txtMensaje.getText().toString();
                    Archivo.writeToFile(cad,getApplicationContext());
                    //Intent intent= new Intent("my.action.string");
                    Intent intent = new Intent();
                    intent.setAction("com.example.broadcastreceiverytelefonarespuestaautomtica");
                    //intent.putExtra("Numero",txtNumero.getText().toString());
                    //intent.putExtra("Mensaje",txtMensaje.getText().toString()+"");
                    sendBroadcast(intent);
                    Toast.makeText(_ctx,"Guardado"+cad,Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(_ctx,e+"",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}