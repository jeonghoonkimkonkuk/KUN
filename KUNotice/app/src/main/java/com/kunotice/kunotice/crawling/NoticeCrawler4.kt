package com.kunotice.kunotice.crawling

import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
    건국대학교 홈페이지에 게시되는 공지사항을 크롤링하는 클래스이다.

    about select
    0 -> 학사 공지사항
    1 -> 장학 공지사항
    2 -> 취창업 공지사항 
    3 -> 국제 공지사항
    4 -> 학생 공지사항
    5 -> 산학/연구 공지사항
    6 -> 일반 공지사항
    7 -> 코로나19알림 공지사항
 */
class NoticeCrawler4(val select: Int) {

    private val VIEWURL: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/"
    private val URL0: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&cat=0000300001&p="
    private val URL1: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11688412&p="
    private val URL2: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11731332&p="
    private val URL3: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&cat=0000300002&p="
    private val URL4: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&cat=0000300003&p="
    private val URL5: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=65659&cat=0010300001&p="
    private val URL6: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&cat=0000300006&p="
    private val URL7: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11782906&p="

    lateinit var pageNum: String
    var maxPageNum: Int? = null

    lateinit var upperNotice: ArrayList<Notice>
    lateinit var normalNotice: ArrayList<Notice>

    // Secondary Constructor
    constructor(select: Int, pageNum: String) : this(select) {
        this.pageNum = pageNum

        upperNotice = ArrayList<Notice>()
        normalNotice = ArrayList<Notice>()
    }

    fun crawlingNoticeList(): Boolean {
        return when (select) {
            1 -> crawlingNoticeList_extra_1()
            7 -> crawlingNoticeList_extra_1()
            else -> crawlingNoticeList_1()
        }
    }

    fun crawlingNoticeList_extra_1(): Boolean {
        val connUrl: String = getUrl()

        if (connUrl.isBlank()) return false

        val element: Element = Jsoup.connect(connUrl).get()

        if (maxPageNum == null) {
            val board_box_02: Element = element.getElementsByClass("board_box_02").first()
            val str: String = board_box_02.child(1).ownText().trim()
            maxPageNum = str.split("/")[1].toInt()
        }

        val table: Element = element.getElementsByClass("list").first()
        val trTags: ArrayList<Element> =
            table.getElementsByTag("tbody").first().getElementsByTag("tr")
        for (tr in trTags) {
            // 상단 고정 공지사항인 경우에 해당
            if (tr.hasAttr("class")) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                upperNotice.add(temp)
            }
            // 일반 공지사항인 경우에 해당
            else {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
        }

        return true
    }

    fun crawlingNoticeList_extra_2(): Boolean {
        val connUrl: String = getUrl()

        if (connUrl.isBlank()) return false

        val element: Element = Jsoup.connect(connUrl).get()

        if (maxPageNum == null) {
            val board_box_02: Element = element.getElementsByClass("board_box_02").first()
            val str: String = board_box_02.child(1).ownText().trim()
            maxPageNum = str.split("/")[1].toInt()
        }

        normalNotice.clear()
        val table: Element = element.getElementsByClass("list").first()
        val trTags: ArrayList<Element> =
            table.getElementsByTag("tbody").first().getElementsByTag("tr")
        for (tr in trTags) {
            // 상단 고정 공지사항인 경우에 해당
            if (tr.hasAttr("class")) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
            // 일반 공지사항인 경우에 해당
            else {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
        }

        return true
    }

    fun crawlingNoticeList_1(): Boolean {
        val connUrl: String = getUrl()

        if (connUrl.isBlank()) return false

        val element: Element = Jsoup.connect(connUrl).get()

        if (maxPageNum == null) {
            val board_box_02: Element = element.getElementsByClass("board_box_02").first()
            val str: String = board_box_02.child(1).ownText().trim()
            maxPageNum = str.split("/")[1].toInt()
        }

        val table: Element = element.getElementsByClass("list").first()
        val trTags: ArrayList<Element> =
            table.getElementsByTag("tbody").first().getElementsByTag("tr")
        for (tr in trTags) {
            // 상단 고정 공지사항인 경우에 해당
            if (tr.hasAttr("class")) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = VIEWURL + td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                upperNotice.add(temp)
            }
            // 일반 공지사항인 경우에 해당
            else {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[2].child(0).html().trim()
                val url: String = td[2].child(0).attr("href")
                val writer: String = td[1].html().trim()
                val writeDay: String = td[3].html().trim()
                val hits: String = td[4].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
        }

        return true
    }

    fun crawlingNoticeList_2(): Boolean {
        val connUrl: String = getUrl()

        if (connUrl.isBlank()) return false

        val element: Element = Jsoup.connect(connUrl).get()

        if (maxPageNum == null) {
            val board_box_02: Element = element.getElementsByClass("board_box_02").first()
            val str: String = board_box_02.child(1).ownText().trim()
            maxPageNum = str.split("/")[1].toInt()
        }

        normalNotice.clear()
        val table: Element = element.getElementsByClass("list").first()
        val trTags: ArrayList<Element> =
            table.getElementsByTag("tbody").first().getElementsByTag("tr")
        for (tr in trTags) {
            // 상단 고정 공지사항인 경우에 해당
            if (tr.hasAttr("class")) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[1].child(0).html().trim()
                val url: String = VIEWURL + td[1].child(0).attr("href")
                val writer: String = "관리자"
                val writeDay: String = td[2].html().trim()
                val hits: String = td[3].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
            // 일반 공지사항인 경우에 해당
            else {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[2].child(0).html().trim()
                val url: String = td[2].child(0).attr("href")
                val writer: String = td[1].html().trim()
                val writeDay: String = td[3].html().trim()
                val hits: String = td[4].html().trim()
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            }
        }

        return true
    }

    fun nextPage(): Boolean {
        val ret: Boolean = ((pageNum?.toInt())!! < maxPageNum!!)
        if (ret)
            pageNum = ((pageNum?.toInt())!! + 1).toString()

        return ret
    }

    fun getTotalNotice(): ArrayList<Notice> {
        val ret: ArrayList<Notice> = ArrayList<Notice>()
        ret.addAll(upperNotice)
        ret.addAll(normalNotice)

        return ret
    }

    fun getUrl(): String {
        return when (select) {
            0 -> URL0 + pageNum
            1 -> URL1 + pageNum
            2 -> URL2 + pageNum
            3 -> URL3 + pageNum
            4 -> URL4 + pageNum
            5 -> URL5 + pageNum
            6 -> URL6 + pageNum
            7 -> URL7 + pageNum
            else -> ""
        }
    }
}