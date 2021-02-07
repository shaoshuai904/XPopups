package com.maple.popups.lib

import android.annotation.TargetApi
import android.graphics.*
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import kotlin.math.max
import kotlin.math.min

/**
 * @author : shaoshuai
 * @date ：2020/11/30
 */
object MyViewUtils {

    /**
     * 设置View的弧度角和阴影
     *
     * @param owner           目标View
     * @param radius          圆角弧度
     * @param shadowElevation 阴影高度
     * @param shadowAlpha     阴影透明度
     * @param shadowColor     阴影颜色
     */
    @JvmStatic
    fun setRadiusAndShadow(
            owner: View,
            radius: Float,
            shadowElevation: Float = 0f,
            shadowAlpha: Float = 0.95f,
            shadowColor: Int = Color.BLACK
    ) {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= 28) {
                owner.outlineAmbientShadowColor = shadowColor
                owner.outlineSpotShadowColor = shadowColor
            }
            owner.elevation = shadowElevation
            owner.outlineProvider = object : ViewOutlineProvider() {
                @TargetApi(21)
                override fun getOutline(view: View, outline: Outline) {
                    if (view.width == 0 || view.height == 0) {
                        return
                    }
                    // outline.setAlpha will work even if shadowElevation == 0
                    outline.alpha = if (shadowElevation == 0f) 1f else shadowAlpha
                    outline.setRoundRect(0, 0, view.width, max(1, view.height), getRealRadius(owner, radius))
                }
            }
            owner.clipToOutline = radius > 0
        }
        owner.invalidate()
    }

    /**
     * 画圆形边框
     *
     * @param owner         目标View
     * @param canvas        画布
     * @param mRadius       边框弧度
     * @param mBorderWidth  边框宽度
     * @param mBorderColor  边框颜色
     */
    @JvmStatic
    fun dispatchRoundBorderDraw(owner: View?, canvas: Canvas, mRadius: Float, mBorderWidth: Float, mBorderColor: Int) {
        val needDrawBorder = mBorderWidth > 0 && mBorderColor != 0
        if (owner == null || !needDrawBorder) {
            return
        }
        val width = canvas.width
        val height = canvas.height
        canvas.save()
        canvas.translate(owner.scrollX.toFloat(), owner.scrollY.toFloat())
        val mBorderRect = RectF(mBorderWidth, mBorderWidth, width - mBorderWidth, height - mBorderWidth) // 边框范围
        val radius = getRealRadius(owner, mRadius)

        // 边框线 外部区域 填充色
        val layerId = canvas.saveLayer(0f, 0f, width.toFloat(), height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvas.drawColor(mBorderColor)
        val mClipPaint = Paint()
        mClipPaint.isAntiAlias = true
        mClipPaint.color = mBorderColor
        mClipPaint.style = Paint.Style.FILL
        // 预留边框范围，抠出内容区
        mClipPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawRoundRect(mBorderRect, radius, radius, mClipPaint)
        mClipPaint.xfermode = null
        canvas.restoreToCount(layerId)
        canvas.restore()
    }

    // 获取规范化的圆角弧度
    private fun getRealRadius(owner: View?, mRadius: Float): Float {
        var radius = mRadius
        if (owner != null) {
            val min = min(owner.width, owner.height)
            radius = min(radius, min.toFloat() / 2)
        }
        radius = max(radius, 0f)
        return radius
    }

    // 画 ∨/∧ 箭头
    @JvmStatic
    fun drawArrow(
            owner: View,
            canvas: Canvas,
            direction: MsNormalPopup.Direction,// 显示方向
            mShowInfo: ShowInfo,
            mContextBgColor: Int,// 内容背景色
            mBorderWidth: Float,// 边框粗细
            mBorderColor: Int,// 边框颜色
            mArrowWidth: Int,// 箭头宽
            mArrowHeight: Int// 箭头高
    ) {
        val mArrowPaint = Paint()
        mArrowPaint.isAntiAlias = true
        canvas.save()
        when (direction) {
            MsNormalPopup.Direction.TOP -> {
                // 画 ∨ 箭头
                // 移动到 指定位置（左上角）
                val moveX: Float = mShowInfo.anchorCenterX - mShowInfo.x - mArrowWidth / 2f
                // moveX = min(max(moveX, mShowInfo.decorationLeft.toFloat()), owner.width.toFloat() - mShowInfo.decorationRight - mArrowWidth)
                val moveY: Float = mShowInfo.decorationTop + mShowInfo.height - mBorderWidth
                canvas.translate(moveX, moveY)
                // 设置保留区
                val mArrowSaveRect = RectF(0f, 0f, mArrowWidth.toFloat(), (mArrowHeight + mBorderWidth))
                val saveLayer = canvas.saveLayer(mArrowSaveRect, mArrowPaint, Canvas.ALL_SAVE_FLAG)
                // 三角形
                val mArrowPath = Path()
                // mArrowPath.reset();
                mArrowPath.setLastPoint(0f, -mBorderWidth)
                mArrowPath.lineTo(mArrowWidth / 2f, mArrowHeight.toFloat())
                mArrowPath.lineTo((mArrowWidth.toFloat()), -mBorderWidth)
                mArrowPath.close()
                mArrowPaint.style = Paint.Style.FILL
                mArrowPaint.color = mContextBgColor
                mArrowPaint.xfermode = null
                canvas.drawPath(mArrowPath, mArrowPaint)
                // 小三角 顶角边
                if (mBorderWidth > 0 && mBorderColor != 0) {
                    mArrowPaint.strokeWidth = mBorderWidth
                    mArrowPaint.color = mBorderColor
                    mArrowPaint.style = Paint.Style.STROKE
                    canvas.drawPath(mArrowPath, mArrowPaint)
                }
                canvas.restoreToCount(saveLayer)
            }
            MsNormalPopup.Direction.BOTTOM -> {
                // 画 ∧ 箭头
                // 移动到 指定位置（左上角）
                val moveX: Float = mShowInfo.anchorCenterX - mShowInfo.x - mArrowWidth / 2f
                // moveX = min(max(moveX, mShowInfo.decorationLeft), owner.width - mShowInfo.decorationRight - mArrowWidth)
                val moveY: Float = mShowInfo.decorationTop - mArrowHeight.toFloat()//  - mBorderWidth
                canvas.translate(moveX, moveY)
                // 设置保留区
                val mArrowSaveRect = RectF(0f, 0f, mArrowWidth.toFloat(), mArrowHeight + mBorderWidth)
                val saveLayer = canvas.saveLayer(mArrowSaveRect, mArrowPaint, Canvas.ALL_SAVE_FLAG)
                // 三角形
                val mArrowPath = Path()
                // mArrowPath.reset();
                mArrowPath.setLastPoint(0f, mArrowHeight + mBorderWidth * 2)
                mArrowPath.lineTo(mArrowWidth / 2f, mBorderWidth)
                mArrowPath.lineTo((mArrowWidth.toFloat()), mArrowHeight + mBorderWidth * 2)
                mArrowPath.close()
                mArrowPaint.style = Paint.Style.FILL
                mArrowPaint.color = mContextBgColor
                mArrowPaint.xfermode = null
                canvas.drawPath(mArrowPath, mArrowPaint)
                // 小三角 顶角边
                if (mBorderWidth > 0 && mBorderColor != 0) {
                    mArrowPaint.strokeWidth = mBorderWidth
                    mArrowPaint.style = Paint.Style.STROKE
                    mArrowPaint.color = mBorderColor
                    canvas.drawPath(mArrowPath, mArrowPaint)
                }
                canvas.restoreToCount(saveLayer)
            }
            MsNormalPopup.Direction.LEFT -> {
                // 画 > 箭头
                // 移动到 指定位置（左上角）
                val moveX: Int = mShowInfo.decorationLeft + mShowInfo.width - mBorderWidth.toInt()
                // moveX = min(max(moveX, mShowInfo.decorationLeft), owner.width - mShowInfo.decorationRight - mArrowWidth)
                val moveY: Float = mShowInfo.anchorCenterY - mShowInfo.y - mArrowHeight / 2f
                canvas.translate(moveX.toFloat(), moveY)
                // 设置保留区
                val mArrowSaveRect = RectF(0f, 0f, (mArrowWidth + mBorderWidth), mArrowHeight.toFloat())
                val saveLayer = canvas.saveLayer(mArrowSaveRect, mArrowPaint, Canvas.ALL_SAVE_FLAG)
                // 三角形
                val mArrowPath = Path()
                mArrowPath.setLastPoint(-mBorderWidth, 0f)
                mArrowPath.lineTo(mArrowWidth.toFloat(), mArrowHeight / 2f)
                mArrowPath.lineTo(-mBorderWidth, mArrowHeight.toFloat())
                mArrowPath.close()
                mArrowPaint.style = Paint.Style.FILL
                mArrowPaint.color = mContextBgColor
                mArrowPaint.xfermode = null
                canvas.drawPath(mArrowPath, mArrowPaint)
                // 小三角 顶角边
                if (mBorderWidth > 0 && mBorderColor != 0) {
                    mArrowPaint.strokeWidth = mBorderWidth
                    mArrowPaint.color = mBorderColor
                    mArrowPaint.style = Paint.Style.STROKE
                    canvas.drawPath(mArrowPath, mArrowPaint)
                }
                canvas.restoreToCount(saveLayer)
            }
            MsNormalPopup.Direction.RIGHT -> {
                // 画 < 箭头
                // 移动到 指定位置（左上角）
                val moveX: Float = 0f
                // moveX = min(max(moveX, mShowInfo.decorationLeft), owner.width - mShowInfo.decorationRight - mArrowWidth)
                val moveY: Float = mShowInfo.anchorCenterY - mShowInfo.y - mArrowHeight / 2f
                canvas.translate(moveX, moveY)
                // 设置保留区
                val mArrowSaveRect = RectF(0f, 0f, (mArrowWidth + mBorderWidth), mArrowHeight.toFloat())
                val saveLayer = canvas.saveLayer(mArrowSaveRect, mArrowPaint, Canvas.ALL_SAVE_FLAG)
                // 三角形
                val mArrowPath = Path()
                mArrowPath.setLastPoint(mArrowWidth + mBorderWidth * 2, 0f)
                mArrowPath.lineTo(mBorderWidth, mArrowHeight / 2f)
                mArrowPath.lineTo(mArrowWidth + mBorderWidth * 2, mArrowHeight.toFloat())
                mArrowPath.close()
                mArrowPaint.style = Paint.Style.FILL
                mArrowPaint.color = mContextBgColor
                mArrowPaint.xfermode = null
                canvas.drawPath(mArrowPath, mArrowPaint)
                // 小三角 顶角边
                if (mBorderWidth > 0 && mBorderColor != 0) {
                    mArrowPaint.strokeWidth = mBorderWidth
                    mArrowPaint.color = mBorderColor
                    mArrowPaint.style = Paint.Style.STROKE
                    canvas.drawPath(mArrowPath, mArrowPaint)
                }
                canvas.restoreToCount(saveLayer)
            }
            else -> {
                // 居中不画箭头
            }
        }
        canvas.restore()
    }
}