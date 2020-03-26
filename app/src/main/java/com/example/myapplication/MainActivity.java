package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn;
    EditText et;
    TextView textView;
    VModel model;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mViewModel = ViewModelProvider.of(this).get(MainActivity2ViewModel.class);
        final ProgressBar p = (ProgressBar) findViewById(R.id.progressBar1);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearlayout);
        final EditText et = (EditText) findViewById(R.id.editText1);
        final TextView textView = (TextView) findViewById(R.id.textView1);
        final Button btn = findViewById(R.id.button1);
        final String hello = "hello";

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);


        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        final VModel.OnFactGetListener listener;
        //mAdapter = new MyAdapter(List<CatFact> facts, listener);
        recyclerView.setAdapter(mAdapter);




        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String res = et.getText().toString();
                textView.setText(res);

            }

        });


        model = new ViewModelProvider(this).get(VModel.class);

        final Observer<String> nameObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                textView.setText(newName);
            }
        };


        model.getFact(new VModel.OnFactGetListener() {
            @Override
            public void onFactGet(final String fact) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                      /*  Toast msg = Toast.makeText(getBaseContext(), fact, Toast.LENGTH_LONG);
                        msg.show(); */
                      model.getCurrentName().setValue(fact);


                    }

                });
                p.setVisibility(View.INVISIBLE);
            }


        });
        //model.getFacts();
        model.getCurrentName().observe(this,new Observer<List<CatFact>>() {
            @Override
            public void onChanged(List<CatFact> catFacts) {
                p.setVisibility(View.GONE);
                MyAdapter adapter= new MyAdapter(catFacts,listener);
                recyclerView.setAdapter(adapter);

            }
                });


    }

   @Override
    public void onClick(View v) {
        String str=et.getText().toString();
        Toast msg = Toast.makeText(getBaseContext(), str, Toast.LENGTH_LONG);
        msg.show();


    }

    public void onFactClicked(CatFact catFact) {
        Toast.makeText(getBaseContext(), catFact.fact, Toast.LENGTH_LONG).show();


    }

}


