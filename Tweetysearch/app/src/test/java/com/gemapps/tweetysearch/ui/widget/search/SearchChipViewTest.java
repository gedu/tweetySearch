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

package com.gemapps.tweetysearch.ui.widget.search;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by edu on 3/8/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchChipViewTest {

    private static final String INPUT_TEXT = "Android";

    @Mock
    private SearchChipViewBridge mMockViewBridge;

    private SearchViewPresenter mViewPresenter;

    @Before
    public void setup(){
        mViewPresenter = new SearchViewPresenter(mMockViewBridge);
    }

    @Test
    public void gettingEmptyTextIsTrue(){
        when(mMockViewBridge.getText()).thenReturn("");
        String text = mViewPresenter.getText();
        assertTrue(text.length() == 0);
    }

    @Test
    public void gettingInputTextIsTrue(){
        when(mMockViewBridge.getText()).thenReturn(INPUT_TEXT);
        String text = mViewPresenter.getText();
        assertTrue(text.length() > 0);
    }

    @Test
    public void wroteTextIsValid(){
        when(mMockViewBridge.isContentValid()).thenReturn(true);
        assertTrue(mViewPresenter.isContentValid());
    }

    @Test
    public void wroteTextIsInvalid(){
        when(mMockViewBridge.isContentValid()).thenReturn(false);
        assertFalse(mViewPresenter.isContentValid());
    }

    @Test
    public void showErrorLabelWhenContentIsInvalid(){
        mViewPresenter.showErrorLabel();
        verify(mMockViewBridge).showErrorLabel();
    }
}
