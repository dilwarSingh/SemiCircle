package com.rentee

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Math.pow
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

open class dilwarSemi : View {
    constructor(context: Context) : super(context) {
        initStart()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initStart()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initStart()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    ) {
        initStart()
    }

    var listner: dilwarSemi.clickListner? = null
    fun setOnItemClickListener(listner: dilwarSemi.clickListner? = null) {
        this.listner = listner
    }


    val quadSet by lazy { emptyMap<Int, Quad>().toMutableMap() }
    val colorSet = emptyList<Paint>().toMutableList()


    val black by lazy {
        Paint().apply {
            strokeWidth = 5.0f
            isAntiAlias = true
            isDither = true
            color = Color.BLACK
        }
    }

    val white by lazy {
        Paint().apply {
            strokeWidth = 5.0f
            isAntiAlias = true
            isDither = true
            color = Color.WHITE
        }
    }

    private var tipAtLocation = 0

    fun selectTipAt(tipAtLocation: Int) {
        this.tipAtLocation = tipAtLocation
        invalidate()
    }

    fun initStart() {
        val red by lazy { Paint() }
        val lightOrange by lazy { Paint() }
        val orange by lazy { Paint() }
        val green by lazy { Paint() }
        val darkGreen by lazy { Paint() }

        red.strokeWidth = 5.0f
        red.isAntiAlias = true
        red.isDither = true
        red.color = Color.parseColor("#ef0041")

        lightOrange.strokeWidth = 5.0f
        lightOrange.isAntiAlias = true
        lightOrange.isDither = true
        lightOrange.color = Color.parseColor("#fda819")

        orange.strokeWidth = 5f
        orange.isAntiAlias = true
        orange.isDither = true
        orange.color = Color.parseColor("#e13535")

        green.strokeWidth = 5f
        green.isAntiAlias = true
        green.isDither = true
        green.color = Color.parseColor("#90bd1c")

        darkGreen.strokeWidth = 5f
        darkGreen.isAntiAlias = true
        darkGreen.isDither = true
        darkGreen.color = Color.parseColor("#91d626")

        colorSet.add(red)
        colorSet.add(lightOrange)
        colorSet.add(orange)
        colorSet.add(green)
        colorSet.add(darkGreen)

    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {

        val noOfAngles = colorSet.size
        val angleSize = 180.0f / noOfAngles
        var oldAngle = 0.0f
        val cutOffVal = 175.0f

        /* canvas.drawArc(0.0f, 0.0f, width.toFloat(), height.toFloat(), 0.0f, -30.0f, true, red)
         canvas.drawArc(0.0f, 0.0f, width.toFloat(), height.toFloat(), -30.0f, -30.0f, true, black) */

        val diameter = Math.min(width.toFloat(), height.toFloat())

        val radious = diameter / 2
        val smallRadous = diameter / 4
        val tipRadoius = (diameter / 4) - 50

        val rectF = RectF(0.0f, 0.0f, diameter, diameter)

        for (angleLoc in 0 until noOfAngles) {
            val color = colorSet[angleLoc]

            makeArc(
                (angleLoc * angleSize).toDouble(),
                ((angleLoc + 1) * angleSize).toDouble(),
                color,
                tipAtLocation == angleLoc,
                radious,
                smallRadous,
                tipRadoius,
                rectF,
                canvas
            )
        }

        /*
           makeArc(0.0, 36.0, red, false, radious, smallRadous, tipRadoius, rectF, canvas)
            makeArc(36.0, 72.0, black, false, radious, smallRadous, tipRadoius, rectF, canvas)
            makeArc(72.0, 108.0, green, false, radious, smallRadous, tipRadoius, rectF, canvas)
            makeArc(108.0, 144.0, orange, false, radious, smallRadous, tipRadoius, rectF, canvas)
            makeArc(144.0, 180.0, grey, true, radious, smallRadous, tipRadoius, rectF, canvas)
        */
        canvas.drawText("D=$diameter", 50f, diameter - 120, black)
        canvas.drawText("R=$radious", 50f, diameter - 80, black)
        /*   canvas.drawText("p=$P1", 50.0f, diameter - 40, black)
           canvas.drawText("b=$B1", 50.0f, diameter, black)*/
    }

    private fun makeArc(
        startAngle: Double,
        endAngle: Double,
        color: Paint,
        showTip: Boolean,
        radious: Float,
        smallRadous: Float,
        tipRadoius: Float,
        rectF: RectF,
        canvas: Canvas
    ) {

        val P0 = sin(Math.toRadians(startAngle)).toFloat() * radious
        val B0 = cos(Math.toRadians(startAngle)).toFloat() * radious

        val P1 = sin(Math.toRadians(endAngle)).toFloat() * radious
        val B1 = cos(Math.toRadians(endAngle)).toFloat() * radious

        val p1 = sin(Math.toRadians(endAngle)).toFloat() * smallRadous
        val b1 = cos(Math.toRadians(endAngle)).toFloat() * smallRadous

        val p0 = sin(Math.toRadians(startAngle)).toFloat() * smallRadous
        val b0 = cos(Math.toRadians(startAngle)).toFloat() * smallRadous


        val path = Path().apply {
            moveTo(radious + b1, radious - p1)
            for (i in endAngle.toInt() downTo startAngle.toInt()) {
                val p = sin(Math.toRadians(i.toDouble())).toFloat() * smallRadous
                val b = cos(Math.toRadians(i.toDouble())).toFloat() * smallRadous
                lineTo(radious + b, radious - p)
            }
            lineTo(radious + B0, radious - P0)
            lineTo(radious + B1, radious - P1)
            arcTo(rectF, startAngle.toFloat() * -1, ((endAngle - startAngle) * -1).toFloat())
        }


        val pt1 = sin(Math.toRadians((endAngle + startAngle) / 2)).toFloat() * tipRadoius
        val bt1 = cos(Math.toRadians((endAngle + startAngle) / 2)).toFloat() * tipRadoius

        val tipPath = Path().apply {
            moveTo(radious + bt1, radious - pt1)
            lineTo(radious + B0, radious - P0)
            lineTo(radious + B1, radious - P1)
        }



        quadSet.put(
            0, Quad(
                Point((radious + b1).toInt(), (radious - p1).toInt()),
                Point((radious + b0).toInt(), (radious - p0).toInt()),
                Point((radious + B0).toInt(), (radious - P0).toInt()),
                Point((radious + B1).toInt(), (radious - P1).toInt())
            )
        )


        canvas.drawPath(path, color)
        if (showTip)
            canvas.drawPath(tipPath, color)
    }


    /*override fun onTouchEvent(event: MotionEvent?): Boolean {
        val angleSize = 36.0f
        val diameter = Math.min(width.toFloat(), height.toFloat())

        val radious = diameter / 2
        val smallRadous = diameter / 4
        val tipRadoius = (diameter / 4) - 50

        if (event != null) {
            val screenX = event.x
            val screenY = event.y
            Log.d("codinates", "$screenX | $screenY")

            val distanceFromCenter =
                sqrt(pow((screenX - radious).toDouble(), 2.0) + pow((screenY - radious).toDouble(), 2.0))
            val prependicular = radious - screenY

            val clickAngle = prependicular / distanceFromCenter
            val toDegrees = Math.toDegrees(clickAngle)

            Log.d("codinates = ", "$toDegrees")

            return true
        }
        return false
    }*/

    interface clickListner {
        fun onClick(location: Int)
    }

    data class Quad(
        val belowA: Point,
        val belowB: Point,
        val topA: Point,
        val topB: Point
    )

}
