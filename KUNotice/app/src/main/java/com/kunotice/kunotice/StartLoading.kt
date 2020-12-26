package com.kunotice.kunotice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.LinearLayout
import com.kunotice.kunotice.crawling.PopupNoticeCrawler
import com.kunotice.kunotice.translate.Translater_papago

class StartLoading : Activity() {

    lateinit var popupNoticeString: String
    lateinit var languageCode: String
    var threadFinished: Boolean = false
    var delayFinished: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_loading)

        languageCode = intent.getStringExtra("languageCode")!!

        val popupNoticeThread: PopupNoticeThread = PopupNoticeThread()
        popupNoticeThread.start()

        val id: Int = when (languageCode) {
            Translater_papago.korean -> R.drawable.start_loading_korean
            Translater_papago.english -> R.drawable.start_loading_english
            Translater_papago.chinese -> R.drawable.start_loading_chinese
            Translater_papago.japanese -> R.drawable.start_loading_japanese
            else -> R.drawable.start_loading_korean
        }
        findViewById<LinearLayout>(R.id.mainBackground).setBackgroundResource(id)

        startLoading()
    }

    private fun startLoading() {
        val handler: Handler = Handler()
        handler.postDelayed({
            if (threadFinished) {
                val intent: Intent = Intent()
                intent.putExtra("popupNoticeString", popupNoticeString)
                setResult(RESULT_OK, intent);
                Log.i("finish","in delay function")
                finish()
            } else {
                delayFinished = true
            }
        }, 2000)
    }

    inner class PopupNoticeThread : Thread() {
        override fun run() {
            val crawler: PopupNoticeCrawler = PopupNoticeCrawler(languageCode)
            popupNoticeString = crawler.crawling()
//            popupNoticeString.replace("\\n", "\n")
            threadFinished = true

            if (delayFinished) {
                val intent: Intent = Intent()
                intent.putExtra("popupNoticeString", popupNoticeString)
                setResult(RESULT_OK, intent);
                Log.i("finish","in thread function")
                finish()
            }
        }
    }
}
