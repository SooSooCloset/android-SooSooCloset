package com.example.soosoocloset

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.soosoocloset.adapter.ClothAdapter
import com.example.soosoocloset.adapter.CodiAdapter
import com.example.soosoocloset.domain.Cloth
import com.example.soosoocloset.domain.Codi
import kotlinx.android.synthetic.main.fragment_closet.*

// author: Sumin, created: 21.05.19
class CodiFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_codi, container, false)
        setHasOptionsMenu(true) // 상단바의 메뉴 허용

        var codiList = arrayListOf<Codi>(Codi(), Codi(), Codi())
        val rv_codi : RecyclerView = view.findViewById(R.id.rv_codi)
        val codiAdapater = CodiAdapter(codiList)
        val layoutManager : GridLayoutManager = GridLayoutManager(view.context, 2)

        rv_codi.adapter = codiAdapater
        rv_codi.layoutManager = layoutManager

        return view
    }

}