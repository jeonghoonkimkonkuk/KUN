package com.kunotice.kunotice.crawling

data class Notice(
    var title: String,
    var url: String,
    var writer: String,
    var writeDay: String,
    var hits: String,
    var majorName: String
) : Comparable<Notice> {
    /*
    날짜 순으로 정렬한다
    return 1    -> this > other
    return 0    -> this == other
    return -1   -> this < other
     */
    override fun compareTo(other: Notice): Int {
        fun getSplitNum(majorName: String): Int {
            return when (majorName) {
                "건국대학교" -> 3
                "공과대학 사회환경공학부" -> 2
                "공과대학 기계공학부" -> 2
                "공과대학 전기전자공학부" -> 1
                "공과대학 화학공학부" -> 1
                "공과대학 컴퓨터공학부" -> 1
                "공과대학 신산업융합학과" -> 2
                "공과대학 K뷰티산업융합학과" -> 2
                "공과대학 항공우주정비시스템학과" -> 2
                "공과대학 생물공학과" -> 2
                "공과대학 산업공학과" -> 1
                "공과대학 기술융합공학과" -> 1
                "문과대학 국어국문학과" -> 1
                "문과대학 영어영문학과" -> 1
                "문과대학 중어중문학과" -> 2
                "문과대학 철확과" -> 2
                "문과대학 사학과" -> 2
                "문과대학 지리학과" -> 2
                "문과대학 미디어커뮤니케이션학과" -> 2
                "문과대학 문화콘텐츠학과" -> 2
                "이과대학 수학과" -> 2
                "이과대학 물리학과" -> 2
                "이과대학 화학과" -> 2
                "사회과학대학 정치외교학과" -> 2
                "사회과학대학 경제학과" -> 1
                "사회과학대학 행정학과" -> 2
                "사회과학대학 국제무역학과" -> 2
                "사회과학대학 응용통계학과" -> 2
                "사회과학대학 융합인재학과" -> 1
                "사회과학대학 글로벌비즈니스학과" -> 2
                "경영대학 경영학과" -> 1
                "경영대학 기술경영학과" -> 2
                "부동산과학원 부동산학과" -> 3
                "KU융합과학기술원 미래에너지공학과" -> 1
                "KU융합과학기술원 스마트운행체공학과" -> 1
                "KU융합과학기술원 스마트ICT융합공학과" -> 1
                "KU융합과학기술원 화장품공학과" -> 1
                "KU융합과학기술원 줄기세포재생공학과" -> 2
                "KU융합과학기술원 의생명공학과" -> 1
                "KU융합과학기술원 시스템생명공학과" -> 2
                "KU융합과학기술원 융합생명공학과" -> 2
                "수의과대학" -> 1
                "예술디자인대학 산업디자인학과" -> 1
                "예술디자인대학 의상디자인학과" -> 2
                "예술디자인대학 리빙디자인학과" -> 2
                "예술디자인대학 현대미술학과" -> 2
                "예술디자인대학 영상영화학과" -> 1
                "사범대학 일어교육과" -> 2
                "사범대학 수학교육과" -> 2
                "사범대학 체육교육과" -> 2
                "사범대학 음악교육과" -> 2
                "사범대학 교육공학과" -> 2
                "사범대학 영어교육과" -> 2
                "사범대학 교직과" -> 1
                "상허생명과학대학 생명과학특성학과" -> 3
                "상허생명과학대학 동물자원과학과" -> 1
                "상허생명과학대학 식량자원과학과" -> 1
                "상허생명과학대학 축산식품생명공학과" -> 1
                "상허생명과학대학 식품유통공학과" -> 1
                "상허생명과학대학 환경보건과학과" -> 1
                "상허생명과학대학 산림조경학과" -> 1
                else -> -1
            }
        }

        val splitNum: Int = getSplitNum(majorName)
        val otherSplitNum: Int = getSplitNum(other.majorName)

        lateinit var day: List<String>
        lateinit var otherDay: List<String>

        if (splitNum == -1 || otherSplitNum == -1) {
            return 0
        }

        when (splitNum) {
            1 -> day = writeDay.split("-")
            2 -> day = writeDay.split(".")
            3 -> day = writeDay.split("-")
        }

        when (otherSplitNum) {
            1 -> otherDay = other.writeDay.split("-")
            2 -> otherDay = other.writeDay.split(".")
            3 -> otherDay = other.writeDay.split("-")
        }

        if (day[0].toInt() > otherDay[0].toInt()) return -1
        if (day[0].toInt() < otherDay[0].toInt()) return 1
        if (day[1].toInt() > otherDay[1].toInt()) return -1
        if (day[1].toInt() < otherDay[1].toInt()) return 1
        if (day[2].toInt() > otherDay[2].toInt()) return -1
        if (day[2].toInt() < otherDay[2].toInt()) return 1
        return 0
    }

}