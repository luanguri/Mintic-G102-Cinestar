package com.cinestar.MovieApiService;

import com.cinestar.models.MovieRespuesta;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiService {

    @GET("pokemon")
    Call<MovieRespuesta> obtenerListaMovie(@Query("limit")int limit,@Query("offset") int offset);

}
