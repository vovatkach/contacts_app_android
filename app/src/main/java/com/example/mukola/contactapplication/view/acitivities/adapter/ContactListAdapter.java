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

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Contact> itemsData;

    private OnItemClicked onClick;

    private Context  context;

    private PhotoSaver photoSaver;

    public static final int SECTION_VIEW = 0;

    public static final int CONTENT_VIEW = 1;

    WeakReference<Context> mContextWeakReference;


    public interface OnItemClicked {
        void onFavClick(@NonNull Contact contact);
        void onUserClick(@NonNull Contact contact);
    }

    public ContactListAdapter(ArrayList<Contact> itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
        photoSaver = new PhotoSaver(context);
        this.mContextWeakReference = new WeakReference<Context>(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent,false);

        Context context = mContextWeakReference.get();
        if (viewType == SECTION_VIEW) {
            Log.d("SECTION","YES");
            return new ContactListAdapter.SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
        }
        return new ContactListAdapter.ViewHolder(itemLayoutView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Context context = mContextWeakReference.get();
        if (context == null) {
            return;
        }
        if (SECTION_VIEW == getItemViewType(position)) {

            SectionHeaderViewHolder sectionHeaderViewHolder = (SectionHeaderViewHolder) holder;
            Contact sectionItem = itemsData.get(position);
            sectionHeaderViewHolder.headerTitleTextview.setText(sectionItem.getName());
            return;
        }

        ContactListAdapter.ViewHolder viewHolder = (ContactListAdapter.ViewHolder) holder;

        initListeners(viewHolder,position);
        initListItem(viewHolder,position);

    }

    @Override
    public int getItemViewType(int position) {
        if (itemsData.get(position).isSectioned()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
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

        CircleImageView photo;

        ImageView fav;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ci);

            phone = (TextView) itemLayoutView.findViewById(R.id.tv_number_ci);

            email = (TextView) itemLayoutView.findViewById(R.id.tv_email_ci);

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


    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextview;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = (TextView) itemView.findViewById(R.id.headerTitleTextview);
        }
    }


    public void removeItem(int position) {
        itemsData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemsData.size());
    }


}
