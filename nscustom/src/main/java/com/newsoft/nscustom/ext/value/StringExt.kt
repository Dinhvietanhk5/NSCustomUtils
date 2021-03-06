package com.newsoft.nscustom.ext.value

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Base64
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.transition.TransitionManager
import com.newsoft.nscustom.R
import org.joda.time.DateTimeZone
import org.joda.time.format.ISODateTimeFormat
import java.net.URLEncoder
import java.nio.charset.Charset
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Sanitize
 */
fun String.sanitize(): String =
    this.replace(" {2}".toRegex(), " ")
        .replace("\n\n\n+".toRegex(), "\n\n")
        .trim()

/**
 * Phone number sanitation
 */
fun String.sanitizePhoneNumber(): String =
    this.sanitize()
        .replace("-", "")
        .replace("(", "")
        .replace(")", "")
        .replace(" ", "")
        .trim()


/**
 * Prefix a url with "http" if it isn't yet prefixed
 */
fun String.precedeLinkWithHttp() = if (!this.startsWith("http")) "http://$this" else this

/**
 * Prefix a url with "https" if it isn't yet prefixed
 */
fun String.precedeLinkWithHttps() = if (!this.startsWith("http")) "https://$this" else this

/**
 * Checks if starts with a vowel
 */
fun String.startsWithVowel(): Boolean = "AEIOUaeiou".indexOf(this.first()) != -1

/**
 * Check if valid name
 */
fun String.isValidName(): Boolean =
    Pattern.compile("^[a-zA-Z]+(([',. -][a-zA-Z])[a-zA-Z',.-]*)*\$").matcher(this).matches()

/**
 * Check if not valid name
 */
fun String.isNotValidName(): Boolean = !isValidName()

/**
 * Check if valid email
 */
fun String.isValidEmail(): Boolean =
    matches("^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$".toRegex())

/**
 * Check if not valid email
 */
fun String.isNotValidEmail(): Boolean = !isValidEmail()

/**
 * Check if valid phone number
 */
fun String.isValidPhoneNumber(): Boolean = Patterns.PHONE.matcher(this).matches()

/**
 * Check if not valid phone number
 */
fun String.isNotValidPhoneNumber(): Boolean = !isValidPhoneNumber()

/**
 * Check if valid IP address
 */
fun String.isValidIpAddress(): Boolean {
    return if (this.sanitize().isNotEmpty()) Patterns.IP_ADDRESS.matcher(this.sanitize())
        .matches() else false
}

/**
 * Check if contains letters
 */
fun String.hasLetters(): Boolean = contains("[A-Za-z0-9]+".toRegex())

/**
 * Check if doesn't contain letters
 */
fun String.hasNoLetters(): Boolean = !hasLetters()

/**
 * Check if contains digits
 */
fun String.hasDigits(): Boolean = matches(".*\\d+.*".toRegex())

/**
 * Check if doesn't contain digits
 */
fun String.hasNoDigits(): Boolean = !hasDigits()

/**
 * Remove digit characters in string
 */
fun String.removeDigits(): String = replace("\\d".toRegex(), "")

/**
 * Replace spaces to underscore and to lowercase
 */
fun String.toUniqueName(): String = this.toLowerCase().replace(" ", "_")

/**
 * Add commas to currency figure
 */
fun String.addCurrencyComma(locale: Locale): String {
    return try {
        val numberFormat = NumberFormat.getNumberInstance(locale)
        numberFormat.format(this.toDouble())
    } catch (e: Exception) {
        this
    }
}

/**
 * URL Encode for URL queries etc.
 */
fun String.urlEncode(): String = URLEncoder.encode(this, "utf-8")

/**
 * Convert to Editable for Edit text utility
 */
fun String.toEditable() = SpannableStringBuilder(this)


/**
 * Convert date string [fromFormat] to [toFormat] date string
 */
@SuppressLint("SimpleDateFormat")
fun String.dateStringToFormattedDate(
    fromFormat: String = "yyyy-MM-dd",
    toFormat: String = "MMMM dd, yyyy"
): String {
    return try {
        val dateFormat = SimpleDateFormat(fromFormat)
        val date = dateFormat.parse(this)

        SimpleDateFormat(toFormat).format(date)
    } catch (e: Exception) {
        this
    }
}

/**
 * Convert date string to millis
 */
@SuppressLint("SimpleDateFormat", "NewApi")
fun String.dateToMillis(format: String = "yyyy-MM-dd'T'hh:mm:ssXXX"): Long {
    return try {
        val dateFormat = SimpleDateFormat(format)
        dateFormat.parse(this)?.time ?: Date().time
    } catch (e: Exception) {
        0.toLong()
    }
}


/**
 * Convert date string to date object
 */
@SuppressLint("SimpleDateFormat", "NewApi")
fun String.dateToObject(format: String = "yyyy-MM-dd'T'HH:mm:ssXXX"): Date {
    return try {
        SimpleDateFormat(format).parse(this) ?: Date()
    } catch (e: Exception) {
        Date()
    }
}


@SuppressLint("SimpleDateFormat")
fun String.IsoDateToObject(): Date {
    return try {
        val dateFormat = ISODateTimeFormat.dateTime().withZone(DateTimeZone.UTC)
        dateFormat.parseDateTime(this).toDate()
    } catch (e: Exception) {
        Date()
    }
}

fun formatString(number1: String): String? {
    return try {
        if (number1.length >= 4) {
            val number = number1.toDouble()
            if (number < 0 && number > -1000 || number > 0 && number < 1000) {
                return number.toString()
            }
            val formatter: NumberFormat = DecimalFormat("###,###")
            var resp = formatter.format(number)
            resp = resp.replace(",".toRegex(), ".")
            resp
        } else number1
    } catch (e: java.lang.Exception) {
        ""
    }
}

fun String.base64Decode(): String {
    return try {
        String(
            Base64.decode(this, Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING),
            Charset.defaultCharset()
        )
    } catch (e: IllegalArgumentException) {
        ""
    }
}

/**
 * More text
 */

var checkText = true

fun Context.setTextShowHide(view: ViewGroup, tvConten: TextView, lines: Int, item: String) {

    var lineCount = 0

    tvConten.post {
        lineCount = tvConten.lineCount
        if (lineCount > lines) {
            val lastCharShown: Int = tvConten.layout.getLineVisibleEnd(lines - 1)
            tvConten.setLines(lines)
            val moreString = " xem th??m >>"
            val suffix = "  $moreString"
            val actionDisplayText =
                Html.fromHtml(
                    item.substring(
                        0,
                        lastCharShown - suffix.length - 20
                    ) + "..." + suffix
                ).toString()

            val truncatedSpannableString = SpannableString(actionDisplayText)
            val startIndex = actionDisplayText.indexOf(moreString)
            // setOnClick moreString
            truncatedSpannableString.setSpan(object : ClickableSpan() {
                override fun onClick(textView: View) {
                    textView.invalidate()
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                    tvConten.invalidate()
                }
            }, startIndex, startIndex + moreString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            //set color moreString
            truncatedSpannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(this, R.color.black)),
                startIndex,
                startIndex + moreString.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // set background moreString onClick
            tvConten.highlightColor = ContextCompat.getColor(this, R.color.gray_text_color)
            tvConten.setText(truncatedSpannableString, TextView.BufferType.SPANNABLE)
            tvConten.movementMethod = LinkMovementMethod.getInstance()
        } else {
            tvConten.setLines(lineCount)
        }
//        if (tvConten.getLineCount() > 3) {
//            textView6.setLines(3)
//        } else textView6.setLines(tvConten.getLineCount())

        tvConten.setOnClickListener { v: View? ->
            if (checkText) {
                tvConten.text = Html.fromHtml(item)
                tvConten.post {
                    tvConten.setLines(tvConten.lineCount)
                    TransitionManager.beginDelayedTransition(view)
                }
            } else {
                setTextShowHide(
                    view,
                    tvConten,
                    lines,
                    item
                )
                TransitionManager.beginDelayedTransition(view)
            }
            checkText = !checkText
        }

        tvConten.visibility = View.VISIBLE
    }
}

fun formatColor(context: Context, color: Int): String {
    return String.format(
        "#%06X",
        0xFFFFFF and ContextCompat.getColor(context, color)
    )
}

/**
 * Copy text
 */

fun Context.copyToClipboard(text: CharSequence) {
    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("label", text)
    clipboard.setPrimaryClip(clip)
}

/**
 * capitalize first letter
 */

fun capitalize(str: String): String? {
    if (TextUtils.isEmpty(str))
        return str
    val arr = str.toCharArray()
    var capitalizeNext = true
    var phrase: String? = ""
    for (c in arr) {
        if (capitalizeNext && Character.isLetter(c)) {
            phrase += Character.toUpperCase(c)
            capitalizeNext = false
            continue
        } else if (Character.isWhitespace(c)) {
            capitalizeNext = true
        }
        phrase += c
    }
    return phrase
}