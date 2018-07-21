package com.example.mukola.contactapplication.view.fragments.tinder;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mukola.contactapplication.R;
import com.example.mukola.contactapplication.model.models.Contact;
import com.example.mukola.contactapplication.view.acitivities.cleanUp.CleanUpActivity;
import com.mindorks.placeholderview.SwipeDecor;
import com.mindorks.placeholderview.SwipeDirectionalView;
import com.mindorks.placeholderview.SwipePlaceHolderView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class TinderFragment extends Fragment implements TinderContract.ITinderView ,Updateable{


    @NonNull
    private SwipeDirectionalView mSwipeView;

    @NonNull
    private Context mContext;

    @NonNull
    private OnTinderFragmentInteractionListener mListener;

    @NonNull
    private Unbinder unbinder;

    @NonNull
    private int userId;

    @NonNull
    private TinderContract.ITinderPresenter presenter;

    @NonNull
    private ArrayList<String> mBlacklist;


    @BindView(R.id.tinder_no)
    TextView tv;

    @BindView(R.id.progressBar_tind)
    ProgressBar progressBar;

    View v;

    public TinderFragment() {
        // Required empty public constructor
    }

    public static TinderFragment newInstance(int userId) {
        TinderFragment fragment = new TinderFragment();
        fragment.setUserId(userId);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tinder, container, false);

        unbinder = ButterKnife.bind(this,view);

        presenter = new TinderPresenter(this,getContext());

        if(((CleanUpActivity) getActivity()).getContacts().isEmpty()){
                tv.setVisibility(View.VISIBLE);
                tv.setText(getString(R.string.no_contact));
            }else {
                mSwipeView = (SwipeDirectionalView) view.findViewById(R.id.swipeView);
                mContext = getActivity().getApplicationContext();
                tv.setVisibility(View.GONE);
                presenter.initTab(view);
            }
            v = view;

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTinderFragmentInteractionListener) {
            mListener = (OnTinderFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTinderFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        presenter.detachView();
        unbinder.unbind();
        getActivity().finish();
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initTab(final View view) {
        presenter.getBlackList(userId);
        mSwipeView.getBuilder()
                .setDisplayViewCount(3)
                .setSwipeDecor(new SwipeDecor()
                        .setPaddingTop(20)
                        .setRelativeScale(0.01f)
                        .setSwipeInMsgLayoutId(R.layout.tinder_swipe_in_msg_view)
                        .setSwipeOutMsgLayoutId(R.layout.tinder_swipe_out_msg_view));

        for(Contact person : ((CleanUpActivity) getActivity()).getContacts()){
            boolean indicator = false;
            if (mBlacklist!=null) {
                for (String s : mBlacklist) {
                    if (person.getBlacklistId().equals(s)) {
                        indicator = true;
                    }
                }
            }
            if (!indicator) {
                mSwipeView.addView(new TinderCard(mContext, person, mSwipeView, presenter, userId));
            }
        }
        view.findViewById(R.id.rejectBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(false);
            }
        });

        view.findViewById(R.id.acceptBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeView.doSwipe(true);
            }
        });

    }

    @Override
    public void setBlacklist(@NonNull ArrayList<String> blacklist) {
        mBlacklist = blacklist;
    }

    public void setUserId(@NonNull int userId) {
        this.userId = userId;
    }

    @Override
    public void update() {
        //initTab(v);
    }

    public interface OnTinderFragmentInteractionListener {
    }
}
