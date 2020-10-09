package com.maxdexter.mynote.utils

import android.graphics.Matrix
import android.graphics.PointF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import kotlin.math.sqrt
const val TAG = "touch"
//три возможные состояния матрици
const val NONE = 0
const val DRAG = 1
const val ZOOM = 2
//Эти матрици будут использоваться для перемещения и масштабирования изображегия
var matrix: Matrix = Matrix()
var savedMatrix: Matrix = Matrix()
var mode = NONE
//Данные для масштабирования
val start: PointF = PointF()
val mid: PointF = PointF()
var oldDist = 1F

private fun zoom(view: View) {

    view.setOnTouchListener(object : View.OnTouchListener {
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {
            var imageView = v as ImageView
            imageView.scaleType = ImageView.ScaleType.MATRIX
            var scale: Float

            //Обработка событий
            when (event?.action) {
                MotionEvent.ACTION_DOWN -> {
                    savedMatrix.set(matrix)
                    start.set(event.x, event.y)
                    Log.d(TAG, "mode DRAG")
                    mode = DRAG
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                    mode = NONE
                    Log.d(TAG, "mode None")
                }
                MotionEvent.ACTION_POINTER_DOWN -> {
                    oldDist = spacing(event)
                    if (oldDist > 5f) {
                        savedMatrix.set(matrix)
                        midPoint(mid, event)
                        mode = ZOOM
                        Log.d(TAG, "mode = ZOOM")
                    }
                }
                MotionEvent.ACTION_MOVE -> {
                    if (mode == DRAG) {
                        matrix.set(savedMatrix)
                        view.let {
                            if (it.left >= -392) {
                                matrix.postTranslate(event.x - start.x, event.y - start.y)
                            }
                        }

                    } else if (mode == ZOOM) {
                        var newDist = spacing(event)
                        Log.d(TAG, "newDist $newDist")
                        if (newDist > 5f) {
                            matrix.set(savedMatrix)
                            scale = newDist / oldDist
                            matrix.postScale(scale, scale, mid.x, mid.y)
                        }
                    }
                }


            }
            imageView.imageMatrix = matrix
            return true
        }
    })
}
private fun midPoint(mid: PointF, event: MotionEvent) {
    val x = event.getX(0) + event.getX(1)
    val y = event.getY(0) + event.getY(1)
    mid.set(x / 2, y / 2)

}

private fun spacing(event: MotionEvent): Float {
    val x = event.getX(0) - event.getX(1)
    val y = event.getY(0) - event.getY(1)
    val fl = sqrt(x * x + y * y)
    return fl
}