package com.example.test_myrealtrip

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import org.jsoup.Jsoup
import org.xml.sax.Attributes
import org.xml.sax.helpers.DefaultHandler
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.SAXParser
import javax.xml.parsers.SAXParserFactory

class DataHandler(rssUrl: URL) : DefaultHandler(){

    private val parserFactory : SAXParserFactory = SAXParserFactory.newInstance()
    private val parser : SAXParser = parserFactory.newSAXParser()
    private var startTagName : String? = ""
    private var isItem : Boolean = false
    private var sBuffer : StringBuffer = StringBuffer()
    private val rssUrl = rssUrl
    private val itemList :ArrayList<Item> = ArrayList()

    var title : String =""
    var link : String =""

    override fun startElement(
        uri: String?,
        localName: String?,
        qName: String?,
        attributes: Attributes?
    ) {
        startTagName=qName
        sBuffer.setLength(0)
    }

    override fun characters(ch: CharArray?, start: Int, length: Int) {
        sBuffer.append(ch, start, length)

        if(! isItem) {
            when (startTagName) {
                "title" -> title = sBuffer.toString().trim()
                "link" -> link = sBuffer.toString().trim()
            }
        }else {
            if(startTagName == "item") isItem = true
        }
    }

    override fun endElement(uri: String?, localName: String?, qName: String?) {

        if(qName == "item") {
            isItem=false
            val docNews = Jsoup.connect(link).get()

            var description :String
            var img : Bitmap?

            try {
                description=
                    docNews.select("head meta[property=og:description]").first().attr("content")

                if (description == "") description = Load_Fail_Message

                description = description.replace("\n","")

            }catch (e : Exception){
                description = Load_Fail_Message
            }

            try {
                val imgSrc =
                    URL(docNews.select("head meta[property=og:image]").first().attr("content"))

                val conn = imgSrc.openConnection() as HttpURLConnection
                conn.setDoInput(true)
                conn.connect()

                img = BitmapFactory.decodeStream(conn.inputStream)
                conn.disconnect()
            }catch (e : Exception){
                img = null
            }

            itemList.add(Item(title,link, description, img))
        }

    }

    fun parse() : ArrayList<Item>{
        try {
            parser.parse(rssUrl.toString(), this)
        }catch (e: Exception){
            e.printStackTrace()
        }

        return itemList
    }

}