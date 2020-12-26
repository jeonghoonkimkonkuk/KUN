package com.kunotice.kunotice.translate

import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.jsoup.Jsoup

class Translater_kakao(val lang: String, val strs: ArrayList<String>) {

    lateinit var translatedStrs: ArrayList<String>

    companion object {
        val korean = "kr"
        val chinese = "cn"
        val english = "en"
        val japanese = "jp"
    }

    fun translateStr(): ArrayList<String> {
        if (lang == korean) {
            return strs
        }

        translatedStrs = ArrayList<String>()
        var url =
            "https://kapi.kakao.com/v1/translation/translate?src_lang=kr&target_lang=$lang&query="
        for (str in strs) {
            val strr = str.replace("&", "").replace("%", "")
            url += "$strr%0A"
        }
        // 6b766caf2401dbc3a85ea13acc434ea8 - 김정훈 키
        // 405d9ca31b5510b5d2122c14fb0e4e53 - 김병수 키
        val doc =
            Jsoup.connect(url).header("Authorization", "KakaoAK 405d9ca31b5510b5d2122c14fb0e4e53")
                .ignoreContentType(true).get()


        val json = JSONObject(doc.body().text())

        val dd = json.getJSONArray("translated_text")
        for (i in strs.indices) {
            translatedStrs.add((dd[i] as JSONArray)[0].toString())
            Log.i(i.toString(), (dd[i] as JSONArray)[0].toString())
        }

        return translatedStrs
    }

}