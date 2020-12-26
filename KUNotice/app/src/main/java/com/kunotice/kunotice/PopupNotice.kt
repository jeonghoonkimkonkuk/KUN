package com.kunotice.kunotice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_popup_notice.*

class PopupNotice : Activity() {

    lateinit var languageCode: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.6f
        window.attributes = lpWindow

        setContentView(R.layout.activity_popup_notice)

        languageCode = intent.getStringExtra("languageCode")!!
        popupNotice.text = intent.getStringExtra("popupNotice")!!.replace("\\n", "\n")
//        // 언어 설정이 한국어인 경우
//        if (languageCode.equals(Translater_papago.korean)) {
//            // 디폴트가 한국어이기 때문에 아무런 작업도 수행하지 않아도 된다
//        }
//        // 언어 설정이 영어인 경우
//        else if (languageCode.equals(Translater_papago.english)) {
//            findViewById<TextView>(R.id.popupNotice0).text = "Notice"
//            findViewById<TextView>(R.id.popupNotice1).text =
//                "Currently, there is an unknown error in the homepage of the department for the next five departments, which can not receive the announcement information."
//            findViewById<TextView>(R.id.popupNotice2).text =
//                "KU Convergence Science and Technology Institute of Smart Operational Engineering\n" +
//                        "Department of Biotechnology at KU Convergence Science and Technology\n" +
//                        "Department of Architecture, College of Architecture\n" +
//                        "Department of Environmental Health Sciences, Sanghe University of Life Sciences\n" +
//                        "Department of Industrial Design, University of Arts and Design"
//            findViewById<TextView>(R.id.popupNotice3).text =
//                "\nAnd the department of Sanghe Life Science University does not provide the function in the curriculum security because the department information is not listed on the homepage of Konkuk University."
//            findViewById<TextView>(R.id.popupNotice4).text =
//                "\nIn addition, the Department of Communication Design at the College of Arts and Design and the Department of Life Sciences at the Sanghe Life Sciences University do not support all functions due to the problems of the homepage."
//            findViewById<TextView>(R.id.popupNotice5).text =
//                "\nI would like to ask those who use KUN to understand and I will do my best to normalize the functions in the announcement and the school security.\nThank you."
//            findViewById<TextView>(R.id.closeBtn1).text = "Never See Again 24 Hours"
//            findViewById<TextView>(R.id.closeBtn2).text = "Close"
//        }
//        // 언어 설정이 중국어인 경우
//        else if (languageCode.equals(Translater_papago.chinese)) {
//            findViewById<TextView>(R.id.popupNotice0).text = "通知"
//            findViewById<TextView>(R.id.popupNotice1).text =
//                "目前,由于部门主页的未知错误,下一个五个部门无法收到通知信息。"
//            findViewById<TextView>(R.id.popupNotice2).text = "KU融合科学技术研究所智能操作系统工程系\n" +
//                    "KU融合科学技术研究所生物技术系\n" +
//                    "建筑学院建筑系\n" +
//                    "上海生物科学大学环境卫生科学系\n" +
//                    "艺术设计大学工业设计系"
//            findViewById<TextView>(R.id.popupNotice3).text =
//                "\n而且,由于康库克大学主页上没有列出部门信息,所以没有提供部门信息指导功能。"
//            findViewById<TextView>(R.id.popupNotice4).text =
//                "\n此外,艺术设计大学通信设计系和商业生命科学大学生命科学系由于主页问题而没有支持所有功能。"
//            findViewById<TextView>(R.id.popupNotice5).text =
//                "\n希望大家多多谅解,并为使公告事项和部门信息指南功能正常化而尽最大努力。\n" +
//                        "谢谢"
//            findViewById<TextView>(R.id.closeBtn1).text = "24小时内不再看"
//            findViewById<TextView>(R.id.closeBtn2).text = "关闭"
//        }
//        // 언어 설정이 일본어인 경우
//        else if (languageCode.equals(Translater_papago.japanese)) {
//            findViewById<TextView>(R.id.popupNotice0).text = "お知らせ"
//            findViewById<TextView>(R.id.popupNotice1).text =
//                "現在、次の5つの学科に対して学科のホームページの未知のエラーでお知らせ情報を受け取ることができない現象が発生しています。"
//            findViewById<TextView>(R.id.popupNotice2).text =
//                "KU融合科学技術院スマートオペレーティングシステム工学\n" +
//                        "KU融合科学技術院のバイオテクノロジー\n" +
//                        "建築大学建築学部\n" +
//                        "サンホ生命科学大学環境保健科学\n" +
//                        "芸術デザイン大学産業デザイン学科"
//            findViewById<TextView>(R.id.popupNotice3).text =
//                "\nそしてサンホ生命科学大学所属学科について建国大学のホームページに学科情報が記載されていない理由で学科情報案内機能が提供されません。"
//            findViewById<TextView>(R.id.popupNotice4).text =
//                "\nまた、芸術デザイン大学コミュニケーションデザイン学科とサンホ生命科学大学生命科学特性学科は、とホームページの問題で、すべての機能がサポートされません。"
//            findViewById<TextView>(R.id.popupNotice5).text =
//                "\nクン(KUN)を利用してくださる方々にご了承をお願いしますし、お知らせと学科情報案内機能の正常化のために最大限努力します。\n" +
//                        "ありがとう"
//            findViewById<TextView>(R.id.closeBtn1).text = "24時間の間もう一度見ません"
//            findViewById<TextView>(R.id.closeBtn2).text = "閉め"
//        }
//        // 그 외의 경우 -> 이 경우에는 오류가 된다.
//        else {
//            Log.e("languageCode error", " in PopupNotice : " + languageCode)
//        }
    }

    // activity_popup_notice 레이아웃 외의 부분 클릭시 닫히지 않게 설정
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

    // 24시간동안다시보지않기 버튼 클릭시 호출됨
    fun onCloseBtn1Clicked(view: View) {
        val intent: Intent = Intent();
        intent.putExtra("result", "24hours");
        setResult(RESULT_OK, intent);
        finish()
    }

    // 닫기 버튼 클릭시 호출됨
    fun onCloseBtn2Clicked(view: View) {
        val intent: Intent = Intent();
        intent.putExtra("result", "close");
        setResult(RESULT_OK, intent);
        finish()
    }
}