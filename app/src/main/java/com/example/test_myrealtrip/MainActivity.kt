package com.example.test_myrealtrip

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.progressBar
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.activity_main.swipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.view.*
import java.net.URL

class MainActivity : AppCompatActivity() {

    private val rssUrl :URL = URL("https://news.google.com/rss?hl=ko&gl=KR&ceid=KR:ko")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RssReadTask(rssUrl, this@MainActivity, recyclerView, swipeRefreshLayout, progressBar).execute()

        with(swipeRefreshLayout){

            setOnRefreshListener {
                RssReadTask(rssUrl, this@MainActivity, recyclerView, swipeRefreshLayout).execute()
            }
        }
    }

    class RssReadTask(
        private val rssUrl: URL,
        private val context : Context,
        private val recyclerView: RecyclerView,
        private val swipeRefreshLayout: SwipeRefreshLayout,
        private val progressBar: ProgressBar? = null
    ): AsyncTask<String, Unit, ArrayList<Item>>(){

        override fun onPreExecute() {
            if(progressBar != null) progressBar.visibility= View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): ArrayList<Item> {
            return DataHandler(rssUrl).parse()
        }

        override fun onPostExecute(result: ArrayList<Item>) {

            try {
                val adapter = RecyclerAdapter(result, context)

                if (progressBar != null) progressBar.visibility = View.INVISIBLE
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(context)
                swipeRefreshLayout.isRefreshing = false
            }catch (e:Exception){
                e.printStackTrace()
            }
        }

    }

    class RecyclerAdapter(
        val itemList : ArrayList<Item>,
        val context: Context

    ):RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

        val inflater: LayoutInflater = LayoutInflater.from(context)

        inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
            val title : TextView
            val description : TextView
            val img : ImageView
            val keyword1 : TextView
            val keyword2 : TextView
            val keyword3 : TextView

            init{
                title = itemView.findViewById(R.id.textTitle)
                description = itemView.findViewById(R.id.textDescription)
                img = itemView.findViewById(R.id.imgNews)
                keyword1 = itemView.findViewById(R.id.keyword1)
                keyword2 = itemView.findViewById(R.id.keyword2)
                keyword3 = itemView.findViewById(R.id.keyword3)

                itemView.setOnClickListener {
                    val item = itemList.get(adapterPosition)

                    val intent = Intent(context, NewsWebViewActivity::class.java)
                    intent.apply{
                        putExtra("title", item.title)
                        putExtra("link", item.link)
                        putExtra("keywords", item.keywords)
                    }
                    context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = inflater.inflate(R.layout.layout_item_list, parent, false)
            return ViewHolder(view)
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.title.setText(itemList.get(position).title)
            holder.description.setText(itemList.get(position).description)
            if(itemList.get(position).img !=null)
                holder.img.setImageBitmap(itemList.get(position).img)
            else
                holder.img.setImageDrawable(context.getDrawable(R.drawable.img_load_fail))

            if(itemList.get(position).description != Load_Fail_Message) {
                holder.keyword1.setText(itemList.get(position).keywords[0])
                holder.keyword2.setText(itemList.get(position).keywords[1])
                holder.keyword3.setText(itemList.get(position).keywords[2])
            }
            else {
                holder.keyword1.visibility = View.GONE
                holder.keyword2.visibility = View.GONE
                holder.keyword3.visibility = View.GONE
            }
        }
    }

}
