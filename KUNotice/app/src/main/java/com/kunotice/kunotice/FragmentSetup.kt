package com.kunotice.kunotice

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.kunotice.kunotice.translate.Translater_papago
import kotlinx.android.synthetic.main.fragment_setup.*

class FragmentSetup(var languageCode: String) : Fragment() {

    interface OnFragmentInteraction {
        fun setLanguage(languageCode: String)
    }

    lateinit var rootView: ViewGroup

    var fragmentListener: OnFragmentInteraction? = null

    val setupBtn: ArrayList<Button> = ArrayList<Button>()
    val setupBtnClicked: MutableList<Boolean> = mutableListOf(false, false, false)
    val setupMenu: ArrayList<TextView> = ArrayList<TextView>()
    val menuContent: ArrayList<LinearLayout> = ArrayList<LinearLayout>()

    val languageCheckBox: ArrayList<CheckBox> = ArrayList<CheckBox>()
    val translatedLanguage: ArrayList<TextView> = ArrayList<TextView>()

    lateinit var menuContent1_1: TextView

    lateinit var developerInfo: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            inflater.inflate(R.layout.fragment_setup, container, false) as ViewGroup
        menuContent1_1 = rootView.findViewById(R.id.menuContent1_1)

        init()

        developerInfo = rootView.findViewById(R.id.developerInfo1)
        developerInfo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val clipboardManager: ClipboardManager =
                    activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData: ClipData =
                    ClipData.newPlainText("email", "kimbsu3000@gmail.com")
                clipboardManager.setPrimaryClip(clipData)
                val msg: String = when (languageCode) {
                    Translater_papago.korean -> "복사되었습니다."
                    Translater_papago.english -> ".It is copied"
                    Translater_papago.chinese -> "已复制。"
                    Translater_papago.japanese -> "コピーされました."
                    else -> "복사되었습니다."
                }
                Toast.makeText(
                    context,
                    msg,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })

        MobileAds.initialize(context)
        lateinit var adLoader: AdLoader
        // ca-app-pub-3940256099942544/2247696110 -> 디버그용 광고 단위 ID
        adLoader = AdLoader.Builder(context, "ca-app-pub-4238407253412923/7956001823")
            .forUnifiedNativeAd(object : UnifiedNativeAd.OnUnifiedNativeAdLoadedListener {
                override fun onUnifiedNativeAdLoaded(unifiedNativeAd: UnifiedNativeAd?) {
//                    val styles: NativeTemplateStyle = NativeTemplateStyle.Builder().withMainBackgroundColor

                    val templateView: TemplateView = tpAdmob
//                    templateView.setStyles(styles)
                    templateView.setNativeAd(unifiedNativeAd)
                }
            })
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    // Handle the failure by logging, altering the UI, and so on.
                    Log.e("onAdFailedToLoad()", "Ad is not loaded.")
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    // Methods in the NativeAdOptions.Builder class can be
                    // used here to specify individual options settings.
                    .build()
            )
            .build()
        adLoader.loadAd(AdRequest.Builder().build())

        return rootView
    }

    fun init() {
        setupBtn.add(rootView.findViewById(R.id.setupBtn1))
        setupBtn.add(rootView.findViewById(R.id.setupBtn2))
        setupBtn.add(rootView.findViewById(R.id.setupBtn3))

        setupMenu.add(rootView.findViewById(R.id.setupMenu1))
        setupMenu.add(rootView.findViewById(R.id.setupMenu2))
        setupMenu.add(rootView.findViewById(R.id.setupMenu3))
        setupMenu.add(rootView.findViewById(R.id.setupMenu4))

        var index: Int = 0
        while (index < setupBtn.size) {
            setupBtn[index].setOnClickListener(SetupMenuClickListener())
            setupMenu[index].setOnClickListener(SetupMenuClickListener())
            index++
        }

        menuContent.add(rootView.findViewById(R.id.menuContent1))
        menuContent.add(rootView.findViewById(R.id.menuContent2))
        menuContent.add(rootView.findViewById(R.id.menuContent3))

        languageCheckBox.add(rootView.findViewById(R.id.korean))
        languageCheckBox.add(rootView.findViewById(R.id.english))
        languageCheckBox.add(rootView.findViewById(R.id.chinese))
        languageCheckBox.add(rootView.findViewById(R.id.japanese))

        index = 0
        while (index < languageCheckBox.size) {
            languageCheckBox[index].setOnCheckedChangeListener(CheckBoxClickListener())
            index++
        }

        translatedLanguage.add(rootView.findViewById(R.id.translated1))
        translatedLanguage.add(rootView.findViewById(R.id.translated2))
        translatedLanguage.add(rootView.findViewById(R.id.translated3))
        translatedLanguage.add(rootView.findViewById(R.id.translated4))

        when (languageCode) {
            Translater_papago.korean -> languageCheckBox[0].isChecked = true
            Translater_papago.english -> languageCheckBox[1].isChecked = true
            Translater_papago.chinese -> languageCheckBox[2].isChecked = true
            Translater_papago.japanese -> languageCheckBox[3].isChecked = true
        }

        if (!languageCode.equals(Translater_papago.korean))
            translate()
    }

    fun translate() {
        // 언어 설정이 한국어인 경우
        if (languageCode.equals(Translater_papago.korean)) {
            // 디폴트가 한국어이기 때문에 아무런 작업도 수행하지 않아도 된다
        }
        // 언어 설정이 영어인 경우
        else if (languageCode.equals(Translater_papago.english)) {
            setupMenu[0].text = "Notice"
            setupMenu[1].text = "Language Setting"
            setupMenu[2].text = "Developer Information"
            setupMenu[3].text = "Version Information"
//            rootView.findViewById<TextView>(R.id.menuContent1_1).text =
//                "Currently, there is an unknown error in the homepage of the department for the next five departments, which can not receive the announcement information."
//            rootView.findViewById<TextView>(R.id.menuContent1_2).text =
//                "KU Convergence Science and Technology Institute of Smart Operational Engineering\n" +
//                        "Department of Biotechnology at KU Convergence Science and Technology\n" +
//                        "Department of Architecture, College of Architecture\n" +
//                        "Department of Environmental Health Sciences, Sanghe University of Life Sciences\n" +
//                        "Department of Industrial Design, University of Arts and Design"
//            rootView.findViewById<TextView>(R.id.menuContent1_3).text =
//                "\nAnd the department of Sanghe Life Science University does not provide the function in the curriculum security because the department information is not listed on the homepage of Konkuk University."
//            rootView.findViewById<TextView>(R.id.menuContent1_4).text =
//                "\nIn addition, the Department of Communication Design at the College of Arts and Design and the Department of Life Sciences at the Sanghe Life Sciences University do not support all functions due to the problems of the homepage."
//            rootView.findViewById<TextView>(R.id.menuContent1_5).text =
//                "\nI would like to ask those who use KUN to understand and I will do my best to normalize the functions in the announcement and the school security.\nThank you."
            translatedLanguage[0].text = "Korean"
            translatedLanguage[1].text = "English"
            translatedLanguage[2].text = "Chinese"
            translatedLanguage[3].text = "Japanese"
            rootView.findViewById<TextView>(R.id.developerInfo1).text =
                "Name: Kim Byeong-su, Kim Jung-hoon\n" +
                        "e-mail: kimbsu3000@gmail.com"
            rootView.findViewById<TextView>(R.id.developerInfo1_copy).text =
                "(You can copy it by touching the email.)"
            rootView.findViewById<TextView>(R.id.developerInfo2).text =
                "I hope you find errors in the application, or I hope this part is uncomfortable and I hope it will improve! Or if it has this function!If you give your opinion by e-mail, I will try to accept it as much as possible.\n" +
                        "\n" +
                        "I will return to the next update as a better application."
        }
        // 언어 설정이 중국어인 경우
        else if (languageCode.equals(Translater_papago.chinese)) {
            setupMenu[0].text = "通知"
            setupMenu[1].text = "语言设置"
            setupMenu[2].text = "开发人员资料"
            setupMenu[3].text = "版本信息"
//            rootView.findViewById<TextView>(R.id.menuContent1_1).text =
//                "目前,由于部门主页的未知错误,下一个五个部门无法收到通知信息。"
//            rootView.findViewById<TextView>(R.id.menuContent1_2).text = "KU融合科学技术研究所智能操作系统工程系\n" +
//                    "KU融合科学技术研究所生物技术系\n" +
//                    "建筑学院建筑系\n" +
//                    "上海生物科学大学环境卫生科学系\n" +
//                    "艺术设计大学工业设计系"
//            rootView.findViewById<TextView>(R.id.menuContent1_3).text =
//                "\n而且,由于康库克大学主页上没有列出部门信息,所以没有提供部门信息指导功能。"
//            rootView.findViewById<TextView>(R.id.menuContent1_4).text =
//                "\n此外,艺术设计大学通信设计系和商业生命科学大学生命科学系由于主页问题而没有支持所有功能。"
//            rootView.findViewById<TextView>(R.id.menuContent1_5).text =
//                "\n希望大家多多谅解,并为使公告事项和部门信息指南功能正常化而尽最大努力。\n" +
//                        "谢谢"
            translatedLanguage[0].text = "韩国语"
            translatedLanguage[1].text = "英文"
            translatedLanguage[2].text = "中文"
            translatedLanguage[3].text = "日文"
            rootView.findViewById<TextView>(R.id.developerInfo1).text =
                "姓名: Kim Byeong-su, Kim Jung-hoon\n" +
                        "e-mail: kimbsu3000@gmail.com"
            rootView.findViewById<TextView>(R.id.developerInfo1_copy).text = "(触摸电子邮件可以复制。)"
            rootView.findViewById<TextView>(R.id.developerInfo2).text =
                "希望在使用应用程序时发现错误,或者因为这些部分不方便而有所改善!或者有这样的功能!您的客人会尽量接受电子邮件的意见。\n" +
                        "\n" +
                        "下次更新将是一个更好的应用程序。"
        }
        // 언어 설정이 일본어인 경우
        else if (languageCode.equals(Translater_papago.japanese)) {
            setupMenu[0].text = "お知らせ"
            setupMenu[1].text = "言語設定"
            setupMenu[2].text = "開発者情報"
            setupMenu[3].text = "バージョン情報"
//            rootView.findViewById<TextView>(R.id.menuContent1_1).text =
//                "現在、次の5つの学科に対して学科のホームページの未知のエラーでお知らせ情報を受け取ることができない現象が発生しています。"
//            rootView.findViewById<TextView>(R.id.menuContent1_2).text =
//                "KU融合科学技術院スマートオペレーティングシステム工学\n" +
//                        "KU融合科学技術院のバイオテクノロジー\n" +
//                        "建築大学建築学部\n" +
//                        "サンホ生命科学大学環境保健科学\n" +
//                        "芸術デザイン大学産業デザイン学科"
//            rootView.findViewById<TextView>(R.id.menuContent1_3).text =
//                "\nそしてサンホ生命科学大学所属学科について建国大学のホームページに学科情報が記載されていない理由で学科情報案内機能が提供されません。"
//            rootView.findViewById<TextView>(R.id.menuContent1_4).text =
//                "\nまた、芸術デザイン大学コミュニケーションデザイン学科とサンホ生命科学大学生命科学特性学科は、とホームページの問題で、すべての機能がサポートされません。"
//            rootView.findViewById<TextView>(R.id.menuContent1_5).text =
//                "\nクン(KUN)を利用してくださる方々にご了承をお願いしますし、お知らせと学科情報案内機能の正常化のために最大限努力します。\n" +
//                        "ありがとう"
            translatedLanguage[0].text = "韓国語"
            translatedLanguage[1].text = "英語"
            translatedLanguage[2].text = "中国語"
            translatedLanguage[3].text = "日本語"
            rootView.findViewById<TextView>(R.id.developerInfo1).text =
                "名前: Kim Byeong-su, Kim Jung-hoon\n" +
                        "e-mail: kimbsu3000@gmail.com"
            rootView.findViewById<TextView>(R.id.developerInfo1_copy).text = "(電子メールに触れるとコピーできます)"
            rootView.findViewById<TextView>(R.id.developerInfo2).text =
                "アプリケーションの使用中にエラーを発見したり、このような部分が不便で改善して欲しい!または、このような機能があれば、より良いだろう!ご覧の方は、電子メールで意見提示いただければ、可能な限り受け入れるように努力します。\n" +
                        "\n" +
                        "次の更新では、より良いアプリケーションになって帰ってきます。"
        }
        // 그 외의 경우 -> 이 경우에는 오류가 된다.
        else {
            Log.e("languageCode error", " in FragmentSeup : " + languageCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 200) {
            if (data != null) {
                val index: Int = when (data.getStringExtra("languageCode")) {
                    Translater_papago.korean -> 0
                    Translater_papago.english -> 1
                    Translater_papago.chinese -> 2
                    Translater_papago.japanese -> 3
                    else -> -1
                }
                if (data.getBooleanExtra("result", false)) {
                    languageCode = data.getStringExtra("languageCode")
                    var i: Int = 0
                    while (i < languageCheckBox.size) {
                        if (i == index)
                            languageCheckBox[i].isChecked = true
                        else
                            languageCheckBox[i].isChecked = false
                        i++
                    }
                    /*
                     여기에 나머지 Fragment와 Activity에 대하여 번역 작업을 수행하도록 하는 코드 작성하면 된다.
                     MainActivity와 통신해서 MainActivity에서 한꺼번에 번역 작업 수행하면 될 듯.
                    */
                    fragmentListener?.setLanguage(languageCode)

                } else {
                    languageCheckBox[index].isChecked = false
                }
            }
        }

    }

    fun setOnFragmentInteraction(fragmentListener: OnFragmentInteraction) {
        this.fragmentListener = fragmentListener
    }

    inner class SetupMenuClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            var index: Int? = null
            if (v != null) {
                index = when (v.id) {
                    R.id.setupBtn1 -> 0
                    R.id.setupBtn2 -> 1
                    R.id.setupBtn3 -> 2
                    R.id.setupMenu1 -> 0
                    R.id.setupMenu2 -> 1
                    R.id.setupMenu3 -> 2
                    else -> -1
                }
            }
            if (index != null && index != -1) {
                if (index == 1) {
                    val sorryText: String = when (languageCode) {
                        Translater_papago.korean -> "어플리케이션 사정상 번역 기능을 일시적으로 제공 중단합니다.\n최대한 빠른 시일내에 복구하도록 노력하겠습니다."
                        Translater_papago.english -> "Due to the application situation, the translation function is temporarily suspended. We ask for your understanding and will try to restore it as soon as possible.\nThank you."
                        Translater_papago.chinese -> "因应用软件原因暂时中断翻译功能。 请各位用户谅解，我们将尽最大努力尽快恢复。\n谢谢。"
                        Translater_papago.japanese -> "アプリケーションの事情により、翻訳機能を一時的に提供停止します。 使用者の皆様にご了承ください、できるだけ早いうちに復旧するよう努力します。\nありがとうございます。"
                        else -> "어플리케이션 사정상 번역 기능을 일시적으로 제공 중단합니다. 사용자 여러분들의 양해를 부탁드리며, 최대한 빠른 시일내에 복구하도록 노력하겠습니다.\n" +
                                "감사합니다."
                    }
                    Toast.makeText(context, sorryText, Toast.LENGTH_LONG).show()
                } else if (setupBtnClicked[index]) {
                    setupBtn[index].setBackgroundResource(R.drawable.down_arrow)
                    menuContent[index].visibility = View.GONE
                } else {
                    setupBtn[index].setBackgroundResource(R.drawable.up_arrow)
                    menuContent[index].visibility = View.VISIBLE
                }
                setupBtnClicked[index] = !setupBtnClicked[index]
            }
        }
    }

    inner class CheckBoxClickListener : CompoundButton.OnCheckedChangeListener {
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (buttonView != null) {
                val code: String = when (buttonView.id) {
                    R.id.korean -> Translater_papago.korean
                    R.id.english -> Translater_papago.english
                    R.id.chinese -> Translater_papago.chinese
                    R.id.japanese -> Translater_papago.japanese
                    else -> ""
                }
                if (!(code.isBlank()) && !(code.equals(languageCode)) && isChecked) {
                    Log.i("onCheckedChanged", code + " " + isChecked.toString())
//                    val intent: Intent = Intent(context, PopupLanguageChangeMenu::class.java)
//                    intent.putExtra("languageCode", code)
//                    startActivityForResult(intent, 200)
                    val sorryText: String = when (code) {
                        Translater_papago.korean -> "어플리케이션 사정상 번역 기능을 일시적으로 제공 중단합니다. 사용자 여러분들의 양해를 부탁드리며, 최대한 빠른 시일내에 복구하도록 노력하겠습니다.\n감사합니다."
                        Translater_papago.english -> "Due to the application situation, the translation function is temporarily suspended. We ask for your understanding and will try to restore it as soon as possible.\nThank you."
                        Translater_papago.chinese -> "因应用软件原因暂时中断翻译功能。 请各位用户谅解，我们将尽最大努力尽快恢复。\n谢谢。"
                        Translater_papago.japanese -> "アプリケーションの事情により、翻訳機能を一時的に提供停止します。 使用者の皆様にご了承ください、できるだけ早いうちに復旧するよう努力します。\nありがとうございます。"
                        else -> "어플리케이션 사정상 번역 기능을 일시적으로 제공 중단합니다. 사용자 여러분들의 양해를 부탁드리며, 최대한 빠른 시일내에 복구하도록 노력하겠습니다.\n" +
                                "감사합니다."
                    }
                    Toast.makeText(context, sorryText, Toast.LENGTH_LONG).show()
                } else if ((code.equals(languageCode)) && !isChecked) {
                    buttonView.isChecked = true
                }
            }
        }
    }

}