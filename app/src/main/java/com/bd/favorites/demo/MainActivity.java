package com.bd.favorites.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bd.favorite.Favo;
import com.bd.favorite.FavoritePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FavoritePicker picker= (FavoritePicker) findViewById(R.id.picker);
        picker.setLimitFavo(5);
        picker.setColumnNumber(3);
        List<Favo> favos = new ArrayList<>();
        favos.add(new Favo(0,"第一张","http://download.easyicon.net/png/1082117/128/"));
        favos.add(new Favo(0,"第二张","http://download.easyicon.net/png/1082114/128/"));
        favos.add(new Favo(0,"第三张","http://download.easyicon.net/png/1082113/128/"));
        favos.add(new Favo(0,"第四张","http://download.easyicon.net/png/1082115/128/"));

        picker.bind(favos);
    }


}
