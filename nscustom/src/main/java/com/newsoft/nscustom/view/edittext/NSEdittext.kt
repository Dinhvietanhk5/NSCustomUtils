package com.newsoft.nscustom.view.edittext

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.ColorFilter
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.newsoft.nscustom.R
import com.newsoft.nscustom.view.validatetor.ValidateTor
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*
import kotlin.Exception

@SuppressLint("AppCompatCustomView")
class NSEdittext @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    var editText: TextInputEditText? = null
    var validateTor = ValidateTor()
    private var mPaddingTop = 0f
    private var mPaddingBottom = 0f
    private var mPaddingStart = 0f
    private var mPaddingEnd = 0f
    private var mPaddingVertical = 0f
    private var mPaddingHorizontal = 0f
    private var mEtBg = 0
    private var mEdtColorHint = 0
    private var mFont: String? = null
    private var mText: String? = ""
    private var mHint: String? = ""
    private var mInputType = 0
    private var mImeOptions = 0
    private var mEdtStyle = 0
    private var mGravity = 0
    private var mEdtSize = 0
    private var mEdtColor = 0
    private var mEdtAllCaps = false
    protected var emptyAllowed = false
    protected var classType: String? = null
    protected var customRegexp: String? = null
    protected var customFormat: String? = null
    protected var errorString: String? = null
    protected var minNumber = 0
    protected var maxNumber = 0
    protected var floatminNumber = 0f
    protected var floatmaxNumber = 0f
    private var isHintTextInputLayout = false

    //TODO: value validation
    private var min = 0
    private var max = 0
    private var floatmin = 0f
    private var floatmax = 0f
    private var strContains: String? = null
    var pass: String? = null
    private var imeOptionsListener: EdittextImeOptionsListener? = null

    /**
     * init View
     *
     * @param attrs
     */
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        setWillNotDraw(false)
        editText = TextInputEditText(getContext())
        createEditBox(editText!!)
        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.NSEdittext, 0, 0
        )
        parseStyledAttributes(typedArray)

//        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        if (imm.isAcceptingText) {
//            Log.e("NSEdittext", "Software Keyboard was shown")
//        } else {
//            Log.e("NSEdittext", "Software Keyboard was not shown")
//        }
        setTextInputLayout()
        setEditText()
    }

    @SuppressLint("RtlHardcoded")
    private fun parseStyledAttributes(typedArray: TypedArray) {
        mInputType =
            typedArray.getInt(R.styleable.NSEdittext_inputType, Constant.TEXT_NOCHECK)
        mFont = typedArray.getString(R.styleable.NSEdittext_android_fontFamily)
        mEdtColor = typedArray.getColor(
            R.styleable.NSEdittext_android_textColor, resources.getColor(
                R.color.black
            )
        )
        mHint = typedArray.getString(R.styleable.NSEdittext_android_hint)
        mEdtColorHint = typedArray.getResourceId(
            R.styleable.NSEdittext_android_textColorHint, -1
        )
        mImeOptions = typedArray.getInt(
            R.styleable.NSEdittext_android_imeOptions,
            EditorInfo.IME_ACTION_DONE
        )
        mEdtStyle = typedArray.getInt(R.styleable.NSEdittext_android_textStyle, 0)
        mEdtAllCaps =
            typedArray.getBoolean(R.styleable.NSEdittext_android_textAllCaps, false)
        isHintTextInputLayout =
            typedArray.getBoolean(R.styleable.NSEdittext_hintInputLayoutEnabled, false)
        mGravity = typedArray.getInt(
            R.styleable.NSEdittext_android_gravity,
            Gravity.LEFT
        )
        mEdtSize =
            typedArray.getDimensionPixelSize(R.styleable.NSEdittext_android_textSize, 36)

//        mValidationType = NSEdittext.ValidationType.values()
//                [typedArray.getInt(R.styleable.NSEdittext_pattern, 0)];
        mEtBg = typedArray.getResourceId(R.styleable.NSEdittext_android_background, -1)
        mText = typedArray.getString(R.styleable.NSEdittext_android_text)

        mPaddingVertical = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingVertical, 0
        ).toFloat()
        mPaddingHorizontal = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingHorizontal, 0
        ).toFloat()

        mPaddingStart = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingStart, 0
        ).toFloat()
        mPaddingEnd = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingEnd, 0
        ).toFloat()
        mPaddingTop = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingTop, 0
        ).toFloat()
        mPaddingBottom = typedArray.getDimensionPixelSize(
            R.styleable.NSEdittext_android_paddingBottom, 0
        ).toFloat()
        emptyAllowed = typedArray.getBoolean(R.styleable.NSEdittext_emptyAllowed, false)
        classType = typedArray.getString(R.styleable.NSEdittext_classType)
        customRegexp = typedArray.getString(R.styleable.NSEdittext_customRegexp)
        errorString = typedArray.getString(R.styleable.NSEdittext_errorText)
        //        mTextColorError = typedArray.getColor(R.styleable.NSEdittext_errorColor,
//                getResources().getColor(R.color.red));
        customFormat = typedArray.getString(R.styleable.NSEdittext_customFormat)
        if (mInputType == Constant.TEXT_NUMERIC_RANGE) {
            minNumber = typedArray.getInt(R.styleable.NSEdittext_minNumber, Int.MIN_VALUE)
            maxNumber = typedArray.getInt(R.styleable.NSEdittext_maxNumber, Int.MAX_VALUE)
        }
        if (mInputType == Constant.TEXT_FLOAT_NUMERIC_RANGE) {
            floatminNumber =
                typedArray.getFloat(R.styleable.NSEdittext_floatminNumber, Float.MIN_VALUE)
            floatmaxNumber =
                typedArray.getFloat(R.styleable.NSEdittext_floatmaxNumber, Float.MAX_VALUE)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setEditText() {
        this.background = null
        editText!!.setTextColor(mEdtColor)
        if (!isHintTextInputLayout) editText!!.hint = mHint
        editText!!.setHintTextColor(mEdtColorHint)
        editText!!.imeOptions = mImeOptions
        editText!!.typeface = Typeface.defaultFromStyle(mEdtStyle)
        editText!!.gravity = mGravity

        if (mGravity == Gravity.CENTER) {
            mPaddingStart = 137f
            mPaddingEnd = 137f
        }

        if (mEtBg != -1) {
            @SuppressLint("UseCompatLoadingForDrawables") val drawable =
                resources.getDrawable(mEtBg)
            if (drawable != null) editText!!.background =
                drawable else editText!!.setBackgroundColor(mEtBg)
        }

        if (mEdtColorHint != -1) editText!!.setHintTextColor(resources.getColor(mEdtColorHint))
        if (mEdtSize > 0)
            editText!!.setTextSize(TypedValue.COMPLEX_UNIT_PX, mEdtSize.toFloat())
        if (mText != null)
            editText!!.setText(if (mEdtAllCaps) mText!!.toUpperCase() else mText!!.toLowerCase())

        if (mInputType != EditorInfo.TYPE_NULL)
            when (mInputType) {
                Constant.TEXT_PHONE -> editText!!.inputType = InputType.TYPE_CLASS_PHONE
                Constant.TEXT_EMAIL -> editText!!.inputType =
                    InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                Constant.TEXT_DATE -> editText!!.inputType = InputType.TYPE_CLASS_DATETIME
                Constant.TEXT_PASS -> {
                    this.isPasswordVisibilityToggleEnabled = true
                    this.passwordVisibilityToggleContentDescription = "description"
                    editText!!.inputType =
                        InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                Constant.TEXT_NUMERIC, Constant.TEXT_NUMERIC_RANGE, Constant.TEXT_FLOAT_NUMERIC_RANGE, Constant.TEXT_MONEY -> editText!!.inputType =
                    InputType.TYPE_CLASS_NUMBER
                else -> editText!!.inputType = InputType.TYPE_CLASS_TEXT
            }


        if (mPaddingVertical != 0f) {
            mPaddingTop = mPaddingVertical
            mPaddingBottom = mPaddingVertical
        }
        if (mPaddingHorizontal != 0f) {
            mPaddingStart = mPaddingHorizontal
            mPaddingEnd = mPaddingHorizontal
        }

        editText!!.setPadding(
            mPaddingStart.toInt(),
            mPaddingTop.toInt(),
            mPaddingEnd.toInt(),
            mPaddingBottom.toInt()
        )

        editText!!.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
            if (imeOptionsListener == null) return@setOnEditorActionListener false else {
                imeOptionsListener!!.onClick(actionId)
                return@setOnEditorActionListener true
            }
        }
        editText!!.setOnTouchListener { view: View?, motionEvent: MotionEvent? ->
            showError(false)
            false
        }

        var current = ""
        var selectionEnd = 0

        editText!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (mInputType == Constant.TEXT_MONEY) {
                    if (s.toString().isNotEmpty() && s.toString() != current) {
                        var formatted = ""
                        try {
                            selectionEnd = editText!!.selectionEnd

                            editText!!.removeTextChangedListener(this)
                            formatted = formatMoney(s.toString())
                            current = formatted
                            editText!!.setText(formatted)
                            editText!!.addTextChangedListener(this)
                            editText!!.setSelection(selectionEnd)
                        } catch (e: Exception) {
                            editText!!.setSelection(formatted.length)
                            e.printStackTrace()
                        }
                    }
                }

                if (s.toString().isNotEmpty())
                    showError(false)
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        if (mInputType == Constant.TEXT_MONEY && editText!!.text.toString().isNotEmpty()) {
            val formatted = formatMoney(editText!!.text.toString())
            editText!!.setText(formatted)
        }

        if (mInputType != Constant.TEXT_PASS)
            setEndIconOnClickListener {
                editText!!.setText("")
                showError(false)
            }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTextInputLayout() {
        if (endIconMode != END_ICON_CUSTOM) endIconMode =
            if (mInputType == Constant.TEXT_PASS) END_ICON_PASSWORD_TOGGLE else END_ICON_CLEAR_TEXT
        isHintEnabled = isHintTextInputLayout
        gravity = mGravity
        setPadding(0, 0, 0, 0)

    }


    fun formatMoney(s: String): String {
        val cleanString = s.replace("[$,.]".toRegex(), "")
        val parsed = cleanString.toDouble()
        return formatNumber(parsed.toLong())
    }

    private fun formatNumber(number: Long): String {
        return try {
//        if (number < 0 && number > -1000 || number in 1..999999999) {
//            return number.toString()
//        }
            val formatter: NumberFormat =
                DecimalFormat("###,###,###,###,###,###,###,###,###,###,###")
            var resp = formatter.format(number)
//            resp = resp.replace(".", ",")
            resp
        } catch (e: Exception) {
            Log.e("formatNumber", e.message!!)
            "0"
        }
    }


    /**
     * set View
     */
    @SuppressLint("SetTextI18n")
    private fun showError(isShow: Boolean) {
        isErrorEnabled = isShow
        error = if (isShow) {
            if (!TextUtils.isEmpty(errorString)) errorString else "Vui lòng nhập " + (if (TextUtils.isEmpty(
                    text
                )
            ) "" else "lại ") + if (TextUtils.isEmpty(mHint)) "thông tin" else mHint!!.toLowerCase()
        } else null
    }

    fun reset() {
//        tvError.setVisibility(GONE);
    }//TODO: true có lỗi, false ko lỗi

    // https://github.com/nisrulz/validatetor
    private val isCheckValidate: Boolean
        get() {
            showError(false)
            var isValidate = false
            when (mInputType) {
                Constant.TEXT_NOCHECK -> isValidate = true
                Constant.TEXT_ALPHA -> isValidate = validateTor.isAlpha(
                    text
                )
                Constant.TEXT_ALPHANUMERIC -> isValidate = validateTor.isAlphanumeric(
                    text
                )
                Constant.TEXT_NUMERIC -> isValidate = validateTor.isNumeric(
                    text
                )
                Constant.TEXT_NUMERIC_RANGE -> isValidate =
                    Utility.isNumericRangeValidator(editText, min, max)
                Constant.TEXT_FLOAT_NUMERIC_RANGE -> isValidate =
                    Utility.isFloatNumericRangeValidator(editText, floatmin, floatmax)
                Constant.TEXT_REGEXP -> isValidate =
                    Utility.isRegexpValidator(editText, customRegexp)
                Constant.TEXT_CREDITCARD -> isValidate = Utility.isCreditCardValidator(editText)
                Constant.TEXT_EMAIL -> isValidate = validateTor.isEmail(
                    text
                )
                Constant.TEXT_PHONE -> isValidate = Utility.isPhone(editText) && text!!.length > 8
                Constant.TEXT_DOMAINNAME -> isValidate = validateTor.isDecimal(
                    text
                )
                Constant.TEXT_IPADDRESS -> isValidate = validateTor.isIPAddress(
                    text
                )
                Constant.TEXT_PERSONNAME -> isValidate = Utility.isPersonNameValidator(editText)
                Constant.TEXT_PERSONFULLNAME -> isValidate =
                    Utility.isPersonFullNameValidator(editText)
                Constant.TEXT_WEBURL -> isValidate = Utility.isWebUrlValidator(editText)
                Constant.TEXT_DATE -> isValidate = Utility.isDateValidator(editText)
                Constant.TEXT_TEXT -> isValidate = !validateTor.isEmpty(
                    text
                )
                Constant.TEXT_CONTAINS -> isValidate = validateTor.containsSubstring(
                    text, strContains
                )
                Constant.TEXT_PASS -> {

                    if (!TextUtils.isEmpty(pass)) {
                        isValidate = text == pass
                        if (!isValidate) errorString = "Mật khẩu không khớp !"
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

    fun setImeOptionsListener(imeOptionsListener: EdittextImeOptionsListener?) {
        this.imeOptionsListener = imeOptionsListener
    }

    fun setmInputType(mInputType: Int) {
        this.mInputType = mInputType
    }

    fun setmImeOptions(mImeOptions: Int) {
        this.mImeOptions = mImeOptions
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

    private fun createEditBox(editText: TextInputEditText) {
        val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        //        editText.setPadding(0,10,0,0);
        editText.layoutParams = layoutParams
        addView(editText)
    }

    override fun setError(error: CharSequence?) {
        val defaultColorFilter = backgroundDefaultColorFilter
        super.setError(error)
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter)
    }

    override fun drawableStateChanged() {
        val defaultColorFilter = backgroundDefaultColorFilter
        super.drawableStateChanged()
        //Reset EditText's background color to default.
        updateBackgroundColorFilter(defaultColorFilter)
    }

    private fun updateBackgroundColorFilter(colorFilter: ColorFilter?) {
        if (getEditText() != null && getEditText()!!.background != null) getEditText()!!.background.colorFilter =
            colorFilter
    }

    private val backgroundDefaultColorFilter: ColorFilter?
        private get() {
            var defaultColorFilter: ColorFilter? = null
            if (getEditText() != null && getEditText()!!.background != null) defaultColorFilter =
                DrawableCompat.getColorFilter(
                    getEditText()!!.background
                )
            return defaultColorFilter
        }

    companion object {
        @SuppressLint("NewApi")
        fun showKeyboard(context: Context) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            Objects.requireNonNull(imm).toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }

    init {
        init(context, attrs, defStyleAttr)
    }
}