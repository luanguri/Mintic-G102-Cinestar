package com.cinestar.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cinestar.Adapters.ListaMovieAdapter;
import com.cinestar.MovieApiService.MovieApiService;
import com.cinestar.R;
import com.cinestar.models.Movie;
import com.cinestar.models.MovieRespuesta;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment {

    private Retrofit retrofit;
    private static final String TAG="MOVIDEX";
    private RecyclerView recyclerView;
    private ListaMovieAdapter listaMovieAdapter;
    private int offset;
    private boolean aptoParaCargar;
    View vista;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FiltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FiltersFragment newInstance(String param1, String param2) {
        FiltersFragment fragment = new FiltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_filter, container, false);
        recyclerView=vista.findViewById(R.id.recylerview);
        listaMovieAdapter=new ListaMovieAdapter(getContext());
        recyclerView.setAdapter(listaMovieAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager layoutManager=new GridLayoutManager(requireContext(),3);
        recyclerView.setLayoutManager(layoutManager);


recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if(dy>0){
            int visibleItemCount=layoutManager.getChildCount();
            int totalItemCount=layoutManager.getItemCount();
            int pastVisibleItems=layoutManager.findFirstCompletelyVisibleItemPosition();
            if(aptoParaCargar){
                if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                    Log.i(TAG,"llegamos al final ...");
                    aptoParaCargar=false;
                    offset+=20;
                    obtenerDatos(offset);
                }
            }

        }
    }
});

       retrofit=new Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();//para obtener respuestas
        aptoParaCargar=true;
        offset=0;//variable global unica en cero
        obtenerDatos(offset);
        return vista;
    }

    private void obtenerDatos(int  offset) {
        MovieApiService service=retrofit.create(MovieApiService.class);
        Call<MovieRespuesta> movieRespuestaCall=service.obtenerListaMovie(151,offset);
        movieRespuestaCall.enqueue(new Callback<MovieRespuesta>() {
            @Override
            public void onResponse(Call<MovieRespuesta> call, Response<MovieRespuesta> response) {
                aptoParaCargar=true;
                if(response.isSuccessful()){
                    MovieRespuesta movieRespuesta= response.body();
                    ArrayList<Movie>listamovie=movieRespuesta.getResults();
                    listaMovieAdapter.adicionarListaMovie(listamovie);
                    for(int i=0;i<listamovie.size();i++){
                        Movie m=listamovie.get(i);
                        Log.i(TAG,"Movie"+m.getName());
                    }
                }else {
                    Log.e(TAG,"onResponse"+response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<MovieRespuesta> call, Throwable t) {
                aptoParaCargar=true;
                Log.e(TAG,"onFailure"+t.getMessage());

            }
        });

    }
}