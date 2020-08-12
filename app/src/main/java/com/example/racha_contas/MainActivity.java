package com.example.racha_contas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText edValor, edPessoas;
    TextView tvRes;
    FloatingActionButton share, play;
    TextToSpeech ttsPlayer;
    int pessoas = 2;
    double valor = 0.0;
    String resFormatado = "0,00";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share = (FloatingActionButton) findViewById(R.id.btShare);
        play = (FloatingActionButton) findViewById(R.id.btPlay);

        edValor = (EditText) findViewById(R.id.editValor);
        edPessoas = (EditText) findViewById(R.id.editPessoas);
        tvRes = (TextView) findViewById(R.id.textViewResultado);

        edValor.addTextChangedListener(this);
        edPessoas.addTextChangedListener(this);
        share.setOnClickListener(this);
        play.setOnClickListener(this);

        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent,1122);
    }

    protected void rachar(){
        DecimalFormat df = new DecimalFormat("#0.00");
        try {
            pessoas = Integer.parseInt(edPessoas.getText().toString());
            valor = Double.parseDouble(edValor.getText().toString());
            if (pessoas != 0) {
                resFormatado = df.format(valor/pessoas);
                tvRes.setText("R$"+resFormatado);
            } else {
                tvRes.setText("R$ 0.00");
            }
        } catch (Exception e){
            Log.v("erro","algo de errado não está certo");
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1122){
            ttsPlayer = new TextToSpeech(this,this);
        } else {
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            startActivity(installTTSIntent);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        rachar();
        /*Log.v("PDM",edValor.getText().toString());

        try {
            double res = Double.parseDouble(edValor.getText().toString());
            res = (res/2.0);
            DecimalFormat df = new DecimalFormat("#.00");
            tvRes.setText("R$"+df.format(res));
        } catch (Exception e){
            tvRes.setText("R$ 0.00");
        }*/
    }

    @Override
    public void onClick(View view) {
        if (view==share){
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,"Cada caloteiro deve pagar R$"+resFormatado+". Não enrolem, paguem.");
            startActivity(intent);
        }
        if (view==play){
            if (ttsPlayer!=null){//mensagem em inglês para melhor testar o TTS
                ttsPlayer.speak("Each deadbeat must pay "+ resFormatado + " bolsonaros. I'm not your sugar mommy.", TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int initStatus) {
        if(initStatus==TextToSpeech.SUCCESS){
            Toast.makeText(this, "TTS ativado...", Toast.LENGTH_LONG).show();
        } else if (initStatus==TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS habilitado...", Toast.LENGTH_LONG).show();
        }
    }
}