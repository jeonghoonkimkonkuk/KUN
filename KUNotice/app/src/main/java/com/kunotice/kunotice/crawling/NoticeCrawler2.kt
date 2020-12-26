package com.kunotice.kunotice.crawling

import android.util.Log
import com.kunotice.kunotice.translate.Translater_papago
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/*
    about major
    CIVILENV -> 사회환경공학부
    ME -> 기계공학부
    AIF -> 신산업융합학과
    KBEAUTY -> K뷰티산업융합학과
    AEROENG -> 항공우주정비시스템학과
    MICROBIO -> 생물공학과

    CHINA -> 중어중문학과
    PHILO -> 철학과
    KHISTORY -> 사학과
    KUGEO -> 지리학과
    COMM -> 미디어커뮤니케이션학과
    CULTURECONTENTS -> 문화콘텐츠학과
    
    MATH -> 수학과
    PHYS -> 물리학과
    CHEMI -> 화학과

    POL -> 정치외교학과
    KKUPA -> 행정학과
    ITRADE -> 국제무역학과
    STAT -> 응용통계학과
    DOIS -> 글로벌비즈니스학과
    
    MOT -> 기술경영학과

    SCRB -> 줄기세포재생공학과
    KUSYSBT -> 시스템생명공학과
    IBB -> 융합생명공학과

    APPAREL -> 의상디자인학과
    LIVINGDESIGN -> 리빙디자인학과
    CONTEMPORARYART -> 현대미술학과

    JAPAN -> 일어교육과
    MATHEDU -> 수학교육과
    KUPE -> 체육교육과
    MUSIC -> 음악교육과
    EDUTECH -> 교육공학과
    ENGLISHEDU -> 영어교육과
 */
class NoticeCrawler2(val major: String, val languageCode: String) {

    private val viewUrl: String = "http://home.konkuk.ac.kr/cms/Common/MessageBoard/"

    // 공과대학 시작
    private val CIVILENV_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=17714015&menuId=17667135&sitePath=ceseng&p="
    private val ME_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=16350555&menuId=16346315&sitePath=me&p="
    private val AIF_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=4031738&menuId=4031671&sitePath=aif&p="
    private val KBEAUTY_LISTURL: String =
        "http://home.konkuk.ac.kr:8080/cms/Common/MessageBoard/ArticleList.do?forum=17258184&menuId=17256779&sitePath=kbeauty&p="
    private val AEROENG_LISTURL: String =
        "http://home.konkuk.ac.kr:80/cms/Common/MessageBoard/ArticleList.do?forum=10789&menuId=10771&sitePath=aeroeng&p="
    private val MICROBIO_LISTURL: String =
        "http://home.konkuk.ac.kr:80/cms/Common/MessageBoard/ArticleList.do?forum=7846&menuId=7839&sitePath=microbio&p="
    // 공과대학 끝

    // 문과대학 시작
    private val CHINA_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=5335&p="
    private val PHILO_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=8559&p="
    private val KHISTORY_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=10834&p="
    private val KUGEO_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=7775&p="
    private val COMM_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=11890826&p="
    private val CULTRUECONTENTS_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=8443&p="
    // 문과대학 끝

    // 이과대학 시작
    private val MATH_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=8652&p="
    private val PHYS_LISTURL: String =
        "http://home.konkuk.ac.kr:8080/cms/Common/MessageBoard/ArticleList.do?forum=8744&p="
    private val CHEMI_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=8897&p="
    // 이과대학 끝

    // 사회과학대학 시작
    private val POL_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=6875&p="
    private val KKUPA_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=7160&p="
    private val ITRADE_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=9517&p="
    private val STAT_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=5083&p="
    private val DOIS_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=7521&p="
    // 사회과학대학 끝

    // 경영대학 시작
    private val MOT_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=3329471&menuId=3251241&sitePath=mot&p="
    // 경영대학 끝

    // KU융합과학기술원 시작
    private val SCRB_LISTURL: String =
        "http://home.konkuk.ac.kr:8080/cms/Common/MessageBoard/ArticleList.do?forum=14901014&p="
    private val KUSYSBT_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=14715702&p="
    private val IBB_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=14596747&p="
    // KU융합과학기술원 끝

    // 예술디자인대학 시작
    private val APPAREL_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=9720&p="
    private val LIVINGDESIGN_LISTURL: String =
        "http://home.konkuk.ac.kr:8080/cms/Common/MessageBoard/ArticleList.do?forum=15382254&menuId=15382225&sitePath=living&p="
    private val CONTEMPORARYART_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=3549&menuId=3544&sitePath=artani&p="
    // 예술디자인대학 끝

    // 사범대학 시작
    private val JAPAN_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=12703&p="
    private val MATHEDU_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=4034&p="
    private val KUPE_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=5701&p="
    private val MUSIC_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=9801&p="
    private val EDUTECH_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=11707&p="
    private val ENGLISHEDU_LISTURL: String =
        "http://home.konkuk.ac.kr/cms/Common/MessageBoard/ArticleList.do?forum=11707&p="
    // 사범대학 끝

    var pageNum: String? = null         // 크롤링할 공지사항 목록의 페이지를 의미 -> url 구성에 사용됨
    var maxPageNum: Int? = null         // 공지사항 목록의 마지막 페이지를 의미

    lateinit var upperNotice: ArrayList<Notice>
    lateinit var normalNotice: ArrayList<Notice>

    // Secondary Constructor
    constructor(major: String, pageNum: String, languageCode: String) : this(major, languageCode) {
        this.pageNum = pageNum

        upperNotice = ArrayList<Notice>()
        normalNotice = ArrayList<Notice>()
    }

    fun crawlingNoticeList(): Boolean {
        // 크롤링할 공지사항 목록 url을 설정
        val listUrl: String = getListUrl()

        if (listUrl.isBlank()) return false

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val txt_totalNum: Element = element.getElementsByClass("txt_totalnum").first()
            maxPageNum = txt_totalNum.getElementsByTag("span")[2].html().toInt()
        }

        val board: Element = element.getElementsByClass("board").first()
        val trTags: ArrayList<Element> = board.getElementsByTag("tbody").first().children()

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        val isUpper: ArrayList<Boolean> = ArrayList()
        for (tr in trTags) {
            val td: ArrayList<Element> = tr.getElementsByTag("td")

            if (td[1].childrenSize() == 0) continue
            val title: String = td[1].child(0).html()
            val url: String = viewUrl + td[1].child(0).attr("href")
            Log.i("noticeList url", url)
            val writer: String = td[3].html()
            val writeDay: String = td[4].html()
            val hits: String = td[5].html()

            if (languageCode.equals(Translater_papago.korean)) {
                val temp: Notice = Notice(title, url, writer, writeDay, hits, getMajorName())
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
                            getMajorName()
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
                            getMajorName()
                        )
                    )
                }
                index++
            }
        }

        return true
    }

    fun crawlingNoticeList_2(): Boolean {
        // 크롤링할 공지사항 목록 url을 설정
        val listUrl: String = getListUrl()

        if (listUrl.isBlank()) return false

        val element: Element = Jsoup.connect(listUrl).get()

        // 공지사항 목록의 마지막 페이지수를 알아내는 작업
        if (maxPageNum == null) {
            val txt_totalNum: Element = element.getElementsByClass("txt_totalnum").first()
            maxPageNum = txt_totalNum.getElementsByTag("span")[2].html().toInt()
        }

        normalNotice.clear()
        val board: Element = element.getElementsByClass("board").first()
        val trTags: ArrayList<Element> = board.getElementsByTag("tbody").first().children()

        val list: ArrayList<String> = ArrayList()
        val urlList: ArrayList<String> = ArrayList()
        val writeDayList: ArrayList<String> = ArrayList()
        val hitsList: ArrayList<String> = ArrayList()
        for (tr in trTags) {
            val td: ArrayList<Element> = tr.getElementsByTag("td")

            val title: String = td[1].child(0).html()
            val url: String = viewUrl + td[1].child(0).attr("href")
            Log.i("noticeList url", url)
            val writer: String = td[3].html()
            val writeDay: String = td[4].html()
            val hits: String = td[5].html()
            if (languageCode.equals(Translater_papago.korean)) {
                val temp: Notice = Notice(title, url, writer, writeDay, hits, getMajorName())
                normalNotice.add(temp)
            } else {
                list.add(title)
                urlList.add(url)
                list.add(writer)
                writeDayList.add(writeDay)
                hitsList.add(hits)
            }
        }

        if (!languageCode.equals(Translater_papago.korean)){
            list.add(getMajorName())
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

    fun nextPage(): Boolean {
        val ret: Boolean = ((pageNum?.toInt())!! < maxPageNum!! - 1)
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
            "CIVILENV" -> CIVILENV_LISTURL + pageNum
            "ME" -> ME_LISTURL + pageNum
            "AIF" -> AIF_LISTURL + pageNum
            "KBEAUTY" -> KBEAUTY_LISTURL + pageNum
            "AEROENG" -> AEROENG_LISTURL + pageNum
            "MICROBIO" -> MICROBIO_LISTURL + pageNum
            "CHINA" -> CHINA_LISTURL + pageNum
            "PHILO" -> PHILO_LISTURL + pageNum
            "KHISTORY" -> KHISTORY_LISTURL + pageNum
            "KUGEO" -> KUGEO_LISTURL + pageNum
            "COMM" -> COMM_LISTURL + pageNum
            "CULTURECONTENTS" -> CULTRUECONTENTS_LISTURL + pageNum
            "MATH" -> MATH_LISTURL + pageNum
            "PHYS" -> PHYS_LISTURL + pageNum
            "CHEMI" -> CHEMI_LISTURL + pageNum
            "POL" -> POL_LISTURL + pageNum
            "KKUPA" -> KKUPA_LISTURL + pageNum
            "ITRADE" -> ITRADE_LISTURL + pageNum
            "STAT" -> STAT_LISTURL + pageNum
            "DOIS" -> DOIS_LISTURL + pageNum
            "MOT" -> MOT_LISTURL + pageNum
            "SCRB" -> SCRB_LISTURL + pageNum
            "KUSYSBT" -> KUSYSBT_LISTURL + pageNum
            "IBB" -> IBB_LISTURL + pageNum
            "APPAREL" -> APPAREL_LISTURL + pageNum
            "LIVINGDESIGN" -> LIVINGDESIGN_LISTURL + pageNum
            "CONTEMPORARYART" -> CONTEMPORARYART_LISTURL + pageNum
            "JAPAN" -> JAPAN_LISTURL + pageNum
            "MATHEDU" -> MATHEDU_LISTURL + pageNum
            "KUPE" -> KUPE_LISTURL + pageNum
            "MUSIC" -> MUSIC_LISTURL + pageNum
            "EDUTECH" -> EDUTECH_LISTURL + pageNum
            "ENGLISHEDU" -> ENGLISHEDU_LISTURL + pageNum
            else -> ""
        }
    }

    fun getMajorName(): String {
        return when (major) {
            "CIVILENV" -> "공과대학 사회환경공학부"
            "ME" -> "공과대학 기계공학부"
            "AIF" -> "공과대학 신산업융합학과"
            "KBEAUTY" -> "공과대학 K뷰티산업융합학과"
            "AEROENG" -> "공과대학 항공우주정비시스템학과"
            "MICROBIO" -> "공과대학 생물공학과"
            "CHINA" -> "문과대학 중어중문학과"
            "PHILO" -> "문과대학 철학과"
            "KHISTORY" -> "문과대학 사학과"
            "KUGEO" -> "문과대학 지리학과"
            "COMM" -> "문과대학 미디어커뮤니케이션학과"
            "CULTURECONTENTS" -> "문과대학 문화콘텐츠학과"
            "MATH" -> "이과대학 수학과"
            "PHYS" -> "이과대학 물리학과"
            "CHEMI" -> "이과대학 화학과"
            "POL" -> "사회과학대학 정치외교학과"
            "KKUPA" -> "사회과학대학 행정학과"
            "ITRADE" -> "사회과학대학 국제무역학과"
            "STAT" -> "사회과학대학 응용통계학과"
            "DOIS" -> "사회과학대학 글로벌비즈니스학과"
            "MOT" -> "경영대학 기술경영학과"
            "SCRB" -> "KU융합과학기술원 줄기세포재생공학과"
            "KUSYSBT" -> "KU융합과학기술원 시스템생명공학과"
            "IBB" -> "KU융합과학기술원 융합생명공학과"
            "APPAREL" -> "예술디자인대학 의상디자인학과"
            "LIVINGDESIGN" -> "예술디자인대학 리빙디자인학과"
            "CONTEMPORARYART" -> "예술디자인대학 현대미술학과"
            "JAPAN" -> "사범대학 일어교육과"
            "MATHEDU" -> "사범대학 수학교육과"
            "KUPE" -> "사범대학 체육교육과"
            "MUSIC" -> "사범대학 음악교육과"
            "EDUTECH" -> "사범대학 교육공학과"
            "ENGLISHEDU" -> "사범대학 영어교육과"
            else -> ""
        }
    }
}