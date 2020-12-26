package com.kunotice.kunotice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.adapter.FragmentNoticeListAdapter
import com.kunotice.kunotice.crawling.Notice
import com.kunotice.kunotice.crawling.NoticeCrawler
import com.kunotice.kunotice.crawling.NoticeCrawler2
import com.kunotice.kunotice.crawling.NoticeCrawler3
import com.kunotice.kunotice.translate.Translater_papago

class FragmentNotice(
    val majorMap: HashMap<String, String>,
    val majorListToCrawling: ArrayList<String>,
    val crawlerNum: HashMap<String, Int>,
    var languageCode: String,
    val otherLanguageToKorean: HashMap<String, String>
) : Fragment() {

    interface OnFragmentInteraction {
        fun crawlingDone()
    }

    fun setOnFragmentInteraction(fragmentListener: OnFragmentInteraction) {
        this.fragmentListener = fragmentListener
    }

    var fragmentListener: OnFragmentInteraction? = null

    val myProgressBar: MyProgressBar = MyProgressBar()

    // 크롤링 관련 변수 시작
//    val crawlerNum: HashMap<String, Int> = HashMap<String, Int>()
    val notice: ArrayList<Notice> = ArrayList<Notice>()
    val isFinished: ArrayList<Boolean> = ArrayList<Boolean>()
    val crawlers: ArrayList<NoticeCrawler> = ArrayList<NoticeCrawler>()
    val crawlers2: ArrayList<NoticeCrawler2> = ArrayList<NoticeCrawler2>()
    val crawlers3: ArrayList<NoticeCrawler3> = ArrayList<NoticeCrawler3>()
    private val noticeHandler: NoticeHandler = NoticeHandler()
    // 크롤링 관련 변수 끝

    // UI 관련 변수 시작
    lateinit var noBookmarkText: TextView
    lateinit var noticeListView: RecyclerView
    lateinit var adapter: FragmentNoticeListAdapter
    lateinit var rootView: ViewGroup
    // UI 관련 변수 끝

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView =
            inflater.inflate(R.layout.fragment_notice, container, false) as ViewGroup

        noBookmarkText = rootView.findViewById(R.id.noBookmarkText)
        init()

        noticeListView = rootView.findViewById(R.id.bookmarkNoticeListView)
        noticeListView.layoutManager = LinearLayoutManager(context)
        noticeListView.addOnScrollListener(ScrollListener())
        adapter = FragmentNoticeListAdapter(notice)
        noticeListView.adapter = adapter
        adapter.setOnItemClickListener(object : FragmentNoticeListAdapter.OnItemClickListener {
            override fun onItemClick(v: View, notice: Notice) {
//                val intent: Intent = Intent(context, NoticePost::class.java)
//                intent.putExtra("url", notice.url)
//                intent.putExtra("postTitle", notice.title)
//                intent.putExtra(
//                    "postInfo",
//                    notice.writer + " | " + notice.writeDay + " | " + notice.hits
//                )
//                intent.putExtra("languageCode",languageCode)
//                startActivityForResult(intent, 101)
                val intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(notice.url))
                intent.setPackage("com.android.chrome")
                startActivity(intent)
            }

        })

        val fromdb = (context as MainActivity).myDBHelper.getAllRecord()
        for (mname in fromdb) {
            addMajor(mname)
        }
        isBookmarkExist()

        return rootView
    }

    fun init() {
        val text: String = when (languageCode) {
            Translater_papago.korean -> "등록된 즐겨찾기가 없습니다.\n즐겨찾기를 추가하시면 공지사항을 빠르게 볼 수 있습니다.\n즐겨찾기를 추가하고 사용해 주세요!"
            Translater_papago.english -> "No registered favorites.\nAdd your favorites to quickly view the notices.\nPlease add and use your favorites!"
            Translater_papago.chinese -> "没有注册的喜爱。\n添加一个最喜欢的,你可以快速查看公告。\n请添加和使用您的喜爱!"
            Translater_papago.japanese -> "登録されたお気に入りはありません。\nお気に入りを追加すると、お知らせをすばやく表示できます。\nお気に入りを追加して使用してください!"
            else -> "등록된 즐겨찾기가 없습니다.\n즐겨찾기를 추가하시면 공지사항을 빠르게 볼 수 있습니다.\n즐겨찾기를 추가하고 사용해 주세요!"
        }
        noBookmarkText.text = text

        for (majorName in majorListToCrawling) {
            isFinished.add(false)
            when (crawlerNum.get(majorName)) {
                1 -> crawlers.add(NoticeCrawler(majorMap.get(majorName)!!, "10", "1", languageCode))
                2 -> crawlers2.add(NoticeCrawler2(majorMap.get(majorName)!!, "0", languageCode))
                3 -> crawlers3.add(NoticeCrawler3(majorMap.get(majorName)!!, "0", languageCode))
                else -> Log.e("error occured", "in FragmentNotice init() function")
            }
        }
    }

    fun startCrawling(select: Int) {
        myProgressBar.progressON(activity, null)
        val noticeThread: NoticeThread = NoticeThread(select)
        noticeThread.start()
    }

    fun isBookmarkExist() {
        if (crawlers.isEmpty() && crawlers2.isEmpty() && crawlers3.isEmpty()) {
            noBookmarkText.visibility = View.VISIBLE
            noticeListView.visibility = View.GONE
        } else {
            noBookmarkText.visibility = View.GONE
            noticeListView.visibility = View.VISIBLE
        }
    }

    fun clearNotice() {
        notice.clear()
        for (crawler in crawlers) {
            crawler.normalNotice.clear()
            crawler.pageNum = "1"
        }
        for (crawler2 in crawlers2) {
            crawler2.normalNotice.clear()
            crawler2.pageNum = "0"
        }
        for (crawler3 in crawlers3) {
            crawler3.normalNotice.clear()
            crawler3.pageNum = "0"
        }
    }

    fun setIsFinished(set: Boolean) {
        var index: Int = 0
        while (index < isFinished.size) {
            isFinished[index++] = set
        }
    }

    fun addMajor(majorName: String) {
        // 추가하려는 학과가 이미 존재하는 경우에 리턴
        if (majorListToCrawling.contains(majorName)) return

        when (crawlerNum.get(majorName)) {
            1 -> crawlers.add(NoticeCrawler(majorMap.get(majorName)!!, "10", "1", languageCode))
            2 -> crawlers2.add(NoticeCrawler2(majorMap.get(majorName)!!, "0", languageCode))
            3 -> crawlers3.add(NoticeCrawler3(majorMap.get(majorName)!!, "0", languageCode))
            else -> Log.e("error occured", "in FragmentNotice addMajor() function")
        }
//        crawlers.add(NoticeCrawler(majorMap.get(majorName)!!, "5", "1"))
        majorListToCrawling.add(majorName)
        isFinished.add(false)

        clearNotice()
        adapter.notifyDataSetChanged()
    }

    fun deleteMajor(majorName: String) {
        // 제거하려는 학과가 존재하지 않는 경우에 리턴
        if (!majorListToCrawling.contains(majorName)) return


        val deleteList: ArrayList<Int> = ArrayList<Int>()
        var i: Int = notice.size - 1
        while (i >= 0) {
            if (majorName.equals(otherLanguageToKorean.get(notice[i].majorName)))
                deleteList.add(i)
            i--
        }
        for (k in deleteList) {
            notice.removeAt(k)
        }
        adapter.notifyDataSetChanged()

        var index: Int = 0
        forloop@
        for (str in majorListToCrawling) {
            if (majorName.equals(str)) break@forloop
            index++
        }
        majorListToCrawling.removeAt(index)
        isFinished.removeAt(index)

        if (crawlerNum.get(majorName) == 1) {
            var k: Int = 0
            while (k < crawlers.size) {
                if (crawlers[k].major.equals(majorMap.get(majorName))) break
                k++
            }
            crawlers.removeAt(k)
        } else if (crawlerNum.get(majorName) == 2) {
            var k: Int = 0
            while (k < crawlers2.size) {
                if (crawlers2[k].major.equals(majorMap.get(majorName))) break
                k++
            }
            crawlers2.removeAt(k)
        } else if (crawlerNum.get(majorName) == 3) {
            var k: Int = 0
            while (k < crawlers3.size) {
                if (crawlers3[k].major.equals(majorMap.get(majorName))) break
                k++
            }
            crawlers3.removeAt(k)
        }
    }

    inner class NoticeHandler : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val bundle: Bundle = msg.data
            if (!(bundle.isEmpty)) {
                /*
                about endCheckList: ArrayList<Boolean>
                true -> index번째 크롤러는 크롤링할 공지사항이 더 남아있음을 의미
                false -> index번째 크롤러는 더 이상 크롤링할 공지사항이 없음을 의미
                */
                if (bundle.containsKey("endCheckList")) {
                    val endCheckList: ArrayList<Boolean> =
                        bundle.getSerializable("endCheckList") as ArrayList<Boolean>
                    if (bundle.getBoolean("isCrawled")) {
                        // 모든 크롤러가 크롤링을 끝냈는지를 확인
                        var check = true
                        for (temp in isFinished) {
                            check = check && temp
                        }

                        if (check) {
                            var i: Int = 0
                            var endCheck: Boolean = false
                            for (temp in crawlers) {
                                if (endCheckList[i])
                                    notice.addAll(temp.normalNotice)
                                endCheck = endCheck || endCheckList[i++]
                            }
                            for (temp in crawlers2) {
                                if (endCheckList[i])
                                    notice.addAll(temp.normalNotice)
                                endCheck = endCheck || endCheckList[i++]
                            }
                            for (temp in crawlers3) {
                                if (endCheckList[i])
                                    notice.addAll(temp.normalNotice)
                                endCheck = endCheck || endCheckList[i++]
                            }
                            if (!endCheck) {
                                val msg: String = when (languageCode) {
                                    Translater_papago.korean -> "공지사항이 더 이상 존재하지 않습니다."
                                    Translater_papago.english -> "Notices no longer exist."
                                    Translater_papago.chinese -> "通知不再存在。"
                                    Translater_papago.japanese -> "通知はもはや存在しません。"
                                    else -> "공지사항이 더 이상 존재하지 않습니다."
                                }
                                Toast.makeText(
                                    context,
                                    msg,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            notice.sort()
                            adapter.notifyDataSetChanged()
                            setIsFinished(false)

                            myProgressBar.progressOFF()
//                        fragmentListener?.crawlingDone()
                        }
                    }
                } else {
                    if (bundle.getBoolean("isCrawled")) {
                        // 모든 크롤러가 크롤링을 끝냈는지를 확인
                        var check = true
                        for (temp in isFinished) {
                            check = check && temp
                        }

                        if (check) {
                            for (temp in crawlers) {
                                notice.addAll(temp.normalNotice)
                            }
                            for (temp in crawlers2) {
                                notice.addAll(temp.normalNotice)
                            }
                            for (temp in crawlers3) {
                                notice.addAll(temp.normalNotice)
                            }
                            notice.sort()
                            adapter.notifyDataSetChanged()
                            setIsFinished(false)

                            myProgressBar.progressOFF()
//                        fragmentListener?.crawlingDone()
                        }
                    }
                }
            }
        }
    }

    inner class NoticeThread(val select: Int) : Thread() {
        override fun run() {
            val message: Message = noticeHandler.obtainMessage()
            val bundle: Bundle = Bundle()
            // 처음 크롤링 하는 경우
            if (select == 1) {
                var index: Int = 0
                for (crawler in crawlers) {
                    crawler.crawlingNoticeList_2()
                    isFinished[index++] = true
                }
                for (crawler2 in crawlers2) {
                    crawler2.crawlingNoticeList_2()
                    isFinished[index++] = true
                }
                for (crawler3 in crawlers3) {
                    crawler3.crawlingNoticeList_2()
                    isFinished[index++] = true
                }
                bundle.putBoolean("isCrawled", true)
            }
            // 추가로 크롤링 하는 경우
            else if (select == 2) {
                var index: Int = 0
                val endCheckList: ArrayList<Boolean> = ArrayList<Boolean>()
                for (crawler in crawlers) {
                    val temp: Boolean = crawler.nextPage()
                    if (temp) {
                        crawler.crawlingNoticeList_2()
                    }
                    endCheckList.add(temp)
                    isFinished[index++] = true
                }
                for (crawler2 in crawlers2) {
                    val temp: Boolean = crawler2.nextPage()
                    if (temp) {
                        crawler2.crawlingNoticeList_2()
                    }
                    endCheckList.add(temp)
                    isFinished[index++] = true
                }
                for (crawler3 in crawlers3) {
                    val temp: Boolean = crawler3.nextPage()
                    if (temp) {
                        crawler3.crawlingNoticeList_2()
                    }
                    endCheckList.add(temp)
                    isFinished[index++] = true
                }
                bundle.putBoolean("isCrawled", true)
                bundle.putSerializable("endCheckList", endCheckList)
            }

            message.data = bundle
            noticeHandler.sendMessage(message)
        }
    }

    inner class ScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (!recyclerView.canScrollVertically((1))) {
                myProgressBar.progressON(activity, null)
                startCrawling(2)
            }
        }
    }

}