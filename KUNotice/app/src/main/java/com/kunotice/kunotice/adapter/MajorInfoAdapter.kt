package com.kunotice.kunotice.adapter

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.R
import com.kunotice.kunotice.crawling.Professor

class MajorInfoAdapter(var items: ArrayList<Professor>) :
    RecyclerView.Adapter<MajorInfoAdapter.ViewHolder>() {

    // RecyclerView 의 Item 클릭 이벤트 처리 관련 시작
    var listener: OnItemClickListener? = null

    // interface 의 구현은 Notice.kt 파일의 inner class로 구현되어 있음
    interface OnItemClickListener {
        fun onItemClick(v: View, professor: Professor)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }
    // RecyclerView 의 Item 클릭 이벤트 처리 관련 끝

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById(R.id.textView)
            itemView.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    val pos: Int = adapterPosition
                    if (pos != RecyclerView.NO_POSITION) {
                        listener?.onItemClick(v!!, items[pos])
                    }
                }
            })
        }
    }

    // viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MajorInfoAdapter.ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(v)
    }

    // 전체 아이템 개수 리턴
    override fun getItemCount(): Int {
        return items.size
    }

    // position 에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    override fun onBindViewHolder(holder: MajorInfoAdapter.ViewHolder, position: Int) {
        val name: String = if (items[position].name.isBlank()) "???" else items[position].name
        val office: String = if (items[position].office.isBlank()) "없음" else items[position].office
        val tel: String = if (items[position].tel.isBlank()) "없음" else items[position].tel
        val email: String = if (items[position].email.isBlank()) "없음" else items[position].email

        val sb: SpannableStringBuilder = SpannableStringBuilder()
        sb.append(name + "\n" + office + "\n" + tel + "\n" + email)
        sb.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            name.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
//        sb.setSpan(
//            AbsoluteSizeSpan(13, true),
//            name.length,
//            name.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        sb.setSpan(
//            AbsoluteSizeSpan(13, true),
//            items[position].name.length + 5,
//            sb.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
        holder.textView.text = sb
    }
}