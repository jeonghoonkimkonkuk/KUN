package com.kunotice.kunotice

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.adapter.MajorInfoAdapter
import com.kunotice.kunotice.crawling.MajorInfoCrawler
import com.kunotice.kunotice.crawling.Professor
import com.kunotice.kunotice.translate.Translater_papago
import kotlinx.android.synthetic.main.activity_major_info.*

class MajorInfo : AppCompatActivity() {

    lateinit var languageCode: String

    // UI 관련 변수 시작
    private lateinit var majorName: TextView
    private lateinit var majorLocation: TextView
    private lateinit var majorTel: TextView
    private lateinit var majorFax: TextView
    private lateinit var pfInfoListView: RecyclerView
    lateinit var adapter: MajorInfoAdapter

    private val myProgressBar: MyProgressBar = MyProgressBar()
    // UI 관련 변수 시작

    // 크롤링 관련 변수 시작
    lateinit var crawler: MajorInfoCrawler
    // 크롤링 관련 변수 끝

    // 쓰레드 관련 변수 시작
    val infoHandler: InfoHandler = InfoHandler()
    // 쓰레드 관련 변수 끝

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_major_info)
        
        myProgressBar.progressON(this, null)
        init()
    }

    fun init() {
        val majorCode = intent.getStringExtra("majorCode")!!

        languageCode = intent.getStringExtra("languageCode")!!

        showMap.text = when (languageCode) {
            Translater_papago.korean -> "지도 보기"
            Translater_papago.english -> "View Map"
            Translater_papago.chinese -> "看地图"
            Translater_papago.japanese -> "地図を見る"
            else -> "지도 보기"
        }

        Log.i("majorCode", majorCode)
        crawler = MajorInfoCrawler(majorCode, languageCode)
        val infoThread: InfoThread = InfoThread()
        infoThread.start()

        majorName = findViewById(R.id.majorName)
        majorName.text = intent.getStringExtra("majorName")
//        koreanText[0] = intent.getStringExtra("majorName")
        majorLocation = findViewById(R.id.majorLocation)
        majorTel = findViewById(R.id.majorTel)
        majorFax = findViewById(R.id.majorFax)
        pfInfoListView = findViewById(R.id.pfInfoListView)
        pfInfoListView.layoutManager = LinearLayoutManager(this)
    }

    fun onBackBtnClicked(view: View) {
        val intent: Intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onShowMapClicked(view: View) {
        val majorCode: String = this.intent.getStringExtra("majorCode")!!
        val intent: Intent = Intent(this, PopupMapView::class.java)
        intent.putExtra("languageCode", languageCode)
        intent.putExtra("majorCode", majorCode)
        intent.putExtra("majorName", this.intent.getStringExtra("majorName"))
        intent.putExtra("majorLocation", majorLocation.text)
        startActivity(intent)
    }

    inner class InfoHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val bundle: Bundle = msg.data
            if (!bundle.isEmpty) {
                if (bundle.getBoolean("isCrawled")) {
                    var index: Int = 0
                    while (index < crawler.majorInfo.size) {
                        when (index) {
                            0 -> {
                                if (!(crawler.majorInfo[0].equals("학과위치 :"))) {
                                    majorLocation.text = crawler.majorInfo[0]
                                }
                            }
                            2 -> {
                                if (!(crawler.majorInfo[2].equals("전화번호 :"))) {
                                    majorTel.text = crawler.majorInfo[2]
                                }
                            }
                            3 -> {
                                if ((!crawler.majorInfo[3].equals("팩스번호 :"))) {
                                    majorFax.text = crawler.majorInfo[3]
                                }
                            }
                        }
                        index++
                    }

//                    pfInfo.clear()
//                    pfInfo.addAll(crawler.professor)
                    adapter = MajorInfoAdapter(crawler.professor)
                    pfInfoListView.adapter = adapter
                    adapter.setOnItemClickListener(OnItemClickListener())
                    myProgressBar.progressOFF()
                }
            }
        }
    }

    inner class InfoThread : Thread() {
        override fun run() {
            val message: Message = infoHandler.obtainMessage()
            val bundle: Bundle = Bundle()

            // 크롤링 성공한 경우
            if (crawler.crawlingInfo()) {
                bundle.putBoolean("isCrawled", true)
            }
            // 크롤링 실패한 경우
            else {
                bundle.putBoolean("isCrawled", false)
            }

            message.data = bundle
            infoHandler.sendMessage(message)
        }
    }

    inner class OnItemClickListener : MajorInfoAdapter.OnItemClickListener {

        fun getMenuId(): Int {
            return when (languageCode) {
                Translater_papago.korean -> R.menu.menu_majorinfo
                Translater_papago.english -> R.menu.menu_majorinfo_english
                Translater_papago.chinese -> R.menu.menu_majorinfo_chinese
                Translater_papago.japanese -> R.menu.menu_majorinfo_japanese
                else -> R.menu.menu_majorinfo
            }
        }

        override fun onItemClick(v: View, professor: Professor) {
            val popupMenu: PopupMenu = PopupMenu(this@MajorInfo, v)
            popupMenu.menuInflater.inflate(getMenuId(), popupMenu.menu)

            popupMenu.setOnMenuItemClickListener(object :
                PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    val clipboardManager: ClipboardManager =
                        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                    if (item != null) {
                        // 연구실복사
                        if (item.itemId == R.id.menuCopy1) {
                            val clipData: ClipData =
                                ClipData.newPlainText("office", professor.office)
                            clipboardManager.setPrimaryClip(clipData)
                        }
                        // 연락처복사
                        else if (item.itemId == R.id.menuCopy2) {
                            val clipData: ClipData =
                                ClipData.newPlainText("office", professor.tel)
                            clipboardManager.setPrimaryClip(clipData)
                        }
                        // 이메일복사
                        else if (item.itemId == R.id.menuCopy3) {
                            val clipData: ClipData =
                                ClipData.newPlainText("office", professor.email)
                            clipboardManager.setPrimaryClip(clipData)
                        }
                        // 전체복사
                        else if (item.itemId == R.id.menuCopy4) {
                            val copyStr: String =
                                professor.name + "\n" + professor.office + "\n" + professor.tel + "\n" + professor.email
                            val clipData: ClipData =
                                ClipData.newPlainText("office", copyStr)
                            clipboardManager.setPrimaryClip(clipData)
                        }
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "복사되었습니다."
                            Translater_papago.english -> "It is copied."
                            Translater_papago.chinese -> "已复制。"
                            Translater_papago.japanese -> "コピーされました."
                            else -> "복사되었습니다."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return true
                }
            })
            popupMenu.show()
        }

    }

}
