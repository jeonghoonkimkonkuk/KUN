package com.kunotice.kunotice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kunotice.kunotice.adapter.SearchListAdapter
import com.kunotice.kunotice.data.Majors
import com.kunotice.kunotice.data.MyListModel
import com.kunotice.kunotice.translate.Translater_papago

class FragmentSearch(
    val majors: Majors,
    val majorMap: HashMap<String, String>,
    var isBookmark: HashMap<String, Boolean>,
    var languageCode: String
) : Fragment() {

    interface OnFragmentInteraction {
        fun bookmarkAdd(majorName: String, majorCode: String)
        fun bookmarkDelete(majorName: String, majorCode: String)
    }

    var fragmentListener: OnFragmentInteraction? = null

    val otherLanguageToKorean: HashMap<String, String> = HashMap<String, String>()
//    val majorList_otherLanguage: ArrayList<String> = ArrayList<String>()

    lateinit var searchView: SearchView
    lateinit var searchListView: RecyclerView
    lateinit var adapter: SearchListAdapter
    val majorList: ArrayList<String> = ArrayList()

    val mList: ArrayList<MyListModel> = ArrayList<MyListModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView: ViewGroup =
            inflater.inflate(R.layout.fragment_search, container, false) as ViewGroup

        searchListView = rootView.findViewById(R.id.searchListView)
        searchListView.layoutManager = LinearLayoutManager(context)

        init()

        searchView = rootView.findViewById(R.id.searchView)
        searchView.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                searchView.isIconified = false
            }

        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            // 검색 버튼이 클릭되었을 때 이벤트 처리
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return true
            }

            // 검색어가 변경될 때 이벤트 처리
            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }

        })

        return rootView
    }

    // MainActivity 에서 변경한 북마킹 정보에 따라 변수를 업데이트 하는 함수
    fun bookmarkChanged(krMajorName: String) {
        adapter.notifyDataSetChanged()
    }

    fun init() {
        majorList.clear()
        when (languageCode) {
            Translater_papago.korean -> majorList.addAll(majors.majorList_korean)
            Translater_papago.english -> majorList.addAll(majors.majorList_english)
            Translater_papago.chinese -> majorList.addAll(majors.majorList_chinese)
            Translater_papago.japanese -> majorList.addAll(majors.majorList_japanese)
        }

        for (i in majors.majorList_korean.indices) {
            otherLanguageToKorean.put(majorList[i], majors.majorList_korean[i])
        }

        majorList.sort()

        for (i in majorList.indices) {
            mList.add(MyListModel(majorList[i], 1))
        }
        mList.add(1, MyListModel("AD", 2))
        mList.add(21, MyListModel("AD", 2))
        mList.add(41, MyListModel("AD", 2))
        mList.add(61, MyListModel("AD", 2))

        adapter = SearchListAdapter(
            mList,
            mList,
            isBookmark,
            otherLanguageToKorean,
            context!!
        )
        searchListView.adapter = adapter
        adapter.setOnItemClickListener(ItemClickListener())

    }

    fun setOnFragmentInteraction(fragmentListener: OnFragmentInteraction) {
        this.fragmentListener = fragmentListener
    }

    inner class ItemClickListener : SearchListAdapter.OnItemClickListener {
        fun getMenuId(menuNum: Int): Int {
            if (menuNum == 1) {
                return when (languageCode) {
                    Translater_papago.korean -> R.menu.menu_search
                    Translater_papago.english -> R.menu.menu_search_english
                    Translater_papago.chinese -> R.menu.menu_search_chinese
                    Translater_papago.japanese -> R.menu.menu_search_japanese
                    else -> R.menu.menu_search
                }
            } else {
                return when (languageCode) {
                    Translater_papago.korean -> R.menu.menu_search_2
                    Translater_papago.english -> R.menu.menu_search_2_english
                    Translater_papago.chinese -> R.menu.menu_search_2_chinese
                    Translater_papago.japanese -> R.menu.menu_search_2_japanese
                    else -> R.menu.menu_search_2
                }
            }
        }

        override fun onTextViewClick(v: View, btn: Button, majorName: String) {
            val krMajorName: String =
                if (languageCode.equals(Translater_papago.korean)) majorName
                else otherLanguageToKorean.get(majorName)!!

            val popupMenu: PopupMenu = PopupMenu(context, v)
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
//                                lateinit var intent: Intent
//                                when (majorName) {
//                                    "건국대학교" -> intent = Intent(context, NoticeList2::class.java)
//                                    else -> intent = Intent(context, NoticeList::class.java)
//                                }
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
                            startActivityForResult(intent, 100)
                        }
                        // 학과정보안내를 선택한 경우
                        else if (item.itemId == R.id.menuMajorInfo) {
                            Log.i("Clicked PopupMenu", majorName + " 학과정보안내")
                            val intent: Intent = Intent(context, MajorInfo::class.java)
                            intent.putExtra("languageCode", languageCode)
                            intent.putExtra("majorName", majorName)
                            intent.putExtra(
                                "majorCode",
                                majorMap.get(krMajorName)
                            )
                            startActivityForResult(intent, 102)
                        }
                        /*
                        즐겨찾기추가/제거 를 선택한 경우
                        즐겨찾기가 되어있는 경우 -> 즐겨찾기에서 제거
                        즐겨찾기가 되어있지 않은 경우 -> 즐겨찾기에 추가
                         */
                        else if (item.itemId == R.id.menuAddOrDelete) {
                            btn.callOnClick()
                        }
                    }
                    return true
                }
            })
            popupMenu.show()
        }

        override fun onBtnClick(v: View, majorName: String) {
            val krMajorName: String =
                if (languageCode.equals(Translater_papago.korean)) majorName
                else otherLanguageToKorean.get(majorName)!!
            // 즐겨찾기가 이미 되어있는 경우 -> 즐겨찾기 삭제
            if (isBookmark.get(krMajorName)!!) {
                v.setBackgroundResource(R.drawable.star_uncolored)

                // 즐겨찾기 해제 명령을 MainAcitivity로 전달
                fragmentListener?.bookmarkDelete(
                    krMajorName,
                    majorMap.get(krMajorName)!!
                )
            }
            // 즐겨찾기가 되어있지 않은 경우 -> 즐겨찾기 추가
            else {
                v.setBackgroundResource(R.drawable.star_colored)

                // 즐겨찾기 추가 명령을 MainAcitivity로 전달
                fragmentListener?.bookmarkAdd(
                    krMajorName,
                    majorMap.get(krMajorName)!!
                )
            }
        }
    }

}