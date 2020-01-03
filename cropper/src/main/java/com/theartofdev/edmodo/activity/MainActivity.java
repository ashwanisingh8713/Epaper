package com.theartofdev.edmodo.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.github.chrisbanes.VertexData;
import com.github.chrisbanes.photoview.OnMatrixChangedListener;
import com.github.chrisbanes.photoview.OnViewTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.theartofdev.edmodo.cropper.R;
import com.theartofdev.edmodo.parsing.LocalParserUtil;
import com.theartofdev.edmodo.parsing.RequestCallback;
import com.theartofdev.edmodo.ui.CustomLayout;
import com.theartofdev.edmodo.utils.CropImageViewOptions;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CropImageView.OnSetImageUriCompleteListener,
        CropImageView.OnCropImageCompleteListener, RequestCallback<List<VertexData>> {

    private CropImageView mCropImageView;

    private RectF mRectF;

    private FrameLayout mFrameLayout;
    PhotoView mPhotoView;


    List<VertexData> mOriginalVertexData;
    List<VertexData> mModifiedVertexData;

    private Button mShowHideCropLayoutBtn;
    private Button mCroppingBtn;

    private float mScaleValue = 1.0f;

    public void updateCurrentCropViewOptions() {
        CropImageViewOptions options = new CropImageViewOptions();
        options.scaleType = mCropImageView.getScaleType();
        options.cropShape = mCropImageView.getCropShape();
        options.guidelines = mCropImageView.getGuidelines();
        options.aspectRatio = mCropImageView.getAspectRatio();
        options.fixAspectRatio = mCropImageView.isFixAspectRatio();
        options.showCropOverlay = mCropImageView.isShowCropOverlay();
        options.showProgressBar = mCropImageView.isShowProgressBar();
        options.autoZoomEnabled = mCropImageView.isAutoZoomEnabled();
        options.maxZoomLevel = mCropImageView.getMaxZoom();
        options.flipHorizontally = mCropImageView.isFlippedHorizontally();
        options.flipVertically = mCropImageView.isFlippedVertically();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFrameLayout = findViewById(R.id.frameLayout);
        mCropImageView = findViewById(R.id.cropImageView);

        // Buttons
        mShowHideCropLayoutBtn = findViewById(R.id.hideShowBtn);
        mCroppingBtn = findViewById(R.id.cropBtn);

        mShowHideCropLayoutBtn.setVisibility(View.GONE);

        mCropImageView.setOnSetImageUriCompleteListener(this);
        mCropImageView.setOnCropImageCompleteListener(this);

        // Auto zoom should be enabled currently to work with CROP feature.
        // Currently CROP feature will not work correctly if auto zoom is disabled.
        mCropImageView.setAutoZoomEnabled(true);
        mCropImageView.setShowCropOverlay(false);
        mCropImageView.setMultiTouchEnabled(false);

        mPhotoView = mCropImageView.getPhotoView();



        mShowHideCropLayoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCropImageView.setShowCropOverlay(!mCropImageView.isShowCropOverlay());

            }
        });

        mCroppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCropImageView.isShowCropOverlay()) {
                    mCropImageView.getCroppedImageAsync();
                }
            }
        });


        updateCurrentCropViewOptions();
        mCropImageView.setImageResource(R.drawable.epaper);

        // PhotoView, ViewTreeObserver to get Initial width and Height
        ViewTreeObserver viewTreeObserver = mPhotoView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mPhotoView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewWidth = mPhotoView.getWidth();
                    int viewHeight = mPhotoView.getHeight();

                    int origImgWidth = 1600;
                    int origImgHeight = 2469;

                    double widthRatio = (double) viewWidth / origImgWidth;
                    double heightRatio = (double) viewHeight / origImgHeight;

                    // Parsing Server vertex data
                    LocalParserUtil.parseVertexFile2(MainActivity.this, MainActivity.this,
                            "sample.json", viewWidth, viewHeight);

                }
            });
        }

        // PhotoView Zoom in/out change listener
        mPhotoView.setOnMatrixChangeListener(new MatrixChangeListener());

        // PhotoView Tap Listener
        mPhotoView.setOnViewTapListener(new OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {

                String objectId = "";


                ArrayList<Pair<VertexData, Paint>> selectedPair = new ArrayList<>();

                Log.i("", "" + mOriginalVertexData);

                for (VertexData data : mModifiedVertexData) {

                    float checkingRectL = data.getX();
                    float checkingRectT = data.getY();
                    float checkingRectR = data.getWidth() + data.getX();
                    float checkingRectB = data.getHeight() + data.getY();

                    if (mRectF != null) {
                        checkingRectL = (mRectF.left + checkingRectL);
                        checkingRectT = (mRectF.top + checkingRectT);
                        checkingRectR = (mRectF.left + checkingRectR);
                        checkingRectB = (mRectF.top + checkingRectB);
                    }

                    RectF checkingXYRect = new RectF(checkingRectL, checkingRectT, checkingRectR, checkingRectB);

                    if (checkingXYRect.contains((int) x, (int) y)) {
                        /*Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
                        mPaint.setStrokeWidth(5);
                        mPaint.setColor(Color.RED);
                        data.setRect(rect);
                        selectedPair.add(Pair.create(data, mPaint));*/

                        objectId = data.getArticleID();
                        break;
                    }

                }

                for (VertexData data2 : mModifiedVertexData) {
                    if(data2.getArticleID().equals(objectId)) {

                        float modifiedRectL = data2.getX();
                        float modifiedRectT = data2.getY();
                        float modifiedRectR = data2.getWidth() + data2.getX();
                        float modifiedRectB = data2.getHeight() + data2.getY();

                        if (mRectF != null) {
                            modifiedRectL = (mRectF.left + modifiedRectL);
                            modifiedRectT = (mRectF.top + modifiedRectT);
                            modifiedRectR = (mRectF.left + modifiedRectR);
                            modifiedRectB = (mRectF.top + modifiedRectB);
                        }

                        RectF modifiedXYRect = new RectF(modifiedRectL, modifiedRectT, modifiedRectR, modifiedRectB);

                        Paint mPaint = new Paint();
                        // mPaint.setStyle(Paint.Style.STROKE);
//                        mPaint.setStrokeWidth(5);
                        mPaint.setColor(Color.RED);
                        data2.setRect(modifiedXYRect);
                        selectedPair.add(Pair.create(data2, mPaint));
                    }
                }

                addBoundView(selectedPair);

            }
        });


        // PhotoView Long Click Listener
        mPhotoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(MainActivity.this, "Long", Toast.LENGTH_SHORT).show();
                mPhotoView.setScale(mScaleValue);
                mCropImageView.setShowCropOverlay(!mCropImageView.isShowCropOverlay());
                return false;
            }
        });


    }


    private void addBoundView(ArrayList<Pair<VertexData, Paint>> selectedPair) {
        // Removes Bound view
        removeBoundView();

        int w = (int) (mPhotoView.getWidth() * mPhotoView.getScale());
        int h = (int) (mPhotoView.getHeight() * mPhotoView.getScale());

        float scaleX = mPhotoView.getScaleX();
        float scaleY = mPhotoView.getScaleY();

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(w, h);
        CustomLayout layout = new CustomLayout(this);
        layout.setX(scaleX);
        layout.setY(scaleY);
        layout.setLayoutParams(params);

        mFrameLayout.addView(layout);
        layout.setSelectedPair(selectedPair);


        // Below code, starts Detail Activity
        mHandler.removeMessages(100);

        Bundle bundle = new Bundle();
        ArrayList<VertexData> vertexData = new ArrayList<>();
        for(Pair<VertexData, Paint> data : selectedPair) {
            vertexData.add(data.first);
        }
        bundle.putParcelableArrayList("vertexData", vertexData);

        Message msg = new Message();
        msg.what = 100;
        msg.setData(bundle);

        mHandler.sendMessageDelayed(msg, 300);


    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 100) {
                Bundle bundle = msg.getData();
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("vertexData", bundle.getParcelableArrayList("vertexData"));
                startActivity(intent);
                removeBoundView();
            }


        }
    };


    private void removeBoundView() {
        int childCount = mFrameLayout.getChildCount();
        if (childCount > 1) {
            mFrameLayout.removeViewAt(1);
        }
    }

    @Override
    public void onDestroy() {
        if (mCropImageView != null) {
            mCropImageView.setOnSetImageUriCompleteListener(null);
            mCropImageView.setOnCropImageCompleteListener(null);
        }
        super.onDestroy();
    }

    @Override
    public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
        if (error == null) {
            Toast.makeText(this, "Image load successful", Toast.LENGTH_SHORT).show();
        } else {
            Log.e("AIC", "Failed to load image by URI", error);
            Toast.makeText(this, "Image load failed: " + error.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
        handleCropResult(result);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            handleCropResult(result);
        }
    }

    @Override
    public void onBackPressed() {
        if(mCropImageView.isShowCropOverlay()) {
            mCropImageView.setShowCropOverlay(false);
        }
        else {
            super.onBackPressed();
        }
    }

    private void handleCropResult(CropImageView.CropResult result) {
        if (result.getError() == null) {
            Intent intent = new Intent(this, CropResultActivity.class);
            intent.putExtra("SAMPLE_SIZE", result.getSampleSize());
            if (result.getUri() != null) {
                intent.putExtra("URI", result.getUri());
            } else {
                CropResultActivity.mImage =
                        mCropImageView.getCropShape() == CropImageView.CropShape.OVAL
                                ? CropImage.toOvalBitmap(result.getBitmap())
                                : result.getBitmap();
            }
            startActivity(intent);
        } else {
            Log.e("AIC", "Failed to crop image", result.getError());
            Toast.makeText(
                    this,
                    "Image crop failed: " + result.getError().getMessage(),
                    Toast.LENGTH_LONG)
                    .show();
        }
    }

    @Override
    public void onNext(List<VertexData> vertexData) {
        mOriginalVertexData = vertexData;

        mModifiedVertexData = new ArrayList<>(vertexData);
    }

    @Override
    public void onError(Throwable t, String str) {

    }

    @Override
    public void onComplete(String str) {

    }


    private class MatrixChangeListener implements OnMatrixChangedListener {

        @Override
        public void onMatrixChanged(RectF rect) {
            // Removes Bound view
            removeBoundView();

            float scale = mPhotoView.getScale();
            if (mOriginalVertexData != null) {
                mRectF = rect;
                mModifiedVertexData = new ArrayList<>();
                for (VertexData data : mOriginalVertexData) {
                    int x = (int) (((data.getX() * scale)));
                    int y = (int) (((data.getY() * scale)));
                    int w = (int) (data.getWidth() * scale);
                    int h = (int) (data.getHeight() * scale);

                    VertexData newData = new VertexData();
                    newData.setX(x);
                    newData.setY(y);
                    newData.setWidth(w);
                    newData.setHeight(h);
                    newData.setArticleID(data.getArticleID());
                    newData.setReference(data.getReference());

                    mModifiedVertexData.add(newData);

                }
            }

            Log.i("Ashwani", "PhotoView Scale :: "+scale);


            ///////////////////////////////////////////////////////


        }
    }
}
