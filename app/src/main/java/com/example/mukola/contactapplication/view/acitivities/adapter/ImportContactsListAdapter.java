package com.example.mukola.contactapplication.view.acitivities.adapter;



import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.mukola.contactapplication.R;
import com.google.api.services.people.v1.model.Person;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImportContactsListAdapter extends RecyclerView.Adapter<ImportContactsListAdapter.ViewHolder> {

    private ArrayList<Person> itemsData;

    private OnItemClicked onClick;

    private Activity activity;

    public interface OnItemClicked {
        void onAddClick(@NonNull Person person);
        void onUserClick(@NonNull Person person);
    }

    public ImportContactsListAdapter(ArrayList<Person> itemsData,Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
    }

    @Override
    public ImportContactsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.import_contact_item, parent,false);

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
        viewHolder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onAddClick(itemsData.get(position));
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

        if (itemsData.get(position).getNames()!=null) {
            String nm = itemsData.get(position).getNames().get(0).getDisplayName();
            if (nm!=null){
                viewHolder.name.setText(nm);
            }else{
                viewHolder.name.setText(activity.getString(R.string.no_name));
            }
        }
        if (itemsData.get(position).getPhotos()!=null) {
            Glide
                    .with(activity)
                    .load(itemsData.get(position).getPhotos().get(0).getUrl())
                    .into(viewHolder.photo);
        }else{
            Log.d("PHOTO","NULL");

        }
    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name;

        CircleImageView photo;

        ImageView add;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ici);


            photo = (CircleImageView) itemLayoutView.findViewById(R.id.profile_image_ici);

            add = (ImageView) itemLayoutView.findViewById(R.id.img_aff_ici);
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
