package com.kunotice.kunotice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import com.kunotice.kunotice.translate.Translater_papago

class PopupLanguageChangeMenu : Activity() {

    lateinit var languageCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.6f
        window.attributes = lpWindow

        setContentView(R.layout.activity_popup_language_change_menu)

        languageCode = intent.getStringExtra("languageCode")!!
        Log.i("code from intent", languageCode)

        val questionText: String = when (languageCode) {
            Translater_papago.korean -> "언어를 한국어로 변경하시겠습니까?"
            Translater_papago.english -> "Would you like to change the language to English?"
            Translater_papago.chinese -> "您想把语言变为中文吗?"
            Translater_papago.japanese -> "言語を日本語に変更しますか?"
            else -> "언어를 변경하시겠습니까?"
        }
        val confirmText: String = when (languageCode) {
            Translater_papago.korean -> "확인"
            Translater_papago.english -> "Confirm"
            Translater_papago.chinese -> "确认"
            Translater_papago.japanese -> "確認"
            else -> "확인"
        }
        val cancelText: String = when (languageCode) {
            Translater_papago.korean -> "취소"
            Translater_papago.english -> "Cancel"
            Translater_papago.chinese -> "取消"
            Translater_papago.japanese -> "キャンセル"
            else -> "취소"
        }

        findViewById<TextView>(R.id.question).text = questionText
        findViewById<TextView>(R.id.confirmBtn).text = confirmText
        findViewById<TextView>(R.id.cancleBtn).text = cancelText
    }

    // activity_popup_language_change_menu 레이아웃 외의 부분 클릭시 닫히지 않게 설정
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            if (event.action == MotionEvent.ACTION_OUTSIDE) {
                return false
            }
        }
        return true
    }

    // 안드로이드 Back 버튼 막아놓음 -> 화면의 버튼을 이용해서만 닫을 수 있도록 함
    override fun onBackPressed() {
        return
    }

    fun onConfirmBtnClicked(view: View) {
        val intent: Intent = Intent();
        intent.putExtra("languageCode", languageCode)
        intent.putExtra("result", true);
        setResult(RESULT_OK, intent);
        finish()
    }

    fun onCancleBtnClicked(view: View) {
        val intent: Intent = Intent();
        intent.putExtra("languageCode", languageCode)
        intent.putExtra("result", false);
        setResult(RESULT_OK, intent);
        finish()
    }

}