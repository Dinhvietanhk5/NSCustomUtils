package com.newsoft.nscustomutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.newsoft.nscustom.ext.context.finishActivityForResultExt
import com.newsoft.nscustom.ext.context.getDataExtras
import kotlinx.android.synthetic.main.activity_intent.*

class IntentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

//        val title = getDataExtras<String>("title","")
//        tvTitle.text = title

        tvTitle.setOnClickListener {
            finishActivityForResultExt("intemnt" to 1)
        }
    }
}