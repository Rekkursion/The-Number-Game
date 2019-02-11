package com.example.user.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

public class RankingItemDecoration extends RecyclerView.ItemDecoration {
    final Context context;
    final int newRecordIdx;
    final int dividerHeight = 1;

    public RankingItemDecoration(Context context, int newRecordIdx) {
        this.context = context;
        this.newRecordIdx = newRecordIdx;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if(parent.getChildAdapterPosition(view) == 0)
            outRect.set(0, 0, 0, 0);
        else
            outRect.set(0, dividerHeight, 0, 0);
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int itemCount = parent.getAdapter().getItemCount();
        for(int k = 0; k < itemCount; ++k) {
            View view = parent.getChildAt(k);
            if(view != null) {
                int top = view.getTop() - dividerHeight;
                int bottom = view.getTop();

                paint.setARGB(100, 170, 170, 170);
                c.drawRect(left, top, right, bottom, paint);
            }
            else
                Log.e("RID onDraw", String.valueOf(k));
        }
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int itemCount = parent.getAdapter().getItemCount();
        for(int k = 0; k < itemCount; ++k) {
            View view = parent.getChildAt(k);
            if(view != null) {
                if(parent.getChildAdapterPosition(view) == newRecordIdx)
                    paint.setARGB(40, 201, 32, 94);
                else {
                    if((parent.getChildAdapterPosition(view) & 1) == 1)
                        paint.setColor(Color.parseColor("#00000000"));
                    else
                        paint.setColor(Color.parseColor("#20707070"));
                }

                c.drawRect(view.getLeft(), view.getTop(), view.getRight(), view.getBottom(), paint);
            }
            else
                Log.e("RID onDrawOver", String.valueOf(k));
        }
    }
}
