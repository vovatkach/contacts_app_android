package com.example.mukola.contactapplication.view.acitivities.adapter;


import android.app.Activity;
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

public class ImportContactsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Contact> itemsData;

    private OnItemClicked onClick;

    public static final int SECTION_VIEW = 0;

    public static final int CONTENT_VIEW = 1;

    WeakReference<Context> mContextWeakReference;

    private PhotoSaver photoSaver;

    public interface OnItemClicked {
        void onAddClick(@NonNull Contact contact);
        void onFavoriteClick(@NonNull Contact contact);
        void onArchiveClick(@NonNull Contact contact);
        void onUserClick(@NonNull Contact contact);
    }

    public ImportContactsListAdapter(ArrayList<Contact> itemsData,Context context) {
        this.itemsData = itemsData;
        photoSaver = new PhotoSaver(context);
        this.mContextWeakReference = new WeakReference<Context>(context);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_contact_item, parent,false);

        Context context = mContextWeakReference.get();
        if (viewType == SECTION_VIEW) {
            Log.d("SECTION","YES");
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
        }
        return new ViewHolder(itemLayoutView);

        }

    @Override
    public int getItemViewType(int position) {
        if (itemsData.get(position).isSectioned()) {
            return SECTION_VIEW;
        } else {
            return CONTENT_VIEW;
        }
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

        ViewHolder viewHolder = (ViewHolder) holder;

        initListeners(viewHolder,position);
        initListItem(viewHolder,position);

    }

    // Replace the contents of a view (invoked by the layout manager)


    private void initListeners(ViewHolder viewHolder,final int position){
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onAddClick(itemsData.get(position));
            }
        });

        viewHolder.archive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onArchiveClick(itemsData.get(position));
            }
        });

        viewHolder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onFavoriteClick(itemsData.get(position));
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

        Log.d("LOL",String.valueOf(itemsData.get(position).getName().charAt(0))+position);

        viewHolder.name.setText(itemsData.get(position).getName());

        viewHolder.number.setText(itemsData.get(position).getNumber());

        viewHolder.email.setText(itemsData.get(position).getEmail());

        if ( itemsData.get(position).getPhotoUrl()!=null && !itemsData.get(position).getPhotoUrl().equals("null")) {
            viewHolder.photo.setImageBitmap(photoSaver.loadImageFromStorage(itemsData.get(position).getPhotoUrl()));
        }else{
            Log.d("PHOTO","NULL");
            viewHolder.photo.setImageResource(R.drawable.profile);
        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        public TextView number;

        public TextView email;

        CircleImageView photo;

        ImageView add;

        ImageView archive;

        ImageView fav;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ici);

            number = (TextView) itemLayoutView.findViewById(R.id.tv_number_ici);

            email = (TextView) itemLayoutView.findViewById(R.id.tv_email_ici);

            photo = (CircleImageView) itemLayoutView.findViewById(R.id.profile_image_ici);

            add = (ImageView) itemLayoutView.findViewById(R.id.img_aff_ici);

            archive = (ImageView) itemLayoutView.findViewById(R.id.img_archive_ici);

            fav = (ImageView) itemLayoutView.findViewById(R.id.img_vip_ici);

        }
    }

    public class SectionHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitleTextview;

        public SectionHeaderViewHolder(View itemView) {
            super(itemView);
            headerTitleTextview = (TextView) itemView.findViewById(R.id.headerTitleTextview);
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
