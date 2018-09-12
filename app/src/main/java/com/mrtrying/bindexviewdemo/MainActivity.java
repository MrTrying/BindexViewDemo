package com.mrtrying.bindexviewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.mrtrying.library.BindexNavigationView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView textView = findViewById(R.id.textView);
        BindexNavigationView navigationView = findViewById(R.id.bindexNavigationView);
        navigationView.addOnItemSelectedListener(new BindexNavigationView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int position, BindexNavigationView.IndexBean bean) {
                textView.setText(bean.getIndexValue());
            }
        });
        String[] wrods = {"↑", "☆", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z","#"};
        ArrayList<BindexNavigationView.IndexBean> indexBeans = new ArrayList<>();
        for(String str:wrods){
            indexBeans.add(new BindexNavigationView.IndexBean(str));
        }
        navigationView.setData(indexBeans);
    }
}
