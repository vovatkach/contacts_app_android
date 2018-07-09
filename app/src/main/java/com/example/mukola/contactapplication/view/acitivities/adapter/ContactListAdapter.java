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

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<Contact> itemsData;

    private OnItemClicked onClick;

    private Context  context;

    private PhotoSaver photoSaver;

    private char p;


    public interface OnItemClicked {
        void onFavClick(@NonNull Contact contact);
        void onUserClick(@NonNull Contact contact);
    }

    public ContactListAdapter(ArrayList<Contact> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
        photoSaver = new PhotoSaver(context);
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent,false);

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {


        initListeners(viewHolder,position);

        initListItem(viewHolder,position);

    }

    private void initListeners(final ViewHolder viewHolder, final int position){
        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onClick.onFavClick(itemsData.get(position));
                if (itemsData.get(position).isFavorite()){
                    viewHolder.fav.setImageResource(R.drawable.ic_star_border_black_24dp);
                    itemsData.get(position).setFavorite(false);
                }else {
                    viewHolder.fav.setImageResource(R.drawable.ic_star_black_24dp);
                    itemsData.get(position).setFavorite(true);
                }
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

        if (position==0){
            viewHolder.alp.setText(String.valueOf(itemsData.get(0).getName().charAt(0)));
            p = itemsData.get(0).getName().charAt(0);
        }

        if (itemsData.get(position).getName().charAt(0) != p){
            viewHolder.alp.setText(String.valueOf(itemsData.get(position).getName().charAt(0)));
            p = itemsData.get(position).getName().charAt(0);
        }

        viewHolder.name.setText(itemsData.get(position).getName());

        viewHolder.phone.setText(itemsData.get(position).getNumber());

        viewHolder.email.setText(itemsData.get(position).getEmail());

        if (itemsData.get(position).isFavorite()){
            viewHolder.fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_black_24dp));
        }else {
            viewHolder.fav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_border_black_24dp));

        }


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

        public TextView alp;


        CircleImageView photo;

        ImageView fav;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ci);

            phone = (TextView) itemLayoutView.findViewById(R.id.tv_number_ci);

            email = (TextView) itemLayoutView.findViewById(R.id.tv_email_ci);

            alp = (TextView) itemLayoutView.findViewById(R.id.alp);

            photo = (CircleImageView) itemLayoutView.findViewById(R.id.profile_image);

            fav = (ImageView) itemLayoutView.findViewById(R.id.imgView_fav_ci);

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
