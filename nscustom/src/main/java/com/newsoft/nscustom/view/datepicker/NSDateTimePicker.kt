package com.newsoft.nscustom.view.datepicker

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import android.widget.TimePicker
import androidx.core.content.ContextCompat
import com.newsoft.nscustom.R
import java.text.SimpleDateFormat
import java.util.*

class NSDateTimePicker(
    private var pickerType: DatePickerEnum,
    private var context: Context,
    customDateTimeListener: ICustomDateTimeListener
) : View.OnClickListener {

    private var datePicker: DatePicker? = null
    private var timePicker: TimePicker? = null
    private var viewSwitcher: ViewSwitcher? = null

    private var btnSetDate: Button? = null
    private var btnSetTime: Button? = null
    private var btnSet: Button? = null
    private var btnCancel: Button? = null

    private var calendarDate: Calendar? = null

    private var iCustomDateTimeListener: ICustomDateTimeListener? = null

    private val dialog: Dialog

    private var is24HourView = true
    private var isAutoDismiss = true


    private var selectedHour: Int = 0
    private var selectedMinute: Int = 0
    private var maxDateInMillis: Long? = null
    private var minDateInMillis: Long? = null
    private var maxTimeInMinute: Int? = null
    private var minTimeInMinute: Int? = null

    private val dateNSDateTimePickerLayout: View
        @SuppressLint("SetTextI18n")
        get() {
            val linearMatchWrap = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            val linearWrapWrap = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val frameMatchWrap = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            val buttonParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f)

            val linearMain = LinearLayout(context)
            linearMain.layoutParams = linearMatchWrap
            linearMain.orientation = LinearLayout.VERTICAL
            linearMain.gravity = Gravity.CENTER

            val linearChild = LinearLayout(context)
            linearChild.layoutParams = linearWrapWrap
            linearChild.orientation = LinearLayout.VERTICAL

            var linearTop: LinearLayout? = null
            when (pickerType) {
                //TODO: view top chọn ngày và thời gian
                DatePickerEnum.DATE_TIME -> {
                    linearTop = LinearLayout(context)
                    linearTop.layoutParams = linearMatchWrap

                    btnSetDate = Button(context)
                    btnSetDate?.apply {
                        layoutParams = buttonParams
                        text = "Chọn Ngày"
                        id = SET_DATE
                        setTextColor(ContextCompat.getColor(context, R.color.black))
//                background = context.getDrawable(R.color.white)
                        setOnClickListener(this@NSDateTimePicker)
                    }

                    btnSetTime = Button(context)
                    btnSetTime?.apply {
                        layoutParams = buttonParams
                        text = "Chọn Giờ"
                        id = SET_TIME
                        setTextColor(ContextCompat.getColor(context, R.color.black))
//                background = context.getDrawable(R.color.white)
                        setOnClickListener(this@NSDateTimePicker)
                    }

                    linearTop.addView(btnSetDate)
                    linearTop.addView(btnSetTime)

                    datePicker = DatePicker(context)
                    timePicker = TimePicker(context)

                    hideKeyboardInputInTimePicker(
                        context.resources.configuration.orientation,
                        timePicker!!
                    )
                    timePicker!!.setIs24HourView(true)

                    timePicker!!.setOnTimeChangedListener { view, hourOfDay, minute ->
                        // updateTime(hourOfDay, minute)
                    }

                }
                DatePickerEnum.DATE -> {
                    datePicker = DatePicker(context)
                    setViewPicker(SET_DATE)
                }
                DatePickerEnum.TIME -> {
                    timePicker = TimePicker(context)
                    hideKeyboardInputInTimePicker(
                        context.resources.configuration.orientation,
                        timePicker!!
                    )
                    timePicker!!.setIs24HourView(true)

                    timePicker!!.setOnTimeChangedListener { view, hourOfDay, minute ->
                        // updateTime(hourOfDay, minute)
                    }
                    setViewPicker(SET_TIME)
                }
            }

            //TODO: view ngày và giờ
            viewSwitcher = ViewSwitcher(context)
            viewSwitcher!!.layoutParams = frameMatchWrap

            timePicker?.let { timePicker ->
                viewSwitcher!!.addView(timePicker)
            }
            datePicker?.let { datePicker ->
                viewSwitcher!!.addView(datePicker)
            }

            //TODO: view bottom ok hoặc cancel
            val linearBottom = LinearLayout(context)
            linearMatchWrap.topMargin = 8
            linearBottom.layoutParams = linearMatchWrap

            btnSet = Button(context)
            btnSet?.apply {
                layoutParams = buttonParams
                text = "OK"
                id = SET
                isAllCaps = false
                setTextColor(ContextCompat.getColor(context, R.color.black))
//                background = context.getDrawable(R.color.white)
                setOnClickListener(this@NSDateTimePicker)
            }

            btnCancel = Button(context)
            btnCancel?.apply {
                layoutParams = buttonParams
                text = "Cancel"
                id = CANCEL
                isAllCaps = false
                setTextColor(ContextCompat.getColor(context, R.color.black))
//                background = context.getDrawable(R.color.white)
                setOnClickListener(this@NSDateTimePicker)
            }

            linearBottom.addView(btnCancel)
            linearBottom.addView(btnSet)


            linearTop?.let {
                linearChild.addView(it)
            }
            linearChild.addView(viewSwitcher)
            linearChild.addView(linearBottom)

            linearMain.addView(linearChild)

            return linearMain
        }


    private fun updateTime(hourOfDay: Int, minute: Int) {
        if (minTimeInMinute != null) {
            val calendar = Calendar.getInstance()
            datePicker?.let {
                calendar.set(
                    it.year,
                    it.month,
                    it.dayOfMonth,
                    hourOfDay,
                    minute
                )
            }

            if (calendar.timeInMillis - Calendar.getInstance().timeInMillis >= minTimeInMinute!! * 60 * 1000) {
                selectedHour = hourOfDay
                selectedMinute = minute
            } else {
                selectedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                selectedMinute = Calendar.getInstance().get(Calendar.MINUTE) + minTimeInMinute!!
            }
        } else {
            selectedHour = hourOfDay
            selectedMinute = minute
        }

        updateDisplayedTime()
    }

    init {
        iCustomDateTimeListener = customDateTimeListener

        dialog = Dialog(context)
        dialog.setOnDismissListener { resetData() }

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val dialogView = dateNSDateTimePickerLayout
        dialog.setContentView(dialogView)
    }

    fun showDialog() {
        if (!dialog.isShowing) {
            if (calendarDate == null)
                calendarDate = Calendar.getInstance()

            val hourOfDay = calendarDate!!.get(Calendar.HOUR_OF_DAY)
            val minute = calendarDate!!.get(Calendar.MINUTE)

            updateTime(hourOfDay, minute)

            updateDisplayedDate()

            dialog.show()
            btnSetDate?.performClick()

            dialog.setOnCancelListener {
            }
        }
    }

    override fun onClick(v: View) {
        setViewPicker(v.id)
    }

    private fun setViewPicker(id: Int) {
        when (id) {
            SET_DATE -> {
                btnSetDate?.apply {
                    isEnabled = false
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                btnSetTime?.apply {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }

                viewSwitcher?.apply {
                    if (currentView != datePicker)
                        showPrevious()
                }
            }

            SET_TIME -> {
                btnSetTime?.apply {
                    isEnabled = false
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
                btnSetDate?.apply {
                    isEnabled = true
                    setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
                }

                viewSwitcher?.apply {
                    if (currentView == datePicker)
                        showNext()
                }
            }

            SET -> {
                if (iCustomDateTimeListener != null) {
                    var month = 0
                    var year = 0
                    var day = 0

                    datePicker?.let {
                        month = it.month
                        year = it.year
                        day = it.dayOfMonth
                    } ?: kotlin.run {
                        month = calendarDate!!.get(Calendar.MONTH)
                        year = calendarDate!!.get(Calendar.YEAR)
                        day = calendarDate!!.get(Calendar.DAY_OF_MONTH)
                    }

                    timePicker?.let {
                        updateTime(it.hour, it.minute)
                    }

                    calendarDate!!.set(year, month, day, selectedHour, selectedMinute)
                    iCustomDateTimeListener!!.onSet(
                        dialog = dialog,
                        calendarSelected = calendarDate!!,
                        dateSelected = calendarDate!!.time,
                        year = calendarDate!!.get(Calendar.YEAR),
                        monthFullName = getMonthFullName(calendarDate!!.get(Calendar.MONTH)),
                        monthShortName = getMonthShortName(calendarDate!!.get(Calendar.MONTH)),
                        monthNumber = calendarDate!!.get(Calendar.MONTH),
                        day = calendarDate!!.get(Calendar.DAY_OF_MONTH),
                        weekDayFullName = getWeekDayFullName(calendarDate!!.get(Calendar.DAY_OF_WEEK)),
                        weekDayShortName = getWeekDayShortName(calendarDate!!.get(Calendar.DAY_OF_WEEK)),
                        hour24 = if (is24HourView) calendarDate!!.get(Calendar.HOUR_OF_DAY) else 0,
                        hour12 = getHourIn12Format(calendarDate!!.get(Calendar.HOUR_OF_DAY)),
                        min = calendarDate!!.get(Calendar.MINUTE),
                        sec = calendarDate!!.get(Calendar.SECOND),
                        AM_PM = getAMPM(calendarDate!!)
                    )
                }
                if (dialog.isShowing && isAutoDismiss)
                    dialog.dismiss()
            }

            CANCEL -> {
                iCustomDateTimeListener?.onCancel()
                if (dialog.isShowing)
                    dialog.dismiss()
            }
        }
    }

    private fun updateDisplayedDate() {
        datePicker?.updateDate(
            calendarDate!!.get(Calendar.YEAR),
            calendarDate!!.get(Calendar.MONTH),
            calendarDate!!.get(Calendar.DATE)
        )

        maxDateInMillis?.let {
            datePicker?.maxDate = maxDateInMillis as Long
        }
        minDateInMillis?.let {
            datePicker?.minDate = minDateInMillis as Long
        }
    }

    private fun updateDisplayedTime() {
        timePicker?.apply {
            setIs24HourView(is24HourView)
            currentMinute = selectedMinute
            currentHour = selectedHour
        }
    }

    fun setMaxMinDisplayDate(minDate: Long? = null, maxDate: Long? = null) {
        minDate?.let {
            minDateInMillis = minDate
        }
        maxDate?.let {
            maxDateInMillis = maxDate
        }
    }

    fun setMaxMinDisplayedTime(minTimeMinute: Int? = null, maxTimeMinute: Int? = null) {
        minTimeMinute?.let {
            minTimeInMinute = minTimeMinute
        }
        maxTimeMinute?.let {
            maxTimeInMinute = maxTimeMinute
        }
    }

    fun setAutoDismiss(isAutoDismiss: Boolean) {
        this.isAutoDismiss = isAutoDismiss
    }

    fun dismissDialog() {
        if (!dialog.isShowing)
            dialog.dismiss()
    }

    fun setDate(calendar: Calendar?) {
        if (calendar != null)
            calendarDate = calendar
    }

    fun setDate(date: Date?) {
        if (date != null) {
            calendarDate = Calendar.getInstance()
            calendarDate!!.time = date
        }
    }

    fun setDate(year: Int, month: Int, day: Int) {
        if (month in 0..11 && day < 32 && day >= 0 && year > 100 && year < 3000) {
            calendarDate = Calendar.getInstance()
            calendarDate!!.set(year, month, day)
        }

    }

    fun setTimeIn24HourFormat(hourIn24Format: Int, minute: Int) {
        if (hourIn24Format in 0..23 && minute >= 0 && minute < 60) {
            if (calendarDate == null)
                calendarDate = Calendar.getInstance()

            calendarDate!!.set(
                calendarDate!!.get(Calendar.YEAR),
                calendarDate!!.get(Calendar.MONTH),
                calendarDate!!.get(Calendar.DAY_OF_MONTH), hourIn24Format,
                minute
            )

            is24HourView = true
        }
    }

    fun setTimeIn12HourFormat(_hourIn12Format: Int, minute: Int, isAM: Boolean) {
        var hourIn12Format = _hourIn12Format
        if (hourIn12Format in 1..12 && minute >= 0
            && minute < 60
        ) {
            if (hourIn12Format == 12)
                hourIn12Format = 0

            var hourIn24Format = hourIn12Format

            if (!isAM)
                hourIn24Format += 12

            if (calendarDate == null)
                calendarDate = Calendar.getInstance()

            calendarDate!!.set(
                calendarDate!!.get(Calendar.YEAR),
                calendarDate!!.get(Calendar.MONTH),
                calendarDate!!.get(Calendar.DAY_OF_MONTH), hourIn24Format,
                minute
            )

            is24HourView = false
        }
    }

    fun set24HourFormat(is24HourFormat: Boolean) {
        is24HourView = is24HourFormat
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMonthFullName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getMonthShortName(monthNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, monthNumber)

        val simpleDateFormat = SimpleDateFormat("MMM")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekDayFullName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EEEE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    @SuppressLint("SimpleDateFormat")
    private fun getWeekDayShortName(weekDayNumber: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, weekDayNumber)

        val simpleDateFormat = SimpleDateFormat("EE")
        simpleDateFormat.calendar = calendar

        return simpleDateFormat.format(calendar.time)
    }

    private fun getHourIn12Format(hour24: Int): Int {
        return when {
            hour24 == 0 -> 12
            hour24 <= 12 -> hour24
            else -> hour24 - 12
        }
    }

    private fun getAMPM(calendar: Calendar): String {
        return if (calendar.get(Calendar.AM_PM) == Calendar.AM)
            "am"
        else
            "pm"
    }

    private fun resetData() {
        calendarDate = null
        is24HourView = true
    }

    companion object {
        /**
         * @param date       date in String
         * @param fromFormat format of your **date** eg: if your date is 2011-07-07
         * 09:09:09 then your format will be **yyyy-MM-dd hh:mm:ss**
         * @param toFormat   format to which you want to convert your **date** eg: if
         * required format is 31 July 2011 then the toFormat should be
         * **d MMMM yyyy**
         * @return formatted date
         */
        private const val SET_DATE = 100
        private const val SET_TIME = 101
        private const val SET = 102
        private const val CANCEL = 103

        fun convertDate(_date: String, fromFormat: String, toFormat: String): String {
            var date = _date
            try {
                var simpleDateFormat = SimpleDateFormat(fromFormat)
                val d = simpleDateFormat.parse(date)
                val calendar = Calendar.getInstance()
                calendar.time = d

                simpleDateFormat = SimpleDateFormat(toFormat)
                simpleDateFormat.calendar = calendar
                date = simpleDateFormat.format(calendar.time)

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return date
        }

        fun pad(integerToPad: Int): String {
            return if (integerToPad >= 10 || integerToPad < 0)
                integerToPad.toString()
            else
                "0$integerToPad"
        }
    }

    private fun hideKeyboardInputInTimePicker(orientation: Int, timePicker: TimePicker) {
        try {
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                ((timePicker.getChildAt(0) as LinearLayout).getChildAt(4) as LinearLayout)
                    .getChildAt(0).visibility = View.GONE
            } else {
                (((timePicker.getChildAt(0) as LinearLayout).getChildAt(2) as LinearLayout)
                    .getChildAt(2) as LinearLayout).getChildAt(0).visibility = View.GONE
            }
        } catch (ex: Exception) {
        }
    }

    interface ICustomDateTimeListener {
        fun onSet(
            dialog: Dialog, calendarSelected: Calendar,
            dateSelected: Date, year: Int, monthFullName: String,
            monthShortName: String, monthNumber: Int, day: Int,
            weekDayFullName: String, weekDayShortName: String, hour24: Int,
            hour12: Int, min: Int, sec: Int, AM_PM: String
        )

        fun onCancel()
    }


}