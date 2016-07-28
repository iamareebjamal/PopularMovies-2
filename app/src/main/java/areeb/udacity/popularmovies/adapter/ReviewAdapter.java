package areeb.udacity.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.model.Review;
import areeb.udacity.popularmovies.model.Reviews;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {
    private Context context;
    private List<Review> reviews;

    public ReviewAdapter(Context context, List<Review> reviews){
        this.context = context;
        this.reviews = reviews;
    }

    public ReviewAdapter(Context context, Reviews reviews){
        this.context = context;
        this.reviews = reviews.getReviews();
    }

    public void changeReviews(Reviews reviews){
        this.reviews = reviews.getReviews();
        notifyDataSetChanged();
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ReviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.review.setText(review.getContent());
        holder.author.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review)
        TextView review;
        @BindView(R.id.author)
        TextView author;
        public ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
