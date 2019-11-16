package cn.com.sample.intelligent.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.appcompat.widget.AppCompatRadioButton;

import cn.com.sample.intelligent.R;

/**
 * Description ：带提醒RadioButton
 * creator ：但子龙
 * Create time : 2018/2/8
 */

public class AlarmRadioButton extends AppCompatRadioButton {

    public static final int SIGN_TYPE_POINT = 1;
    public static final int SIGN_TYPE_NUMBER = 2;
    public static final int SIGN_LOCATION_LEFT_TOP = 1;
    public static final int SIGN_LOCATION_RIGHT_TOP = 2;
    public static final int SIGN_LOCATION_LEFT_BOTTOM = 3;
    public static final int SIGN_LOCATION_RIGHT_BOTTOM = 4;

    private boolean isShowSign = true;
    private int signLocation;
    private int signType;
    private float signWidth;
    private float signHeight;
    private float signMargin;
    private float signMarginRight;
    private float signMarginLeft;
    private float signMarginTop;
    private float signMarginBottom;
    @ColorInt
    private int signColor;
    @ColorInt
    private int signTextColor;
    private float signTextSize;
    private String signText;

    private RectF rectF;  // 画圆所在的距形区域
    private Paint paint;

    public AlarmRadioButton(Context context) {
        super(context);
    }

    public AlarmRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public AlarmRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AlarmRadioButton);
        signLocation = typedArray.getInt(R.styleable.AlarmRadioButton_signLocation, 1);
        signType = typedArray.getInt(R.styleable.AlarmRadioButton_signType, 1);
        signWidth = typedArray.getDimension(R.styleable.AlarmRadioButton_signWidth, 0);
        signHeight = typedArray.getDimension(R.styleable.AlarmRadioButton_signHeight, 0);
        signMargin = typedArray.getDimension(R.styleable.AlarmRadioButton_signMargin, 0);
        signMarginRight = typedArray.getDimension(R.styleable.AlarmRadioButton_signMarginRight, -1);
        signMarginLeft = typedArray.getDimension(R.styleable.AlarmRadioButton_signMarginLeft, -1);
        signMarginTop = typedArray.getDimension(R.styleable.AlarmRadioButton_signMarginTop, -1);
        signMarginBottom = typedArray.getDimension(R.styleable.AlarmRadioButton_signMarginBottom, -1);
        signColor = typedArray.getColor(R.styleable.AlarmRadioButton_signColor, Color.RED);
        signTextColor = typedArray.getColor(R.styleable.AlarmRadioButton_signTextColor, Color.WHITE);
        signTextSize = typedArray.getDimension(R.styleable.AlarmRadioButton_signTextSize, 0);
        signText = typedArray.getString(R.styleable.AlarmRadioButton_signText);
        signText = (signText == null ? "" : signText);
        typedArray.recycle();

        this.paint = new Paint();
        this.rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
//        setMeasuredDimension(widthSize, heightSize);
        float left = 0;
        float top = 0;
        float right = 0;
        float bottom = 0;

        if (signTextSize <= 0) {
            signTextSize = signWidth / 2;
        }
        if (signHeight <= 0) {
            signHeight = signWidth;
        }
        if (signLocation == SIGN_LOCATION_LEFT_TOP) {
            left = signMarginLeft == -1 ? signMargin : signMarginLeft;
            right = left + signWidth;
            top = (signMarginTop == -1 ? signMargin : signMarginTop);
            bottom = top + signHeight;
        } else if (signLocation == SIGN_LOCATION_RIGHT_TOP) {
            right = widthSize - (signMarginRight == -1 ? signMargin : signMarginRight);
            left = right - signWidth;
            top = (signMarginTop == -1 ? signMargin : signMarginTop);
            bottom = top + signHeight;
        } else if (signLocation == SIGN_LOCATION_LEFT_BOTTOM) {
            left = signMarginLeft == -1 ? signMargin : signMarginLeft;
            right = left + signWidth;
            bottom = heightSize - (signMarginBottom == -1 ? signMargin : signMarginBottom);
            top = bottom - signHeight;
        } else if (signLocation == SIGN_LOCATION_RIGHT_BOTTOM) {
            right = widthSize - (signMarginRight == -1 ? signMargin : signMarginRight);
            left = right - signWidth;
            bottom = heightSize - (signMarginBottom == -1 ? signMargin : signMarginBottom);
            top = bottom - signHeight;
        } else {
            left = widthSize - (signMarginRight == -1 ? signMargin : signMarginRight) - signWidth;
            top = (signMarginTop == -1 ? signMargin : signMarginTop);
            right = widthSize - (signMarginRight == -1 ? signMargin : signMarginRight);
            bottom = top + signHeight + (signMarginBottom == -1 ? signMargin : signMarginBottom);
        }

        rectF.set(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setAntiAlias(true);
        paint.setColor(signColor);
        paint.setStyle(Paint.Style.FILL);
        if (isShowSign) {
            if (signType == SIGN_TYPE_POINT) {
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.width(), rectF.height()) / 2, paint);
            } else if (signType == SIGN_TYPE_NUMBER) {
                canvas.drawCircle(rectF.centerX(), rectF.centerY(), Math.min(rectF.width(), rectF.height()) / 2, paint);

                paint.setColor(signTextColor);
                paint.setTextSize(signTextSize);

                Rect bounds = new Rect();
                paint.getTextBounds(signText, 0, signText.length(), bounds);
                Paint.FontMetricsInt fontMetrics = paint.getFontMetricsInt();
                float x = rectF.left + (rectF.width() - bounds.width()) / 2;
                float y = rectF.top + (rectF.height() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
                canvas.drawText(signText, x, y, paint);
            }
        }
    }

    public void showSign() {
        this.isShowSign = true;
        invalidate();
    }

    public void dismissSign() {
        this.isShowSign = false;
        invalidate();
    }

    public String getSignText() {
        return signText;
    }

    public void setSignText(String signText) {
        this.signText = signText;
        invalidate();
    }
}

