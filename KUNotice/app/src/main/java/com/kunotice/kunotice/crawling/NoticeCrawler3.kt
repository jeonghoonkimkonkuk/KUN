package com.kunotice.kunotice.crawling

import android.util.Log
import com.kunotice.kunotice.translate.Translater_papago
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
    건국대학교 홈페이지에 게시되는 공지사항을 크롤링하는 클래스이다.
    또는 그 외의 과사 홈페이지가 독특한 학과 공지사항을 크롤링하는 클래스이다.

    KU -> 건국대학교
    REALESTATE -> 부동산학과
    BIOLOGY -> 생명과학특성학과 -> 공지사항 게시물에 접근하려면 특정 권한이 있어야 하나본데..
 */
class NoticeCrawler3(val major: String, val languageCode: String) {

    // 건국대학교 시작
    private val KU_LISTURL: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=notice&p="
    private val KU_VIEWURL: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/"
    // 건국대학교 끝

    // 부동산학과 시작
    private val REALESTATE_LISTURL: String =
        "http://www.realestate.ac.kr/gb/bbs/board.php?bo_table=notice&sca=%ED%95%99%EB%B6%80&page="
    // 부동산학과 끝

    // 생명과학특성학과 시작
    private val BIOLOGY_LISTURL: String =
        "http://www.konkuk.ac.kr/do/MessageBoard/ArticleList.do?forum=11676214&p="
    // 생명과학특성학과 끝

    var pageNum: String? = null         // 크롤링할 공지사항 목록의 페이지를 의미 -> url 구성에 사용됨
    var maxPageNum: Int? = null         // 공지사항 목록의 마지막 페이지를 의미

    lateinit var upperNotice: ArrayList<Notice>
    lateinit var normalNotice: ArrayList<Notice>

    // Secondary Constructor
    constructor(major: String, pageNum: String, languageCode: String) : this(major, languageCode) {
        this.pageNum = if (major.equals("REALESTATE")) (pageNum.toInt() + 1).toString() else pageNum

        upperNotice = ArrayList<Notice>()
        normalNotice = ArrayList<Notice>()
    }

    fun crawlingNoticeList(): Boolean {
        return when (major) {
            "KU" -> crawling_KU()
            "REALESTATE" -> crawling_REALESTATE()
            "BIOLOGY" -> crawling_BIOLOGY()
            else -> false
        }
    }

    fun crawlingNoticeList_2(): Boolean {
        return when (major) {
            "KU" -> crawling_KU_2()
            "REALESTATE" -> crawling_REALESTATE_2()
            "BIOLOGY" -> crawling_BIOLOGY_2()
            else -> false
        }
    }

    fun crawling_KU(): Boolean {
        val listUrl: String = KU_LISTURL + pageNum

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val next: Element = element.getElementsByClass("next").first()
            var href: String = next.attr("href")
            href = href.replace("ArticleList.do?forum=notice&sort=6&p=", "").trim()
            maxPageNum = href[0].toInt()
        }

        val trTags: ArrayList<Element> =
            element.getElementsByTag("tbody").first().getElementsByTag("tr")

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        val isUpper: ArrayList<Boolean> = ArrayList()
        for (tr in trTags) {
            val td: ArrayList<Element> = tr.getElementsByTag("td")

            val title: String = td[2].child(0).html().trim()
            val url: String = KU_VIEWURL + td[2].child(0).attr("href")
            Log.i("noticeList url", url)
            val writer: String = td[1].html().trim()
            val writeDay: String = td[3].html()
            val hits: String = td[4].html()
            if (languageCode.equals(Translater_papago.korean)) {
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                // 일반 공지사항인 경우
                if (td[0].childrenSize() == 0) {
                    normalNotice.add(temp)
                    isUpper.add(false)
                }
                // 상단 고정 공지사항인 경우
                else {
                    upperNotice.add(temp)
                    isUpper.add(true)
                }
            } else {
                if (td[0].childrenSize() == 0) {
                    isUpper.add(false)
                } else {
                    isUpper.add(true)
                }
                list.add(title)
                urlList.add(url)
                list.add(writer)
                writeDayList.add(writeDay)
                hitsList.add(hits)

            }
        }
        if (!languageCode.equals(Translater_papago.korean)) {
            val translater: Translater_papago = Translater_papago(languageCode, list)
            val temp: ArrayList<String> = translater.translateStr()
            list.clear()
            list.addAll(temp)

            var index: Int = 0
            while (index < list.size / 2) {
                if (isUpper[index]) {
                    upperNotice.add(
                        Notice(
                            list[index * 2],
                            urlList[index],
                            list[index * 2 + 1],
                            writeDayList[index],
                            hitsList[index],
                            "건국대학교"
                        )
                    )
                } else {
                    normalNotice.add(
                        Notice(
                            list[index * 2],
                            urlList[index],
                            list[index * 2 + 1],
                            writeDayList[index],
                            hitsList[index],
                            "건국대학교"
                        )
                    )
                }
                index++
            }
        }

        return true
    }

    fun crawling_KU_2(): Boolean {
        val listUrl: String = KU_LISTURL + pageNum

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val next: Element = element.getElementsByClass("next").first()
            var href: String = next.attr("href")
            href = href.replace("ArticleList.do?forum=notice&sort=6&p=", "").trim()
            maxPageNum = href[0].toInt()
        }

        normalNotice.clear()
        val trTags: ArrayList<Element> = element.getElementsByTag("tr")

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        for (tr in trTags) {
            if (tr.attr("class").equals("notice")) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")

                val title: String = td[2].child(0).html().trim()
                val url: String = KU_VIEWURL + td[2].child(0).attr("href")
                Log.i("noticeList url", url)
                val writer: String = td[1].html().trim()
                val writeDay: String = td[3].html()
                val hits: String = td[4].html()
                if (languageCode.equals(Translater_papago.korean)) {
                    val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                    normalNotice.add(temp)
                } else {
                    list.add(title)
                    urlList.add(url)
                    list.add(writer)
                    writeDayList.add(writeDay)
                    hitsList.add(hits)
                }
            }
        }
        if (!languageCode.equals(Translater_papago.korean)) {
            list.add("건국대학교")
            val translater: Translater_papago = Translater_papago(languageCode, list)
            val temp: ArrayList<String> = translater.translateStr()
            list.clear()
            list.addAll(temp)

            var index: Int = 0
            while (index < (list.size-1) / 2) {
                normalNotice.add(
                    Notice(
                        list[index * 2],
                        urlList[index],
                        list[index * 2 + 1],
                        writeDayList[index],
                        hitsList[index],
                        list[list.size-1]
                    )
                )
                index++
            }
        }

        return true
    }

    fun crawling_REALESTATE(): Boolean {
        val listUrl: String = REALESTATE_LISTURL + pageNum

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val paging: Element = element.getElementsByClass("paging").first()
            val a: Element = paging.getElementsByTag("li")[2].getElementsByTag("a")[1]
            val str: String = a.attr("href")
            maxPageNum =
                str.replace("./board.php?bo_table=notice&sca=%ED%95%99%EB%B6%80&page=", "").trim()
                    .toInt()
        }

        val trTags: ArrayList<Element> =
            element.getElementsByClass("board_list").first().getElementsByTag("tbody").first()
                .getElementsByTag("tr")

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        for (tr in trTags) {
            val td: ArrayList<Element> = tr.getElementsByTag("td")

            val title: String = td[2].child(0).html().trim()
            val url: String = td[2].child(0).attr("href")
            Log.i("noticeList url", url)
            val writer: String = td[1].child(0).html().trim()
            val writeDay: String = td[3].html().trim()
            val hits: String = td[4].html().trim()
            if (languageCode.equals(Translater_papago.korean)) {
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "건국대학교")
                normalNotice.add(temp)
            } else {
                list.add(title)
                urlList.add(url)
                list.add(writer)
                writeDayList.add(writeDay)
                hitsList.add(hits)
            }
        }
        if (!languageCode.equals(Translater_papago.korean)) {
            val translater: Translater_papago = Translater_papago(languageCode, list)
            val temp: ArrayList<String> = translater.translateStr()
            list.clear()
            list.addAll(temp)

            var index: Int = 0
            while (index < list.size / 2) {
                normalNotice.add(
                    Notice(
                        list[index * 2],
                        urlList[index],
                        list[index * 2 + 1],
                        writeDayList[index],
                        hitsList[index],
                        "건국대학교"
                    )
                )
                index++
            }
        }

        return true
    }

    fun crawling_REALESTATE_2(): Boolean {
        val listUrl: String = REALESTATE_LISTURL + pageNum

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val paging: Element = element.getElementsByClass("paging").first()
            val a: Element = paging.getElementsByTag("li")[2].getElementsByTag("a")[1]
            val str: String = a.attr("href")
            maxPageNum =
                str.replace("./board.php?bo_table=notice&sca=%ED%95%99%EB%B6%80&page=", "")
                    .trim()
                    .toInt()
        }

        normalNotice.clear()
        val trTags: ArrayList<Element> =
            element.getElementsByClass("board_list").first().getElementsByTag("tbody").first()
                .getElementsByTag("tr")

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        for (tr in trTags) {
            val td: ArrayList<Element> = tr.getElementsByTag("td")

            val title: String = td[2].child(0).html().trim()
            val url: String = td[2].child(0).attr("href")
            Log.i("noticeList url", url)
            val writer: String = td[1].child(0).html().trim()
            val writeDay: String = td[3].html().trim()
            val hits: String = td[4].html().trim()
            if (languageCode.equals(Translater_papago.korean)) {
                val temp: Notice = Notice(title, url, writer, writeDay, hits, "부동산학과")
                normalNotice.add(temp)
            } else {
                list.add(title)
                urlList.add(url)
                list.add(writer)
                writeDayList.add(writeDay)
                hitsList.add(hits)
            }
        }
        if (!languageCode.equals(Translater_papago.korean)) {
            list.add("부동산학과")
            val translater: Translater_papago = Translater_papago(languageCode, list)
            val temp: ArrayList<String> = translater.translateStr()
            list.clear()
            list.addAll(temp)

            var index: Int = 0
            while (index < (list.size-1) / 2) {
                normalNotice.add(
                    Notice(
                        list[index * 2],
                        urlList[index],
                        list[index * 2 + 1],
                        writeDayList[index],
                        hitsList[index],
                        list[list.size-1]
                    )
                )
                index++
            }
        }

        return true
    }

    fun crawling_BIOLOGY(): Boolean {
//        val listUrl: String = BIOLOGY_LISTURL + pageNum
//
//        val element: Element = Jsoup.connect(listUrl).get()
//
//        if (maxPageNum == null) {
//            val board_box_02: Element = element.getElementsByClass("board_box_02").first()
//            val str: String = board_box_02.child(1).ownText().trim()
//            maxPageNum = str.split("/")[1].toInt()
//        }
//
//        val table: Element = element.getElementsByClass("list").first()
//        val trTags: ArrayList<Element> =
//            table.getElementsByTag("tbody").first().getElementsByTag("tr")
//        for (tr in trTags) {
//            // 상단 고정 공지사항인 경우에 해당
//            if (tr.hasAttr("class")) {
//
//            }
//            // 일반 공지사항인 경우에 해당
//            else {
//
//            }
//        }

        return false
    }

    fun crawling_BIOLOGY_2(): Boolean {


        return false
    }

    fun nextPage(): Boolean {
        val ret: Boolean = ((pageNum?.toInt())!! < maxPageNum!!)
        if (ret)
            pageNum = ((pageNum?.toInt())!! + 1).toString()

        return ret
    }

    fun prevPage(): Boolean {
        val ret: Boolean = ((pageNum?.toInt())!! > 0)
        if (ret)
            pageNum = ((pageNum?.toInt())!! - 1).toString()

        return ret
    }

    fun getTotalNotice(): ArrayList<Notice> {
        val ret: ArrayList<Notice> = ArrayList<Notice>()
        ret.addAll(upperNotice)
        ret.addAll(normalNotice)

        return ret
    }
}