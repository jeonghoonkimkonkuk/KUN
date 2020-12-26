package com.kunotice.kunotice.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
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
import com.kunotice.kunotice.crawling.Notice
import com.kunotice.kunotice.data.MyListModel2

class NoticeListAdapter(
    var items: ArrayList<MyListModel2>,
    var upperNoticeNum: Int,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // RecyclerView 의 Item 클릭 이벤트 처리 관련 시작
    var listener: OnItemClickListener? = null

    // interface 의 구현은 Notice.kt 파일의 inner class로 구현되어 있음
    interface OnItemClickListener {
        fun onItemClick(v: View, notice: Notice)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    // RecyclerView 의 Item 클릭 이벤트 처리 관련 끝

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.textView)
            itemView.setOnClickListener(ClickedListener())
        }

        inner class ClickedListener : View.OnClickListener {
            override fun onClick(v: View?) {
                val pos: Int = adapterPosition
                if (pos != RecyclerView.NO_POSITION) {
                    Log.i("Clicked Notice", items[pos].notice.title)
                    // 여기에 공지사항 게시물 액티비티를 띄워주는 구문 작성하면 된다.
                    listener?.onItemClick(v!!, items[pos].notice)
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

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerView.ViewHolder {
        lateinit var ret: RecyclerView.ViewHolder
        when (viewType) {
            // 일반적인 경우
            1 -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
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
//            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
//        return ViewHolder(v)
    }

    // 전체 아이템 개수 리턴
    override fun getItemCount(): Int {
        return items.size
    }

    // position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model: MyListModel2 = items.get(holder.adapterPosition)
        if (holder.itemViewType == 1) {
            val viewHolder: ViewHolder = holder as ViewHolder

            val sb: SpannableStringBuilder = SpannableStringBuilder()
            sb.append(items[position].notice.title + "\n" + items[position].notice.writer + " | " + items[position].notice.writeDay + " | " + items[position].notice.hits)
            sb.setSpan(
                StyleSpan(Typeface.BOLD),
                0,
                items[position].notice.title.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            sb.setSpan(
                AbsoluteSizeSpan(13, true),
                items[position].notice.title.length,
                sb.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            // 상단에 강조되어 나타나는 공지사항인 경우 -> 공지사항의 제목 색을 변경해서 강조해줌
            if (position < upperNoticeNum) {
                sb.setSpan(
                    ForegroundColorSpan(Color.parseColor("#00A03E")),       // #00A03E or #0F80DA
                    0,
                    sb.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            viewHolder.textView.text = sb
        }
    }
}