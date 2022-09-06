package com.newsoft.nscustom.view.recyclerview

import android.content.res.Resources
import android.graphics.Outline
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi

object AdapterUtils {
    /**
     * Nhận vị trí thực sự
     *
     * @param isIncrease Là kết thúc đầu tiên có sự gia tăng?
     * @param position  vị trí hiện tại
     * @param realCount Số lượng thực
     * @return
     */
    fun getRealPosition(isIncrease: Boolean, position: Int, realCount: Int): Int {
        if (!isIncrease) {
            return position
        }
        val realPosition: Int
        realPosition = if (position == 0) {
            realCount - 1
        } else if (position == realCount + 1) {
            0
        } else {
            position - 1
        }
        return realPosition
    }

    /**
     * Xoay tệp bố cục đến chế độ xem, ở đây cao và rộng trong ViewPager2, Cao và rộng phải match_parent
     *
     * @param parent
     * @param layoutId
     * @return
     */
    fun getView(parent: ViewGroup, @LayoutRes layoutId: Int): View {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        val params = view.layoutParams
        //Nó được đánh giá ở đây để xác định xem chiều cao và băng thông rộng có là match_parent
        if (params.height != -1 || params.width != -1) {
            params.height = -1
            params.width = -1
            view.layoutParams = params
        }
        return view
    }

    fun dp2px(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            Resources.getSystem().displayMetrics
        )
    }

    /**
     * Đặt xem tròn
     *
     * @param radius
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun setBannerRound(view: View, radius: Float) {
        view.outlineProvider = object : ViewOutlineProvider() {
            override fun getOutline(view: View, outline: Outline) {
                outline.setRoundRect(0, 0, view.width, view.height, radius)
            }
        }
        view.clipToOutline = true
    }
}