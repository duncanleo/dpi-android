package com.duncan.dpi.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.duncan.dpi.R;
import com.duncan.dpi.util.CalcUtil;

/**
 * Created by duncan on 10/3/15.
 * NOTE: THIS IS NOT A VIEW CONTROLLER IN THE iOS CONTEXT. It's literally a class with methods to *control* the view.
 */
public class DynamicViewController {
    View rootView;
    DynamicViewHolder viewHolder;
    int height = -1, width = -1;
    double screenSize = -1;
    float maxWidth, maxHeight;

    public DynamicViewController(View rootView) {
        this.rootView = rootView;
        this.viewHolder = new DynamicViewHolder(rootView);
        maxWidth = CalcUtil.INSTANCE.getWidth((Activity)rootView.getContext());
        maxHeight = pxFromDp(rootView.getContext(), 200);

        updateAllText();
    }

    public void setHeight(int height) {
        this.height = height;
        updateAllText();
    }

    public void setWidth(int width) {
        this.width = width;
        updateAllText();
    }

    public void setScreenSize(double screenSize) {
        this.screenSize = screenSize;
        updateAllText();
    }

    private void updateAllText() {
        this.viewHolder.height.setText(this.height == -1 ? "-" : height + "");
        this.viewHolder.width.setText(this.width == -1 ? "-" : width + "");
        this.viewHolder.screenSize.setText(this.screenSize == -1 ? "-" : screenSize + "\"");

        //Check for minimum
        if (height <= 0 || width <= 0) {
            viewHolder.screen.setLayoutParams(new FrameLayout.LayoutParams((int)pxFromDp(rootView.getContext(), 60), (int)pxFromDp(rootView.getContext(), 60)));
            return;
        }

        //Animate a refresh
        rootView.setAlpha(0f);
        rootView.animate().alpha(1f).setDuration(300);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                maxWidth = rootView.getMeasuredWidth() - (2 * (viewHolder.height.getMeasuredWidth() + pxFromDp(rootView.getContext(), 8))) - pxFromDp(rootView.getContext(), 16);

                //Setup sizes
                float calculatedWidth = Math.max(Math.max(viewHolder.width.getMeasuredWidth(), viewHolder.screenSize.getMeasuredWidth()), width / (Math.max(height, maxHeight) / Math.min(height, maxHeight)));
                if (calculatedWidth > maxWidth) {
                    //Dynamic height instead
                    float calculatedHeight = Math.max(Math.max(viewHolder.height.getMeasuredHeight(), viewHolder.screenSize.getMeasuredHeight()), height / (Math.max(width, maxWidth) / Math.min(width, maxWidth)));
                    viewHolder.screen.setLayoutParams(new FrameLayout.LayoutParams((int) maxWidth, (int) calculatedHeight));
                } else {
                    viewHolder.screen.setLayoutParams(new FrameLayout.LayoutParams((int) calculatedWidth, (int) maxHeight));
                }
            }
        }, 30);
    }

    private float pxFromDp(final Context context, final float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    class DynamicViewHolder {
        public TextView height, width, screenSize;
        public ImageView screen;

        public DynamicViewHolder(View rootView) {
            this.height = (TextView)rootView.findViewById(R.id.height);
            this.width = (TextView)rootView.findViewById(R.id.width);
            this.screenSize = (TextView)rootView.findViewById(R.id.screenSize);
            this.screen = (ImageView)rootView.findViewById(R.id.screen);
        }
    }
}
