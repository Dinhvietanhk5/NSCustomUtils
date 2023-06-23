package com.newsoft.nsdatepicker

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("AppCompatCustomView")
class NsDateView : TextView {

    private var dateFormat = "dd-MM-yyyy"
    private var timeFormat = "HH:mm"
    var calendar: Calendar? = null
    var type = 0 //TODO: 0 date, 1 hour
    var countStartDate = 0
    var defaultDate = false
    var isPast = false
    var minDate = 0L
    var isStart7Day = false
    var listener: NsDateViewListener? = null

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val typedArray =
            context.theme.obtainStyledAttributes(attrs, R.styleable.NsDateView, 0, 0)
        typedArray.getString(R.styleable.NsDateView_dateForMat)?.let {
            dateFormat = it
        }
        typedArray.getString(R.styleable.NsDateView_hourForMat)?.let {
            timeFormat = it
        }
        typedArray.getInt(R.styleable.NsDateView_typeForMat, 0).let {
            type = it
        }
        typedArray.getBoolean(R.styleable.NsDateView_defaultDate, false).let {
            defaultDate = it
        }
        typedArray.getBoolean(R.styleable.NsDateView_isPast, false).let {
            isPast = it
        }
//        typedArray.getBoolean(R.styleable.NsDateView_start7Day, false).let {
//            isStart7Day = it
//        }
//        typedArray.getString(R.styleable.NsDateView_countStartDate)?.let {
////            if (it.isNotEmpty())
////                countStartDate = it.toInt()
//        }
        initView()
    }

    private fun initView() {
        calendar = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val form = SimpleDateFormat(if (type == 0) dateFormat else timeFormat)
        try {
//            calendar!!.time = Objects.requireNonNull(form.parse(text.toString()))

//            if (isStart7Day)
//                calendar!!.timeInMillis - 7 * 86400000

            if (defaultDate) text = form.format(calendar!!.time)

        } catch (e: ParseException) {
            e.printStackTrace()
        }
        setOnClickListener {
            when (type) {
                0 -> pickDate()
                1 -> pickTime()
                2 -> pickMonthOfYear(2)
                3 -> pickMonthOfYear(3)
                4 -> pickMonthOfYear(4)
            }
        }
    }

    fun setNsDateViewListener(listener: NsDateViewListener) {
        this.listener = listener
    }

    fun setMinDatePickDialog(minDate: Long) {
        isPast = true
        this.minDate = minDate
    }

    // 2 month year, 3 month, 4 year
    private fun pickMonthOfYear(type: Int) {
        val dialog = LayoutInflater.from(context).inflate(R.layout.date_picker_dialog, null, false)
        val monthPicker = dialog.findViewById<View>(R.id.picker_month) as NumberPicker
        val yearPicker = dialog.findViewById<View>(R.id.picker_year) as NumberPicker

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = calendar!!.get(Calendar.MONTH) + 1

        if (type == 1) monthPicker.visibility = GONE

        val year: Int = calendar!!.get(Calendar.YEAR)
        yearPicker.minValue = year
        yearPicker.maxValue = 2999
        yearPicker.value = year

        if (type == 3)
            yearPicker.visibility = View.GONE
        if (type == 4)
            monthPicker.visibility = View.GONE

        val builder = AlertDialog.Builder(context)
        builder.setView(dialog)
            .setPositiveButton("Ok") { dialog1: DialogInterface?, id: Int ->
                when (type) {
                    2 -> {
                        calendar!![Calendar.YEAR] = yearPicker.value
                        calendar!![Calendar.MONTH] = monthPicker.value - 1
                        @SuppressLint("SimpleDateFormat")
                        val sdf = SimpleDateFormat(dateFormat.replace("dd-", ""))
                        val timezoneID = TimeZone.getDefault().id
                        sdf.timeZone = TimeZone.getTimeZone(timezoneID)
                        text = sdf.format(calendar!!.time).replace(" ", "")
                    }
                    3 -> {
                        text = monthPicker.value.toString()
                    }
                    4 -> {
                        text = yearPicker.value.toString()
                    }
                }
                listener?.onListener()
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.dismiss()
            }
        builder.create()
        builder.show()
    }

    private fun pickDate() {

        val datePickerDialog = DatePickerDialog(
            context, datePickerListener, calendar!![Calendar.YEAR],
            calendar!![Calendar.MONTH],
            calendar!![Calendar.DAY_OF_MONTH]
        )
        if (isPast) {
            datePickerDialog.datePicker.minDate =
                if (minDate != 0L) Calendar.getInstance().timeInMillis else minDate
        }
        datePickerDialog.show()
    }

    private val datePickerListener =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            Log.d("onDateSet", " year: $year monthOfYear: $monthOfYear dayOfMonth: $dayOfMonth ")
            calendar!!.set(Calendar.YEAR, year)
            calendar!!.set(Calendar.MONTH, monthOfYear)
            calendar!!.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            @SuppressLint("SimpleDateFormat") val sdf =
                SimpleDateFormat(dateFormat)
            val timezoneID = TimeZone.getDefault().id
            sdf.timeZone = TimeZone.getTimeZone(timezoneID)
            text = sdf.format(calendar!!.time).replace(" ", "")
            listener?.onListener()
        }

    // Time picker
    private fun pickTime() {
//        val mcurrentTime = Calendar.getInstance()
        val hour = calendar!![Calendar.HOUR_OF_DAY]
        val minute = calendar!![Calendar.MINUTE]
        val theme =
            if (context.isDarkThemeOn()) AlertDialog.THEME_HOLO_DARK else AlertDialog.THEME_HOLO_LIGHT
        val mTimePicker =
            TimePickerDialog(
                context,
                theme, timePickerListener, hour, minute, true
            ) //Yes 24 hour time

        mTimePicker.show()
    }

    private val timePickerListener =
        TimePickerDialog.OnTimeSetListener { time, selectedHour, selectedMinute ->
            calendar!![Calendar.HOUR_OF_DAY] = selectedHour
            calendar!![Calendar.MINUTE] = selectedMinute
            @SuppressLint("SimpleDateFormat")
            val sdf = SimpleDateFormat(timeFormat)
            text = sdf.format(calendar!!.time)
            listener?.onListener()
        }

    fun Context.isDarkThemeOn(): Boolean {
        return resources.configuration.uiMode and
                Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


    interface NsDateViewListener {
        fun onListener()
    }
}