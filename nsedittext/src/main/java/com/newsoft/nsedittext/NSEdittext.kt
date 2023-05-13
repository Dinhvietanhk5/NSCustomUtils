package com.newsoft.nsedittext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color.red
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.newsoft.nsedittext.validatetor.ValidateTor
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

@SuppressLint("AppCompatCustomView")
class NSEdittext : LinearLayout {


    var editText: EditText? = null
    var tvError: TextView? = null
    var imvLeft: ImageView? = null
    var validateTor = ValidateTor()

    //
//    private var mPaddingTop = 0f
//    private var mPaddingBottom = 0f
//    private var mPaddingStart = 0f
//    private var mPaddingEnd = 0f
//    private var mPaddingVertical = 0f
//    private var mPaddingHorizontal = 0f
//    private var mEtBg = 0
//    private var mEdtColorHint = 0
//    private var mFont: String? = null
//    private var mText: String? = ""
//    private var mHint: String? = ""
    private var mInputType = 0
    private var mEdtSize = 0

    //    private var mImeOptions = 0
//    private var mEdtStyle = 0
//    private var mGravity = 0
//    private var mEdtSize = 0
//    private var mEdtColor = 0
//    private var mEdtAllCaps = false
//    protected var emptyAllowed = false
//    protected var classType: String? = null
    protected var customRegexp = ""
    protected var msgError = ""
    private var mUnitMoney = ""
//    protected var customFormat: String? = null
//    protected var errorString: String? = null
//    protected var minNumber = 0
//    protected var maxNumber = 0
//    protected var floatminNumber = 0f
//    protected var floatmaxNumber = 0f

    //TODO: value validation
    private var moneyFormatCharacter = 0
    private var min = 0
    private var max = 0
    private var floatmin = 0f
    private var floatmax = 0f
    private var strContains: String? = null
    var pass: String? = null
    private var imeOptionsListener: EdittextImeOptionsListener? = null
    private var maxMoney = 0L
    private var msgMaxMoney = ""


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context!!, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context!!, attrs)
    }

    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int,
        defStyleRes: Int
    ) : super(
        context,
        attrs,
        defStyle,
        defStyleRes
    ) {
        init(context, attrs)
    }


    /**
     * init View
     *
     * @param attrs
     */
    private fun init(context: Context, attrs: AttributeSet?) {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NSEdittext, 0, 0
        )
        val view = LayoutInflater.from(context).inflate(R.layout.view_edittext, null, false)
        editText = view.findViewById(R.id.edt)
        tvError = view.findViewById(R.id.tvError)
        imvLeft = view.findViewById(R.id.imvLeft)

        initEditText(typedArray, context)
        initTextErrorNotification(typedArray, context)

        view.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        addView(view)
        setPadding(0, 0, 0, 0)
        this.background = null
        gravity = Gravity.CENTER_VERTICAL
        orientation = VERTICAL
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun initEditText(typedArray: TypedArray, context: Context) {

        val mFont = typedArray.getResourceId(R.styleable.NSEdittext_android_fontFamily, 0)
        var mTypeface: Typeface? = null
        if (mFont != 0)
            mTypeface = ResourcesCompat.getFont(context, mFont)

        mInputType =
            typedArray.getInt(R.styleable.NSEdittext_inputType, Constant.TEXT_NOCHECK)

        val mEdtColor = typedArray.getColor(
            R.styleable.NSEdittext_android_textColor,
            resources.getColor(R.color.black)
        )
        val mHint = typedArray.getString(R.styleable.NSEdittext_android_hint)
        val mEdtColorHint =
            typedArray.getResourceId(R.styleable.NSEdittext_android_textColorHint, -1)
        val mImeOptions =
            typedArray.getInt(R.styleable.NSEdittext_android_imeOptions, EditorInfo.IME_ACTION_DONE)
        val mEdtStyle = typedArray.getInt(R.styleable.NSEdittext_android_textStyle, 0)
        val mEdtAllCaps =
            typedArray.getBoolean(R.styleable.NSEdittext_android_textAllCaps, false)
        val mGravity = typedArray.getInt(R.styleable.NSEdittext_android_gravity, Gravity.LEFT)
        mEdtSize =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_textSize, 36)

//       val mValidationType = NSEdittext.ValidationType.values()
//                [typedArray.getInt(R.styleable.NSEdittext_pattern, 0)];
        val mEtBg = typedArray.getResourceId(R.styleable.NSEdittext_android_background, -1)
        val mDrawableEnd = typedArray.getResourceId(R.styleable.NSEdittext_android_drawableEnd, -1)
        val mText = typedArray.getString(R.styleable.NSEdittext_android_text)

        val mPaddingVertical =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingVertical, 0)
                .toFloat()
        val mPaddingHorizontal =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingHorizontal, 0)
                .toFloat()

        var mPaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingStart, 0)
                .toFloat()
        var mPaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingEnd, 0).toFloat()
        var mPaddingTop =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingTop, 0).toFloat()
        var mPaddingBottom =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_paddingBottom, 0)
                .toFloat()
        customRegexp = typedArray.getString(R.styleable.NSEdittext_customRegexp).toString()

        typedArray.getString(R.styleable.NSEdittext_unit_number)?.let {
            mUnitMoney = it
        }

        editText?.apply {
            setTextColor(mEdtColor)
            hint = mHint
            setHintTextColor(mEdtColorHint)

            imeOptions = mImeOptions
            typeface = Typeface.defaultFromStyle(mEdtStyle)
            gravity = mGravity


            if (mEdtColorHint != -1) setHintTextColor(resources.getColor(mEdtColorHint))
            if (mEdtSize > 0)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, mEdtSize.toFloat())
            if (mText != null)
                setText(if (mEdtAllCaps) mText.toUpperCase() else mText.toLowerCase())

            mTypeface?.let {
                typeface = mTypeface
            }

            if (mPaddingVertical != 0f) {
                mPaddingTop = mPaddingVertical
                mPaddingBottom = mPaddingVertical
            }
            if (mPaddingHorizontal != 0f) {
                mPaddingStart = mPaddingHorizontal
                mPaddingEnd = mPaddingHorizontal
            }

            setPadding(
                mPaddingStart.toInt(),
                mPaddingTop.toInt(),
                mPaddingEnd.toInt(),
                mPaddingBottom.toInt()
            )

            if (mEtBg != -1) {
                ContextCompat.getDrawable(context, mEtBg)?.let {
                    background = it
                } ?: kotlin.run {
                    setBackgroundColor(mEtBg)
                }
            }

            setOnEditorActionListener { _: TextView?, actionId: Int, event: KeyEvent? ->
                if (imeOptionsListener == null) return@setOnEditorActionListener false else {
                    imeOptionsListener!!.onClick(actionId)
                    return@setOnEditorActionListener true
                }
            }
            setOnTouchListener { view: View?, motionEvent: MotionEvent? ->
                showError(false)
                false
            }

            if (mInputType != Constant.TEXT_PASS) {
                drawerEndClickNotTypePass()
            }

            when (mInputType) {
                Constant.TEXT_MONEY -> setTextTypeMoney(this)
                Constant.TEXT_PASS -> setTextTypePass()
                Constant.TEXT_PHONE -> inputType = InputType.TYPE_CLASS_PHONE
                Constant.TEXT_EMAIL -> inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                Constant.TEXT_DATE -> inputType = InputType.TYPE_CLASS_DATETIME
                Constant.TEXT_NUMERIC,
                Constant.TEXT_NUMERIC_RANGE,
                Constant.TEXT_FLOAT_NUMERIC_RANGE -> inputType = InputType.TYPE_CLASS_NUMBER
                else -> inputType = InputType.TYPE_CLASS_TEXT
            }

            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    private fun setTextTypeMoney(editText: EditText) {
        Log.e("setTextTypeMoney", " ")
        var current = ""
        var selectionEdt = 0
        var money = 0L
        var formatted = ""
        var isLimitMoney = false

        editText.apply {

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    try {
                        if (s.toString().isNotEmpty() && s.toString() != current) {
                            try {
                                money = this@NSEdittext.text.toString().toLong()
                                //TODO giới hạn tiền
                                if (maxMoney != 0L && money > maxMoney) {

                                    if (msgMaxMoney.isNotEmpty()) {
                                        msgError = msgMaxMoney
                                        showError(true)
                                        isLimitMoney = true
                                    }
                                    formatted = formatNumber(maxMoney)
                                    current = formatted
                                    setText(formatted)
                                    return
                                }

                                selectionEdt = selectionEnd

                                removeTextChangedListener(this)
                                formatted = formatTextMoney(s.toString())
                                current = formatted
                                setText(formatted)
                                addTextChangedListener(this)
                                setSelection(selectionEdt)
                            } catch (e: Exception) {
                                setSelection(formatted.length)
                                e.printStackTrace()
                            }
                        }

                        if (s.toString().isEmpty() || money < maxMoney) {
                            msgError = ""
                            isLimitMoney = false
                        }

                        val errorMoney = s.toString().isNotEmpty() && !isLimitMoney
                        showError(!errorMoney)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })

//            Log.e("isNumericValidator", "${Utility.isNumericValidator(this)}")

            if (text.toString().isNotEmpty() && Utility.isNumericValidator(this)) {
                val formatted = formatTextMoney(text.toString())
                setText(formatted)
            }
            if (!Utility.isNumericValidator(this))
                Log.e("NSEdittext", "error format number $text")
            inputType = InputType.TYPE_CLASS_NUMBER
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTextTypePass() {
        editText?.apply {
            imvLeft!!.setImageResource(R.drawable.ic_pass_invisible)
            imvLeft?.setOnClickListener {
                if (transformationMethod == HideReturnsTransformationMethod.getInstance()) {
                    transformationMethod = PasswordTransformationMethod.getInstance()
                    imvLeft!!.setImageResource(R.drawable.ic_pass_invisible)
                } else {
                    imvLeft!!.setImageResource(R.drawable.ic_pass_visible)
                    transformationMethod = HideReturnsTransformationMethod.getInstance()
                }
            }
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        }
    }

    private fun drawerEndClickNotTypePass() {
        editText?.apply {
            imvLeft?.setOnClickListener {
                imvLeft?.visibility = View.GONE
                setText("")
                showError(false)
            }

            this.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    imvLeft?.visibility = if (text!!.isNotEmpty()) View.VISIBLE else View.GONE
                }

                override fun afterTextChanged(editable: Editable?) {

                }
            })
        }
    }

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    private fun initTextErrorNotification(
        typedArray: TypedArray,
        context: Context
    ) {
        val mText = typedArray.getString(R.styleable.NSEdittext_errorText)
        val mColor = typedArray.getColor(
            R.styleable.NSEdittext_errorTextColor,
            resources.getColor(R.color.red)
        )
        val mSize =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_errorTextSize, mEdtSize)
        val mStyle = typedArray.getInt(R.styleable.NSEdittext_errorTextStyle, 0)
        moneyFormatCharacter = typedArray.getInt(R.styleable.NSEdittext_moneyFormatCharacter, 0)

        var mPaddingStart =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_errorPaddingStart, 0)
                .toFloat()
        var mPaddingEnd =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_errorPaddingEnd, 0)
                .toFloat()
        val mPaddingHorizontal =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_errorPaddingHorizontal, 0)
                .toFloat()
        val mPaddingTop =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_errorPaddingTop, 0)
                .toFloat()
        val mBackground = typedArray.getResourceId(R.styleable.NSEdittext_errorBackground, -1)
        if (mPaddingHorizontal != 0f) {
            mPaddingStart = mPaddingHorizontal
            mPaddingEnd = mPaddingHorizontal
        }

        tvError?.apply {
            if (mText != null) text = mText

            setTextColor(mColor)

            if (mSize > 0) setTextSize(TypedValue.COMPLEX_UNIT_PX, mSize.toFloat())
            typeface = Typeface.defaultFromStyle(mStyle)

            if (mBackground != -1) {
                ContextCompat.getDrawable(context, mBackground)?.let {
                    background = it
                } ?: kotlin.run {
                    setBackgroundColor(mBackground)
                }
            } else
                background = null

            if (mPaddingStart.toInt() != 0 || mPaddingTop.toInt() != 0 || mPaddingEnd.toInt() != 0)
                setPadding(
                    mPaddingStart.toInt(),
                    mPaddingTop.toInt(),
                    mPaddingEnd.toInt(),
                    0
                )

            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        showError(false)
    }


    fun formatTextMoney(s: String): String {
        val cleanString = s.replace("[$,.]".toRegex(), "")
        val parsed = cleanString.toDouble()
        return formatNumber(parsed.toLong())
    }

    private fun formatNumber(number: Long): String {
        return try {
            val formatter: NumberFormat =
                DecimalFormat("###,###,###,###,###,###,###,###,###,###,###")
            var resp = formatter.format(number)
            if (moneyFormatCharacter == 1)
                resp = resp.replace(".", ",")
            resp
        } catch (e: Exception) {
            Log.e("formatNumber", e.message!!)
            "0"
        } + " $mUnitMoney"
    }

    /**
     * set View
     */
    @SuppressLint("SetTextI18n")
    private fun showError(isShow: Boolean) {
        Log.e("showError", " $isShow")
        if (isShow) {
            var hintEdt = ""
            editText?.let {
                if (it.hint != null)
                    hintEdt = it.hint.toString()
            }

            tvError?.apply {
                text = msgError.ifEmpty {
                    "Vui lòng nhập " + (if (this@NSEdittext.text!!.isEmpty()) "" else "lại ") + if (hintEdt.isEmpty()) "thông tin" else hintEdt.toLowerCase()
                }
                visibility = View.VISIBLE
            }
        } else
            tvError?.visibility = View.GONE
    }

    fun reset() {
        showError(false)
    }//TODO: true có lỗi, false ko lỗi

    // https://github.com/nisrulz/validatetor
    private val isCheckValidate: Boolean
        get() {
            showError(false)
            var isValidate = false
            when (mInputType) {
                Constant.TEXT_NOCHECK -> isValidate = true
                Constant.TEXT_ALPHA -> isValidate = validateTor.isAlpha(text)
                Constant.TEXT_ALPHANUMERIC -> isValidate = validateTor.isAlphanumeric(text)
                Constant.TEXT_NUMERIC -> isValidate = validateTor.isNumeric(text)
                Constant.TEXT_NUMERIC_RANGE -> isValidate =
                    Utility.isNumericRangeValidator(editText, min, max)
                Constant.TEXT_FLOAT_NUMERIC_RANGE -> isValidate =
                    Utility.isFloatNumericRangeValidator(editText, floatmin, floatmax)
                Constant.TEXT_REGEXP -> isValidate =
                    Utility.isRegexpValidator(editText, customRegexp)
                Constant.TEXT_CREDITCARD -> isValidate = Utility.isCreditCardValidator(editText)
                Constant.TEXT_EMAIL -> isValidate = validateTor.isEmail(text)
                Constant.TEXT_PHONE -> isValidate = Utility.isPhone(editText) && text!!.length > 8
                Constant.TEXT_DOMAINNAME -> isValidate = validateTor.isDecimal(text)
                Constant.TEXT_IPADDRESS -> isValidate = validateTor.isIPAddress(text)
                Constant.TEXT_PERSONNAME -> isValidate = Utility.isPersonNameValidator(editText)
                Constant.TEXT_PERSONFULLNAME -> isValidate =
                    Utility.isPersonFullNameValidator(editText)
                Constant.TEXT_WEBURL -> isValidate = Utility.isWebUrlValidator(editText)
                Constant.TEXT_DATE -> isValidate = Utility.isDateValidator(editText)
                Constant.TEXT_TEXT -> isValidate = !validateTor.isEmpty(text)
                Constant.TEXT_CONTAINS -> isValidate =
                    validateTor.containsSubstring(text, strContains)
                Constant.TEXT_PASS -> {

                    if (!TextUtils.isEmpty(pass)) {
                        isValidate = text == pass
                        if (!isValidate) msgError = "Mật khẩu không khớp !"
                    } else
                        isValidate = text!!.isNotEmpty()

//                    val isAlphanumeric = validateTor.isEmpty(text)
//                    if (TextUtils.isEmpty(pass)) {
//                        isValidate = isAlphanumeric
//                    } else {
//                        val isAlphanumeric2 = validateTor.isEmpty(pass)
//                        if (isAlphanumeric && isAlphanumeric2) {
//                            isValidate = validateTor.containsSubstring(text, pass)
//                            if (!isValidate) errorString = "Mật khẩu không khớp !"
//                        } else isValidate = false
//                    }
                }
                else -> {
                    isValidate = text!!.isNotEmpty()
                }
            }
            isValidate = !isValidate
            //TODO: true có lỗi, false ko lỗi
            if (isValidate) showError(true)
            return isValidate
        }

    fun validate(): Boolean {
        return isCheckValidate
    }

    fun setMaxMoney(maxMoney: Long, msgMaxMoney: String = "") {
        this.maxMoney = maxMoney
        this.msgMaxMoney = msgMaxMoney
    }

    fun setImeOptionsListener(imeOptionsListener: EdittextImeOptionsListener?) {
        this.imeOptionsListener = imeOptionsListener
    }

    fun setmInputType(mInputType: Int) {
        this.mInputType = mInputType
    }

    fun setmImeOptions(mImeOptions: Int) {
        editText!!.imeOptions = mImeOptions
    }

    fun validate(min: Int, max: Int): Boolean {
        this.min = min
        this.max = max
        return isCheckValidate
    }

    fun validate(min: Float, max: Float): Boolean {
        floatmax = min
        floatmin = max
        return isCheckValidate
    }

    fun validatePass(pass: String?): Boolean {
        this.pass = pass
        return isCheckValidate
    }

    fun validateContains(strContains: String?): Boolean {
        this.strContains = strContains
        return isCheckValidate
    }

    private fun getDrawable(drawable: Int): Drawable? {
        return if (drawable == 0) null else ContextCompat.getDrawable(context, drawable)
    }

    private fun getDimension(id: Int): Int {
        return if (id == 0) 0 else context.resources.getDimensionPixelSize(id)
    }

    var text: String?
        get() = if (mInputType == Constant.TEXT_MONEY)
            editText!!.text.toString().replace("[$,.]".toRegex(), "").trim { it <= ' ' }
        else editText!!.text.toString()
        set(text) {
            editText!!.setText(text)
        }

    public fun setTextEdt(text: String) {
        editText?.setText(text)
    }

//    private fun createEditBox(editText: TextInputEditText) {
//        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
//        //        editText.setPadding(0,10,0,0);
//        editText.layoutParams = layoutParams
//        addView(editText)
//    }

//    override fun setError(error: CharSequence?) {
//        val defaultColorFilter = backgroundDefaultColorFilter
//        super.setError(error)
//        //Reset EditText's background color to default.
//        updateBackgroundColorFilter(defaultColorFilter)
//    }

//    override fun drawableStateChanged() {
////        val defaultColorFilter = backgroundDefaultColorFilter
//        super.drawableStateChanged()
//        //Reset EditText's background color to default.
////        updateBackgroundColorFilter(defaultColorFilter)
//    }

//    private fun updateBackgroundColorFilter(colorFilter: ColorFilter?) {
//        if (getEditText() != null && getEditText()!!.background != null) getEditText()!!.background.colorFilter =
//            colorFilter
//    }

//    private val backgroundDefaultColorFilter: ColorFilter?
//        private get() {
//            var defaultColorFilter: ColorFilter? = null
//            if (getEditText() != null && getEditText()!!.background != null) defaultColorFilter =
//                DrawableCompat.getColorFilter(
//                    getEditText()!!.background
//                )
//            return defaultColorFilter
//        }


}