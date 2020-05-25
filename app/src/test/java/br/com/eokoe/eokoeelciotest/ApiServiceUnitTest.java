package br.com.eokoe.eokoeelciotest;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPI;
import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPIService;
import br.com.eokoe.eokoeelciotest.domian.model.Detail;
import br.com.eokoe.eokoeelciotest.domian.model.Popular;
import br.com.eokoe.eokoeelciotest.domian.model.Result;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


public class ApiServiceUnitTest {

    MovieDBAPIService service;

    @Before
    public void getService() {
        service = MovieDBAPI.getClient().create(MovieDBAPIService.class);
    }


    @Test
    public void test_popular_api_call() {

        callPopularApi().enqueue(new Callback<Popular>() {
            @Override
            public void onResponse(Call<Popular> call, Response<Popular> response) {

                List<Result> results = response.body().getResults();

                assertNotEquals(0, results.size());

            }

            @Override
            public void onFailure(Call<Popular> call, Throwable t) {
                // nothing
            }
        });
    }

    @Test
    public void test_detail_api_call() {

        callDetailApi("360920").enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                Detail detail = response.body();
                assertEquals("The Grinch", detail.getTitle());
            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                // nothing
            }
        });
    }

    private Call<Popular> callPopularApi() {
        return service.getPopularMovies(
                MovieDBAPI.publicKey,
                "en_US",
                1
        );
    }

    private Call<Detail> callDetailApi(String id) {
        return service.getDetails(
                id,
                MovieDBAPI.publicKey,
                "en_US"
        );
    }
}