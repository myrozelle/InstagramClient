package com.firstapp.manunya.instagramclient;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;


/**
 * Created by Manunya on 5/6/2015.
 */
//extend ArrayAdapter because array is the data source in this case
public class InstagramPhotoAdapter extends ArrayAdapter<InstagramPhoto> {
    private int displayWidth;
    // View lookup cache
    private static class ViewHolder {
        TextView tvLikesCt;
        LinkifiedTextView tvUsername;
        ImageView ivPhoto;
        ImageView ivProfilePhoto;
        TextView tvProfileUsername;
        TextView tvCreatedTime;
        TextView tvAllComments;
        LinearLayout llComments;
    }

    //what data do we need from the activity
    //Context, Data source
    public InstagramPhotoAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        displayWidth = DeviceDimensionsHelper.getDisplayWidth(context);
    }

    //what our item looks like
    //use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get the data item for this position
        final InstagramPhoto photo = getItem(position);
        final ViewHolder viewHolder; // view lookup cache stored in tag
        // check if we're using a recycled view, if not we need to inflate
        if (convertView == null) {
            //no recycle view, create a new view from template
            //inflate is the process that turns xml file into actual view
            //parent is is the container in which the view reside
            //false means not attached to container now
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);
            viewHolder.tvLikesCt = (TextView) convertView.findViewById(R.id.tvLikesCt);
            viewHolder.tvUsername = (LinkifiedTextView) convertView.findViewById(R.id.tvUsername);
            //viewHolder.tvCaption = (LinkifiedTextView) convertView.findViewById(R.id.tvCaption);
            viewHolder.ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);
            viewHolder.ivProfilePhoto = (ImageView) convertView.findViewById(R.id.ivProfilePhoto);
            viewHolder.tvProfileUsername = (TextView) convertView.findViewById(R.id.tvProfileUsername);
            viewHolder.tvCreatedTime = (TextView) convertView.findViewById(R.id.tvCreatedTime);
            viewHolder.llComments = (LinearLayout) convertView.findViewById(R.id.llComments);
            viewHolder.tvAllComments = (TextView) convertView.findViewById(R.id.tvAllComments);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // insert the model data into each of the view items
        viewHolder.tvProfileUsername.setText(photo.getUsername());
        String tString = DateUtils.getRelativeTimeSpanString(Long.valueOf(photo.getCreated_time()) * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        viewHolder.tvCreatedTime.setText(InstagramPhoto.shortenTimeSpanString(tString));
        viewHolder.tvLikesCt.setText(photo.getLikesCount() + " likes");
        viewHolder.tvUsername.setStyledText(photo.getUsername(), photo.getCaption());
        viewHolder.tvAllComments.setText("View all " + photo.getCommentsCount() + " comments");
        // clear out the imageview in case it was a recycled one while we're loading the new image
        // happen right away
        viewHolder.ivPhoto.setImageResource(0);
        viewHolder.ivProfilePhoto.setImageResource(0);
        // insert image using Picasso (send out asyncronously)
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.getProfilePhotoURL()).fit().transform(transformation).into(viewHolder.ivProfilePhoto);
        Picasso.with(getContext()).load(photo.getImageURL()).resize(displayWidth, 0).placeholder(R.drawable.ic_photo_placeholder).into(viewHolder.ivPhoto);
        // clear out comments
        viewHolder.llComments.removeAllViews();
        // if there are comments, insert comments
        if (photo.getCommentsCount() > 0) {
            int numComments = photo.getComments().size();
            // show the latest 2 comments
            for (int i = 1; i <= 2; i++) {
                View line = LayoutInflater.from(getContext()).inflate(R.layout.recent_comment, null);
                LinkifiedTextView tvComment = (LinkifiedTextView) line.findViewById(R.id.tvComment);
                tvComment.setStyledText(photo.getComment(numComments-i).getUsername(), photo.getComment(numComments-i).getCommentString());
                viewHolder.llComments.addView(line);
            }
        }
        // make 'view all comments' clickable
        viewHolder.tvAllComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), viewHolder.tvAllComments.getText().toString(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getContext(), CommentsActivity.class);
                // Create a Bundle and Put Bundle in to it
                Bundle bundleObject = new Bundle();
                bundleObject.putSerializable("comments", photo.getComments());
                // put bundle into intent
                i.putExtras(bundleObject);
                getContext().startActivity(i);
            }
        });
        // return the created items as a view
        return convertView;
    }
}
