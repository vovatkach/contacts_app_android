package com.example.mukola.contactapplication.view.fragments.tinder;

import android.content.Context;
import android.graphics.Path;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.database.PhotoSaver;
import com.example.mukola.contactapplication.model.models.Contact;
import com.mindorks.placeholderview.SwipeDirection;
import com.mindorks.placeholderview.SwipePlaceHolderView;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
import com.mindorks.placeholderview.annotations.swipe.SwipeInDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutDirectional;
import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
import com.mindorks.placeholderview.annotations.swipe.SwipingDirection;

@Layout(R.layout.tinder_card_view)
public class TinderCard {


    private static final String  TOP = "TOP";
    private static final String  LEFT_TOP = "LEFT_TOP";
    private static final String  RIGHT_TOP = "RIGHT_TOP";
    private static final String  BOTTOM = "BOTTOM";
    private static final String  LEFT_BOTTOM = "LEFT_BOTTOM";
    private static final String  RIGHT_BOTTOM = "RIGHT_BOTTOM";
    private static final String  LEFT = "LEFT";
    private static final String  RIGHT = "RIGHT";





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

    @SwipeOutDirectional
    private void onSwipeOutDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeOutDirectional " + direction.name());

        if (direction.name().equals(TOP) || direction.name().equals(LEFT_TOP)
                || direction.name().equals(RIGHT_TOP)){

            mPerson.setFavorite(true);
            mPresenter.addToContacts(mUserId,mPerson);
            mPresenter.addToBlackList(mUserId,mPerson.getBlacklistId());

        }else if(direction.name().equals(LEFT)|| direction.name().equals(LEFT_BOTTOM )){

            mSwipeView.addView(this);
        }
    }

    @SwipeInDirectional
    private void onSwipeInDirectional(SwipeDirection direction) {
        Log.d("DEBUG", "SwipeInDirectional " + direction.name());
        if (direction.name().equals(BOTTOM)){

            mPresenter.addToArchive(mUserId,mPerson);

        }else if (direction.name().equals(RIGHT)|| direction.name().equals(RIGHT_BOTTOM)){
            mPresenter.addToContacts(mUserId,mPerson);
            mPresenter.addToBlackList(mUserId,mPerson.getBlacklistId());
        }
    }
}