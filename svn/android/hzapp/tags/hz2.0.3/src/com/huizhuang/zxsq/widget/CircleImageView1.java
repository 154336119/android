//package com.huizhuang.zxsq.widget;
//
//import android.content.Context;
//import android.content.res.TypedArray;
//import android.graphics.Bitmap;
//import android.graphics.BitmapShader;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.graphics.Shader;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.ColorDrawable;
//import android.graphics.drawable.Drawable;
//import android.util.AttributeSet;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.huizhuang.hz.R;
//
//public class CircleImageView1 extends RelativeLayout {
//
//    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
//    private static final int COLORDRAWABLE_DIMENSION = 1;
//
//    private static final int DEFAULT_BORDER_WIDTH = 0;
//    private static final int DEFAULT_BORDER_COLOR = Color.BLACK;
//
//    private final RectF mDrawableRect = new RectF();
//    private final RectF mBorderRect = new RectF();
//
//    private final Matrix mShaderMatrix = new Matrix();
//    private final Paint mBitmapPaint = new Paint();
//    private final Paint mBorderPaint = new Paint();
//
//    private int mBorderColor = DEFAULT_BORDER_COLOR;
//    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
//
//    private Bitmap mBitmap;
//    private BitmapShader mBitmapShader;
//    private int mBitmapWidth;
//    private int mBitmapHeight;
//
//    private float mDrawableRadius;
//    private float mBorderRadius;
//
//    private boolean mReady;
//    private boolean mSetupPending;
//    private Context mContext;
//    private ImageView mIvImg;
//    private TextView mTvText;
//
//    public CircleImageView1(Context context) {
//        super(context);
//        mContext = context;
//        initViews();
//    }
//
//    public CircleImageView1(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//        mContext = context;
//        initViews();
//    }
//
//    public CircleImageView1(Context context, AttributeSet attrs, int defStyle) {
//        super(context, attrs, defStyle);
//        mContext = context;
//        initViews();
//        
//        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
//
//        mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
//        mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
//
//        a.recycle();
//
//        mReady = true;
//
//        if (mSetupPending) {
//            setup();
//            mSetupPending = false;
//        }
//        
//    }
//
//    private void initViews() {
//        View rootLayout = LayoutInflater.from(mContext).inflate(R.layout.widget_circular_text, this);
//        mIvImg = (ImageView) rootLayout.findViewById(R.id.iv_img);
//        mTvText = (TextView) rootLayout.findViewById(R.id.tv_text);
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        if (mIvImg.getDrawable() == null) {
//            return;
//        }
//        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
//        if(mBorderWidth != 0){
//          canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
//        }
//    }
//
//    @Override
//    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
//        super.onSizeChanged(w, h, oldw, oldh);
//        setup();
//    }
//
//    public int getBorderColor() {
//        return mBorderColor;
//    }
//
//    public void setBorderColor(int borderColor) {
//        if (borderColor == mBorderColor) {
//            return;
//        }
//
//        mBorderColor = borderColor;
//        mBorderPaint.setColor(mBorderColor);
//        invalidate();
//    }
//
//    public int getBorderWidth() {
//        return mBorderWidth;
//    }
//
//    public void setBorderWidth(int borderWidth) {
//        if (borderWidth == mBorderWidth) {
//            return;
//        }
//
//        mBorderWidth = borderWidth;
//        setup();
//    }
//
//    public void setImageBitmap(Bitmap bm) {
//        mIvImg.setImageBitmap(bm);
//        mBitmap = bm;
//        setup();
//    }
//
//    public void setImageDrawable(Drawable drawable) {
//        mIvImg.setImageDrawable(drawable);
//        mBitmap = getBitmapFromDrawable(drawable);
//        setup();
//    }
//
//    public void setImageResource(int resId) {
//        mIvImg.setImageResource(resId);
//        mBitmap = getBitmapFromDrawable(mIvImg.getDrawable());
//        setup();
//    }
//    
//    public void setText(String text){
//        mTvText.setText(text);
//    }
//    
//    private Bitmap getBitmapFromDrawable(Drawable drawable) {
//        if (drawable == null) {
//            return null;
//        }
//
//        if (drawable instanceof BitmapDrawable) {
//            return ((BitmapDrawable) drawable).getBitmap();
//        }
//
//        try {
//            Bitmap bitmap;
//
//            if (drawable instanceof ColorDrawable) {
//                bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION, COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
//            } else {
//                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
//            }
//
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
//            drawable.draw(canvas);
//            return bitmap;
//        } catch (OutOfMemoryError e) {
//            return null;
//        }
//    }
//
//    private void setup() {
//        if (!mReady) {
//            mSetupPending = true;
//            return;
//        }
//
//        if (mBitmap == null) {
//            return;
//        }
//
//        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//        mBitmapPaint.setAntiAlias(true);
//        mBitmapPaint.setShader(mBitmapShader);
//
//        mBorderPaint.setStyle(Paint.Style.STROKE);
//        mBorderPaint.setAntiAlias(true);
//        mBorderPaint.setColor(mBorderColor);
//        mBorderPaint.setStrokeWidth(mBorderWidth);
//
//        mBitmapHeight = mBitmap.getHeight();
//        mBitmapWidth = mBitmap.getWidth();
//
//        mBorderRect.set(0, 0, getWidth(), getHeight());
//        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
//
//        mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width() - mBorderWidth, mBorderRect.height() - mBorderWidth);
//        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
//
//        updateShaderMatrix();
//        invalidate();
//    }
//
//    private void updateShaderMatrix() {
//        float scale;
//        float dx = 0;
//        float dy = 0;
//
//        mShaderMatrix.set(null);
//
//        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
//            scale = mDrawableRect.height() / mBitmapHeight;
//            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
//        } else {
//            scale = mDrawableRect.width() / mBitmapWidth;
//            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
//        }
//
//        mShaderMatrix.setScale(scale, scale);
//        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth, (int) (dy + 0.5f) + mBorderWidth);
//
//        mBitmapShader.setLocalMatrix(mShaderMatrix);
//    }
//
//}