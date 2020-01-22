package com.example.webtoon_downloader

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat.startActivity

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.webtoon_tiles.view.*

class WebtoonTiles @JvmOverloads constructor(
        context:Context,
        attrs:AttributeSet?=null,
        defStyleAttr:Int=0
) : LinearLayout(context,attrs,defStyleAttr){
    init{
        LayoutInflater.from(context).inflate(R.layout.webtoon_tiles,this,true)
    }
    fun setThumbnail(title:String,link:String,comic:String){
        titlename.setText(title)
        try {
            Glide.with(context).load(link).into(thumbnail)
        }
        catch (e:Exception)
        {
            Log.d("mydebug",e.toString())
            Log.d("mydebug",thumbnail.toString())
        }

    }

}