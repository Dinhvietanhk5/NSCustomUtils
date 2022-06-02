package com.newsoft.nscustom.constants

interface Defaults {
    companion object {
        const val DATE_FORMAT = "dd/MM/yyyy"
        const val DATE_FORMAT2 = "dd-MM-yyyy"
        const val DATE_FORMAT_SIMPLE = "MMM d, yyyy"
        const val DATE_FORMAT_COMPLETE = "EEEE, MMM d, yyyy"
        const val DATE_TIME_FORMAT_COMPLETE = "EEE, MMM d, yyyy • h:mm a"
        const val TIME_FORMAT_12 = "h:mm a"
        const val DATE_TIME_SERVER_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX"

        const val TOKEN_FAILURE_MSG = "Phiên đăng nhập hết hạn"
        const val ERROR_API = "Có lỗi trong quá trình kết nối!"
    }
}
