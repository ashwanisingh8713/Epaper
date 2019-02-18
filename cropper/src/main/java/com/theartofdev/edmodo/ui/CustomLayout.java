package com.theartofdev.edmodo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v4.util.Pair;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import com.github.chrisbanes.VertexData;

import java.util.ArrayList;

public class CustomLayout extends View {


    private ArrayList<Pair<VertexData, Paint>> mSelectedPair = new ArrayList<>();



    public CustomLayout(Context context) {
        super(context);

    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }





    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);

        for (Pair<VertexData, Paint> pair : mSelectedPair) {
            canvas.drawRect(pair.first.getRect(), pair.second);
        }

    }


    public void setSelectedPair(ArrayList<Pair<VertexData, Paint>> selectedPair) {
        mSelectedPair = selectedPair;
        postInvalidate();
    }




    boolean clearCanvas = false;

    public void clearCanvas() {
        clearCanvas = true;
        invalidate();
    }


}
