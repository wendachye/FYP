package com.example.wenda.tarucnfc;

/**
 * Created by Wenda on 1/14/2016.
 */
import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class UIUtils {

    static AlertDialog ad;

    private static Typeface sMediumTypeface;

    protected ActionBarActivity mActivity;
    private Handler mHandler = new Handler();

    private UIUtils(ActionBarActivity activity) {
        mActivity = activity;
    }

    public static UIUtils getInstance(ActionBarActivity activity) {
        return new UIUtils(activity);
    }

    private static boolean hasL() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static AlertDialog getProgressDialog(Activity activity, String toggle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        final View view = LayoutInflater.from(activity).inflate(
                R.layout.progress_dialog, null);
        View img1 = view.findViewById(R.id.pd_circle1);
        View img2 = view.findViewById(R.id.pd_circle2);
        View img3 = view.findViewById(R.id.pd_circle3);
        int ANIMATION_DURATION = 400;
        Animator anim1 = setRepeatableAnim(activity, img1, ANIMATION_DURATION, R.animator.growndisappear);
        Animator anim2 = setRepeatableAnim(activity, img2, ANIMATION_DURATION, R.animator.growndisappear);
        Animator anim3 = setRepeatableAnim(activity, img3, ANIMATION_DURATION, R.animator.growndisappear);
        setListeners(img1, anim1, anim2, ANIMATION_DURATION);
        setListeners(img2, anim2, anim3, ANIMATION_DURATION);
        setListeners(img3, anim3, anim1, ANIMATION_DURATION);
        if (toggle.equals("ON")) {
            builder.setView(view);
            ad = builder.create();
            Log.d("Track", "On");
            anim1.start();
            ad.setCanceledOnTouchOutside(false);
            ad.requestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
            ad.show();
            ad.getWindow().setLayout(dpToPx(200, activity), dpToPx(125, activity));
        } else {
            ad.dismiss();
            Log.d("Track", "Dismissed");
        }
        return ad;
    }


    /**
     * Convert dp to px
     *
     * @param i
     * @param mContext
     * @return
     * @author Sachin
     */

    public static int dpToPx(int i, Context mContext) {

        DisplayMetrics displayMetrics = mContext.getResources()
                .getDisplayMetrics();
        return (int) ((i * displayMetrics.density) + 0.5);

    }


    private static Animator setRepeatableAnim(Activity activity, View target, final int duration, int animRes) {
        final Animator anim = AnimatorInflater.loadAnimator(activity, animRes);
        anim.setDuration(duration);
        anim.setTarget(target);
        return anim;
    }

    private static void setListeners(final View target, Animator anim, final Animator animator, final int duration) {
        anim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animat) {
                if (target.getVisibility() == View.INVISIBLE) {
                    target.setVisibility(View.VISIBLE);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animator.start();
                    }
                }, duration - 100);
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
