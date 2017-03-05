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

package com.gemapps.tweetysearch.ui.widget;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;

import com.gemapps.tweetysearch.R;
import com.gemapps.tweetysearch.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by edu on 7/21/16.
 *
 * Base activity for the cards activities with Butter knife. This activity handle the enter and exit animation
 *
 * For this activity works the root should be a Frame
 * then the first child should be translucent_bg layout include it then the content
 * preferably inside a Card with an id card_panel_container
 * Set in the manifest @style/AppTheme.Dialog like style
 */
public class BaseCardActivity extends AppCompatActivity {

    private static final String TAG = "BaseCardActivity";

    protected final static Interpolator ACC_DEC_INTERPOLATOR = new FastOutLinearInInterpolator();
    private static final String CENTER_X_KEY = "tweety.CENTER_X_KEY";
    private static final String CENTER_Y_KEY = "tweety.CENTER_Y_KEY";

    @BindView(R.id.trans_bg_view)
    View mBackground;
    @BindView(R.id.card_panel_container) protected View mCardPanel;
    private int mCenterX = 0;
    private int mCenterY = 0;

    /**
     * Create a {@link Intent} instance with the center X param, to use later in the animation
     * @param view The view to which get the center x
     * @param className The activity to start
     * @return
     */
    public static Intent getInstance(View  view, Class<?> className){

        Intent intent = new Intent(view.getContext(), className);
        intent.putExtra(CENTER_X_KEY, getCenterX(view));
        intent.putExtra(CENTER_Y_KEY, getCenterY(view));

        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null) {

            mCenterX = (int) getIntent().getExtras()
                    .getFloat(CENTER_X_KEY);
            mCenterY = (int) getIntent().getExtras()
                    .getFloat(CENTER_Y_KEY);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    /**
     * To make the animation works this should be called after the doEntryAnimation
     * on the onCreate method
     */
    protected void overrideTrans(){
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isFinishing()) dismiss(null);
    }

    @Override
    public void onBackPressed() {
        dismiss(null);
    }

    /**
     * Should be called on the onCreate function at the end before {#link overrideTrans}
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void doEntryAnimation(){

        if(!Util.isLollipop()) return;

        mBackground.animate()
                .alpha(1)
                .setDuration(500L)
                .setInterpolator(ACC_DEC_INTERPOLATOR)
                .start();

        mCardPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                mCardPanel.getViewTreeObserver().removeOnPreDrawListener(this);

                int revealRadius = getRevealSize();

                Animator show = ViewAnimationUtils.createCircularReveal(mCardPanel,
                        mCenterX,
                        mCenterY,
                        0f, revealRadius);
                show.setDuration(350L);
                show.setInterpolator(ACC_DEC_INTERPOLATOR);
                show.start();

                return false;
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    protected void doExitAnimation(){

        if(!Util.isLollipop()) return;

        mBackground.animate()
                .alpha(0f)
                .setDuration(200L)
                .setInterpolator(ACC_DEC_INTERPOLATOR)
                .start();

        mCardPanel.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {

                mCardPanel.getViewTreeObserver().removeOnPreDrawListener(this);

                int revealRadius = getRevealSize();

                Animator show = ViewAnimationUtils.createCircularReveal(mCardPanel,
                        mCenterX,
                        mCenterY,
                        revealRadius, 0f);
                show.setDuration(350L);
                show.setInterpolator(ACC_DEC_INTERPOLATOR);
                show.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mCardPanel.setVisibility(View.INVISIBLE);
//                        ActivityCompat.finishAfterTransition(BaseCardActivity.this);
                    }
                });
                show.start();

                return false;
            }
        });
    }

    @OnClick(R.id.trans_bg_view)
    public void onBgClicked(){
        dismiss(null);
    }

    protected void dismiss(View view){
        if (Util.isLollipop()) {
            doExitAnimation();
            ActivityCompat.finishAfterTransition(BaseCardActivity.this);
        }else{
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCenterX(View view){
        return (view.getX() + view.getWidth() / 2);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static float getCenterY(View view){
        return (view.getY() + view.getWidth() / 2);
    }

    protected int getRevealSize(){
        return mCardPanel.getWidth();
    }

    protected int getCenterX(){
        return mCenterX;
    }

}