package io.appflate.restmock.androidsample.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.appflate.restmock.androidsample.R;
import io.appflate.restmock.androidsample.model.Repository;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class ReposRecyclerAdapter extends RecyclerView.Adapter<ReposRecyclerAdapter.RepoViewHolder> {
    private final List<Repository> repositories;

    public ReposRecyclerAdapter(List<Repository> repositories) {
        this.repositories = repositories;
    }

    @Override
    public RepoViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        return new RepoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_repo_view,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(RepoViewHolder holder,
                                 int position) {
        Repository repository = repositories.get(position);
        holder.title.setText(repository.name);
        holder.starsCount.setText(String.format("%s ★", repository.stargazersCount));

    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }

    public static class RepoViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.repoTitleText) TextView title;
        @Bind(R.id.repoStarsCountText) TextView starsCount;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
