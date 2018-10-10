package udacity.example.com.stage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import udacity.example.com.stage.model.Movie;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder> {

    private List<Movie> mReviewsList;

    private final MovieAdapterOnClickHandler mClickHandler;


    public ReviewAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.reviews_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        ReviewAdapterViewHolder viewHolder = new ReviewAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder holder, int position) {
        String author = "Wrote by " + mReviewsList.get(position).getAuthor() + ":";
        String content = mReviewsList.get(position).getContent();
        holder.author.setText(author);
        holder.reviewContent.setText(content);
    }

    @Override
    public int getItemCount() {
        if (mReviewsList == null) {
            return 0;
        }
        return mReviewsList.size();
    }

    public void setReviewsList(List<Movie> reviewsList) {
        mReviewsList = reviewsList;
        notifyDataSetChanged();
    }

    public List<Movie> getReviewsList() {
        return mReviewsList;
    }

    class ReviewAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_review_author)
        TextView author;

        @BindView(R.id.tv_review_content)
        TextView reviewContent;

        public ReviewAdapterViewHolder(View itemView) {
            super(itemView);

            // binding view
            ButterKnife.bind(this, itemView);

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View view) {
//            int adapterPosition = getAdapterPosition();
//            mClickHandler.onClick(adapterPosition);
//        }
    }
}
