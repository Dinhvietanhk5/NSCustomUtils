package com.newsoft.nscustomutils

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class IntentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent)

//        val title = getDataExtras<String>("title","")
//        tvTitle.text = title

//        tvTitle.setOnClickListener {
//            finishActivityForResultExt("intemnt" to 1)
//        }
    }
}