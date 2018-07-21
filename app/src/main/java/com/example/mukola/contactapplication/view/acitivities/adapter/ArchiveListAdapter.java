package com.example.mukola.contactapplication.view.acitivities.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArchiveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SectionIndexer {

    private ArrayList<Contact> itemsData;

    private OnItemClicked onClick;

    private PhotoSaver photoSaver;

    public static final int SECTION_VIEW = 0;

    public static final int CONTENT_VIEW = 1;

    WeakReference<Context> mContextWeakReference;

    private ArrayList<Integer> mSectionPositions;

    private List<String> mDataArray;
    @Override
    public Object[] getSections() {
        List<String> sections = new ArrayList<>(26);
        mSectionPositions = new ArrayList<>(26);
        for (int i = 0, size = mDataArray.size(); i < size; i++) {
            String section = String.valueOf(mDataArray.get(i).charAt(0)).toUpperCase();
            if (!sections.contains(section)) {
                sections.add(section);
                mSectionPositions.add(i);
            }
        }
        return sections.toArray(new String[0]);
    }

    @Override
    public int getPositionForSection(int i) {
        int x = 0,y = 0;
        for (Contact c:itemsData) {
            if (c.isSectioned()){
                x++;
            }
            if (x==i+1){
                i = y;
                break;
            }
            y++;
        }
        return i;
    }

    @Override
    public int getSectionForPosition(int i) {
        return 0;
    }


    public interface OnItemClicked {
        void onFavClick(@NonNull Contact contact);
        void onKeepClick(@NonNull Contact contact);
        void onUserClick(@NonNull Contact contact);
    }

    public ArchiveListAdapter(ArrayList<Contact> itemsData, Context context) {
        this.itemsData = itemsData;
        photoSaver = new PhotoSaver(context);
        mDataArray = new ArrayList<>();
        this.mContextWeakReference = new WeakReference<Context>(context);
        for (Contact c: itemsData) {
            if (!c.isSectioned()){
                mDataArray.add(c.getName());
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_item, parent,false);

        Context context = mContextWeakReference.get();
        if (viewType == SECTION_VIEW) {
            Log.d("SECTION","YES");
            return new SectionHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false));
        }
        return new ArchiveListAdapter.ViewHolder(itemLayoutView);
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

        ArchiveListAdapter.ViewHolder viewHolder = (ArchiveListAdapter.ViewHolder) holder;

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
