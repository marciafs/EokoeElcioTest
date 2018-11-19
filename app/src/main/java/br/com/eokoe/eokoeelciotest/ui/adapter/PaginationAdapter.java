package br.com.eokoe.eokoeelciotest.ui.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import br.com.eokoe.eokoeelciotest.R;
import br.com.eokoe.eokoeelciotest.domian.model.Result;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private static final int HERO = 2;

    private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w200";

    private List<Result> movieResults;
    private List<Result> movieResultsBackup;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;
    private boolean reload = true;

    private PaginationAdapterCallback mCallback;

    private final OnItemClickListener clickListener;

    private String errorMsg;

    public PaginationAdapter(Context context, OnItemClickListener clickListener) {

        this.context = context;
        this.mCallback = (PaginationAdapterCallback) context;
        this.clickListener = clickListener;
        movieResults = new ArrayList<>();
        movieResultsBackup = new ArrayList<>();

    }

    public boolean canReload(){
        return reload;
    }

    public List<Result> getMovies() {
        return movieResults;
    }

    public void setMovies(List<Result> movieResults) {
        this.movieResults = movieResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.item_list, parent, false);
                viewHolder = new MovieVH(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(viewLoading);
                break;
            case HERO:
                View viewHero = inflater.inflate(R.layout.item_hero, parent, false);
                viewHolder = new HeroVH(viewHero);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Result result = movieResults.get(position);

        switch (getItemViewType(position)) {

            case HERO:
                final HeroVH heroVh = (HeroVH) holder;

                heroVh.mMovieTitle.setText(result.getTitle());
                heroVh.mYear.setText(formatYearLabel(result));
                heroVh.mMovieDesc.setText(result.getOverview());

                loadImage(result.getBackdropPath())
                        .into(heroVh.mPosterImg);
                break;

            case ITEM:
                final MovieVH movieVH = (MovieVH) holder;

                movieVH.mMovieTitle.setText(result.getTitle());
                movieVH.mYear.setText(formatYearLabel(result));
                movieVH.mMovieDesc.setText(result.getOverview());

                loadImage(result.getPosterPath())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                                movieVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(movieVH.mPosterImg);
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public int getItemCount() {

        if(movieResults != null){
            return movieResults.size();
        } else {
            return 0;
        }

        //return movieResults == null ? 0 : movieResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HERO;
        } else {
            return (position == movieResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
        }
    }


    /**
     * @param result
     * @return [releasedate] | [2letterlangcode]
     */
    private String formatYearLabel(Result result) {
        return result.getReleaseDate().substring(0, 4)  // we want the year only
                + " | "
                + result.getOriginalLanguage().toUpperCase();
    }


    private DrawableRequestBuilder<String> loadImage(@NonNull String posterPath) {
        return Glide
                .with(context)
                .load(BASE_URL_IMG + posterPath)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade();
    }



    public void add(Result r) {
        movieResults.add(r);
        notifyItemInserted(movieResults.size() - 1);
    }

    public void addAll(List<Result> movieResults) {
        restoreData();
        for (Result result : movieResults) {
            add(result);
        }
        copyData();
    }

    public void copyData(){
        movieResultsBackup.clear();
        movieResultsBackup.addAll(movieResults);
    }

    public void restoreData(){
        movieResults.clear();
        movieResults.addAll(movieResultsBackup);
    }

    public void remove(Result r) {
        int position = movieResults.indexOf(r);
        if (position > -1) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
        copyData();
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
        copyData();
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = movieResults.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            movieResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return movieResults.get(position);
    }


    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(movieResults.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    /**
     * Header ViewHolder
     */
    protected class HeroVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_title) TextView mMovieTitle;
        @BindView(R.id.movie_desc) TextView mMovieDesc;
        @BindView(R.id.movie_year) TextView mYear;
        @BindView(R.id.movie_poster) ImageView mPosterImg;

        public HeroVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getItem(getPosition()));
        }
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.movie_title) TextView mMovieTitle;
        @BindView(R.id.movie_desc) TextView mMovieDesc;
        @BindView(R.id.movie_year) TextView mYear;
        @BindView(R.id.movie_poster) ImageView mPosterImg;
        @BindView(R.id.movie_progress) ProgressBar mProgress;

        public MovieVH(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(v, getItem(getPosition()));
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.loadmore_progress) ProgressBar mProgressBar;
        @BindView(R.id.loadmore_retry) ImageButton mRetryBtn;
        @BindView(R.id.loadmore_errortxt) TextView mErrorTxt;
        @BindView(R.id.loadmore_errorlayout) LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        public void onClick(View view, Result item);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    restoreData();
                    reload = true;
                } else {
                    reload = false;
                    List<Result> filteredList = new ArrayList<>();
                    for (Result result : movieResultsBackup) {
                        if (result.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(result);
                        }
                    }
                    movieResults = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = movieResults;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieResults = (ArrayList<Result>) filterResults.values;

                notifyDataSetChanged();
            }
        };
    }

}
