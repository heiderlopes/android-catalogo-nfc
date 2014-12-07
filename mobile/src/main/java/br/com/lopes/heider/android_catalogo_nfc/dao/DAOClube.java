package br.com.lopes.heider.android_catalogo_nfc.dao;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import br.com.lopes.heider.android_catalogo_nfc.R;
import br.com.lopes.heider.android_catalogo_nfc.model.Clube;

/**
 * Created by heiderlopes on 07/12/14.
 */
public class DAOClube {


    private int[] musicas = new int[] { R.raw.hino_palmeiras, R.raw.hino_corinthians};

    public List<Clube> getAllClubes (Context context) {
        List<Clube> clubes = new ArrayList<Clube>();
        Clube heyapps = new Clube();
        heyapps.setBackground(R.color.red_light);
        heyapps.setCores(R.drawable.red_button);
        heyapps.setNome(context.getString(R.string.nome_clube));
        heyapps.setHino(context.getString(R.string.hino_clube));
        heyapps.setHinosom(R.raw.hino_palmeiras);
        heyapps.setEscudo(R.drawable.ic_launcher);

        Clube palmeiras = new Clube();
        palmeiras.setBackground(R.color.green_light);
        palmeiras.setCores(R.drawable.green_button);
        palmeiras.setNome(context.getString(R.string.nome_palmeiras));
        palmeiras.setHino(context.getString(R.string.hino_palmeiras));
        palmeiras.setHinosom(R.raw.hino_palmeiras);
        palmeiras.setEscudo(R.drawable.palmeiras);
        Clube corinthians = new Clube();
        corinthians.setBackground(R.color.gray);
        corinthians.setCores(R.drawable.black_button);
        corinthians.setNome(context.getString(R.string.nome_corinthians));
        corinthians.setHino(context.getString(R.string.hino_corinthians));
        corinthians.setHinosom(R.raw.hino_corinthians);
        corinthians.setEscudo(R.drawable.corinthians);

        clubes.add(heyapps);
        clubes.add(palmeiras);
        clubes.add(corinthians);

        return clubes;
    }
}
