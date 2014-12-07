package br.com.lopes.heider.android_catalogo_nfc;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.lopes.heider.android_catalogo_nfc.dao.DAOClube;
import br.com.lopes.heider.android_catalogo_nfc.model.Clube;


public class MainActivity extends Activity {

    private LinearLayout llBackground;
    private MediaPlayer mp;
    private Button btTocarHino;
    private ImageView ivEscudo;
    private TextView tvNomeClube;
    private TextView tvHinoClube;

    private Clube clubeSelecioando;
    private List<Clube> clubes;
    private DAOClube dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        llBackground = (LinearLayout)findViewById(R.id.llBackground);
        ivEscudo = (ImageView) findViewById(R.id.ivEscudo);
        tvNomeClube = (TextView) findViewById(R.id.tvNomeClube);
        tvHinoClube = (TextView) findViewById(R.id.tvHinoClube);
        btTocarHino = (Button)findViewById(R.id.btTocarHino);
        dao = new DAOClube();

        clubes = dao.getAllClubes(this);

        btTocarHino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp == null) {
                    btTocarHino.setText("Parar hino");
                    startPlayer();}
                else {
                    if(mp.isPlaying()) {
                        btTocarHino.setText("Tocar hino");
                        stopPlayer();
                    } else {
                        btTocarHino.setText("Parar hino");
                        startPlayer();
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        IntentFilter tech = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        try {
            tech.addDataType("*/*");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("fail", e);
        }

        IntentFilter[] intentFiltersArray = new IntentFilter[]{ndef, tech};

        String[][] techList = new String[][]{new String[]{
                NfcA.class.getName(), NfcB.class.getName(),
                NfcF.class.getName(), NfcV.class.getName(),
                IsoDep.class.getName(), MifareClassic.class.getName(),
                MifareUltralight.class.getName(), Ndef.class.getName()}};

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent,
                intentFiltersArray, techList);
        super.onResume();
    }

    @Override
    public void onNewIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Toast.makeText(this, "TAG DISCOVERED", Toast.LENGTH_SHORT).show();

        } else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent
                    .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                NdefMessage[] msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    NdefRecord[] records = msgs[i].getRecords();

                    if (new String(records[0].getPayload()).endsWith("palmeiras")) {
                        selectClube(1);
                    } else if (new String(records[0].getPayload()).endsWith("corinthians")) {
                        selectClube(2);
                    }else {
                        selectClube(0);
                        Toast.makeText(this, "Nenhuma acao registrada para esta TAG!", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Toast.makeText(this, "TECH DISCOVERED", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            stopPlayer();
            mp = null;
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void selectClube(int index) {
        clubeSelecioando = clubes.get(index);
        llBackground.setBackgroundColor(getResources().getColor(clubeSelecioando.getBackground()));
        ivEscudo.setImageDrawable(getResources().getDrawable(clubeSelecioando.getEscudo()));
        tvNomeClube.setText(clubeSelecioando.getNome());
        tvHinoClube.setText(clubeSelecioando.getHino());
        btTocarHino.setBackground(getResources().getDrawable(clubeSelecioando.getCores()));

    }
    private void stopPlayer() {
        if (mp != null)
            mp.stop();
    }

    private void startPlayer() {
        mp = MediaPlayer.create(this, clubeSelecioando.getHinosom());
        mp.start();
    }
}
