package br.com.eokoe.eokoeelciotest.domian.api;

import br.com.eokoe.eokoeelciotest.domian.model.Detail;
import br.com.eokoe.eokoeelciotest.domian.model.Popular;

import br.com.eokoe.eokoeelciotest.domian.model.Review;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieDBAPIService {

    // Popular movies
    @GET("3/movie/popular")
    Call<Popular> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

    // movie detials
    @GET("3/movie/{id}")
    Call<Detail> getDetails(
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language

    );

    // movie reviews
    @GET("3/movie/{id}/reviews")
    Call<Review> getReviews(
            @Path("id") String id,
            @Query("api_key") String apiKey,
            @Query("language") String language

    );

}
