package com.kunotice.kunotice.crawling

import android.util.Log
import com.kunotice.kunotice.translate.Translater_papago
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class MajorInfoCrawler(val major: String, val languageCode: String) {

    private val baseUrl: String = "http://www.konkuk.ac.kr/jsp/Coll/coll_"

    // 공과대학 시작
    private val CIVILENV: String = "01_11_01_01_tab01.jsp"
    private val ME: String = "01_04_02_01_tab01.jsp"
    private val EE: String = "01_04_03_01_tab01.jsp"
    private val CHEMENG: String = "01_04_04_02_tab01.jsp"
    private val SW: String = "01_05_01_03_tab01.jsp"
    private val CSE: String = "01_05_02_03_tab01.jsp"
    private val AIF: String = "01_15_01_05_tab01.jsp"
    private val KBEAUTY: String = "01_04_08_01_tab01.jsp"
    private val AEROENG: String = "01_04_06_01_tab01.jsp"
    private val MICROBIO: String = "01_04_04_03_tab01.jsp"
    private val KIES: String = "01_04_05_01_tab01.jsp"
    private val TFE: String = "01_04_09_01_tab01.jsp"
    // 공과대학 끝

    // 문과대학 시작
    private val KOREA: String = "01_01_01_01_tab01.jsp"
    private val ENGLISH: String = "01_01_02_01_tab01.jsp"
    private val CHINA: String = "01_01_02_02_tab01.jsp"
    private val PHILO: String = "01_01_01_02_tab01.jsp"
    private val KHISTORY: String = "01_01_01_03_tab01.jsp"
    private val KUGEO: String = "01_02_01_05_tab01.jsp"
    private val COMM: String = "01_01_03_02_tab01.jsp"
    private val CULTURECONTENTS: String = "01_01_03_04_tab01.jsp"
    // 문과대학 끝

    // 이과대학 시작
    private val MATH: String = "01_02_01_01_tab01.jsp"
    private val PHYS: String = "01_02_01_02_tab01.jsp"
    private val CHEMI: String = "01_02_01_03_tab01.jsp"
    // 이과대학 끝

    // 건축대학 시작
    private val CAKU: String = "01_03_01_04_tab01.jsp"
    // 건축대학 끝

    // 사회과학대학 시작
    private val POL: String = "01_06_01_01_tab01.jsp"
    private val ECON: String = "01_08_01_01_tab01.jsp"
    private val KKUPA: String = "01_06_01_02_tab01.jsp"
    private val ITRADE: String = "01_08_01_02_tab01.jsp"
    private val STAT: String = "01_08_02_01_tab01.jsp"
    private val DOLA: String = "01_15_01_02_tab01.jsp"
    private val DOIS: String = "01_15_01_03_tab01.jsp"
    // 사회과학대학 끝

    // 경영대학 시작
    private val BIZ: String = "01_09_01_01_tab01.jsp"
    private val MOT: String = "01_09_01_03_tab01.jsp"
    // 경영대학 끝

    // KU융합과학기술원 시작
    private val ENERGY: String = "01_20_01_03_tab01.jsp"
    private val SMARTVEHICLE: String = "01_20_01_04_tab01.jsp"
    private val SICTE: String = "01_20_01_05_tab01.jsp"
    private val COSMETICS: String = "01_20_01_06_tab01.jsp"
    private val SCRB: String = "01_20_01_07_tab01.jsp"
    private val BMSE: String = "01_20_01_10_tab01.jsp"
    private val KUSYSBT: String = "01_20_01_08_tab01.jsp"
    private val IBB: String = "01_20_01_09_tab01.jsp"
    // KU융합과학기술원 끝

    // 수의과대학 시작
    private val VET: String = "01_12_01_01_tab01.jsp"
    // 수의과대학 끝

    // 예술디자인대학 시작
    private val DESIGNID: String = "01_13_01_02_tab01.jsp"
    private val APPAREL: String = "01_13_01_03_tab01.jsp"
    private val LIVINGDESIGN: String = "01_13_01_05_tab01.jsp"
    private val CONTEMPORARYART: String = "01_13_02_01_tab01.jsp"
    private val MOVINGIMAGES: String = "01_13_02_05_tab01.jsp"
    // 예술디자인대학 끝

    // 사범대학 시작
    private val JAPAN: String = "01_14_01_01_tab01.jsp"
    private val MATHEDU: String = "01_14_01_02_tab01.jsp"
    private val KUPE: String = "01_14_01_03_tab01.jsp"
    private val MUSIC: String = "01_14_01_04_tab01.jsp"
    private val EDUTECH: String = "01_14_01_05_tab01.jsp"
    private val ENGLISHEDU: String = "01_14_01_06_tab01.jsp"
    private val GYOJIK: String = "01_14_01_07_tab01.jsp"
    // 사범대학 끝

    // 부동산과학원 시작
    private val REALESTATE: String = "http://www.realestate.ac.kr/gb/bbs/board.php?bo_table=faculty"
    // 부동산과학원 끝

    // 상허생명과학대학 시작
    private val ANIS: String = "http://anis.konkuk.ac.kr/html.do?siteId=ANIS&menuSeq=761"
    private val FOODBIO: String = "http://foodbio.konkuk.ac.kr/html.do?siteId=FOODBIO&menuSeq=1748"
    private val KUFSM: String = "http://kufsm.konkuk.ac.kr/html.do?siteId=KUFSM&menuSeq=1967"
    private val EHS: String = "http://ehs.konkuk.ac.kr/html.do?siteId=HEALTHENV&menuSeq=2040"
    private val FLA: String = "http://fla.konkuk.ac.kr/html.do?siteId=FLA&menuSeq=2704"
    // 상허생명과학대학 끝

    // { 0 -> 학과위치, 1 -> 과 홈페이지, 2 -> 전화번호, 3 -> 팩스번호 }
    val majorInfo: ArrayList<String> = ArrayList<String>()
    val professor: ArrayList<Professor> = ArrayList<Professor>()

    fun crawlingInfo(): Boolean {
        return when (major) {
            "ANIS" -> crawlingInfo_3()
            "FOODBIO" -> crawlingInfo_3()
            "KUFSM" -> crawlingInfo_3()
            "EHS" -> crawlingInfo_3()
            "FLA" -> crawlingInfo_3()
            "REALESTATE" -> crawlingInfo_2()
            else -> crawlingInfo_1()
        }
    }

    fun crawlingInfo_3(): Boolean {
//        val connUrl: String = when (major) {
//            "ANIS" -> ANIS
//            "FOODBIO" -> FOODBIO
//            "KUFSM" -> KUFSM
//            "EHS" -> EHS
//            "FLA" -> FLA
//            else -> ""
//        }
        when (major) {
            "ANIS" -> {     // 동물자원과학과

            }
            "FOODBIO" -> {  // 축산식품생명공학과

            }
            "KUFSM" -> {    // 식품유통공학과

            }
            "EHS" -> {      // 환경보건과학과

            }
            "FLA" -> {      // 산림조경학과

            }
            else -> return false
        }

        return true
    }

    fun crawlingInfo_2(): Boolean {
        val element: Element = Jsoup.connect(REALESTATE).get()

        val liTags: ArrayList<Element> =
            element.getElementsByClass("sub0201_list").first().getElementsByTag("li")

        val list: ArrayList<String> = ArrayList<String>()

        for (li in liTags) {
            val dl: Element = li.getElementsByTag("div")[1].child(0)
            val name: String =
                dl.getElementsByTag("dt").first().getElementsByTag("strong").first().ownText()

            val m_major: String = dl.getElementsByTag("dd").first().text()
            val text: String = li.getElementsByTag("div")[1].child(1).text()
            Log.i("text", text)
            var textArray: List<String> = text.split("연구실 :")
            val email: String = textArray[0].split(":")[1].trim()
            textArray = textArray[1].split("연락처 :")
            val office: String = textArray[0].trim()
            textArray = textArray[1].split("http")
            val tel: String = textArray[0].trim()
            list.add(name + " 교수님")
//            list.add(m_major)
            list.add("연구실 : " + office)
            list.add("연락처 : " + tel)
            list.add("e-mail : " + email)
        }

        if (!languageCode.equals(Translater_papago.korean)) {
            val translater: Translater_papago = Translater_papago(languageCode, list)
            val temp: ArrayList<String> = translater.translateStr()
            list.clear()
            list.addAll(temp)
        }

        // 여기서 professor에 저장해주면 된다.
        var i: Int = 0
        while (i < list.size / 4) {
            professor.add(
                Professor(
                    list[i * 4],
                    list[i * 4 + 1],
                    list[i * 4 + 2],
                    list[i * 4 + 3]
//                    list[i * 5 + 4]
                )
            )
            i += 1
        }

        professor.sort()

        return true
    }

    fun crawlingInfo_1(): Boolean {
        val connUrl: String = getUrl()

        if (connUrl.isBlank()) return false

        val element: Element = Jsoup.connect(connUrl).get()

        val box_cont: Element = element.getElementsByClass("box_cont").first()
        val infoList: ArrayList<Element> = box_cont.getElementsByTag("li")

        var index: Int = 0
        val list: ArrayList<String> = ArrayList<String>()
        while (index < infoList.size) {
            when (index) {
                0 -> list.add(infoList[0].ownText())       // 학과위치
                1 -> list.add(infoList[1].ownText())       // 학과 홈페이지
                2 -> list.add(infoList[2].ownText())       // 전화번호
                3 -> list.add(infoList[3].ownText())       // 팩스번호
            }
            index++
        }
        index = infoList.size
        while (index < 4) {
            when (index) {
                0 -> list.add("학과위치 : 없음")
                1 -> list.add("학과홈페이지 : 없음")
                2 -> list.add("전화번호 : 없음")
                3 -> list.add("팩스번호 : 없음")
            }
            index++
        }
        if (!languageCode.equals(Translater_papago.korean)) {
            val translater1: Translater_papago = Translater_papago(languageCode, list)
            majorInfo.addAll(translater1.translateStr())
        } else {
            majorInfo.addAll(list)
        }


        val tables: ArrayList<Element> = element.getElementsByClass("excel mb30")
        lateinit var pfList: ArrayList<Element>
        for (table in tables) {
            if (table.getElementsByTag("colgroup").first().childrenSize() == 5) {
                pfList = table.getElementsByTag("tr")
            }
        }
//        lateinit var pfList: ArrayList<Element> =
//            element.getElementsByClass("excel mb30").first().getElementsByTag("tr")
        val list2: ArrayList<String> = ArrayList<String>()
        if (!pfList.isEmpty()) {
            for (tr in pfList) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")
                if (!td.isEmpty()) {
                    val name: String = td[0].getElementsByTag("a").first().ownText()
//                    val m_major: String = td[1].ownText()
                    val office: String = td[2].ownText()
                    val tel: String = td[3].ownText()
                    val email: String = td[4].ownText()
                    list2.add(name + " 교수님")
//                    list2.add(m_major)
                    list2.add("연구실 : " + office)
                    list2.add("연락처 : " + tel)
                    list2.add("e-mail : " + email)
//                    professor.add(
//                        Professor(
//                            name + " 교수님",
//                            m_major,
//                            "연락처 : " + office,
//                            "전화번호 : " + tel,
//                            "e-mail : " + email
//                        )
//                    )
                }
            }
            if (!languageCode.equals(Translater_papago.korean)) {
                val translater2: Translater_papago = Translater_papago(languageCode, list2)
                val temp: ArrayList<String> = translater2.translateStr()
                list2.clear()
                list2.addAll(temp)
            }

            // 여기서 professor에 저장해주면 된다.
            var i: Int = 0
            while (i < list2.size / 4) {
                professor.add(
                    Professor(
                        list2[i * 4],
                        list2[i * 4 + 1],
                        list2[i * 4 + 2],
                        list2[i * 4 + 3]
//                        translater2.translatedStrs[i * 5 + 4]
                    )
                )
                i += 1
            }


            professor.sort()
        }

        if (major.equals("CSE")) return crawlingSW()

        return true
    }

    fun crawlingSW(): Boolean {
        val connUrl: String = baseUrl + SW

        val element: Element = Jsoup.connect(connUrl).get()

        val box_cont: Element = element.getElementsByClass("box_cont").first()
        val infoList: ArrayList<Element> = box_cont.getElementsByTag("li")

        val list: ArrayList<String> = ArrayList<String>()
        if (infoList.size == 4) {
            if (languageCode.equals(Translater_papago.korean)) {
                majorInfo[0] = majorInfo[0] + "(컴공)\n" + infoList[0].ownText() + "(소웨)"   // 학과위치
                majorInfo[2] = majorInfo[2] + "(컴공)\n" + infoList[2].ownText() + "(소웨)"   // 전화번호
                majorInfo[3] = majorInfo[3] + "(컴공)\n" + infoList[3].ownText() + "(소웨)"   // 팩스번호
            } else {
                list.add("(컴공)?" + infoList[0].ownText() + "(소웨)")
                list.add("(컴공)?" + infoList[2].ownText() + "(소웨)")
                list.add("(컴공)?" + infoList[3].ownText() + "(소웨)")

                val translater1: Translater_papago = Translater_papago(languageCode, list)
                translater1.translateStr()
                majorInfo[0] = majorInfo[0] + translater1.translatedStrs[0].replace("?", "\n")
                majorInfo[2] = majorInfo[2] + translater1.translatedStrs[1].replace("?", "\n")
                majorInfo[3] = majorInfo[3] + translater1.translatedStrs[2].replace("?", "\n")
            }
        }

        val pfList: ArrayList<Element> =
            element.getElementsByClass("excel mb30").first().getElementsByTag("tr")
        val list2: ArrayList<String> = ArrayList<String>()
        if (!pfList.isEmpty()) {
            for (tr in pfList) {
                val td: ArrayList<Element> = tr.getElementsByTag("td")
                if (!td.isEmpty()) {
                    val name: String = td[0].getElementsByTag("a").first().ownText()
//                    val m_major: String = td[1].ownText()
                    val office: String = td[2].ownText()
                    val tel: String = td[3].ownText()
                    val email: String = td[4].ownText()
                    list2.add(name + " 교수님")
//                    list2.add(m_major)
                    list2.add("연구실 : " + office)
                    list2.add("연락처 : " + tel)
                    list2.add("e-mail : " + email)
//                    professor.add(
//                        Professor(
//                            name + " 교수님",
//                            m_major,
//                            "연락처 : " + office,
//                            "전화번호 : " + tel,
//                            "e-mail : " + email
//                        )
//                    )
                }
            }
            if (!languageCode.equals(Translater_papago.korean)) {
                val translater2: Translater_papago = Translater_papago(languageCode, list2)
                val temp: ArrayList<String> = translater2.translateStr()
                list2.clear()
                list2.addAll(temp)
            }

            // 여기서 professor에 저장해주면 된다.
            var i: Int = 0
            while (i < list2.size / 4) {
                professor.add(
                    Professor(
                        list2[i * 4],
                        list2[i * 4 + 1],
                        list2[i * 4 + 2],
                        list2[i * 4 + 3]
//                        translater2.translatedStrs[i * 5 + 4]
                    )
                )
                i += 1
            }

            professor.sort()
            clearDuplication()
        }

        return true
    }

    // 중복으로 존재하는 데이터 삭제 -> 컴공이랑 소웨에 겹치는 교수님이 존재하는건지 학교 홈페이지에 잘못 기재된것인지 불확실
    fun clearDuplication() {
        val duplicationIndex: ArrayList<Int> = ArrayList<Int>()
        var index: Int = professor.size - 1
        if (!professor.isEmpty()) {
            while (index > 0) {
                val checkName = professor[index].name.equals(professor[index - 1].name)
                val checkOffice = professor[index].office.equals(professor[index - 1].office)
                val checkEmail = professor[index].email.equals(professor[index - 1].email)
                if (checkName && checkOffice && checkEmail) duplicationIndex.add(index)
                index--
            }

            for (i in duplicationIndex) {
                professor.removeAt(i)
            }
        }
    }

    fun getUrl(): String {
        return when (major) {
            "CIVILENV" -> baseUrl + CIVILENV
            "ME" -> baseUrl + ME
            "EE" -> baseUrl + EE
            "CHEMENG" -> baseUrl + CHEMENG
            "SW" -> baseUrl + SW
            "CSE" -> baseUrl + CSE
            "AIF" -> baseUrl + AIF
            "KBEAUTY" -> baseUrl + KBEAUTY
            "AEROENG" -> baseUrl + AEROENG
            "MICROBIO" -> baseUrl + MICROBIO
            "KIES" -> baseUrl + KIES
            "TFE" -> baseUrl + TFE
            "KOREA" -> baseUrl + KOREA
            "ENGLISH" -> baseUrl + ENGLISH
            "CHINA" -> baseUrl + CHINA
            "PHILO" -> baseUrl + PHILO
            "KHISTORY" -> baseUrl + KHISTORY
            "KUGEO" -> baseUrl + KUGEO
            "COMM" -> baseUrl + COMM
            "CULTURECONTENTS" -> baseUrl + CULTURECONTENTS
            "MATH" -> baseUrl + MATH
            "PHYS" -> baseUrl + PHYS
            "CHEMI" -> baseUrl + CHEMI
            "CAKU" -> baseUrl + CAKU
            "POL" -> baseUrl + POL
            "ECON" -> baseUrl + ECON
            "KKUPA" -> baseUrl + KKUPA
            "ITRADE" -> baseUrl + ITRADE
            "STAT" -> baseUrl + STAT
            "DOLA" -> baseUrl + DOLA
            "DOIS" -> baseUrl + DOIS
            "BIZ" -> baseUrl + BIZ
            "MOT" -> baseUrl + MOT
            "ENERGY" -> baseUrl + ENERGY
            "SMARTVEHICLE" -> baseUrl + SMARTVEHICLE
            "SICTE" -> baseUrl + SICTE
            "COSMETICS" -> baseUrl + COSMETICS
            "SCRB" -> baseUrl + SCRB
            "BMSE" -> baseUrl + BMSE
            "KUSYSBT" -> baseUrl + KUSYSBT
            "IBB" -> baseUrl + IBB
            "VET" -> baseUrl + VET
            "DESIGNID" -> baseUrl + DESIGNID
            "APPAREL" -> baseUrl + APPAREL
            "LIVINGDESIGN" -> baseUrl + LIVINGDESIGN
            "CONTEMPORARYART" -> baseUrl + CONTEMPORARYART
            "MOVINGIMAGES" -> baseUrl + MOVINGIMAGES
            "JAPAN" -> baseUrl + JAPAN
            "MATHEDU" -> baseUrl + MATHEDU
            "KUPE" -> baseUrl + KUPE
            "MUSIC" -> baseUrl + MUSIC
            "EDUTECH" -> baseUrl + EDUTECH
            "ENGLISHEDU" -> baseUrl + ENGLISHEDU
            "GYOJIK" -> baseUrl + GYOJIK
            else -> ""
        }
    }
}