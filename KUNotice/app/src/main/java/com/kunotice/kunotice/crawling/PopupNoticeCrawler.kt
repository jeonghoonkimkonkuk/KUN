package com.kunotice.kunotice.crawling

import com.kunotice.kunotice.translate.Translater_papago
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class PopupNoticeCrawler(val languageCode: String) {

    val connUrl: String = "https://tastycodee.tistory.com/3"

    fun crawling(): String {
        var ret: String = ""

        val element: Element = Jsoup.connect(connUrl).get()

        val article: Element = element.getElementsByClass("tt_article_useless_p_margin").first()
        val divs: List<Element> = article.getElementsByTag("div")
        for (div in divs) {
            div.remove()
        }

        val list: List<String> = article.text()
            .split("-----------------------------------------------------------------------------------")
        when (languageCode) {
            Translater_papago.korean -> {
                ret += list[0]
            }
            Translater_papago.english -> {
                ret += list[1]
            }
            Translater_papago.chinese -> {
                ret += list[2]
            }
            Translater_papago.japanese -> {
                ret += list[3]
            }
            else -> {
                ret += list[0]
            }
        }

        return ret
    }
}