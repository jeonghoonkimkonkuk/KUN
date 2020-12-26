package com.kunotice.kunotice.adapter

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
import com.kunotice.kunotice.R
import com.kunotice.kunotice.crawling.Notice

class FragmentNoticeListAdapter(var items: ArrayList<Notice>) :
    RecyclerView.Adapter<FragmentNoticeListAdapter.ViewHolder>() {

    // RecyclerView 의 Item 클릭 이벤트 처리 관련 시작
    var listener: OnItemClickListener? = null

    // interface 의 구현은 FragmentNotice.kt 파일에 구현되어 있음
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
                    Log.i("Clicked Notice", items[pos].title)
                    // 여기에 공지사항 게시물 액티비티를 띄워주는 구문 작성하면 된다.
                    listener?.onItemClick(v!!, items[pos])
                }
            }
        }
    }

    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FragmentNoticeListAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(v)
    }

    // 전체 아이템 개수 리턴
    override fun getItemCount(): Int {
        return items.size
    }

    // position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    override fun onBindViewHolder(holder: FragmentNoticeListAdapter.ViewHolder, position: Int) {
        val sb: SpannableStringBuilder = SpannableStringBuilder()
        sb.append(items[position].majorName + "\n" + items[position].title + "\n" + items[position].writer + " | " + items[position].writeDay + " | " + items[position].hits)
        sb.setSpan(
            ForegroundColorSpan(Color.parseColor("#00A03E")),
            0,
            items[position].majorName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        sb.setSpan(
            StyleSpan(Typeface.BOLD),
            items[position].majorName.length,
            items[position].majorName.length + items[position].title.length + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        sb.setSpan(
            AbsoluteSizeSpan(13, true),
            items[position].majorName.length + items[position].title.length + 1,
            sb.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        holder.textView.text = sb
    }
}