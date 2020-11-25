package com.box.f2j_custom_view

import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView


/**
 * not completed
 * but can be used
 *
 * have to set width
 * */
class RealVerticalSeekbar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private fun setInit(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.RealVerticalSeekbar, 0, 0).apply {
            try {
                bgColor = getColor(R.styleable.RealVerticalSeekbar_seekbar_bg_color, Color.LTGRAY)
                thumbBgColor =
                    getColor(R.styleable.RealVerticalSeekbar_seekbar_thumb_color, Color.WHITE)
                thumbLineColor = getColor(
                    R.styleable.RealVerticalSeekbar_seekbar_progress_line_color,
                    Color.GRAY
                )
                progressColor =
                    getColor(R.styleable.RealVerticalSeekbar_seekbar_progress_color, Color.GRAY)
                thumbRadius = getFloat(R.styleable.RealVerticalSeekbar_seekbar_thumb_radius,20f)

            } finally {
                recycle()
            }
        }
    }

    init {
        mPaint.isAntiAlias = true
        setInit(attrs)
    }

    private var relativPos: Float = 50f

    private var isInit = true

    private var mWidth = 0
    var mHegith = 0
    private var bgColor = 0
    private var thumbBgColor = 0
    private var thumbRadius = 0f
        set(value) {
            field = if (value < 20f) 20f else value
        }
    private var thumbLineColor = 0
    private var progressColor = 0


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawBar(canvas)
        drawProgressPath(canvas)
        if (isInit) {
            drawThumb(canvas)
            isInit = false
        } else {
            //touch
            drawThumb(canvas, relativPos.toInt())
            drawProgress(canvas, relativPos.toInt())
        }
    }

    private fun drawBar(canvas: Canvas?) {
        mPaint.apply {
            color = bgColor
            style = Paint.Style.STROKE
            strokeWidth = mWidth.toFloat()
        }
        canvas?.drawRect(0f, 0f, mWidth.toFloat(), mHegith.toFloat(), mPaint)
    }

    private fun drawProgressPath(canvas: Canvas?) {
        mPaint.apply {
            color = thumbLineColor
            style = Paint.Style.FILL
            strokeWidth = 10F
        }
        canvas?.drawLine(
            (mWidth / 2).toFloat(),
            50f,
            (mWidth / 2).toFloat(),
            (mHegith - 50).toFloat(),
            mPaint
        )
    }

    private fun drawProgress(canvas: Canvas?, pos: Int) {
        mPaint.apply {
            color = progressColor
            style = Paint.Style.FILL
            strokeWidth = 10F
        }
        canvas?.drawLine(
            (mWidth / 2).toFloat(),
            50f,
            (mWidth / 2).toFloat(),
            if (pos == 50) pos.toFloat() else (pos - thumbRadius),
            mPaint
        )
    }

    private fun drawThumb(canvas: Canvas?, pos: Int = 50) {
        mPaint.apply {
            color = thumbBgColor
            style = Paint.Style.FILL
        }

        Log.d("RealVerticalSeekbar", ">>  drawThumb:  ####  $thumbRadius  ####")
        canvas?.drawCircle((mWidth / 2).toFloat(), pos.toFloat(), thumbRadius, mPaint)
    }

    /** -2 for adjustment
     * */
    private var temp = 0f
    val percentPosition: Float
        get() {
            temp = (((relativPos - statusBarHeigth) / (mHegith - 100)) * 100)
            return if(temp < 0){
                temp = 0f
                temp
            }else{
                if(relativPos == (mHegith - 50).toFloat()){
                    temp = 100f
                    temp
                }else temp
            }
        }

    fun moveTo(percent: Float) {
        relativPos = (percent / 100 * (mHegith - 100)) + 50
        invalidate()
    }

    fun bindRecyclerView(recyclerview: RecyclerView, doThrow: Boolean) {
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!isToucingNow) {
                    val offset = recyclerView.computeVerticalScrollOffset()
                    val extent = recyclerView.computeVerticalScrollExtent()
                    val range = recyclerView.computeVerticalScrollRange()
                    if (range - extent > 0) {
                        val percent = (offset * 100) / (range - extent)
                        this@RealVerticalSeekbar.moveTo(percent.toFloat())
                    } else {
                        if (doThrow)
                            throw Throwable("change this function after view created")
                    }
                }
            }
        })
    }

    private var isToucingNow = false
    private var statusBarHeigth = 0
    lateinit var activityforWindow: Activity
    fun setActivity(activity: Activity) {
        activityforWindow = activity
        val rectgle = Rect()
        activityforWindow.window.decorView.getWindowVisibleDisplayFrame(rectgle)
        statusBarHeigth = rectgle.top
        Log.d("RealVerticalSeekbar", ">>  setActivity:  ####  $statusBarHeigth  ####")
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                isToucingNow = true
                if(statusBarHeigth == 0)
                statusBarHeigth = getStatusBarHeight()
                relativPos =
                    when {
                        event.y < 50 -> {
                            50f
                        }
                        event.y > mHegith - 50 -> {
                            (mHegith - 50).toFloat()
                        }
                        else -> {
                            event.y
                        }
                    }

                Log.d("RealVerticalSeekbar", ">>  onTouchEvent:  ####  ${getStatusBarHeight()}  ####")
                Log.d(
                    "RealVerticalSeekbar",
                    ">>  onTouchEvent:  ####  ${(relativPos) / (mHegith - 100)}  ####"
                )
                invalidate()
                true
            }
            MotionEvent.ACTION_UP -> {
                isToucingNow = false
                true
            }
            else -> true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        mHegith = getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)
//        mWidth = MeasureSpec.getSize(widthMeasureSpec)
//        mHegith = MeasureSpec.getSize(heightMeasureSpec)
        Log.d("VerticalSeekbar", ">>  onMeasure:  ####  $mHegith: $mWidth  ####")
//        val min = min(mWidth,mHegith)
        setMeasuredDimension(mWidth, mHegith)
    }

}