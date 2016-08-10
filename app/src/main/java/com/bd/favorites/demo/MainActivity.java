package com.bd.favorites.demo;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bd.favorite.Favo;
import com.bd.favorite.FavoritePicker;
import com.bd.favorite.OperatorListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FavoritePicker picker= (FavoritePicker) findViewById(R.id.picker);
        picker.setLimitFavo(6);
        picker.setColumnNumber(3);
        picker.setOperatorListener(mOperatorListener);
        List<Favo> favos = new ArrayList<>();
        favos.add(new Favo(1,"第一张","http://download.easyicon.net/png/1082117/128/"));
        favos.add(new Favo(2,"第二张","http://download.easyicon.net/png/1082114/128/"));
        favos.add(new Favo(3,"第三张","http://download.easyicon.net/png/1082113/128/"));
        favos.add(new Favo(4,"第四张","http://download.easyicon.net/png/1082115/128/"));

        picker.bind(favos);
    }

    private OperatorListener mOperatorListener = new OperatorListener() {
        @Override
        public void onCancel() {

        }

        @Override
        public void onDone() {

        }

        @Override
        public String onPick() {
            //TODO 这里返回 新建收藏 imageUrl

            return "http://download.easyicon.net/png/1082115/128/";
        }

        @Override
        public void onSelect(Favo favo) {

        }

        @Override
        public void onIntoDelete() {

        }

        @Override
        public void onFavoMakerCancel() {

        }

        @Override
        public void onFavoMakerSave(List<Favo> favos) {

        }

        @Override
        public void onFavoDelete() {

        }
    };

}
