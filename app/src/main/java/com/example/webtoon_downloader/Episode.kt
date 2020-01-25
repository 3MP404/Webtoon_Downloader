package com.example.webtoon_downloader

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.episode_main.*
import org.jsoup.Jsoup
import java.lang.Exception

class Episode : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.episode_main)

        val mcontext=this
        val intent=getIntent().extras!!
        Glide.with(this).load(intent.getString("thumbnail")).into(episode_thumbnail)
        comic_title.setText(intent.getString("title"))

        val handler= @SuppressLint("HandlerLeak")
        object:Handler(){
            override fun handleMessage(msg: Message) {
                val episode=episodeTiles(mcontext,null,0)
                var message=msg.obj.toString()
                val title=message.substring(message.indexOf("-")+1)
                message=message.substring(0,message.indexOf("-"))

                episode.setTile(title,message,intent.getString("title")!!)
                episode_area.addView(episode)
            }
        }
        Thread(Runnable {
            val link=intent.getString("link")+"&page="
            var prev=""
            var page=1
            while(true) {

                val doc = Jsoup.connect(link+page.toString()).get()
                val elements = doc.select("tbody").toString()
                val html = elements.split("<tr>")
                var end=false
                var first=false
                var href: String
                var title: String
                for (i in html) {

                    href = "https://comic.naver.com"
                    try {
                        val amp = i.indexOf("&amp")
                        href += i.substring(i.indexOf("href") + 6, amp) + "&" + i.substring(amp + 5, i.indexOf("weekday") - 5)
                        title = i.substring(i.indexOf("title=") + 7)
                        title = title.substring(0, title.indexOf("\""))
                        if (first==false) {
                            if(prev.equals(title)==true)
                            {
                                end=true
                                break
                            }
                            prev=title
                            first=true
                        }
                    }
                    catch (e: Exception) {
                        continue
                    }

                    val message = Message.obtain()
                    message.obj = href + "-" + title
                    handler.sendMessage(message)
                }
                if(end==true)
                    break
                page++
            }
        }).start()
    }
}