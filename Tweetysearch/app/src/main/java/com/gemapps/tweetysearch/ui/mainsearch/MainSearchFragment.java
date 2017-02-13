/*
 *    Copyright 2017 Edu Graciano
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.gemapps.tweetysearch.ui.mainsearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.networking.searchquery.UrlParameter;
import com.gemapps.tweetysearch.networking.searchquery.paramquery.Query;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainSearchFragment.OnSearchListener} interface
 * to handle interaction events.
 * Use the {@link MainSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainSearchFragment extends Fragment {

    private static final String TAG = "MainSearchFragment";
    public interface OnSearchListener {

        void onSearch(UrlParameter urlParameter);
    }

    private OnSearchListener mListener;
    private MainSearchViewHelper mViewHelper;
    private UrlParameter.Builder mParameterBuilder;

    public MainSearchFragment() {
        // Required empty public constructor
    }

    public static MainSearchFragment newInstance() {
        return new MainSearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParameterBuilder = new UrlParameter.Builder();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSearchListener) {
            mListener = (OnSearchListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main_search, container, false);
        ButterKnife.bind(this, root);
        mViewHelper = new MainSearchViewHelper(root);
        mViewHelper.addSearchClickListener(new MainSearchViewHelper.SearchClickListener() {
            @Override
            public void onSearchClicked() {
                onSearchPressed();
            }
        });
        return root;
    }

    @OnClick(R.id.query_search_button)
    public void onSearchClicked(){
        onSearchPressed();
    }

    public void onSearchPressed() {
        if (mListener != null) {
            mParameterBuilder.addParameter(new Query(mViewHelper.getTextToSearch()));
            mListener.onSearch(mParameterBuilder.build());
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
