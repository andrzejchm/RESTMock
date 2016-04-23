package io.appflate.restmock.androidsample.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by andrzejchm on 23/04/16.
 */
public class Owner {
    @SerializedName("login") public String login;
    @SerializedName("id") public int id;
    @SerializedName("avatar_url") public String avatarUrl;
    @SerializedName("gravatar_id") public String gravatarId;
    @SerializedName("url") public String url;
    @SerializedName("html_url") public String htmlUrl;
    @SerializedName("followers_url") public String followersUrl;
    @SerializedName("following_url") public String followingUrl;
    @SerializedName("gists_url") public String gistsUrl;
    @SerializedName("starred_url") public String starredUrl;
    @SerializedName("subscriptions_url") public String subscriptionsUrl;
    @SerializedName("organizations_url") public String organizationsUrl;
    @SerializedName("repos_url") public String reposUrl;
    @SerializedName("events_url") public String eventsUrl;
    @SerializedName("received_events_url") public String receivedEventsUrl;
    @SerializedName("type") public String type;
    @SerializedName("site_admin") public boolean siteAdmin;
}
