package com.kunotice.kunotice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kunotice.kunotice.data.Majors
import com.kunotice.kunotice.db.MyDBHelper
import com.kunotice.kunotice.translate.Translater_papago
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {

    var languageCode: String = Translater_papago.korean

    private val fragmentManager: FragmentManager = supportFragmentManager
    private lateinit var fragmentSearch: FragmentSearch
    private lateinit var fragmentNotice: FragmentNotice
    private lateinit var fragmentBookmark: FragmentBookmark
    private lateinit var fragmentSetup: FragmentSetup

    val majorMap: HashMap<String, String> = HashMap<String, String>()
    val crawlerNum: HashMap<String, Int> = HashMap<String, Int>()

    val majors: Majors = Majors()

    var isBookmark: HashMap<String, Boolean> = HashMap<String, Boolean>()
    val bookmarkList: ArrayList<String> = ArrayList<String>()

    lateinit var myDBHelper: MyDBHelper

    val FINISH_INTERVAL_TIME: Long = 2000
    var backPressedTime: Long = 0

    lateinit var popupNoticeString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("device language", Locale.getDefault().language)

        myDBHelper = MyDBHelper(this)
        if (!(myDBHelper.getLanguageCode().isEmpty())) {
            languageCode = myDBHelper.getLanguageCode()[0]
        }
        /*
        DB에 저장된 언어 정보가 없는 경우 -> 앱을 설치하고 처음 실행하는 경우에 해당함
        디바이스에 설정되어 있는 언어를 앱 언어로 설정함
        한국어, 영어, 중국어, 일본어를 제외한 언어에 대하여는 언어를 영어로 설정함
        2020-10-13 작성
        파파고 번역 API의 유료화에 따라서 기본 언어설정을 한국어로 모두 수정함
        */
        else {
            val deviceLanguage: String = Locale.getDefault().language
//            languageCode = when (deviceLanguage) {
//                "ko" -> Translater_papago.korean
//                "en" -> Translater_papago.english
//                "zh" -> Translater_papago.chinese
//                "ja" -> Translater_papago.japanese
//                else -> Translater_papago.english
//            }
            myDBHelper.insertLanguageCode(languageCode)
        }
        languageCode = Translater_papago.korean

        if (intent.hasExtra("restart")) {
            val msg: String = when (languageCode) {
                Translater_papago.korean -> "언어가 한국어로 변경되었습니다."
                Translater_papago.english -> "The language has been changed to English."
                Translater_papago.chinese -> "语言变更为中文。"
                Translater_papago.japanese -> "言語は日本語に変更されました。"
                else -> "Error is occured."
            }
            Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
        }

        val intent: Intent = Intent(this, StartLoading::class.java)
        intent.putExtra("languageCode", languageCode)
        startActivityForResult(intent, 2)

        init()
    }

    override fun onBackPressed() {
        val tempTime: Long = System.currentTimeMillis()
        val intervalTime = tempTime - backPressedTime

        if (0 <= intervalTime && intervalTime <= FINISH_INTERVAL_TIME) {
            super.onBackPressed()
        } else {
            backPressedTime = tempTime
            val msg: String = when (languageCode) {
                Translater_papago.korean -> "뒤로 가기를 한 번 더 누르면 종료됩니다."
                Translater_papago.english -> "Pressing Back again will end."
                Translater_papago.chinese -> "再按一下后关就结束了。"
                Translater_papago.japanese -> "「後ろに行く」をもう一度押すと終了します。"
                else -> "뒤로 가기를 한 번 더 누르면 종료됩니다."
            }
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val result: String = data?.getStringExtra("result")!!
            if (result.equals("24hours")) {
                val now: Long = System.currentTimeMillis()
                val date: Date = Date(now)
                val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val nowTime: String = sdf.format(date)
                Log.i("about DB", nowTime)

                myDBHelper.insertTime(nowTime)
            } else {
                Log.i("onActivityResult()", "close button clicked.")
            }
            // NaverMap 구현할 때 사용하는 임시 코드
//            val intent: Intent = Intent(this, PopupMapView::class.java)
//            startActivity(intent)
        } else if (requestCode == 2) {
            this.popupNoticeString = data?.getStringExtra("popupNoticeString")!!
            fragmentSetup.menuContent1_1.text = this.popupNoticeString.replace("\\n", "\n")

            // DB에 저장되어 있는 시간이 없는 경우에만 팝업 공지사항 출력
            if (myDBHelper.getTime().isEmpty()) {
                Log.i("about DB", "DB is empty.")
                val popupNotice: Intent = Intent(this, PopupNotice::class.java)
                popupNotice.putExtra("popupNotice", popupNoticeString)
                popupNotice.putExtra("languageCode", languageCode)
                startActivityForResult(popupNotice, 1)
            }
            // 그 외의 경우에는 24시간이 지난 경우에만 팝업 공지사항 출력
            else {
                Log.i("about DB", "DB is not empty.")
                val now: Long = System.currentTimeMillis()
                val date: Date = Date(now)
                val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
                val nowTime: String = sdf.format(date)
                Log.i("about DB", nowTime)
                val savedTime: String = myDBHelper.getTime()[0]

                if (checkTime(nowTime, savedTime)) {
                    myDBHelper.deleteTime()
                    Log.i("time DB size", myDBHelper.getTime().size.toString())
                    val popupNotice: Intent = Intent(this, PopupNotice::class.java)
                    popupNotice.putExtra("popupNotice", popupNoticeString)
                    popupNotice.putExtra("languageCode", languageCode)
                    startActivityForResult(popupNotice, 1)
                }
            }
        }
    }

    // 24시간이 지난 경우 -> true, 그렇지 않은 경우 -> false
    private fun checkTime(nowTime: String, savedTime: String): Boolean {
        val now: List<String> = nowTime.split(" ")
        val now1: List<String> = now[0].split("/")
        val now2: List<String> = now[1].split(":")

        val saved: List<String> = savedTime.split(" ")
        val saved1: List<String> = saved[0].split("/")
        val saved2: List<String> = saved[1].split(":")

        if (now1[0] != saved1[0]) return true       // 년도가 다른 경우
        if (now1[1] != saved1[1]) return true       // 월이 다른 경우
        if (now1[2].toInt() - saved1[2].toInt() > 1) return true        // 일수 차이가 1보다 큰 경우
        if (now1[2].toInt() - saved1[2].toInt() < 1) return false       // 일수 차이가 1보다 작은 경우
        if (now2[0] > saved2[0]) return true        // 날짜는 하루차이, 시간이 더 큰 경우
        if (now2[0] < saved2[0]) return false       // 날짜는 하루차이, 시간이 더 작은 경우
        if (now2[1] > saved2[1]) return true        // 날짜는 하루차이고 시간은 같고, 분이 더 큰 경우
        if (now2[1] < saved2[1]) return false       // 날짜는 하루차이고 시간은 같고, 분이 더 작은 경우
        if (now2[2] > saved2[2]) return true        // 날짜는 하루차이고 시간과 분은 같고, 초가 더 큰 경우
        if (now2[2] < saved2[2]) return false       // 날짜는 하루차이고 시간과 분은 같고, 초가 더 작은 경우
//        if (now2[1].toInt() - saved2[1].toInt() > 1)
//            return true
//        else
//            return false

        // 초 단위 시간이 정확히 하루차이 나는 경우
        return true
    }

    private fun init() {
        val menuId: Int = when (languageCode) {
            Translater_papago.korean -> R.menu.menu_bottomnavigationview
            Translater_papago.english -> R.menu.menu_bottomnavigationview_english
            Translater_papago.chinese -> R.menu.menu_bottomnavigationview_chinese
            Translater_papago.japanese -> R.menu.menu_bottomnavigationview_japanese
            else -> R.menu.menu_bottomnavigationview
        }
        findViewById<BottomNavigationView>(R.id.navigationView).inflateMenu(menuId)

        majorMap.put("건국대학교", "KU")
        majorMap.put("공과대학 사회환경공학부", "CIVILENV")
        majorMap.put("공과대학 기계공학부", "ME")
        majorMap.put("공과대학 전기전자공학부", "EE")
        majorMap.put("공과대학 화학공학부", "CHEMENG")
        majorMap.put("공과대학 컴퓨터공학부", "CSE")
        majorMap.put("공과대학 신산업융합학과", "AIF")
        majorMap.put("공과대학 K뷰티산업융합학과", "KBEAUTY")
        majorMap.put("공과대학 항공우주정비시스템학과", "AEROENG")
        majorMap.put("공과대학 생물공학과", "MICROBIO")
        majorMap.put("공과대학 산업공학과", "KIES")
        majorMap.put("공과대학 기술융합공학과", "TFE")
        majorMap.put("문과대학 국어국문학과", "KOREA")
        majorMap.put("문과대학 영어영문학과", "ENGLISH")
        majorMap.put("문과대학 중어중문학과", "CHINA")
        majorMap.put("문과대학 철학과", "PHILO")
        majorMap.put("문과대학 사학과", "KHISTORY")
        majorMap.put("문과대학 지리학과", "KUGEO")
        majorMap.put("문과대학 미디어커뮤니케이션학과", "COMM")
        majorMap.put("문과대학 문화콘텐츠학과", "CULTURECONTENTS")
        majorMap.put("이과대학 수학과", "MATH")
        majorMap.put("이과대학 물리학과", "PHYS")
        majorMap.put("이과대학 화학과", "CHEMI")
        majorMap.put("건축대학 건축학부", "CAKU")
        majorMap.put("사회과학대학 정치외교학과", "POL")
        majorMap.put("사회과학대학 경제학과", "ECON")
        majorMap.put("사회과학대학 행정학과", "KKUPA")
        majorMap.put("사회과학대학 국제무역학과", "ITRADE")
        majorMap.put("사회과학대학 응용통계학과", "STAT")
        majorMap.put("사회과학대학 융합인재학과", "DOLA")
        majorMap.put("사회과학대학 글로벌비즈니스학과", "DOIS")
        majorMap.put("경영대학 경영학과", "BIZ")
        majorMap.put("경영대학 기술경영학과", "MOT")
        majorMap.put("부동산과학원 부동산학과", "REALESTATE")
        majorMap.put("KU융합과학기술원 미래에너지공학과", "ENERGY")
        majorMap.put("KU융합과학기술원 스마트운행체공학과", "SMARTVEHICLE")
        majorMap.put("KU융합과학기술원 스마트ICT융합공학과", "SICTE")
        majorMap.put("KU융합과학기술원 화장품공학과", "COSMETICS")
        majorMap.put("KU융합과학기술원 줄기세포재생공학과", "SCRB")
        majorMap.put("KU융합과학기술원 의생명공학과", "BMSE")
        majorMap.put("KU융합과학기술원 시스템생명공학과", "KUSYSBT")
        majorMap.put("KU융합과학기술원 융합생명공학과", "IBB")
        majorMap.put("수의과대학", "VET")
//        majorMap.put("예술디자인대학 커뮤니케이션디자인학과", "KUCD")
        majorMap.put("예술디자인대학 산업디자인학과", "DESIGNID")
        majorMap.put("예술디자인대학 의상디자인학과", "APPAREL")
        majorMap.put("예술디자인대학 리빙디자인학과", "LIVINGDESIGN")
        majorMap.put("예술디자인대학 현대미술학과", "CONTEMPORARYART")
        majorMap.put("예술디자인대학 영상영화학과", "MOVINGIMAGES")
        majorMap.put("사범대학 일어교육과", "JAPAN")
        majorMap.put("사범대학 수학교육과", "MATHEDU")
        majorMap.put("사범대학 체육교육과", "KUPE")
        majorMap.put("사범대학 음악교육과", "MUSIC")
        majorMap.put("사범대학 교육공학과", "EDUTECH")
        majorMap.put("사범대학 영어교육과", "ENGLISHEDU")
        majorMap.put("사범대학 교직과", "GYOJIK")
//        majorMap.put("상허생명과학대학 생명과학특성학과", "BIOLOGY")
        majorMap.put("상허생명과학대학 동물자원과학과", "ANIS")
        majorMap.put("상허생명과학대학 식량자원과학과", "CROPSCIENCE")
        majorMap.put("상허생명과학대학 축산식품생명공학과", "FOODBIO")
        majorMap.put("상허생명과학대학 식품유통공학과", "KUFSM")
        majorMap.put("상허생명과학대학 환경보건과학과", "EHS")
        majorMap.put("상허생명과학대학 산림조경학과", "FLA")

        crawlerNum.put("건국대학교", 3)
        crawlerNum.put("공과대학 사회환경공학부", 2)
        crawlerNum.put("공과대학 기계공학부", 2)
        crawlerNum.put("공과대학 전기전자공학부", 1)
        crawlerNum.put("공과대학 화학공학부", 1)
        crawlerNum.put("공과대학 컴퓨터공학부", 1)
        crawlerNum.put("공과대학 신산업융합학과", 2)
        crawlerNum.put("공과대학 K뷰티산업융합학과", 2)
        crawlerNum.put("공과대학 항공우주정비시스템학과", 2)
        crawlerNum.put("공과대학 생물공학과", 2)
        crawlerNum.put("공과대학 산업공학과", 1)
        crawlerNum.put("공과대학 기술융합공학과", 1)
        crawlerNum.put("문과대학 국어국문학과", 1)
        crawlerNum.put("문과대학 영어영문학과", 1)
        crawlerNum.put("문과대학 중어중문학과", 2)
        crawlerNum.put("문과대학 철학과", 2)
        crawlerNum.put("문과대학 사학과", 2)
        crawlerNum.put("문과대학 지리학과", 2)
        crawlerNum.put("문과대학 미디어커뮤니케이션학과", 2)
        crawlerNum.put("문과대학 문화콘텐츠학과", 2)
        crawlerNum.put("이과대학 수학과", 2)
        crawlerNum.put("이과대학 물리학과", 2)
        crawlerNum.put("이과대학 화학과", 2)
        crawlerNum.put("건축대학 건축학부", 1)
        crawlerNum.put("사회과학대학 정치외교학과", 2)
        crawlerNum.put("사회과학대학 경제학과", 1)
        crawlerNum.put("사회과학대학 행정학과", 2)
        crawlerNum.put("사회과학대학 국제무역학과", 2)
        crawlerNum.put("사회과학대학 응용통계학과", 2)
        crawlerNum.put("사회과학대학 융합인재학과", 1)
        crawlerNum.put("사회과학대학 글로벌비즈니스학과", 2)
        crawlerNum.put("경영대학 경영학과", 1)
        crawlerNum.put("경영대학 기술경영학과", 2)
        crawlerNum.put("부동산과학원 부동산학과", 3)
        crawlerNum.put("KU융합과학기술원 미래에너지공학과", 1)
        crawlerNum.put("KU융합과학기술원 스마트운행체공학과", 1)
        crawlerNum.put("KU융합과학기술원 스마트ICT융합공학과", 1)
        crawlerNum.put("KU융합과학기술원 화장품공학과", 1)
        crawlerNum.put("KU융합과학기술원 줄기세포재생공학과", 2)
        crawlerNum.put("KU융합과학기술원 의생명공학과", 1)
        crawlerNum.put("KU융합과학기술원 시스템생명공학과", 2)
        crawlerNum.put("KU융합과학기술원 융합생명공학과", 2)
        crawlerNum.put("수의과대학", 1)
//        crawlerNum.put("예술디자인대학 커뮤니케이션디자인학과", )
        crawlerNum.put("예술디자인대학 산업디자인학과", 1)
        crawlerNum.put("예술디자인대학 산업디자인학과", 1)
        crawlerNum.put("예술디자인대학 의상디자인학과", 2)
        crawlerNum.put("예술디자인대학 리빙디자인학과", 2)
        crawlerNum.put("예술디자인대학 현대미술학과", 2)
        crawlerNum.put("예술디자인대학 영상영화학과", 1)
        crawlerNum.put("사범대학 일어교육과", 2)
        crawlerNum.put("사범대학 수학교육과", 2)
        crawlerNum.put("사범대학 체육교육과", 2)
        crawlerNum.put("사범대학 음악교육과", 2)
        crawlerNum.put("사범대학 교육공학과", 2)
        crawlerNum.put("사범대학 영어교육과", 2)
        crawlerNum.put("사범대학 교직과", 1)
        crawlerNum.put("상허생명과학대학 동물자원과학과", 1)
        crawlerNum.put("상허생명과학대학 식량자원과학과", 1)
        crawlerNum.put("상허생명과학대학 축산식품생명공학과", 1)
        crawlerNum.put("상허생명과학대학 식품유통공학과", 1)
        crawlerNum.put("상허생명과학대학 환경보건과학과", 1)
        crawlerNum.put("상허생명과학대학 산림조경학과", 1)

        // DB에 저장되어 있는 즐겨찾기 정보 초기 설정은 여기서 처리하면 됨
        val fromdb = myDBHelper.getAllRecord()
        for (str in majors.majorList_korean) {
            if (fromdb.contains(str)) {
                isBookmark.put(str, true)
                bookmarkList.add(str)
            } else
                isBookmark.put(str, false)
        }

        fragmentSearch = FragmentSearch(majors, majorMap, isBookmark, languageCode)
        fragmentNotice =
            FragmentNotice(
                majorMap,
                myDBHelper.getAllRecord(),
                crawlerNum,
                languageCode,
                fragmentSearch.otherLanguageToKorean
            )
        fragmentBookmark = FragmentBookmark(majors, bookmarkList, majorMap, languageCode)
        fragmentSetup = FragmentSetup(languageCode)

        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragmentSearch).commitAllowingStateLoss()
        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentNotice)
            .commitAllowingStateLoss()
        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentBookmark)
            .commitAllowingStateLoss()
        fragmentManager.beginTransaction().add(R.id.frameLayout, fragmentSetup)
            .commitAllowingStateLoss()
        fragmentManager.beginTransaction().hide(fragmentNotice).commitAllowingStateLoss()
        fragmentManager.beginTransaction().hide(fragmentBookmark).commitAllowingStateLoss()
        fragmentManager.beginTransaction().hide(fragmentSetup).commitAllowingStateLoss()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(ItemSelectedListener())

        fragmentNotice.setOnFragmentInteraction(FragmentNoticeInteraction())
        fragmentSearch.setOnFragmentInteraction(FragmentSearchInteraction())
        fragmentBookmark.setOnFragmentInteraction(FragmentBookmarkInteraction())
        fragmentSetup.setOnFragmentInteraction(FragmentSetupInteraction())
    }

    // 이 함수 내부에 DB에 저장되어 있는 majorName: String 을 ArrayList에 담아서 반환하는거 구현해주면 됨 - 김정훈 할 일 4
    fun getBookmarkFromDB(): ArrayList<String> {
        return myDBHelper.getAllRecord()
    }

    // 각 프래그먼트에 변경된 언어를 적용시키는 함수
    fun applyLanguage() {
        fragmentSearch.languageCode = this.languageCode
        fragmentNotice.languageCode = this.languageCode
        fragmentBookmark.languageCode = this.languageCode
        fragmentSetup.languageCode = this.languageCode

        myDBHelper.deleteLanguageCode()
        myDBHelper.insertLanguageCode(this.languageCode)

        val intent: Intent =
            baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)!!
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("restart", true)
        startActivity(intent)
    }

    inner class ItemSelectedListener : BottomNavigationView.OnNavigationItemSelectedListener {
        override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
//        TODO("Not yet implemented")
            val transaction: FragmentTransaction = fragmentManager.beginTransaction()

            when (menuItem.itemId) {
                R.id.searchItem -> changeFragment(1)
                R.id.noticeItem -> changeFragment(2)
                R.id.bookmarkItem -> changeFragment(3)
                R.id.setupItem -> changeFragment(4)
                else -> Log.e("MainActivity", "Error is Occured in ItemSelectedListener")
            }
            return true
        }

        private fun changeFragment(index: Int) {
            if (index == 1) {
                fragmentManager.beginTransaction().show(fragmentSearch).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentNotice).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentBookmark).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentSetup).commitAllowingStateLoss()
            } else if (index == 2) {
                fragmentManager.beginTransaction().hide(fragmentSearch).commitAllowingStateLoss()
                fragmentManager.beginTransaction().show(fragmentNotice).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentBookmark).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentSetup).commitAllowingStateLoss()

                if ((!fragmentNotice.crawlers.isEmpty()) || (!fragmentNotice.crawlers2.isEmpty()) || (!fragmentNotice.crawlers3.isEmpty())) {
                    if (fragmentNotice.notice.isEmpty()) {
                        fragmentNotice.startCrawling(1)
                    }
                }
            } else if (index == 3) {
                fragmentManager.beginTransaction().hide(fragmentSearch).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentNotice).commitAllowingStateLoss()
                fragmentManager.beginTransaction().show(fragmentBookmark).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentSetup).commitAllowingStateLoss()
            } else if (index == 4) {
                fragmentManager.beginTransaction().hide(fragmentSearch).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentNotice).commitAllowingStateLoss()
                fragmentManager.beginTransaction().hide(fragmentBookmark).commitAllowingStateLoss()
                fragmentManager.beginTransaction().show(fragmentSetup).commitAllowingStateLoss()
            }
        }

    }

    /*
    FragmentSearchInteraction 클래스에서 사용하는 majorName은 한국어로 된 학과 이름이다.
    한국어로 된 학과 이름을 넘겨주는 것을 확인함 (2020.06.24 12:29)
    */
    inner class FragmentSearchInteraction : FragmentSearch.OnFragmentInteraction {
        override fun bookmarkAdd(majorName: String, majorCode: String) {
            // 이부분에 DB에 majorName 저장하는거 구현하면 됨 - 김정훈 할 일 1
            // 시작
            myDBHelper.insertProduct(majorName)
            // 끝
            isBookmark.set(majorName, true)

            bookmarkList.add(majorName)

            fragmentBookmark.bookmarkChanged(majorName, 1)

            // FragmentNotice 시작
            fragmentNotice.addMajor(majorName)
            fragmentNotice.isBookmarkExist()
            // FragmentNotice 끝


            val msg: String = when (languageCode) {
                Translater_papago.korean -> "즐겨찾기로 등록되었습니다."
                Translater_papago.english -> "Registered as a favorite."
                Translater_papago.chinese -> "注册为最喜欢的。"
                Translater_papago.japanese -> "お気に入りとして登録されています。"
                else -> "즐겨찾기로 등록되었습니다."
            }
            Toast.makeText(
                this@MainActivity,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun bookmarkDelete(majorName: String, majorCode: String) {
            // 이부분에 DB에 majorName 삭제하는거 구현하면 됨 - 김정훈 할 일 2
            // 시작
            myDBHelper.deleteProduct(majorName)
            // 끝

            isBookmark.set(majorName, false)

            bookmarkList.remove(majorName)

            fragmentBookmark.bookmarkChanged(majorName, -1)

            // FragmentNotice 시작
            fragmentNotice.deleteMajor(majorName)
            fragmentNotice.isBookmarkExist()
            // FragmentNotice 끝

            val msg: String = when (languageCode) {
                Translater_papago.korean -> "즐겨찾기가 삭제되었습니다."
                Translater_papago.english -> "Favorites have been deleted."
                Translater_papago.chinese -> "您的最喜欢的搜索已被删除。"
                Translater_papago.japanese -> "お気に入りが削除されました。"
                else -> "즐겨찾기가 삭제되었습니다."
            }
            Toast.makeText(
                this@MainActivity,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /*
    FragmentBookmarkInteraction 클래스에서 사용하는 majorName은 한국어로 된 학과 이름이다.
    */
    inner class FragmentBookmarkInteraction : FragmentBookmark.OnFragmentInteraction {
        override fun bookmarkDelete(majorName: String, majorCode: String) {
            // 이부분에 DB에 majorName 삭제하는거 구현하면 됨 - 김정훈 할 일 3
            // 시작
            myDBHelper.deleteProduct(majorName)
            // 끝

            isBookmark.set(majorName, false)

            bookmarkList.remove(majorName)

            fragmentSearch.bookmarkChanged(majorName)

            fragmentBookmark.bookmarkChanged(majorName, -1)

            // FragmentNotice 시작
            fragmentNotice.deleteMajor(majorName)
            fragmentNotice.isBookmarkExist()
            // FragmentNotice 끝

            val msg: String = when (languageCode) {
                Translater_papago.korean -> "즐겨찾기가 삭제되었습니다."
                Translater_papago.english -> "Favorites have been deleted."
                Translater_papago.chinese -> "您的最喜欢的搜索已被删除。"
                Translater_papago.japanese -> "お気に入りが削除されました。"
                else -> "즐겨찾기가 삭제되었습니다."
            }
            Toast.makeText(
                this@MainActivity,
                msg,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    inner class FragmentNoticeInteraction : FragmentNotice.OnFragmentInteraction {
        override fun crawlingDone() {
        }

    }

    inner class FragmentSetupInteraction : FragmentSetup.OnFragmentInteraction {
        override fun setLanguage(languageCode: String) {
            this@MainActivity.languageCode = languageCode
            this@MainActivity.applyLanguage()
        }

    }

}