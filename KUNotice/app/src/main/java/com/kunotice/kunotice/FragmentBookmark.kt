package com.kunotice.kunotice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.adapter.BookmarkAdapter
import com.kunotice.kunotice.data.Majors
import com.kunotice.kunotice.data.MyListModel
import com.kunotice.kunotice.translate.Translater_papago

class FragmentBookmark(
    val majors: Majors,
    val bookmarkList: ArrayList<String>,
    val majorMap: HashMap<String, String>,
    var languageCode: String
) :
    Fragment() {

    interface OnFragmentInteraction {
        fun bookmarkDelete(majorName: String, majorCode: String)
    }

    fun setOnFragmentInteraction(fragmentListener: OnFragmentInteraction) {
        this.fragmentListener = fragmentListener
    }

    var fragmentListener: OnFragmentInteraction? = null

    lateinit var noBookmarkText: TextView
    lateinit var bookmarkListView: RecyclerView
    lateinit var adapter: BookmarkAdapter

    val otherLanguageToKorean: HashMap<String, String> = HashMap<String, String>()

    // 외국어로 번역된 학과 이름 리스트
    val bookmarkList_otherLanguage: ArrayList<String> = ArrayList<String>()

    val mList: ArrayList<MyListModel> = ArrayList<MyListModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup =
            inflater.inflate(R.layout.fragment_bookmark, container, false) as ViewGroup

        noBookmarkText = rootView.findViewById(R.id.noBookmarkText)
        isBookmarkExist()

        init()

        bookmarkListView = rootView.findViewById(R.id.bookmarkListView)
        bookmarkListView.layoutManager = LinearLayoutManager(context)

        mList.add(MyListModel("AD", 2))
        for (i in bookmarkList_otherLanguage.indices) {
            mList.add(MyListModel(bookmarkList_otherLanguage[i], 1))
        }
        adapter = BookmarkAdapter(mList, context!!)
        bookmarkListView.adapter = adapter
        adapter.notifyDataSetChanged()
        adapter.setOnItemClickListener(ItemClickListener())

        return rootView
    }

    // MainActivity 에서 변경한 북마킹 정보에 따라 변수를 업데이트 하는 함수
    fun bookmarkChanged(krMajorName: String, how: Int) {
        // 즐겨찾기가 새롭게 추가된 경우
        if (how == 1) {
            val index: Int = majors.majorList_korean.indexOf(krMajorName)
            when (languageCode) {
                Translater_papago.korean -> {
                    bookmarkList_otherLanguage.add(krMajorName)
                    otherLanguageToKorean.put(krMajorName, krMajorName)
                }
                Translater_papago.english -> {
                    bookmarkList_otherLanguage.add(majors.majorList_english[index])
                    otherLanguageToKorean.put(majors.majorList_english[index], krMajorName)
                }
                Translater_papago.chinese -> {
                    bookmarkList_otherLanguage.add(majors.majorList_chinese[index])
                    otherLanguageToKorean.put(majors.majorList_chinese[index], krMajorName)
                }
                Translater_papago.japanese -> {
                    bookmarkList_otherLanguage.add(majors.majorList_japanese[index])
                    otherLanguageToKorean.put(majors.majorList_japanese[index], krMajorName)
                }
            }
        }
        // 즐겨찾기가 제거된 경우
        else if (how == -1) {
            var index: Int = -1
            for (i in bookmarkList_otherLanguage.indices) {
                if (!bookmarkList.contains(otherLanguageToKorean.get(bookmarkList_otherLanguage[i]))) {
                    index = i
                    break
                }
            }
            if (index != -1) {
                otherLanguageToKorean.remove(bookmarkList_otherLanguage[index])
                bookmarkList_otherLanguage.removeAt(index)
            }
        }

        mList.clear()
        mList.add(MyListModel("AD", 2))
        for (i in bookmarkList_otherLanguage.indices) {
            mList.add(MyListModel(bookmarkList_otherLanguage[i], 1))
        }
        adapter.notifyDataSetChanged()
        isBookmarkExist()
    }

    fun init() {
        val text: String = when (languageCode) {
            Translater_papago.korean -> "등록된 즐겨찾기가 없습니다.\n즐겨찾기를 추가하고 사용해 주세요!"
            Translater_papago.english -> "No registered favorites.\nPlease add and use your favorites!"
            Translater_papago.chinese -> "没有注册的喜爱。\n请添加和使用您的喜爱!"
            Translater_papago.japanese -> "登録されたお気に入りはありません。\nお気に入りを追加して使用してください!"
            else -> "등록된 즐겨찾기가 없습니다.\n즐겨찾기를 추가하고 사용해 주세요!"
        }
        noBookmarkText.text = text

        if (!bookmarkList.isEmpty()) {
            when (languageCode) {
                Translater_papago.korean -> {
                    bookmarkList_otherLanguage.addAll(bookmarkList)
                    for (str in bookmarkList) {
                        otherLanguageToKorean.put(str, str)
                    }
                }
                Translater_papago.english -> {
                    for (str in bookmarkList) {
                        val index: Int = majors.majorList_korean.indexOf(str)
                        if (index != -1) {
                            bookmarkList_otherLanguage.add(majors.majorList_english[index])
                            otherLanguageToKorean.put(majors.majorList_english[index], str)
                        }
                    }
                }
                Translater_papago.chinese -> {
                    for (str in bookmarkList) {
                        val index: Int = majors.majorList_korean.indexOf(str)
                        if (index != -1) {
                            bookmarkList_otherLanguage.add(majors.majorList_chinese[index])
                            otherLanguageToKorean.put(majors.majorList_chinese[index], str)
                        }
                    }
                }
                Translater_papago.japanese -> {
                    for (str in bookmarkList) {
                        val index: Int = majors.majorList_korean.indexOf(str)
                        if (index != -1) {
                            bookmarkList_otherLanguage.add(majors.majorList_japanese[index])
                            otherLanguageToKorean.put(majors.majorList_japanese[index], str)
                        }
                    }
                }
            }
        }

    }

    fun isBookmarkExist() {
        if (bookmarkList.isEmpty()) {
            noBookmarkText.visibility = View.VISIBLE
        } else {
            noBookmarkText.visibility = View.GONE
        }
    }

    inner class ItemClickListener : BookmarkAdapter.OnItemClickListener {

        fun getMenuId(menuNum: Int): Int {
            if (menuNum == 1) {
                return when (languageCode) {
                    Translater_papago.korean -> R.menu.menu_bookmark
                    Translater_papago.english -> R.menu.menu_bookmark_english
                    Translater_papago.chinese -> R.menu.menu_bookmark_chinese
                    Translater_papago.japanese -> R.menu.menu_bookmark_japanese
                    else -> R.menu.menu_search
                }
            } else {
                return when (languageCode) {
                    Translater_papago.korean -> R.menu.menu_bookmark_2
                    Translater_papago.english -> R.menu.menu_bookmark_2_english
                    Translater_papago.chinese -> R.menu.menu_bookmark_2_chinese
                    Translater_papago.japanese -> R.menu.menu_bookmark_2_japanese
                    else -> R.menu.menu_search_2
                }
            }
        }

        override fun onItemClick(majorName: String, pos: Int, view: View) {
            Log.i("Clicked Major", majorName)

            val krMajorName: String =
                if (languageCode.equals(Translater_papago.korean)) majorName
                else otherLanguageToKorean.get(majorName)!!
            Log.i("Clicked Major", krMajorName)

            val popupMenu: PopupMenu = PopupMenu(context, view)
            when (krMajorName) {
                "건국대학교" -> popupMenu.menuInflater.inflate(getMenuId(2), popupMenu.menu)
                "상허생명과학대학 동물자원과학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                "상허생명과학대학 식량자원과학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                "상허생명과학대학 축산식품생명공학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                "상허생명과학대학 식품유통공학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                "상허생명과학대학 환경보건과학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                "상허생명과학대학 산림조경학과" -> popupMenu.menuInflater.inflate(
                    getMenuId(2),
                    popupMenu.menu
                )
                else -> popupMenu.menuInflater.inflate(getMenuId(1), popupMenu.menu)
            }

            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem?): Boolean {
                    if (item != null) {
                        // 공지사항을 선택한 경우 -> NoticeList 액티비티로 전환
                        if (item.itemId == R.id.menuNotice) {
                            val intent: Intent = Intent(context, NoticeList::class.java)
//                            lateinit var intent: Intent
//                            when (majorName) {
//                                "건국대학교" -> intent = Intent(context, NoticeList2::class.java)
//                                else -> intent = Intent(context, NoticeList::class.java)
//                            }
                            intent.putExtra("otherLanguageToKorean", otherLanguageToKorean)
                            intent.putExtra("languageCode", languageCode)
                            intent.putExtra(
                                "crawlerNumMap",
                                (context as MainActivity).crawlerNum
                            )
                            intent.putExtra("majorName", majorName)
                            intent.putExtra(
                                "majorCode",
                                majorMap.get(krMajorName)
                            )
                            startActivityForResult(
                                intent, 100
                            )
                        }
                        // 학과정보안내를 선택한 경우 -> 미정
                        else if (item.itemId == R.id.menuMajorInfo) {
                            Log.i("Clicked PopupMenu", majorName + " 전화번호안내")
                            val intent: Intent = Intent(context, MajorInfo::class.java)
                            intent.putExtra("languageCode", languageCode)
                            intent.putExtra("majorName", majorName)
                            intent.putExtra(
                                "majorCode",
                                majorMap.get(krMajorName)
                            )
                            startActivityForResult(intent, 102)
                        }
                        // 제거를 선택한 경우 -> 즐겨찾기에서 제거
                        else if (item.itemId == R.id.menuAddOrDelete) {
                            Log.i("Clicked PopupMenu", majorName + " 삭제")
                            fragmentListener?.bookmarkDelete(
                                krMajorName,
                                majorMap.get(krMajorName)!!
                            )
                        }
                    }
                    return true
                }
            })
            popupMenu.show()
        }

    }

}