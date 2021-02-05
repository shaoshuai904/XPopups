package com.maple.demo.view

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewConfiguration
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.maple.demo.R
import com.maple.popups.utils.DensityUtils.dp2px
import java.lang.ref.WeakReference
import kotlin.math.max
import kotlin.math.min

/**
 * 数字 加减 步进器
 *
 * @author : shaoshuai
 * @date ：2021/2/3
 */
class NumberStepper : RelativeLayout, OnTouchListener {
    lateinit var tvText: TextView
    lateinit var ivLeft: ImageView
    lateinit var ivRight: ImageView

    var contentFinalText: String? = null // 固定文本
    var contentTextStart: String? = null // 追加文本-头
    var contentTextEnd: String? = null // 追加文本-尾部
    var minValue = 0f
    var maxValue = 100f
    var stepValue = 1f // 步长：每次增减数
    var currentValue = 0f
        set(value) {
            field = max(minValue, min(maxValue, value))
            updateContextText(value)
        }


    companion object {
        private const val STEP_SPEED_CHANGE_DURATION: Long = 1000 //按下后多少间隔触发快速改变模式
        private const val UPDATE_DURATION_SLOW: Long = 300 //数值更新频率-慢
        private const val UPDATE_DURATION_FAST: Long = 100 //数值更新频率-快
        private const val STATUS_MIMNUS = -1
        private const val STATUS_PLUS = 1
        private const val STATUS_NORMAL = 0
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initViews(context, attrs)
    }

    private fun initViews(context: Context, attrs: AttributeSet?) {
        val view = LayoutInflater.from(context).inflate(R.layout.ms_view_number_stepper, this, true)
        tvText = view.findViewById(R.id.tv_text)
        ivLeft = view.findViewById(R.id.iv_left)
        ivRight = view.findViewById(R.id.iv_right)
        var viewBackground: Drawable? = null
        var contentBackground: Drawable? = null
        var contentTextSize = 15f
        var contentTextColor = ContextCompat.getColor(context, R.color.ms_stepper_text)
        var buttonWidth = 30f.dp2px(context)
        var leftButtonBackground: Drawable? = null
        var leftButtonIcon: Drawable? = null
        var rightButtonBackground: Drawable? = null
        var rightButtonIcon: Drawable? = null
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.NumberStepper)
            maxValue = a.getFloat(R.styleable.NumberStepper_stepper_maxValue, maxValue)
            minValue = a.getFloat(R.styleable.NumberStepper_stepper_minValue, minValue)
            stepValue = a.getFloat(R.styleable.NumberStepper_stepper_stepValue, stepValue)
            currentValue = a.getFloat(R.styleable.NumberStepper_stepper_currentValue, currentValue)
            viewBackground = a.getDrawable(R.styleable.NumberStepper_stepper_background)
            contentBackground = a.getDrawable(R.styleable.NumberStepper_stepper_contentBackground)
            contentFinalText = a.getString(R.styleable.NumberStepper_stepper_contextFinalText)
            contentTextStart = a.getString(R.styleable.NumberStepper_stepper_contextTextStart)
            contentTextEnd = a.getString(R.styleable.NumberStepper_stepper_contextTextEnd)
            contentTextColor = a.getColor(R.styleable.NumberStepper_stepper_contentTextColor, contentTextColor)
            contentTextSize = a.getFloat(R.styleable.NumberStepper_stepper_contentTextSize, contentTextSize)
            buttonWidth = a.getDimensionPixelSize(R.styleable.NumberStepper_stepper_buttonWidth, buttonWidth)
            leftButtonBackground = a.getDrawable(R.styleable.NumberStepper_stepper_leftButtonBackground)
            leftButtonIcon = a.getDrawable(R.styleable.NumberStepper_stepper_leftButtonIcon)
            rightButtonBackground = a.getDrawable(R.styleable.NumberStepper_stepper_rightButtonBackground)
            rightButtonIcon = a.getDrawable(R.styleable.NumberStepper_stepper_rightButtonIcon)
            a.recycle()
        }
        if (viewBackground != null) {
            background = viewBackground
        } else {
            setBackgroundResource(R.color.ms_stepper_button_press)
        }
        with(tvText) {
            contentBackground?.let { background = it }
            setTextColor(contentTextColor)
            textSize = contentTextSize
            updateContextText(currentValue)
        }
        with(ivLeft) {
            leftButtonBackground?.let { background = it }
            leftButtonIcon?.let { setImageDrawable(it) }
            layoutParams = layoutParams.apply { width = buttonWidth }
        }
        with(ivRight) {
            rightButtonBackground?.let { background = it }
            rightButtonIcon?.let { setImageDrawable(it) }
            layoutParams = layoutParams.apply { width = buttonWidth }
        }
        //设置后onclick产生的点击状态会失效
        ivLeft.setOnTouchListener(this)
        ivRight.setOnTouchListener(this)
        setOnTouchListener(this)
    }

    private var startX = 0f//按下的初始x值
    private var startTime: Long = 0//按下时间
    private var status = STATUS_NORMAL//当前状态
    private var stepTouch = false //是否按着，判断是否还要继续更新数值和界面
    private var startStepperContentLeft = 0f
    private var hasStepperContentLeft = false

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                stepTouch = true
                postDelayed(updateRunnable, UPDATE_DURATION_SLOW)
                //非按钮则记录位置
                startX = event.x
                startTime = System.currentTimeMillis()
                if (!hasStepperContentLeft) {
                    hasStepperContentLeft = true
                    startStepperContentLeft = tvText.left.toFloat()
                }
                //如果是两边的按钮，分别设置为点击状态
                if (v === ivLeft) {
                    ivLeft.isPressed = true
                    status = STATUS_MIMNUS
                } else if (v === ivRight) {
                    ivRight.isPressed = true
                    status = STATUS_PLUS
                }
            }
            MotionEvent.ACTION_MOVE -> {
                // 点击左右按钮 和 恢复位置动画 时 按钮不移动 且 不加减数值
                if (v == ivLeft || v == ivRight || animationing) {
                    // break
                } else {
                    //非按钮则进行移动
                    val moveX = event.x - startX
                    val x = moveX + startStepperContentLeft
                    moveStepperContent(x)
                    // moveEffectStatus(moveX)
                    val scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
                    status = when {
                        moveX > scaledTouchSlop -> STATUS_PLUS
                        moveX < -scaledTouchSlop -> STATUS_MIMNUS
                        else -> STATUS_NORMAL
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                stepTouch = false
                //如果是两边的按钮，分别设置为点击状态
                when (v) {
                    ivLeft -> ivLeft.isPressed = false
                    ivRight -> ivRight.isPressed = false
                    else -> restoreStepperContent()
                }
            }
        }
        return true
    }

    private var animationing = false //动画中,不能进行滑动
    private val updateRunnable: UpdateRunnable by lazy { UpdateRunnable(this) }

    /**
     * 中间滑条恢复原位置
     */
    private fun restoreStepperContent() {
        if (animationing)
            return
        animationing = true
        ValueAnimator.ofFloat(tvText.left.toFloat(), startStepperContentLeft).apply {
            duration = 300L // 恢复动画时间
            interpolator = AccelerateInterpolator()
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
                override fun onAnimationStart(animation: Animator) {}
                override fun onAnimationEnd(animation: Animator) {
                    animationing = false
                }
            })
            addUpdateListener { animation: ValueAnimator ->
                val value = animation.animatedValue as Float
                moveStepperContent(value)
            }
        }.start()
    }

    /**
     * 移动位置
     */
    private fun moveStepperContent(x: Float) {
        //限制子控件移动必须在视图范围内
        if (x < 0 || x + tvText.width > width)
            return
        val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        params.leftMargin = x.toInt()
        params.topMargin = 0
        params.width = tvText.width
        params.height = tvText.height
        tvText.layoutParams = params
    }

    internal class UpdateRunnable(view: NumberStepper) : Runnable {
        private val view: WeakReference<NumberStepper> = WeakReference(view)
        override fun run() {
            view.get()?.updateUI()
        }
    }

    private fun updateUI() {
        currentValue = getNextValue()
        updateContextText(currentValue)
        listener?.onValueChange(this, currentValue)
        if (stepTouch) {
            val duration = if (System.currentTimeMillis() - startTime > STEP_SPEED_CHANGE_DURATION) UPDATE_DURATION_FAST else UPDATE_DURATION_SLOW
            postDelayed(updateRunnable, duration)
        }
    }

    private fun getNextValue(): Float = when (status) {
        STATUS_MIMNUS -> currentValue - stepValue
        STATUS_PLUS -> currentValue + stepValue
        // STATUS_NORMAL
        else -> currentValue
    }

    @SuppressLint("SetTextI18n")
    private fun updateContextText(value: Float) {
        tvText.text = if (contentFinalText.isNullOrEmpty()) {
            "${contentTextStart ?: ""}${value}${contentTextEnd ?: ""}"
        } else {
            contentFinalText
        }
    }

    private var listener: NumberStepperValueChangeListener? = null

    interface NumberStepperValueChangeListener {
        fun onValueChange(view: View, value: Float)
    }

    fun setOnValueChangeListener(listener: NumberStepperValueChangeListener?) {
        this.listener = listener
    }
}