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

package com.gemapps.tweetysearch.ui.mainsearch.presenter;

import com.gemapps.tweetysearch.ui.mainsearch.RecentlySearchedAdapter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by edu on 3/11/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class MainFragmentPresenterTest {

    private MainFragmentContract.OnInteractionListener mPresenter;

    @Mock
    private MainFragmentContract.View mView;
    @Mock
    private MainFragmentContract.OnSearchListener mListener;

    @Before
    public void setup(){
        mPresenter = new MainFragmentPresenter(mView,
                SearchedItemInjector.provideRecentlyItems(),
                mListener);
    }

    @Test
    public void emptySearch_showEmptyView(){
        RecentlySearchedAdapter mockAdapter = getMockAdapter();

        mPresenter.addAdapter(mockAdapter);
        mPresenter.updateViewFromSearch();

        verify(mView).showEmptyView();
    }

    @Test
    public void succeedSearch_hideEmptyView(){
        RecentlySearchedAdapter mockAdapter = getMockAdapter();
        when(mockAdapter.getItemCount()).thenReturn(1);

        mPresenter.addAdapter(mockAdapter);
        mPresenter.updateViewFromSearch();

        verify(mView).hideEmptyView();
    }

    @Test
    public void onSearchButtonClicked_showErrorLabel(){
        mPresenter.onPerformActionSearch();
        verify(mView).isTextToSearchValid();
        verify(mView).showSearchErrorLabel();
    }

    @Test
    public void onSearchButtonClicked_doSearch(){
        when(mView.isTextToSearchValid()).thenReturn(true);

        mPresenter.onPerformActionSearch();

        verify(mView).isTextToSearchValid();
        verify(mView).hideSearchErrorLabel();
        verify(mView).getSearchifiedText();
    }

    private RecentlySearchedAdapter getMockAdapter(){
        return mock(RecentlySearchedAdapter.class);
    }
}
