package com.kunotice.kunotice.crawling

import android.util.Log
import com.kunotice.kunotice.translate.Translater_papago
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
    about major
    CSE -> 컴퓨터공학부
    EE -> 전기전자공학부
    CHEMENG -> 화학공학부
    KIES -> 산업공학과
    TFE -> 기술융합공학과

    KOREA -> 국어국문학과
    ENGLISH -> 영어영문학과

    CAKU -> 건축학부

    ECON -> 경제학과
    DOLA -> 융합인재학과

    BIZ -> 경영학과

    ENERGY -> 미래에너지공학과
    SMARTVEHICLE -> 스마트운행체공학과
    SICTE -> 스마트ICT융합공학과
    COSMETICS -> 화장품공학과
    BMSE -> 의생명공학과

    DESIGNID -> 산업디자인학과
    MOVINGIMAGES -> 영상영화학과
    
    GYOJIK -> 교직과

    ANIS -> 동물자원과학과
    CROPSCIENCE -> 식량자원과학과
    FOODBIO -> 축산식품생명공학과
    KUFSM -> 식품유통과학과
    EHS -> 환경보건과학과
    FLA -> 산림조경학과
 */
class NoticeCrawler(var isUpperNotice: Boolean, val major: String, val languageCode: String) {

    // 공과대학 시작
    private val CSE_LISTURL: String =
        "http://cse.konkuk.ac.kr/noticeList.do?siteId=CSE&boardSeq=882&menuSeq=6097&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CSE_VIEWURL: String =
        "http://cse.konkuk.ac.kr/noticeView.do?siteId=CSE&boardSeq=882&menuSeq=6097&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val EE_LISTURL: String =
        "http://ee.konkuk.ac.kr/noticeList.do?siteId=EE&boardSeq=424&menuSeq=2837&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val EE_VIEWURL: String =
        "http://ee.konkuk.ac.kr/noticeView.do?siteId=EE&boardSeq=424&menuSeq=2837&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CHEMENG_LISTURL: String =
        "http://chemeng.konkuk.ac.kr/noticeList.do?siteId=CHEMENG&boardSeq=228&menuSeq=1871&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CHEMENG_VIEWURL: String =
        "http://chemeng.konkuk.ac.kr/noticeView.do?siteId=CHEMENG&boardSeq=228&menuSeq=1871&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val KIES_LISTURL: String =
        "http://kies.konkuk.ac.kr/noticeList.do?siteId=KIES&boardSeq=840&menuSeq=5857&&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val KIES_VIEWURL: String =
        "http://kies.konkuk.ac.kr/noticeView.do?siteId=KIES&boardSeq=840&menuSeq=5857&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val TFE_LISTURL: String =
        "http://tfe.konkuk.ac.kr/noticeList.do?siteId=TFE&boardSeq=137&menuSeq=1112&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val TFE_VIEWURL: String =
        "http://tfe.konkuk.ac.kr/noticeView.do?siteId=TFE&boardSeq=137&menuSeq=1112&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 공과대학 끝

    // 문과대학 시작
    private val KOREA_LISTURL: String =
        "http://korea.konkuk.ac.kr/noticeList.do?siteId=KOREA&boardSeq=506&menuSeq=3681&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val KOREA_VIEWRUL: String =
        "http://korea.konkuk.ac.kr/noticeView.do?siteId=KOREA&boardSeq=506&menuSeq=3681&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val ENGLISH_LISTURL: String =
        "http://english.konkuk.ac.kr/noticeList.do?siteId=ENGLISH&boardSeq=590&menuSeq=4595&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val ENGLISH_VIEWURL: String =
        "http://english.konkuk.ac.kr/noticeView.do?siteId=ENGLISH&boardSeq=590&menuSeq=4595&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 문과대학 끝

    // 건축대학 시작
    private val CAKU_LISTURL: String =
        "http://caku.konkuk.ac.kr/noticeList.do?siteId=CAKU&boardSeq=700&menuSeq=5168&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CAKU_VIEWURL: String =
        "http://caku.konkuk.ac.kr/noticeView.do?siteId=CAKU&boardSeq=700&menuSeq=5168&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 건축대학 끝

    // 사회과학대학 시작
    private val ECON_LISTURL: String =
        "http://econ.konkuk.ac.kr/noticeList.do?siteId=ECONOMIC&boardSeq=866&menuSeq=5981&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val ECON_VIEWURL: String =
        "http://econ.konkuk.ac.kr/noticeView.do?siteId=ECONOMIC&boardSeq=866&menuSeq=5981&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val DOLA_LISTURL: String =
        "http://dola.konkuk.ac.kr/noticeList.do?siteId=DOLA&boardSeq=175&menuSeq=1294&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val DOLA_VIEWURL: String =
        "http://dola.konkuk.ac.kr/noticeView.do?siteId=DOLA&boardSeq=175&menuSeq=1294&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 사회과학대학 끝

    // 경영대학 시작
    private val BIZ_LISTURL: String =
        "http://biz.konkuk.ac.kr/noticeList.do?siteId=BIZ&boardSeq=454&menuSeq=3225&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val BIZ_VIEWURL: String =
        "http://biz.konkuk.ac.kr/noticeView.do?siteId=BIZ&boardSeq=454&menuSeq=3225&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 경영대학 끝

    // KU융합과학기술원 시작
    private val ENERGY_LISTURL: String =
        "http://energy.konkuk.ac.kr/noticeList.do?siteId=ENERGY&boardSeq=417&menuSeq=2759&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val ENERGY_VIEWURL: String =
        "http://energy.konkuk.ac.kr/noticeView.do?siteId=ENERGY&boardSeq=417&menuSeq=2759&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val SMARTVEHICLE_LISTURL: String =
        "http://smartvehicle.konkuk.ac.kr/noticeList.do?siteId=SMARTVEHICLE&boardSeq=405&menuSeq=2681&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val SMARTVEHICLE_VIEWURL: String =
        "http://smartvehicle.konkuk.ac.kr/noticeView.do?siteId=SMARTVEHICLE&boardSeq=405&menuSeq=2681&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val SICTE_LISTURL: String =
        "http://sicte.konkuk.ac.kr/noticeList.do?siteId=SICTE&boardSeq=401&menuSeq=2632&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val SICTE_VIEWURL: String =
        "http://sicte.konkuk.ac.kr/noticeView.do?siteId=SICTE&boardSeq=401&menuSeq=2632&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val COSMETICS_LISTURL: String =
        "http://cosmetics.konkuk.ac.kr/noticeList.do?siteId=COSMETICS&boardSeq=297&menuSeq=2251&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val COSMETICS_VIEWURL: String =
        "http://cosmetics.konkuk.ac.kr/noticeView.do?siteId=COSMETICS&boardSeq=297&menuSeq=2251&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val BMSE_LISTURL: String =
        "http://bmse.konkuk.ac.kr/noticeList.do?siteId=BMSE&boardSeq=291&menuSeq=2199&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val BMSE_VIEWURL: String =
        "\"http://bmse.konkuk.ac.kr/noticeView.do?siteId=BMSE&boardSeq=291&menuSeq=2199&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // KU융합과학기술원 끝

    // 수의과대학 시작
    private val VET_LISTURL: String =
        "http://vet.konkuk.ac.kr/noticeList.do?siteId=VETERINARY&boardSeq=475&menuSeq=3427&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val VET_VIEWURL: String =
        "http://vet.konkuk.ac.kr/noticeView.do?siteId=VETERINARY&boardSeq=475&menuSeq=3427&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 수의과대학 끝

    // 예술디자인대학 시작
    private val DESIGNID_LISTURL: String =
        "http://designid.konkuk.ac.kr/noticeList.do?siteId=DESIGNID&boardSeq=439&menuSeq=3015&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val DESIGNID_VIEWURL: String =
        "http://designid.konkuk.ac.kr/noticeView.do?siteId=DESIGNID&boardSeq=439&menuSeq=3015&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val MOVINGIMAGES_LISTURL: String =
        "http://movingimages.konkuk.ac.kr/noticeList.do?siteId=MOVINGIMAGES&boardSeq=580&menuSeq=4508&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val MOVINGIMAGES_VIEWURL: String =
        "http://movingimages.konkuk.ac.kr/noticeView.do?siteId=MOVINGIMAGES&boardSeq=580&menuSeq=4508&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 예술디자인대학 끝

    // 사범대학 시작
    private val GYOJIK_LISTURL: String =
        "http://gyojik.konkuk.ac.kr/noticeList.do?siteId=GYOJIK&boardSeq=452&menuSeq=3147&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val GYOJIK_VIEWURL: String =
        "http://gyojik.konkuk.ac.kr/noticeView.do?siteId=GYOJIK&boardSeq=452&menuSeq=3147&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 사범대학 끝

    // 상허생명과학대학 시작
    private val ANIS_LISTURL: String =
        "http://anis.konkuk.ac.kr/noticeList.do?siteId=ANIS&boardSeq=82&menuSeq=797&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val ANIS_VIEWURL: String =
        "http://anis.konkuk.ac.kr/noticeView.do?siteId=ANIS&boardSeq=82&menuSeq=797&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CROPSCIENCE_LISTURL: String =
        "http://cropscience.konkuk.ac.kr/noticeList.do?siteId=CROPSCIENCE&boardSeq=190&menuSeq=1454&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val CROPSCIENCE_VIEWURL: String =
        "http://cropscience.konkuk.ac.kr/noticeView.do?siteId=CROPSCIENCE&boardSeq=190&menuSeq=1454&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val FOODBIO_LISTURL: String =
        "http://foodbio.konkuk.ac.kr/noticeList.do?siteId=FOODBIO&boardSeq=202&menuSeq=1560&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val FOODBIO_VIEWURL: String =
        "http://foodbio.konkuk.ac.kr/noticeView.do?siteId=FOODBIO&boardSeq=202&menuSeq=1560&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val KUFSM_LISTURL: String =
        "http://kufsm.konkuk.ac.kr/noticeList.do?siteId=KUFSM&boardSeq=262&menuSeq=2004&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val KUFSM_VIEWURL: String =
        "http://kufsm.konkuk.ac.kr/noticeView.do?siteId=KUFSM&boardSeq=262&menuSeq=2004&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val EHS_LISTURL: String =
        "http://ehs.konkuk.ac.kr/noticeList.do?siteId=HEALTHENV&boardSeq=267&menuSeq=2059&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val EHS_VIEWURL: String =
        "http://ehs.konkuk.ac.kr/noticeView.do?siteId=HEALTHENV&boardSeq=267&menuSeq=2059&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val FLA_LISTURL: String =
        "http://fla.konkuk.ac.kr/noticeList.do?siteId=FLA&boardSeq=413&menuSeq=2729&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    private val FLA_VIEWURL: String =
        "http://fla.konkuk.ac.kr/noticeView.do?siteId=FLA&boardSeq=413&menuSeq=2729&searchBy=&searchValue=&categorySeq=&curBoardDispType=LIST&curPage="
    // 상허생명과학대학 끝

    var curPage: String? = null     // 한 번에 확인할 공지사항의 개수를 의미 -> url 구성에 사용됨
    var pageNum: String? = null     // 크롤링할 공지사항 목록의 페이지를 의미 -> url 구성에 사용됨
    var maxPageNum: Int? = null     // 공지사항 목록의 마지막 페이지를 의미

    // 공지사항 목록에서 강조되어 있는 공지사항들을 의미 -> pageNum 값에 따라 변하지 않음
    lateinit var upperNotice: ArrayList<Notice>

    // 그 외의 공지사항들을 의미 -> pageNum 값에 따라 변함
    lateinit var normalNotice: ArrayList<Notice>

    // Secondary Constructor
    constructor(major: String, curPage: String, pageNum: String, languageCode: String) : this(
        false,
        major,
        languageCode
    ) {
        this.curPage = curPage
        this.pageNum = pageNum

        upperNotice = ArrayList<Notice>()
        normalNotice = ArrayList<Notice>()
    }

    // 공지사항 목록을 크롤링하는 함수
    fun crawlingNoticeList(): Boolean {
        // 크롤링할 공지사항 목록 url을 설정
        val listUrl: String = getListUrl()
        // 공지사항 게시글 url을 설정
        val viewUrl: String = getViewUrl()
        if (listUrl.equals("") || viewUrl.equals("")) return false
        Log.i("listUrl", listUrl)
        Log.i("viewUrl", viewUrl)

        val element: Element = Jsoup.connect(listUrl).get();

        // 공지사항 목록 중, 첫 페이지에서만 강조되어 표시되는 공지사항 목록을 크롤링하는 부분
//        upperNotice.clear()
        if (!isUpperNotice) {
            val list: ArrayList<String> = ArrayList()
            val urlList: ArrayList<String> = ArrayList()
            val writeDayList: ArrayList<String> = ArrayList()
            val hitsList: ArrayList<String> = ArrayList()
            val noticeList: Element? = element.getElementById("noticeList")
            if (noticeList != null) {
                val trTags: ArrayList<Element> = noticeList.getElementsByTag("tr")
                for (tr in trTags) {
                    val title: String = tr.getElementsByTag("a")[0].html()
                    val url: String =
                        viewUrl + curPage.toString() + "&pageNum=" + pageNum.toString() + "&seq=" + tr.getElementsByTag(
                            "a"
                        )[0].attributes()
                            .get("data-itsp-view-link")
                    Log.i("noticeList url", url)
                    val writer: String = tr.getElementsByClass("list_break")[0].html().trim()
                    val writeDay: String = tr.getElementsByTag("td")[3].html()
                    val hits: String = tr.getElementsByTag("td")[4].html()

                    if (languageCode.equals(Translater_papago.korean)) {
                        val temp: Notice =
                            Notice(title, url, writer, writeDay, hits, getMajorName())
                        upperNotice.add(temp)
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
                    while (index < (list.size) / 2) {
                        Log.e(index.toString(), list[index * 2])
                        Log.e(index.toString(), list[index * 2 + 1])
                        upperNotice.add(
                            Notice(
                                list[index * 2],
                                urlList[index],
                                list[index * 2 + 1],
                                writeDayList[index],
                                hitsList[index],
                                getMajorName()
                            )
                        )
                        index++
                    }
                }

            }
            isUpperNotice = true

            // 공지사항 목록의 마지막 페이지를 알아내는 부분
            val temp: ArrayList<Element> = element.getElementsByClass("roll last")
            if (!temp.isEmpty()) {
                val rollLast: Element? = temp[0]
                if (rollLast != null) {
                    maxPageNum = rollLast.attributes().get("data-itsp-page-link")
                        .toInt()
                }
            } else {
                maxPageNum = 1
            }
        }

        // 공지사항 목록에 표시되는 일반 공지사항 목록을 크롤링하는 부분
//        normalNotice.clear()
        val dispList: Element? = element.getElementById("dispList")
        if (dispList != null) {
            val list: ArrayList<String> = ArrayList()
            val urlList: ArrayList<String> = ArrayList()
            val writeDayList: ArrayList<String> = ArrayList()
            val hitsList: ArrayList<String> = ArrayList()
            val trTags: ArrayList<Element> = dispList.getElementsByTag("tr")
            for (tr in trTags) {
                val title: String = tr.getElementsByTag("a")[0].html()
                val url: String =
                    viewUrl + curPage.toString() + "&pageNum=" + pageNum.toString() + "&seq=" + tr.getElementsByTag(
                        "a"
                    )[0].attributes()
                        .get("data-itsp-view-link")
                Log.i("dispList url", url)
                val writer: String = tr.getElementsByClass("list_break")[0].html().trim()
                val writeDay: String = tr.getElementsByTag("td")[3].html()
                val hits: String = tr.getElementsByTag("td")[4].html()
                if (languageCode.equals(Translater_papago.korean)) {
                    val temp: Notice =
                        Notice(title, url, writer, writeDay, hits, getMajorName())
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
                while (index < (list.size) / 2) {
                    normalNotice.add(
                        Notice(
                            list[index * 2],
                            urlList[index],
                            list[index * 2 + 1],
                            writeDayList[index],
                            hitsList[index],
                            getMajorName()
                        )
                    )
                    index++
                }
            }

        }

        if (upperNotice.isEmpty() || normalNotice.isEmpty() || maxPageNum == -1) return false

        return true
    }

    fun crawlingNoticeList_2(): Boolean {
        // 크롤링할 공지사항 목록 url을 설정
        val listUrl: String = getListUrl()
        // 공지사항 게시글 url을 설정
        val viewUrl: String = getViewUrl()
        if (listUrl.equals("") || viewUrl.equals("")) return false
        Log.i("listUrl", listUrl)
        Log.i("viewUrl", viewUrl)

        val element: Element = Jsoup.connect(listUrl).get();

        if (!isUpperNotice) {
            isUpperNotice = true

            // 공지사항 목록의 마지막 페이지를 알아내는 부분
            val temp: ArrayList<Element> = element.getElementsByClass("roll last")
            if (!temp.isEmpty()) {
                val rollLast: Element? = temp[0]
                if (rollLast != null) {
                    maxPageNum = rollLast.attributes().get("data-itsp-page-link")
                        .toInt()
                }
            } else {
                maxPageNum = 1
            }
        }

        // 공지사항 목록에 표시되는 일반 공지사항 목록을 크롤링하는 부분
        normalNotice.clear()
        val dispList: Element? = element.getElementById("dispList")
        if (dispList != null) {
            val list: ArrayList<String> = ArrayList()
            val urlList: ArrayList<String> = ArrayList()
            val writeDayList: ArrayList<String> = ArrayList()
            val hitsList: ArrayList<String> = ArrayList()
            val trTags: ArrayList<Element> = dispList.getElementsByTag("tr")
            for (tr in trTags) {
                val title: String = tr.getElementsByTag("a")[0].html()
                val url: String =
                    viewUrl + curPage.toString() + "&pageNum=" + pageNum.toString() + "&seq=" + tr.getElementsByTag(
                        "a"
                    )[0].attributes()
                        .get("data-itsp-view-link")
                Log.i("dispList url", url)
                val writer: String = tr.getElementsByClass("list_break")[0].html().trim()
                val writeDay: String = tr.getElementsByTag("td")[3].html()
                val hits: String = tr.getElementsByTag("td")[4].html()
                if (languageCode.equals(Translater_papago.korean)) {
                    val temp: Notice =
                        Notice(title, url, writer, writeDay, hits, getMajorName())
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
                list.add(getMajorName())
                val translater: Translater_papago = Translater_papago(languageCode, list)
                val temp: ArrayList<String> = translater.translateStr()
                list.clear()
                list.addAll(temp)
                var index: Int = 0
                while (index < (list.size - 1) / 2) {
                    normalNotice.add(
                        Notice(
                            list[index * 2],
                            urlList[index],
                            list[index * 2 + 1],
                            writeDayList[index],
                            hitsList[index],
                            list[list.size - 1]
                        )
                    )
                    index++
                }
            }
        }

        if (normalNotice.isEmpty() || maxPageNum == -1) return false

        return true
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

    fun getListUrl(): String {
        return when (major) {
            "CSE" -> CSE_LISTURL + curPage + "&pageNum=" + pageNum
            "EE" -> EE_LISTURL + curPage + "&pageNum=" + pageNum
            "CHEMENG" -> CHEMENG_LISTURL + curPage + "&pageNum=" + pageNum
            "KIES" -> KIES_LISTURL + curPage + "&pageNum=" + pageNum
            "TFE" -> TFE_LISTURL + curPage + "&pageNum=" + pageNum
            "KOREA" -> KOREA_LISTURL + curPage + "&pageNum=" + pageNum
            "ENGLISH" -> ENGLISH_LISTURL + curPage + "&pageNum=" + pageNum
            "CAKU" -> CAKU_LISTURL + curPage + "&pageNum=" + pageNum
            "ECON" -> ECON_LISTURL + curPage + "&pageNum=" + pageNum
            "DOLA" -> DOLA_LISTURL + curPage + "&pageNum=" + pageNum
            "BIZ" -> BIZ_LISTURL + curPage + "&pageNum=" + pageNum
            "ENERGY" -> ENERGY_LISTURL + curPage + "&pageNum=" + pageNum
            "SMARTVEHICLE" -> SMARTVEHICLE_LISTURL + curPage + " &pageNum=" + pageNum
            "SICTE" -> SICTE_LISTURL + curPage + "&pageNum=" + pageNum
            "COSMETICS" -> COSMETICS_LISTURL + curPage + "&pageNum=" + pageNum
            "BMSE" -> BMSE_LISTURL + curPage + "&pageNum=" + pageNum
            "VET" -> VET_LISTURL + curPage + "&pageNum=" + pageNum
            "DESIGNID" -> DESIGNID_LISTURL + curPage + "&pageNum=" + pageNum
            "MOVINGIMAGES" -> MOVINGIMAGES_LISTURL + curPage + "&pageNum=" + pageNum
            "GYOJIK" -> GYOJIK_LISTURL + curPage + "&pageNum=" + pageNum
            "ANIS" -> ANIS_LISTURL + curPage + "&pageNum=" + pageNum
            "CROPSCIENCE" -> CROPSCIENCE_LISTURL + curPage + "&pageNum=" + pageNum
            "FOODBIO" -> FOODBIO_LISTURL + curPage + "&pageNum=" + pageNum
            "KUFSM" -> KUFSM_LISTURL + curPage + "&pageNum=" + pageNum
            "EHS" -> EHS_LISTURL + curPage + "&pageNum=" + pageNum
            "FLA" -> FLA_LISTURL + curPage + "&pageNum=" + pageNum
            else -> ""
        }
    }

    fun getViewUrl(): String {
        return when (major) {
            "CSE" -> CSE_VIEWURL
            "EE" -> EE_VIEWURL
            "CHEMENG" -> CHEMENG_VIEWURL
            "KIES" -> KIES_VIEWURL
            "TFE" -> TFE_VIEWURL
            "KOREA" -> KOREA_VIEWRUL
            "ENGLISH" -> ENGLISH_VIEWURL
            "CAKU" -> CAKU_VIEWURL
            "ECON" -> ECON_VIEWURL
            "DOLA" -> DOLA_VIEWURL
            "BIZ" -> BIZ_VIEWURL
            "ENERGY" -> ENERGY_VIEWURL
            "SMARTVEHICLE" -> SMARTVEHICLE_VIEWURL
            "SICTE" -> SICTE_VIEWURL
            "COSMETICS" -> COSMETICS_VIEWURL
            "BMSE" -> BMSE_VIEWURL
            "VET" -> VET_VIEWURL
            "DESIGNID" -> DESIGNID_VIEWURL
            "MOVINGIMAGES" -> MOVINGIMAGES_VIEWURL
            "GYOJIK" -> GYOJIK_VIEWURL
            "ANIS" -> ANIS_VIEWURL
            "CROPSCIENCE" -> CROPSCIENCE_VIEWURL
            "FOODBIO" -> FOODBIO_VIEWURL
            "KUFSM" -> KUFSM_VIEWURL
            "EHS" -> EHS_VIEWURL
            "FLA" -> FLA_VIEWURL
            else -> ""
        }
    }

    fun getMajorName(): String {
        return when (major) {
            "CSE" -> "공과대학 컴퓨터공학부"
            "EE" -> "공과대학 전기전자공학부"
            "CHEMENG" -> "공과대학 화학공학부"
            "KIES" -> "공과대학 산업공학과"
            "TFE" -> "공과대학 기술융합공학과"
            "KOREA" -> "문과대학 국어국문학과"
            "ENGLISH" -> "문과대학 영어영문학과"
            "CAKU" -> "건축대학 건축학부"
            "ECON" -> "사회과학대학 경제학과"
            "DOLA" -> "사회과학대학 융합인재학과"
            "BIZ" -> "경영대학 경영학과"
            "ENERGY" -> "KU융합과학기술원 미래에너지공학과"
            "SMARTVEHICLE" -> "KU융합과학기술원 스마트운행체공학과"
            "SICTE" -> "KU융합과학기술원 스마트ICT융합공학과"
            "COSMETICS" -> "KU융합과학기술원 화장품공학과"
            "BMSE" -> "KU융합과학기술원 의생명공학과"
            "VET" -> "수의과대학"
            "DESIGNID" -> "예술디자인대학 산업디자인학과"
            "MOVINGIMAGES" -> "예술디자인대학 영상영화학과"
            "GYOJIK" -> "사범대학 교직과"
            "ANIS" -> "상허생명과학대학 동물자원과학과"
            "CROPSCIENCE" -> "상허생명과학대학 식량자원과학과"
            "FOODBIO" -> "상허생명과학대학 축산식품생명공학과"
            "KUFSM" -> "상허생명과학대학 식품유통공학과"
            "EHS" -> "상허생명과학대학 환경보건과학과"
            "FLA" -> "상허생명과학대학 산림조경학과"
            else -> ""
        }
    }

}