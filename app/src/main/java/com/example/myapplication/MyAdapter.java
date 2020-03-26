package com.example.myapplication;

import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Random;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<CatFact> facts;
    private VModel.OnFactGetListener listener;




    public static class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }

        public MyAdapter(List<CatFact> facts, VModel.OnFactGetListener listener) {
            this.facts=facts;
            this.listener=listener;
        }


        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
           // TextView v = (TextView) LayoutInflater.from(parent.getContext())
           //         .inflate(R.layout.my_text_view, parent, false); //Create a xml for text view
            TextView textView= new TextView(parent.getContext());
            textView.setBackgroundColor(new Random().nextInt());
            MyViewHolder vh = new MyViewHolder(textView);
            return vh;
        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final CatFact fact=facts.get(position);
        holder.textView.setText(Html.fromHtml(fact.fact));
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onFactGet(fact.fact);

            }
        });
    }


        @Override
        public int getItemCount() {
            return facts.size();
        }

    }
