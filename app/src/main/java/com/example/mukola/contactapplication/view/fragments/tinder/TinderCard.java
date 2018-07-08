package com.example.mukola.contactapplication.view.fragments.tinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;

@Layout(R.layout.tinder_card_view)
public class TinderCard {

    @View(R.id.profileImageView)
    private ImageView profileImageView;

    @View(R.id.nameAgeTxt)
    private TextView nameAgeTxt;

    @View(R.id.locationNameTxt)
    private TextView locationNameTxt;

    @View(R.id.emailTxt)
    private TextView email;

    @NonNull
    private Contact mPerson;

    @NonNull
    private Context mContext;

    @NonNull
    private SwipePlaceHolderView mSwipeView;

    @NonNull
    private TinderContract.ITinderPresenter mPresenter;

    @NonNull
    private PhotoSaver photoSaver;

    @NonNull
    private int mUserId;

    public TinderCard(@NonNull Context context,@NonNull Contact person,@NonNull SwipePlaceHolderView swipeView,
                      @NonNull TinderContract.ITinderPresenter presenter,@NonNull int userId) {
        mContext = context;
        mPerson = person;
        mSwipeView = swipeView;
        mPresenter = presenter;
        mUserId = userId;
        photoSaver = new PhotoSaver(context);
    }

    @Resolve
    private void onResolved(){

        nameAgeTxt.setText(mPerson.getName());

        locationNameTxt.setText(mPerson.getAddress());

        email.setText(mPerson.getEmail());

        if ( mPerson.getPhotoUrl()!=null && !mPerson.getPhotoUrl().equals("null")) {
            profileImageView.setImageBitmap(photoSaver.loadImageFromStorage(mPerson.getPhotoUrl()));
        }else{
            Log.d("PHOTO","NULL");
            profileImageView.setImageResource(R.drawable.profile);

        }
    }

    @SwipeOut
    private void onSwipedOut(){
        Log.d("EVENT", "onSwipedOut");
        mSwipeView.addView(this);
        //mPresenter.addToBlackList(mUserId,mPerson.getResourceName());
    }

    @SwipeCancelState
    private void onSwipeCancelState(){

        Log.d("EVENT", "onSwipeCancelState");
    }

    @SwipeIn
    private void onSwipeIn(){
        Log.d("EVENT", "onSwipedIn");
        mPresenter.addToContacts(mUserId,mPerson);
        mPresenter.addToBlackList(mUserId,mPerson.getBlacklistId());
    }

    @SwipeInState
    private void onSwipeInState(){
        Log.d("EVENT", "onSwipeInState");
    }

    @SwipeOutState
    private void onSwipeOutState(){
        Log.d("EVENT", "onSwipeOutState");
    }


}