package com.firstapp.manunya.instagramclient;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.List;

/**
 * Created by Manunya on 5/10/2015.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {

    private static class ViewHolder {
        TextView tvProfileUsernameC;
        TextView tvCreatedTimeC;
        LinkifiedTextView tvCommentC;
        ImageView ivProfilePhotoC;
    }
    public CommentAdapter(Context context, List<Comment> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Comment comment = getItem(position);
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
            viewHolder.tvProfileUsernameC =  (TextView) convertView.findViewById(R.id.tvProfileUsernameC);
            viewHolder.tvCreatedTimeC =  (TextView) convertView.findViewById(R.id.tvCreatedTimeC);
            viewHolder.tvCommentC = (LinkifiedTextView) convertView.findViewById(R.id.tvCommentC);
            viewHolder.ivProfilePhotoC = (ImageView) convertView.findViewById(R.id.ivProfilePhotoC);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvProfileUsernameC.setText(comment.getUsername());
        String tString = DateUtils.getRelativeTimeSpanString(Long.valueOf(comment.getCreated_time()) * 1000,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
        viewHolder.tvCreatedTimeC.setText(InstagramPhoto.shortenTimeSpanString(tString));
        viewHolder.tvCommentC.setStyledText("", comment.getCommentString());
        // clear out the imageview in case it was a recycled one while we're loading the new image
        // happen right away
        viewHolder.ivProfilePhotoC.setImageResource(0);
        // insert image using Picasso (send out asyncronously)
        Transformation transformation = new RoundedTransformationBuilder()
                .cornerRadiusDp(40)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(comment.getProfilePhotoURL()).fit().transform(transformation).into(viewHolder.ivProfilePhotoC);
        return convertView;
    }
}
