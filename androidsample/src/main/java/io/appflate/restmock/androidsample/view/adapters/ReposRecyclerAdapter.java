/*
 * Copyright (C) 2016 Appflate.io
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.appflate.restmock.androidsample.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
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
        @BindView(R.id.repoTitleText) TextView title;
        @BindView(R.id.repoStarsCountText) TextView starsCount;

        public RepoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
