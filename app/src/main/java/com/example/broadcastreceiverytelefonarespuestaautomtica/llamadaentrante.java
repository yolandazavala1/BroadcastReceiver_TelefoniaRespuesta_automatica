package com.example.broadcastreceiverytelefonarespuestaautomtica;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class llamadaentrante extends BroadcastReceiver {
    private static final String TAG = "PhoneStatReceiver";
    private static boolean incomingFlag = false;
    private static String incoming_number = null;
    private static String mensajeGuardado = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        //OBTENEMOS EL MENSAJE ENVIADO DE LA CAJA DE TEXTO PARA GUARDARLO EN UNA VARIABLE
        String mensaje = intent.getStringExtra("Mensaje");
        if (mensaje != null) {
            mensajeGuardado = mensaje;
        } else {
            Toast.makeText(context, "desde el receive", Toast.LENGTH_LONG).show();
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                Toast.makeText(context, "else if", Toast.LENGTH_LONG).show();
                incomingFlag = false;
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            } else {
                Toast.makeText(context, "else else", Toast.LENGTH_LONG).show();
                // TELEPHONY MANAGER OBTENIENDO EL SERVICIO DE TELEFONIA.
                TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                //SWITCH QUE PREGUNTA POR EL ESTADO DE MANAGER SI SE ENCUENTRA EN RINGING(SONANDO) O IDLE.
                switch (tm.getCallState()) {
                    // SI EL ESTADO DEL MANAGER ES UN RINGING ES PORQUE HAY UNA LLAMADA ENTRANTE O SONANDO, ENTONCES CAPTURAMOS EL NUMERO TELEFONICO.
                    case TelephonyManager.CALL_STATE_RINGING:
                        incomingFlag = true;
                        incoming_number = intent.getStringExtra("incoming_number"); //OBTENEMOS EL NUMERO DESDE EL INTENT.
                        Toast.makeText(context, "RINGING :" + incoming_number, Toast.LENGTH_LONG).show();
                        try{
                            SmsManager smgr = SmsManager.getDefault(); //CREANDO UN OBJETO DE SMS MANAGER.
                            smgr.sendTextMessage(incoming_number, null, mensajeGuardado, null, null); // ENVIAMOS EL SMS CON EL MENSAJE GUARDADO Y EL NUMERO TELEFONICO.
                            Toast.makeText(context, "Enviando mensaje a :" + incoming_number + " " + mensajeGuardado, Toast.LENGTH_LONG).show();
                            Toast.makeText(context,"Ringing", Toast.LENGTH_LONG).show();
                        }catch (Exception e){
                            Toast.makeText(context,"Error: "+e, Toast.LENGTH_LONG).show();
                        }

                        break;
                    // SI CONTESTAS LA LLAMADA.
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        if (incomingFlag) {
                            Toast.makeText(context, "contesto e incomming accept", Toast.LENGTH_LONG).show();
                            Log.i(TAG, "incoming ACCEPT :" + incoming_number);
                        }
                        break;
                    //AL TERMINAR LA LLAMADA
                    case TelephonyManager.CALL_STATE_IDLE:
                        if (incomingFlag) {
                            Toast.makeText(context, "incoming IDLE", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
            }
        }
    }
}
