package com.kunotice.kunotice

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.adapter.NoticeListAdapter
import com.kunotice.kunotice.crawling.Notice
import com.kunotice.kunotice.crawling.NoticeCrawler
import com.kunotice.kunotice.crawling.NoticeCrawler2
import com.kunotice.kunotice.crawling.NoticeCrawler3
import com.kunotice.kunotice.data.MyListModel2
import com.kunotice.kunotice.translate.Translater_papago

class NoticeList : AppCompatActivity() {

    lateinit var languageCode: String
    lateinit var otherLanguageToKorean: HashMap<String, String>

    // 크롤링 관련 변수 시작
    var crawlerNum: Int? = null
    lateinit var crawlerNumMap: HashMap<String, Int>
    private lateinit var crawler: NoticeCrawler
    private lateinit var crawler2: NoticeCrawler2
    private lateinit var crawler3: NoticeCrawler3
    private val noticeHandler: NoticeHandler = NoticeHandler()
    // 크롤링 관련 변수 끝

    // UI 관련 변수 시작
    private lateinit var majorName: TextView

    private lateinit var officeNoticeListLayout: LinearLayout
    private lateinit var snsNoticeListLayout: LinearLayout
    private lateinit var officeNoticeListView: RecyclerView
    private lateinit var snsNoticeListView: RecyclerView

    private val myProgressBar: MyProgressBar = MyProgressBar()
    // UI 관련 변수 끝

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice_list)

        myProgressBar.progressON(this, null)
        init()
    }

    private fun init() {
        languageCode = intent.getStringExtra("languageCode")!!
        otherLanguageToKorean =
            intent.getSerializableExtra("otherLanguageToKorean") as HashMap<String, String>

        // 크롤링 관련 변수 초기화 시작
        crawlerNumMap = getIntent().getSerializableExtra("crawlerNumMap")!! as HashMap<String, Int>
        crawlerNum =
            crawlerNumMap.get(otherLanguageToKorean.get(getIntent().getStringExtra("majorName")!!))
        // 오류 발생 -> 예외처리
        if (crawlerNum == -1) {
            Log.e("crawlerNum is ", crawlerNum.toString())
        }
        // NoticeCrawler 객체 사용
        else if (crawlerNum == 1) {
            crawler =
                NoticeCrawler(getIntent().getStringExtra("majorCode")!!, "12", "1", languageCode)
            val noticeThread: NoticeThread = NoticeThread(1)
            noticeThread.start()
        }
        // NoticeCrawler2 객체 사용
        else if (crawlerNum == 2) {
            crawler2 = NoticeCrawler2(getIntent().getStringExtra("majorCode")!!, "0", languageCode)
            val noticeThread: NoticeThread = NoticeThread(3)
            noticeThread.start()
        }
        // NoticeCrawler3 객체 사용
        else if (crawlerNum == 3) {
            crawler3 = NoticeCrawler3(getIntent().getStringExtra("majorCode")!!, "0", languageCode)
            val noticeThread: NoticeThread = NoticeThread(5)
            noticeThread.start()
        }
        // 크롤링 관련 변수 초기화 끝

        // UI 관련 변수 초기화 시작
        majorName = findViewById(R.id.majorName)
        majorName.text = intent.getStringExtra("majorName")

        officeNoticeListLayout = findViewById(R.id.officeNoticeListLayout)
        snsNoticeListLayout = findViewById(R.id.snsNoticeListLayout)
        officeNoticeListView = findViewById(R.id.officeNoticeListView)
        officeNoticeListView.layoutManager = LinearLayoutManager(this)
        officeNoticeListView.addOnScrollListener(ScrollListener1())
        snsNoticeListView = findViewById(R.id.snsNoticeListView)
        snsNoticeListView.layoutManager = LinearLayoutManager(this)
        // UI 관련 변수 초기화 끝
    }

    fun onBackBtnClicked(view: View) {
        val intent: Intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun majorNameBtnClicked(view: View) {}

    inner class NoticeHandler : Handler() {
        lateinit var adapter: NoticeListAdapter
        val list: ArrayList<Notice> = ArrayList<Notice>()
        val mList: ArrayList<MyListModel2> = ArrayList()

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val bundle: Bundle = msg.data
            if (!(bundle.isEmpty)) {
                val select: Int = bundle.getInt("select")
                // NoticeCrawler 객체 사용 - 과사 공지사항을 처음 크롤링한 경우
                if (select == 1) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.addAll(crawler.getTotalNotice())
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        adapter.setOnItemClickListener(ItemClickListener())
                    } else {
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                            Translater_papago.english -> "No response from the server.\nTry again."
                            Translater_papago.chinese -> "服务器没有响应。\n请再试一次。"
                            Translater_papago.japanese -> "サーバーから応答がありません。\nもう一度試してみてください。"
                            else -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                // NoticeCrawler 객체 사용 - 과사 공지사항을 추가로 더 크롤링한 경우
                else if (select == 2) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.clear()
                        list.addAll(crawler.getTotalNotice())
                        mList.clear()
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter.notifyDataSetChanged()
                    } else {
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "공지사항이 더 이상 존재하지 않습니다."
                            Translater_papago.english -> "Notices no longer exist."
                            Translater_papago.chinese -> "通知不再存在。"
                            Translater_papago.japanese -> "通知はもはや存在しません。"
                            else -> "공지사항이 더 이상 존재하지 않습니다."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // NoticeCrawler2 객체 사용 - 과사 공지사항을 처음 크롤링하는 경우
                else if (select == 3) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.addAll(crawler2.getTotalNotice())
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler2.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        adapter.setOnItemClickListener(ItemClickListener())
                    } else {
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler2.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                            Translater_papago.english -> "No response from the server.\nTry again."
                            Translater_papago.chinese -> "服务器没有响应。\n请再试一次。"
                            Translater_papago.japanese -> "サーバーから応答がありません。\nもう一度試してみてください。"
                            else -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                // NoticeCrawler2 객체 사용 - 과사 공지사항을 추가로 더 크롤링하는 경우
                else if (select == 4) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.clear()
                        list.addAll(crawler2.getTotalNotice())
                        mList.clear()
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter.upperNoticeNum = crawler2.upperNotice.size
                        adapter.notifyDataSetChanged()
                    } else {
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "공지사항이 더 이상 존재하지 않습니다."
                            Translater_papago.english -> "Notices no longer exist."
                            Translater_papago.chinese -> "通知不再存在。"
                            Translater_papago.japanese -> "通知はもはや存在しません。"
                            else -> "공지사항이 더 이상 존재하지 않습니다."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                // NoticeCrawler3 객체 사용 - 공지사항을 처음 크롤링하는 경우
                else if (select == 5) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.addAll(crawler3.getTotalNotice())
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler3.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        adapter.setOnItemClickListener(ItemClickListener())
                    } else {
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter =
                            NoticeListAdapter(mList, crawler3.upperNotice.size, applicationContext)
                        officeNoticeListView.adapter = adapter
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                            Translater_papago.english -> "No response from the server.\nTry again."
                            Translater_papago.chinese -> "服务器没有响应。\n请再试一次。"
                            Translater_papago.japanese -> "サーバーから応答がありません。\nもう一度試してみてください。"
                            else -> "서버로부터 응답이 없습니다.\n다시 한번 시도해 주세요."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }

                }
                // NoticeCrawler3 객체 사용 - 공지사항을 추가로 더 크롤링하는 경우
                else if (select == 6) {
                    if (bundle.getBoolean("isCrawled")) {
                        list.clear()
                        list.addAll(crawler3.getTotalNotice())
                        mList.clear()
                        for (i in list.indices) {
                            mList.add(MyListModel2(list[i], 1))
                        }
                        mList.add(0, MyListModel2(Notice("", "", "", "", "", ""), 2))
                        adapter.upperNoticeNum = crawler3.upperNotice.size
                        adapter.notifyDataSetChanged()
                    } else {
                        val msg: String = when (languageCode) {
                            Translater_papago.korean -> "공지사항이 더 이상 존재하지 않습니다."
                            Translater_papago.english -> "Notices no longer exist."
                            Translater_papago.chinese -> "通知不再存在。"
                            Translater_papago.japanese -> "通知はもはや存在しません。"
                            else -> "공지사항이 더 이상 존재하지 않습니다."
                        }
                        Toast.makeText(
                            applicationContext,
                            msg,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                myProgressBar.progressOFF()
            }
        }
    }

    // Primary Constructor
    inner class NoticeThread(var select: Int) : Thread() {

        override fun run() {
            val message: Message = noticeHandler.obtainMessage()
            val bundle: Bundle = Bundle()

            // NoticeCrawler 객체 사용 - 과사 공지사항을 처음 크롤링하는 경우
            if (select == 1) {
                bundle.putBoolean("isCrawled", crawler.crawlingNoticeList())
                bundle.putInt("select", select)
            }
            // NoticeCrawler 객체 사용 - 과사 공지사항을 추가로 더 크롤링하는 경우
            else if (select == 2) {
                // 마지막 페이지에 도달하지 않은 경우 -> 추가로 크롤링 가능
                if (crawler.nextPage()) {
                    crawler.crawlingNoticeList()
                    bundle.putBoolean("isCrawled", true)
                }
                // 마지막 페이지에 도달한 경우 -> 추가로 크롤링 불가능
                else {
                    bundle.putBoolean("isCrawled", false)
                }
                bundle.putInt("select", select)
            }
            // NoticeCrawler2 객체 사용 - 과사 공지사항을 처음 크롤링하는 경우
            else if (select == 3) {
                bundle.putBoolean("isCrawled", crawler2.crawlingNoticeList())
                bundle.putInt("select", select)
            }
            // NoticeCrawler2 객체 사용 - 과사 공지사항을 추가로 더 크롤링하는 경우
            else if (select == 4) {
                // 마지막 페이지에 도달하지 않은 경우 -> 추가로 크롤링 가능
                if (crawler2.nextPage()) {
                    crawler2.crawlingNoticeList()
                    bundle.putBoolean("isCrawled", true)
                }
                // 마지막 페이지에 도달한 경우 -> 추가로 크롤링 불가능
                else {
                    bundle.putBoolean("isCrawled", false)
                }
                bundle.putInt("select", select)
            }
            // NoticeCrawler3 객체 사용 - 공지사항을 처음 크롤링하는 경우
            else if (select == 5) {
                bundle.putBoolean("isCrawled", crawler3.crawlingNoticeList())
                bundle.putInt("select", select)
            }
            // NoticeCrawler3 객체 사용 - 공지사항을 추가로 더 크롤링하는 경우
            else if (select == 6) {
                // 마지막 페이지에 도달하지 않은 경우 -> 추가로 크롤링 가능
                if (crawler3.nextPage()) {
                    crawler3.crawlingNoticeList()
                    bundle.putBoolean("isCrawled", true)
                }
                // 마지막 페이지에 도달한 경우 -> 추가로 크롤링 불가능
                else {
                    bundle.putBoolean("isCrawled", false)
                }
                bundle.putInt("select", select)
            }

            message.data = bundle
            noticeHandler.sendMessage(message)
        }
    }

    inner class ScrollListener1 : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically((1))) {
                myProgressBar.progressON(this@NoticeList, null)
                if (crawlerNum == -1) {
                    Log.e("crawlerNum is ", crawlerNum.toString())
                } else if (crawlerNum == 1) {
                    val noticeThread: NoticeThread = NoticeThread(2)
                    noticeThread.start()
                } else if (crawlerNum == 2) {
                    val noticeThread: NoticeThread = NoticeThread(4)
                    noticeThread.start()
                } else if (crawlerNum == 3) {
                    val noticeThread: NoticeThread = NoticeThread(6)
                    noticeThread.start()
                }
            }
        }
    }

    inner class ItemClickListener : NoticeListAdapter.OnItemClickListener {
        override fun onItemClick(v: View, notice: Notice) {
//            val intent: Intent = Intent(applicationContext, NoticePost::class.java)
//            intent.putExtra("url", notice.url)
//            intent.putExtra("postTitle", notice.title)
//            intent.putExtra("languageCode",languageCode)
//            intent.putExtra(
//                "postInfo",
//                notice.writer + " | " + notice.writeDay + " | " + notice.hits
//            )
//            startActivityForResult(intent, 101)
            val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.url))
            intent.setPackage("com.android.chrome")
            startActivity(intent)
        }

    }
}
