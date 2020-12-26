package com.kunotice.kunotice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import com.kunotice.kunotice.translate.Translater_papago
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import kotlinx.android.synthetic.main.activity_popup_map_view.*

class PopupMapView : FragmentActivity(), OnMapReadyCallback {

    private val KU: LatLng = LatLng(37.543264, 127.076049)  // 건국대학교 서울캠퍼스

    // 공과대학 시작
    private val CIVILENV: LatLng = LatLng(37.541601, 127.078838)    // 공학관 A동
    private val ME: LatLng = LatLng(37.541601, 127.078838)
    private val EE: LatLng = LatLng(37.541601, 127.078838)
    private val CHEMENG: LatLng = LatLng(37.541601, 127.078838)
    private val CSE: LatLng = LatLng(37.541601, 127.078838)
    private val TFE: LatLng = LatLng(37.541601, 127.078838)
    private val MICROBIO: LatLng = LatLng(37.541953, 127.079621)     // 공학관 B동
    private val KIES: LatLng = LatLng(37.541953, 127.079621)
    private val AEROENG: LatLng = LatLng(37.541181, 127.079602)      // 공학관 C동
    private val KBEAUTY: LatLng = LatLng(37.539661, 127.073137)      // 산학협동관
    private val AIF: LatLng = LatLng(37.54112, 127.081633)           // 창의관
    // 공과대학 끝

    // 문과대학 시작
    private val KOREA: LatLng = LatLng(37.542433, 127.078807)       // 문과대학 강의동
    private val ENGLISH: LatLng = LatLng(37.542717, 127.078156)     // 문과대학 연구동
    private val CHINA: LatLng = LatLng(37.542717, 127.078156)
    private val PHILO: LatLng = LatLng(37.542717, 127.078156)
    private val KHISTORY: LatLng = LatLng(37.542717, 127.078156)
    private val KUGEO: LatLng = LatLng(37.542717, 127.078156)
    private val COMM: LatLng = LatLng(37.542717, 127.078156)
    private val CULTURECONTENTS: LatLng = LatLng(37.542717, 127.078156)
    // 문과대학 끝

    // 이과대학 시작
    private val MATH: LatLng = LatLng(37.541546, 127.080503)        // 과학관
    private val PHYS: LatLng = LatLng(37.541546, 127.080503)
    private val CHEMI: LatLng = LatLng(37.541546, 127.080503)
    // 이과대학 끝

    // 건축대학 시작
    private val CAKU: LatLng = LatLng(37.543503, 127.078556)        // 건축대학
    // 건축대학 끝

    // 사회과학대학 시작
    private val POL: LatLng = LatLng(37.544165, 127.075382)         // 상허연구관
    private val ECON: LatLng = LatLng(37.544165, 127.075382)
    private val KKUPA: LatLng = LatLng(37.544165, 127.075382)
    private val ITRADE: LatLng = LatLng(37.544165, 127.075382)
    private val STAT: LatLng = LatLng(37.544165, 127.075382)
    private val DOLA: LatLng = LatLng(37.544165, 127.075382)
    private val DOIS: LatLng = LatLng(37.544165, 127.075382)
    // 사회과학대학 끝

    // 경영대학 시작
    private val BIZ: LatLng = LatLng(37.544261, 127.076116)         // 경영관
    private val MOT: LatLng = LatLng(37.544261, 127.076116)
    // 경영대학 끝

    // 부동산과학원 시작
    private val REALESTATE: LatLng = LatLng(37.543409, 127.07814)   // 해봉부동산학관
    // 부동산과학원 끝

    // KU융합과학기술원 시작
    private val ENERGY: LatLng = LatLng(37.540966, 127.074495)      // 생명과학관
    private val SMARTVEHICLE: LatLng = LatLng(37.540966, 127.074495)
    private val SICTE: LatLng = LatLng(37.540966, 127.074495)
    private val COSMETICS: LatLng = LatLng(37.540966, 127.074495)
    private val SCRB: LatLng = LatLng(37.540966, 127.074495)
    private val BMSE: LatLng = LatLng(37.540966, 127.074495)
    private val KUSYSBT: LatLng = LatLng(37.540966, 127.074495)
    private val IBB: LatLng = LatLng(37.540966, 127.074495)
    // KU융합과학기술원 끝

    // 수의과대학 시작
    private val VET: LatLng = LatLng(37.539185, 127.074712)         // 수의과대학
    // 수의과대학 끝

    // 예술디자인대학 시작
    private val DESIGNID: LatLng = LatLng(37.542865, 127.073136)    // 예술문화관
    private val APPAREL: LatLng = LatLng(37.542865, 127.073136)
    private val LIVINGDESIGN: LatLng = LatLng(37.542865, 127.073136)
    private val CONTEMPORARYART: LatLng = LatLng(37.542865, 127.073136)
    private val MOVINGIMAGES: LatLng = LatLng(37.542865, 127.073136)
    // 예술디자인대학 끝

    // 사범대학 시작
    private val JAPAN: LatLng = LatLng(37.543982, 127.074265)       // 교육과학관
    private val MATHEDU: LatLng = LatLng(37.543982, 127.074265)
    private val MUSIC: LatLng = LatLng(37.543982, 127.074265)
    private val EDUTECH: LatLng = LatLng(37.543982, 127.074265)
    private val ENGLISHEDU: LatLng = LatLng(37.543982, 127.074265)
    private val GYOJIK: LatLng = LatLng(37.543982, 127.074265)
    private val KUPE: LatLng = LatLng(37.544422, 127.079777)        // 실내체육관
    // 사범대학 끝

    lateinit var majorCode: String
    lateinit var languageCode: String
    lateinit var coordinates: LatLng

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lpWindow: WindowManager.LayoutParams = WindowManager.LayoutParams()
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND
        lpWindow.dimAmount = 0.6f
        window.attributes = lpWindow

        setContentView(R.layout.activity_popup_map_view)

        majorLocation.text = intent.getStringExtra("majorLocation")!!
        majorCode = intent.getStringExtra("majorCode")!!
        languageCode = intent.getStringExtra("languageCode")!!
        coordinates = getLatLng()

        if (coordinates.equals(KU)) {
            map.visibility = View.GONE
            noMapView.visibility = View.VISIBLE

            noMapView.text = when (languageCode) {
                Translater_papago.korean -> "지도 기능을 지원하지 않는 학과입니다."
                Translater_papago.english -> "A department that does not support map functions."
                Translater_papago.chinese -> "一个不支持地图功能的部门。"
                Translater_papago.japanese -> "地図機能をサポートしていない部門。"
                else -> "지도 기능을 지원하지 않는 학과입니다."
            }
        } else {
            val fm = supportFragmentManager
            val options = NaverMapOptions().camera(CameraPosition(coordinates, 16.0))
            val mapFragment = fm.findFragmentById(R.id.map) as MapFragment?
                ?: MapFragment.newInstance(options).also {
                    fm.beginTransaction().add(R.id.map, it).commit()
                }
            mapFragment.getMapAsync(this)
        }

    }

    override fun onMapReady(naverMap: NaverMap) {
        val marker: Marker = Marker()
        marker.position = coordinates
//        marker.captionText = majorLocation
//        marker.captionRequestedWidth = 100
//        marker.captionOffset = 10
        marker.map = naverMap
    }

    fun onCloseBtnClicked(view: View) {
        val intent: Intent = Intent()
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun getLatLng(): LatLng {
        return when (majorCode) {
            "CIVILENV" -> CIVILENV
            "ME" -> ME
            "EE" -> EE
            "CHEMENG" -> CHEMENG
            "CSE" -> CSE
            "TFE" -> TFE
            "MICROBIO" -> MICROBIO
            "KIES" -> KIES
            "AEROENG" -> AEROENG
            "KBEAUTY" -> KBEAUTY
            "AIF" -> AIF
            "KOREA" -> KOREA
            "ENGLISH" -> ENGLISH
            "CHINA" -> CHINA
            "PHILO" -> PHILO
            "KHISTORY" -> KHISTORY
            "KUGEO" -> KUGEO
            "COMM" -> COMM
            "CULTURECONTENTS" -> CULTURECONTENTS
            "MATH" -> MATH
            "PHYS" -> PHYS
            "CHEMI" -> CHEMI
            "CAKU" -> CAKU
            "POL" -> POL
            "ECON" -> ECON
            "KKUPA" -> KKUPA
            "ITRADE" -> ITRADE
            "STAT" -> STAT
            "DOLA" -> DOLA
            "DOIS" -> DOIS
            "BIZ" -> BIZ
            "MOT" -> MOT
            "REALESTATE" -> REALESTATE
            "ENERGY" -> ENERGY
            "SMARTVEHICLE" -> SMARTVEHICLE
            "SICTE" -> SICTE
            "COSMETICS" -> COSMETICS
            "SCRB" -> SCRB
            "BMSE" -> BMSE
            "KUSYSBT" -> KUSYSBT
            "IBB" -> IBB
            "VET" -> VET
            "DESIGNID" -> DESIGNID
            "APPAREL" -> APPAREL
            "LIVINGDESIGN" -> LIVINGDESIGN
            "CONTEMPORARYART" -> CONTEMPORARYART
            "MOVINGIMAGES" -> MOVINGIMAGES
            "JAPAN" -> JAPAN
            "MATHEDU" -> MATHEDU
            "MUSIC" -> MUSIC
            "EDUTECH" -> EDUTECH
            "ENGLISHEDU" -> ENGLISHEDU
            "GYOJIK" -> GYOJIK
            "KUPE" -> KUPE
            else -> KU
        }
    }

}