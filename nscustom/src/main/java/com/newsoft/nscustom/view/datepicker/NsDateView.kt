package com.newsoft.nscustom.view.datepicker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TextView
import com.newsoft.nscustom.R
import java.lang.reflect.Field
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
                2 -> datePickerDialog().show()
            }
        }
    }


    fun setNsDateViewListener(listener: NsDateViewListener) {
        this.listener = listener
    }

    private fun pickMonth() {
        DatePickerDialog(
            context,
            android.R.style.Theme_Holo_Light_Dialog,
            datePickerListener,
            calendar!![Calendar.YEAR],
            calendar!![Calendar.MONTH],
            calendar!![Calendar.DAY_OF_MONTH]
        ).show()
    }

    private fun pickDate() {

        DatePickerDialog(
            context, datePickerListener, calendar!![Calendar.YEAR],
            calendar!![Calendar.MONTH],
            calendar!![Calendar.DAY_OF_MONTH]
        ).show()
    }

    @SuppressLint("SetTextI18n")
    fun datePickerDialog(): DatePickerDialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Date Picker Dialog
        val datePickerDialog = DatePickerDialog(context,
            { view, year, monthOfYear, dayOfMonth ->
//                date.text = "$dayOfMonth $monthOfYear, $year"
            }, year, month, day)

        return datePickerDialog


    }

    private fun customDatePicker(): DatePickerDialog? {
        Log.e("customDatePicker"," ")
        val dpd = DatePickerDialog(
            context, datePickerListener, calendar!![Calendar.YEAR],
            calendar!![Calendar.MONTH],
            calendar!![Calendar.DAY_OF_MONTH]
        )
        try {
            val datePickerDialogFields: Array<Field> = dpd.javaClass.declaredFields
            for (datePickerDialogField in datePickerDialogFields) {
                if (datePickerDialogField.name.equals("mDatePicker")) {
                    datePickerDialogField.isAccessible = true
                    val datePicker = datePickerDialogField
                        .get(dpd) as DatePicker
                    val datePickerFields: Array<Field> = datePickerDialogField.type
                        .declaredFields
                    for (datePickerField in datePickerFields) {
                        if ("mDayPicker" == datePickerField.name || "mDaySpinner" == datePickerField.name) {
                            datePickerField.isAccessible = true
                            var dayPicker = Any()
                            dayPicker = datePickerField.get(datePicker)
                            (dayPicker as View).visibility = View.GONE
                        }
                    }
                }
            }
        } catch (ex: Exception) {
        }
        return dpd
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
        val mcurrentTime = Calendar.getInstance()
        val hour = mcurrentTime[Calendar.HOUR_OF_DAY]
        val minute = mcurrentTime[Calendar.MINUTE]
        val mTimePicker =
            TimePickerDialog(context, timePickerListener, hour, minute, true) //Yes 24 hour time
        mTimePicker.show()
    }

    private val timePickerListener =
        TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            calendar!![Calendar.HOUR] = selectedHour
            calendar!![Calendar.MINUTE] = selectedMinute
            @SuppressLint("SimpleDateFormat")
            val sdf = SimpleDateFormat(timeFormat)
            text = sdf.format(calendar!!.time)
            listener?.onListener()
        }

    interface NsDateViewListener {
        fun onListener()
    }
}