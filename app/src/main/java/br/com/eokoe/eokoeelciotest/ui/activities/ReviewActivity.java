package br.com.eokoe.eokoeelciotest.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import br.com.eokoe.eokoeelciotest.R;
import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPI;
import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPIService;
import br.com.eokoe.eokoeelciotest.domian.model.Detail;
import br.com.eokoe.eokoeelciotest.domian.model.Review;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    private MovieDBAPIService movieService;

    @BindView(R.id.textView2) TextView textView2;
    @BindView(R.id.textView3) TextView textView3;
    @BindView(R.id.textView4) TextView textView4;
    @BindView(R.id.textView5) TextView textView5;
    @BindView(R.id.textView6) TextView textView6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        movieService = MovieDBAPI.getClient().create(MovieDBAPIService.class);
        Intent intent = getIntent();
        loadReviews(intent.getStringExtra("movieId"));
        ButterKnife.bind(this);
    }

    private void loadReviews(String id) {

        callReviewsApi(id).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(Call<Review> call, Response<Review> response) {
                Review results = fetchResults(response);
                Log.i("BahiaReviewActivity2", ""+results.getResults().get(0).getContent());
                findViewById(R.id.textView2);
                int size = results.getResults().size();
                if (size >= 5) {
                    textView2.setText(results.getResults().get(0).getContent());
                    textView3.setText(results.getResults().get(1).getContent());
                    textView4.setText(results.getResults().get(2).getContent());
                    textView5.setText(results.getResults().get(3).getContent());
                    textView6.setText(results.getResults().get(4).getContent());
                } else if (size == 4) {
                    textView2.setText(results.getResults().get(0).getContent());
                    textView3.setText(results.getResults().get(1).getContent());
                    textView4.setText(results.getResults().get(2).getContent());
                    textView5.setText(results.getResults().get(3).getContent());
                } else if (size == 3) {
                    textView2.setText(results.getResults().get(0).getContent());
                    textView3.setText(results.getResults().get(1).getContent());
                    textView4.setText(results.getResults().get(2).getContent());
                } else if (size == 2) {
                    textView2.setText(results.getResults().get(0).getContent());
                    textView3.setText(results.getResults().get(1).getContent());
                } else if (size == 1) {
                    textView2.setText(results.getResults().get(0).getContent());
                } else if (size == 0) {
                    textView2.setText("Não existem comentários.");
                }


            }

            @Override
            public void onFailure(Call<Review> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    private Call<Review> callReviewsApi(String id) {
        return movieService.getReviews(
                id,
                MovieDBAPI.publicKey,
                "en_US"
        );
    }

    private Review fetchResults(Response<Review> response) {
        Review review = response.body();
        return review;
    }

    private void showErrorView(Throwable throwable) {

        // todo show alert error
    }
}
