package com.kunotice.kunotice.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
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

class SearchListAdapter(
    var filteredList: ArrayList<MyListModel>,
    var unFilteredList: ArrayList<MyListModel>,
    var isBookmark: HashMap<String, Boolean>,
    val majorLanguage: HashMap<String, String>,
    val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    // RecyclerView 의 Item 클릭 이벤트 처리 관련 시작
    var listener: OnItemClickListener? = null

    // interface 의 구현은 Notice.kt 파일의 inner class로 구현되어 있음
    interface OnItemClickListener {
        fun onTextViewClick(v: View, btn: Button, majorName: String)
        fun onBtnClick(v: View, majorName: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    // RecyclerView 의 Item 클릭 이벤트 처리 관련 끝

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var majorTextView: TextView
        var bookmarkBtn: Button

        init {
            bookmarkBtn = itemView.findViewById(R.id.bookmarkBtn)
            bookmarkBtn.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val pos: Int = adapterPosition
                    if (v != null) {
                        listener?.onBtnClick(v, filteredList[pos].name)
                    }
                }

            })

            majorTextView = itemView.findViewById(R.id.majorTextView)
            majorTextView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val pos: Int = adapterPosition
                    if (v != null) {
                        listener?.onTextViewClick(
                            v,
                            bookmarkBtn,
                            filteredList[pos].name
                        )
                    }
                }

            })
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
        return filteredList[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var ret: RecyclerView.ViewHolder
        when (viewType) {
            // 일반적인 경우
            1 -> {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item_3, parent, false)
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
//            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_3, parent, false)
//        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val model: MyListModel = filteredList.get(holder.adapterPosition)
        if (holder.itemViewType == 1) {
            val viewHolder: ViewHolder = holder as ViewHolder
            // 텍스트 설정
            viewHolder.majorTextView.text = filteredList[position].name

            // 즐겨찾기 설정되어 있는 경우는 노란별
            if (isBookmark.get(majorLanguage.get(filteredList[position].name)!!)!!) {
                viewHolder.bookmarkBtn.setBackgroundResource(R.drawable.star_colored)
            }
            // 그렇지 않은 경우는 그냥 별
            else {
                viewHolder.bookmarkBtn.setBackgroundResource(R.drawable.star_uncolored)
            }
        }
    }

    // RecyclerView 의 아이템을 필터링 해주는 함수
    override fun getFilter(): Filter {
        return object : Filter() {
            //
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val str: String = constraint.toString()
                val str_LowerCase: String = str.toLowerCase()
                /*
                 str 대하여
                 1. null
                 2. 아무런 문자도 없는 경우
                 3. 빈칸만 존재하는 문자열
                 1~3에 해당하는 경우에는 return true
                 */
                if (str.isNullOrBlank()) {
                    filteredList = unFilteredList
                } else {
                    val filteringList: ArrayList<MyListModel> = ArrayList<MyListModel>()
                    for (majorName in unFilteredList) {
                        val majorName_LowerCase: String = majorName.name.toLowerCase()
                        if (majorName_LowerCase.contains(str_LowerCase)) {
                            filteringList.add(majorName)
                        }
                    }
                    filteredList = filteringList
                }

                val filterResults: FilterResults = FilterResults()
                filterResults.values = filteredList

                return filterResults
            }

            //
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null) {
                    filteredList = results.values as ArrayList<MyListModel>
                    notifyDataSetChanged()
                }
            }

        }
    }
}