package com.box.f2j_custom_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView


/**
 * not completed
 * but can be used
 *
 * have to set width
 * */
class VerticalSeekbar(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private fun setInit(attrs: AttributeSet) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.VerticalSeekbar, 0, 0).apply {
            try {
//                thumbDrawableRes = getResourceId(R.styleable.VerticalSeekbar_thumb_src, thumbDrawableRes)
                bgColor = getColor(R.styleable.VerticalSeekbar_bg_color, Color.LTGRAY)
                thumbBgColor = getColor(R.styleable.VerticalSeekbar_thumb_bg_color, Color.WHITE)
                thumbLineColor = getColor(R.styleable.VerticalSeekbar_thumb_line_color, Color.GRAY)

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
    private var thumbLineColor = 0


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
            style = Paint.Style.STROKE
            strokeWidth = mWidth.toFloat()
        }
        canvas?.drawRect(0f, 0f, mWidth.toFloat(), mHegith.toFloat(), mPaint)
    }

    private fun drawThumb(canvas: Canvas?, pos: Int = 50) {
        mPaint.apply {
            color = thumbBgColor
            style = Paint.Style.FILL
        }
        //rect height = 100
        canvas?.drawRect(0f, (pos - 50).toFloat(), mWidth.toFloat(), (pos + 50).toFloat(), mPaint)
        mPaint.apply {
            color = thumbLineColor
            style = Paint.Style.STROKE
            strokeWidth = 10f
        }

        canvas?.drawLine(0f, (pos - 20).toFloat(), mWidth.toFloat(), (pos - 20).toFloat(), mPaint)
        canvas?.drawLine(0f, (pos + 20).toFloat(), mWidth.toFloat(), (pos + 20).toFloat(), mPaint)
//        val bitmap = thumbDrawable?.toBitmap(100, 120 ,null)
//        Log.d("VerticalSeekbar", ">>  drawThumb:  ####  ${bitmap?.height}  ####")
//        bitmap?.let {
//            canvas?.drawBitmap(it,10f,pos.toFloat() ,mPaint)
//        }
    }

    /** -2 for adjustment
     * */
    val percentPosition: Int
        get() = (relativPos / (mHegith - 100) * 100 ).toInt()

    fun moveTo(percent:Float){
        relativPos = (percent / 100  * (mHegith - 100) )+50
        invalidate()
    }
    fun bindRecyclerView(recyclerview:RecyclerView){
        recyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!isToucingNow){
                    val offset = recyclerView.computeVerticalScrollOffset()
                    val extent = recyclerView.computeVerticalScrollExtent()
                    val range = recyclerView.computeVerticalScrollRange()
                    val percent = (offset * 100)/(range - extent)
                    this@VerticalSeekbar.moveTo(percent.toFloat())
                    Log.d("VerticalSeekbar", ">>  onScrolled:  ####  percent : ${(offset * 100)/(range - extent)}  ####")
                    Log.d("VerticalSeekbar", ">>  onScrolled:  ####  offset * -1 = ${offset}  ####")
                    Log.d("VerticalSeekbar", ">>  onScrolled:  ####  extent $extent  ####")
                    Log.d("VerticalSeekbar", ">>  onScrolled:  ####  range:$range  ####")
                }
            }
        })
    }
    private var isToucingNow = false
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                isToucingNow = true
                relativPos = when {
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
                Log.d("VerticalSeekbar", ">>  onTouchEvent:  ####  $percentPosition  ####")
                invalidate()
                true
            }
            MotionEvent.ACTION_UP->{
                isToucingNow = false
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