package areeb.udacity.popularmovies.adapter;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import areeb.udacity.popularmovies.R;
import areeb.udacity.popularmovies.model.Trailer;
import areeb.udacity.popularmovies.model.Trailers;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerHolder> {

    private Context context;
    private List<Trailer> trailers;

    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    public TrailerAdapter(Context context, Trailers trailers) {
        this.context = context;
        this.trailers = trailers.getTrailers();
    }

    public void changeTrailers(Trailers trailers){
        this.trailers = trailers.getTrailers();
        notifyDataSetChanged();
    }

    @Override
    public TrailerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer, parent, false);
        return new TrailerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerHolder holder, int position) {
        final Trailer trailer = trailers.get(position);

        holder.name.setText(trailer.getName());
        Picasso.with(context)
                .load(trailer.getThumbnailURI())
                .placeholder(VectorDrawableCompat.create(context.getResources(),R.drawable.vector_movies, null))
                .into(holder.thumbnail);

        holder.trailerRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(trailer.getVideoLink()));
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class TrailerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root)
        LinearLayout trailerRoot;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;
        @BindView(R.id.name)
        TextView name;

        public TrailerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
