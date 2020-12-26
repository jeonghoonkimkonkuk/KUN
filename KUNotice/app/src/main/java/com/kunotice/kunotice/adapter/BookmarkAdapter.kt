package com.kunotice.kunotice.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.formats.NativeAdOptions
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.kunotice.kunotice.R
import com.kunotice.kunotice.data.MyListModel

class BookmarkAdapter(var items: ArrayList<MyListModel>, val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // RecyclerView 의 Item 클릭 이벤트 처리 관련 시작
    var itemListener: OnItemClickListener? = null

    // interface 의 구현은 FragmentBookmark.kt 파일의 inner class로 구현되어 있음
    interface OnItemClickListener {
        fun onItemClick(majorName: String, pos: Int, view: View)
    }

    fun setOnItemClickListener(itemListener: OnItemClickListener) {
        this.itemListener = itemListener
    }
    // RecyclerView 의 Item 클릭 이벤트 처리 관련 끝

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            // UI 변수 초기화 시작
            textView = itemView.findViewById(R.id.textView)
            // UI 변수 초기화 끝

            // UI 이벤트 리스너 설정 시작
            itemView.setOnClickListener(ClickedListener())
            // UI 이벤트 리스너 설정 끝
        }

        inner class ClickedListener : View.OnClickListener {
            override fun onClick(v: View?) {
                val pos: Int = adapterPosition
                if (v != null) {
                    itemListener?.onItemClick(items[pos].name, pos, v)
                }
            }
        }
    }

    inner class ViewHolderAdMob(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tpView: TemplateView

        init {
            tpView = itemView.findViewById(R.id.tpAdmob)
            MobileAds.initialize(context)
            lateinit var adLoader: AdLoader
            // ca-app-pub-3940256099942544/2247696110 -> 디버그용 광고 단위 ID
            adLoader = AdLoader.Builder(context, "ca-app-pub-4238407253412923/7956001823")
                .forUnifiedNativeAd(object : UnifiedNativeAd.OnUnifiedNativeAdLoadedListener {
                    override fun onUnifiedNativeAdLoaded(unifiedNativeAd: UnifiedNativeAd?) {
//                    val styles: NativeTemplateStyle = NativeTemplateStyle.Builder().withMainBackgroundColor
//                    templateView.setStyles(styles)
                        tpView.setNativeAd(unifiedNativeAd)
                    }
                })
                .withAdListener(object : AdListener() {
                    override fun onAdFailedToLoad(errorCode: Int) {
                        // Handle the failure by logging, altering the UI, and so on.
                        Log.e("onAdFailedToLoad()", "Ad is not loaded.")
                    }
                })
                .withNativeAdOptions(
                    NativeAdOptions.Builder()
                        // Methods in the NativeAdOptions.Builder class can be
                        // used here to specify individual options settings.
                        .build()
                )
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        }
    }

    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var ret: RecyclerView.ViewHolder
        when (viewType) {
            // 일반적인 경우
            1 -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_2, parent, false)
                ret = ViewHolder(view)
            }
            // 광고인 경우
            2 -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_admob, parent, false)
                ret = ViewHolderAdMob(view)
            }
        }
        return ret
//        val v =
//            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_2, parent, false)
//        return ViewHolder(v)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    // 전체 아이템 개수 리턴
    override fun getItemCount(): Int {
        return items.size
    }

    // position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model: MyListModel = items.get(holder.adapterPosition)
        if (holder.itemViewType == 1) {
            val viewHolder: ViewHolder = holder as ViewHolder
            viewHolder.textView.text = items[position].name
        }
    }

}