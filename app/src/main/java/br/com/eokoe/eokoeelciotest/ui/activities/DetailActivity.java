package br.com.eokoe.eokoeelciotest.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.eokoe.eokoeelciotest.R;
import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPI;
import br.com.eokoe.eokoeelciotest.domian.api.MovieDBAPIService;
import br.com.eokoe.eokoeelciotest.domian.model.Detail;
import br.com.eokoe.eokoeelciotest.domian.model.Genre;
import br.com.eokoe.eokoeelciotest.domian.model.ProductionCompany;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
<<<<<<< Updated upstream
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;


import java.util.List;
=======
>>>>>>> Stashed changes


public class DetailActivity extends AppCompatActivity {

    private MovieDBAPIService movieService;
    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w500";

    @BindView(R.id.single_item_movie_title)
    TextView singleItemMovieTitle;
    @BindView(R.id.single_item_movie_rating)
    RatingBar singleItemMovieRating;
    @BindView(R.id.single_item_movie_type)
    TextView singleItemMovieType;
    @BindView(R.id.single_item_movie_release_date)
    TextView singleItemMovieReleaseDate;
    @BindView(R.id.single_item_movie_overview)
    TextView singleItemMovieOverview;
    @BindView(R.id.single_item_movie_image)
    ImageView singleItemMovieImage;
    @BindView(R.id.single_item_movie_language)
    TextView singleItemMovieLanguage;
    @BindView(R.id.single_item_movie_produtors)
    TextView singleItemMovieProdutors;
    @BindView(R.id.single_item_movie_tagline)
    TextView singleItemMovieTagline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ButterKnife.bind(this);
        movieService = MovieDBAPI.getClient().create(MovieDBAPIService.class);
        Intent intent = getIntent();
        loadDetails(intent.getStringExtra("movieId"));

    }


    private void loadDetails(String id) {


        callDetailApi(id).enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                Detail results = fetchResults(response);
                singleItemMovieTitle.setText(results.getTitle());
                singleItemMovieType.setText(formatGenres(results.getGenres()));
                singleItemMovieReleaseDate.setText(results.getReleaseDate());
                singleItemMovieRating.setRating((results.getVoteAverage().floatValue()) / 2);
                singleItemMovieOverview.setText(results.getOverview());
                singleItemMovieLanguage.setText("Original Language: " + results.getOriginalLanguage());
                singleItemMovieTagline.setText(results.getTagline());
                singleItemMovieProdutors.setText(formatProdutors(results.getProductionCompanies()));

                Picasso.get().load(BASE_URL_IMG + results.getPosterPath()).into(singleItemMovieImage);

            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }


    private Call<Detail> callDetailApi(String id) {
        return movieService.getDetails(
                id,
                MovieDBAPI.publicKey,
                "en_US"
        );
    }

    private Detail fetchResults(Response<Detail> response) {
        Detail detail = response.body();
        return detail;
    }

    private void showErrorView(Throwable throwable) {

        // todo show allert error
    }

    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(this)
                .load(BASE_URL_IMG + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade();
    }

    private String formatGenres(List<Genre> list) {
        String s = "";
        for (Genre genre : list) {
            if (!s.isEmpty()) {
                s += "/";
            }
            s += genre.getName();
        }
        return s;
    }

    private String formatProdutors(List<ProductionCompany> list) {
        String s = "";
        for (ProductionCompany productionCompany : list) {
            if (!s.isEmpty()) {
                s += "/";
            }
            s += productionCompany.getName();
        }
        return s;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

<<<<<<< Updated upstream
=======
    public void callReviews(View view) {
        Intent intent = new Intent(this, ReviewActivity.class);
        intent.putExtra("movieId", movieId);
        startActivity(intent);
    }
>>>>>>> Stashed changes
}
