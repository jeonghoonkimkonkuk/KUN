package com.kunotice.kunotice.translate

import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Translater_papago(val lang: String, val strs: ArrayList<String>) {

    lateinit var translatedStrs: ArrayList<String>

    companion object {
        val korean = "ko"
        val english = "en"
        val chinese = "zh-CN"   // 중국어 간체
        val japanese = "ja"
    }

    fun translateStr(): ArrayList<String> {
        // Json 데이터가 담겨있는 텍스트
        var outputText: String = ""
        // 애플리케이션 클라이언트 아이디값
        val clientId = "7ppjv0bhr3"
        // 애플리케이션 클라이언트 시크릿값
        val clientSecret = "Zavuxa7w0H4deqLwqIIVsPzrhFzZ595G2PcJ3tuU"

        try {
            var input: String = ""
            for (i in strs.indices) {
                if (i == strs.size - 1) {
                    input += strs[i]
                } else {
                    input += strs[i] + "\n"
                }
            }
            val text = URLEncoder.encode(input, "UTF-8")
            val apiURL = "https://naveropenapi.apigw.ntruss.com/nmt/v1/translation"
            val url = URL(apiURL)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.setRequestMethod("POST")
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId)
            con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret)
            // post request
            val postParams = "source=ko&target=$lang&text=$text"
            con.setDoOutput(true)
            val wr = DataOutputStream(con.getOutputStream())
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode: Int = con.getResponseCode()
            val br: BufferedReader
            if (responseCode == 200) { // 정상 호출
                br = BufferedReader(InputStreamReader(con.getInputStream()))
            } else {  // 에러 발생
                br = BufferedReader(InputStreamReader(con.getErrorStream()))
            }
            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            br.close()
            Log.e("try", response.toString())

            outputText += response.toString()
        } catch (e: Exception) {
            Log.e("catch", e.toString())
            return ArrayList<String>()
        }

        val json: JSONObject = JSONObject(outputText)
        val result: JSONObject = json.getJSONObject("message").getJSONObject("result")
        val temp: List<String> = result.getString("translatedText").split("\n")

        translatedStrs = ArrayList()
        translatedStrs.addAll(temp)

        return translatedStrs
    }

}