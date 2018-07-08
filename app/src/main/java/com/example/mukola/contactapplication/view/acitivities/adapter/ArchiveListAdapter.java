package com.example.mukola.contactapplication.view.acitivities.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArchiveListAdapter extends RecyclerView.Adapter<ArchiveListAdapter.ViewHolder> {

    private ArrayList<Contact> itemsData;

    private OnItemClicked onClick;

    private PhotoSaver photoSaver;

    public interface OnItemClicked {
        void onFavClick(@NonNull Contact contact);
        void onKeepClick(@NonNull Contact contact);
        void onUserClick(@NonNull Contact contact);
    }

    public ArchiveListAdapter(ArrayList<Contact> itemsData, Context context) {
        this.itemsData = itemsData;
        photoSaver = new PhotoSaver(context);
    }

    @Override
    public ArchiveListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_item, parent,false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        initListeners(viewHolder,position);

        initListItem(viewHolder,position);

    }

    private void initListeners(ViewHolder viewHolder,final int position){
        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onFavClick(itemsData.get(position));
            }
        });

        viewHolder.keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onKeepClick(itemsData.get(position));
            }
        });


        viewHolder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onUserClick(itemsData.get(position));
            }
        });

        viewHolder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onUserClick(itemsData.get(position));
            }
        });

    }

    private void initListItem(ViewHolder viewHolder,final int position){

        viewHolder.name.setText(itemsData.get(position).getName());

        viewHolder.phone.setText(itemsData.get(position).getNumber());

        viewHolder.email.setText(itemsData.get(position).getEmail());



        if ( itemsData.get(position).getPhotoUrl()!=null && !itemsData.get(position).getPhotoUrl().equals("null")) {
            viewHolder.photo.setImageBitmap(photoSaver.loadImageFromStorage(itemsData.get(position).getPhotoUrl()));
        }else{
            Log.d("PHOTO","NULL");
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public TextView phone;

        public TextView email;

        CircleImageView photo;

        ImageView fav;

        ImageView keep;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ai);

            phone = (TextView) itemLayoutView.findViewById(R.id.tv_number_ai);

            email = (TextView) itemLayoutView.findViewById(R.id.tv_email_ai);

            photo = (CircleImageView) itemLayoutView.findViewById(R.id.profile_image_ai);

            fav = (ImageView) itemLayoutView.findViewById(R.id.img_vip_ai);

            keep = (ImageView) itemLayoutView.findViewById(R.id.img_keep_ai);

        }
    }

    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return itemsData.size();
    }

    public void setOnClick(OnItemClicked onClick)
    {
        this.onClick=onClick;
    }

    public void removeItem(int position) {
        itemsData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsData.size());
    }


}
