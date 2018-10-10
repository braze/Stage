package udacity.example.com.stage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.stage.model.Movie;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private List<Movie> mTrailersList;

    private final MovieAdapterOnClickHandler mClickHandler;


    public TrailerAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.trailers_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        TrailerAdapterViewHolder viewHolder = new TrailerAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder holder, int position) {
        holder.playIcon.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        String trailerName = mTrailersList.get(position).getTrailerName();
        holder.trailerName.setText(trailerName);
    }

    @Override
    public int getItemCount() {
        if (mTrailersList == null) {
            return 0;
        }
        return mTrailersList.size();
    }

    public void setTrailersList(List<Movie> trailersList) {
        mTrailersList = trailersList;
        notifyDataSetChanged();
    }

    public List<Movie> getTrailersList() {
        return mTrailersList;
    }

    class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_play_icon)
        ImageView playIcon;

        @BindView(R.id.tv_trailer_name)
        TextView trailerName;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);

            // binding view
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }
}
