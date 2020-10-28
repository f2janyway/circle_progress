package com.box.f2j_custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat


/**
 * not completed
 * but can be used
 *
 * have to set width
 * */
class VerticalSeekbar(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.isAntiAlias = true
        setInit(attrs)
    }

    private var thumbDrawableRes: Int = R.drawable.ic_unfold_more_24px
    private var thumbDrawable = ResourcesCompat.getDrawable(resources, thumbDrawableRes, null)
    private var relativPos: Float = 0f
    private var isInit = true

    private var mWidth = 0
    private var mHegith = 0
    private var bgColor = Color.GRAY
    private var thumbBgColor = Color.WHITE
    private var thumbLineColor = Color.GRAY
    private fun setInit(attrs: AttributeSet) {

        context.theme.obtainStyledAttributes(attrs, R.styleable.VerticalSeekbar, 0, 0).apply {
            try {
                thumbDrawableRes = getResourceId(R.styleable.VerticalSeekbar_thumb_src, thumbDrawableRes)
                bgColor = getColor(R.styleable.VerticalSeekbar_bg_color, bgColor)
                thumbBgColor = getColor(R.styleable.VerticalSeekbar_thumb_bg_color, thumbBgColor)
                thumbLineColor = getColor(R.styleable.VerticalSeekbar_thumb_line_color, thumbLineColor)
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBar(canvas)
        if (isInit) {
            drawThumb(canvas)
            isInit = false
        } else {
            //touch
            drawThumb(canvas, relativPos.toInt())
        }
    }

    private fun drawBar(canvas: Canvas?) {
        mPaint.apply {
            color = bgColor
            style = Paint.Style.FILL
            strokeWidth = mWidth.toFloat()
        }
        canvas?.drawRect(0f, 0f, mWidth.toFloat(), mHegith.toFloat(), mPaint)
    }

    private fun drawThumb(canvas: Canvas?, pos: Int = 0) {

        mPaint.apply {
            color = thumbBgColor
            style = Paint.Style.FILL
        }
        canvas?.drawRect(0f, pos.toFloat(), mWidth.toFloat(), (100 + pos).toFloat(), mPaint)
        mPaint.apply {
            color = thumbLineColor
            style = Paint.Style.FILL
            strokeWidth = 10f
        }

        canvas?.drawLine(0f, (pos + 20).toFloat(), mWidth.toFloat(), (pos + 20).toFloat(), mPaint)
        canvas?.drawLine(0f, (pos + 80).toFloat(), mWidth.toFloat(), (pos + 80).toFloat(), mPaint)
//        val bitmap = thumbDrawable?.toBitmap(100, 120 ,null)
//        Log.d("VerticalSeekbar", ">>  drawThumb:  ####  ${bitmap?.height}  ####")
//        bitmap?.let {
//            canvas?.drawBitmap(it,10f,pos.toFloat() ,mPaint)
//        }
    }

    /** is not correct max position*/
    val percentPosition: Int
        get() = (relativPos / mHegith * 100).toInt()

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                relativPos = when {
                    event.y < 0 -> {
                        0f
                    }
                    event.y > mHegith - 100 ->
                        (mHegith- 100).toFloat()
                    else -> {
                        event.y
                    }
                }
                Log.d("VerticalSeekbar", ">>  onTouchEvent:  ####  $percentPosition  ####")
                invalidate()
                true
            }
            else -> true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHegith = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("VerticalSeekbar", ">>  onMeasure:  ####  $mHegith: $mWidth  ####")
//        val min = min(mWidth,mHegith)
        setMeasuredDimension(mWidth, mHegith)
    }

}