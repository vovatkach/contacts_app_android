package com.example.mukola.contactapplication.view.acitivities.mainScreen.adapter;


import android.app.Activity;
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

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private ArrayList<Person> itemsData;

    private OnItemClicked onClick;

    private Activity activity;

    public interface OnItemClicked {
        void onCallClick(String number);
        void onMessageClick(String number);

    }

    public ContactListAdapter(ArrayList<Person> itemsData,Activity activity) {
        this.itemsData = itemsData;
        this.activity = activity;
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

    private void initListeners(ViewHolder viewHolder,final int position){
        viewHolder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onCallClick(itemsData.get(position).getPhoneNumbers().get(0).getCanonicalForm());
                Log.d("CALL","CLICKED");
            }
        });

        viewHolder.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick.onMessageClick(itemsData.get(position).getPhoneNumbers().get(0).getCanonicalForm());
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
        if (itemsData.get(position).getPhoneNumbers()!=null) {
            String ph = itemsData.get(position).getPhoneNumbers().get(0).getCanonicalForm();
            if (ph!=null) {
                viewHolder.phone.setText(ph);
            }else{
                viewHolder.phone.setText(activity.getString(R.string.no_phone));
            }
        }
        if (itemsData.get(position).getEmailAddresses()!=null) {
            String em = itemsData.get(position).getEmailAddresses().get(0).getValue();
            if (em!=null) {
                viewHolder.email.setText(em);
            }else{
                viewHolder.email.setText(activity.getString(R.string.no_email));
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

        public TextView phone;

        public TextView email;

        CircleImageView photo;

        ImageView call;

        ImageView message;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            name = (TextView) itemLayoutView.findViewById(R.id.tv_name_ci);

            phone = (TextView) itemLayoutView.findViewById(R.id.tv_phone_ci);

            email = (TextView) itemLayoutView.findViewById(R.id.tv_email_ci);

            photo = (CircleImageView) itemLayoutView.findViewById(R.id.profile_image);

            call = (ImageView) itemLayoutView.findViewById(R.id.imgView_call_ci);

            message = (ImageView) itemLayoutView.findViewById(R.id.imgView_message_ci);
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
