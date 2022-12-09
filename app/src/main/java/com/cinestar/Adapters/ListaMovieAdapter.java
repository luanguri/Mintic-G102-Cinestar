package com.cinestar.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cinestar.R;
import com.cinestar.models.Movie;

import java.util.ArrayList;

public class ListaMovieAdapter extends RecyclerView.Adapter<ListaMovieAdapter.viewHolder> {
    private ArrayList<Movie>dataset;
    private Context context;

    public ListaMovieAdapter(Context context) {
       this.context=context;
        dataset=new ArrayList<>();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Movie m=dataset.get(position);
        holder.nombreTextView.setText(m.getName());
        Glide.with(context)
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+m.getNumber()+".png")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.fotoImageView);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void adicionarListaMovie(ArrayList<Movie> listamovie) {
        dataset.addAll(listamovie);
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
       private ImageView fotoImageView;
       private TextView nombreTextView;
        public viewHolder(View itemView){
           super(itemView);
           fotoImageView=itemView.findViewById(R.id.fotoImageView);
           nombreTextView=itemView.findViewById(R.id.nombreTextView);
       }

    }
}
