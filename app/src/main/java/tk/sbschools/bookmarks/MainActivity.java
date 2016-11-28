package tk.sbschools.bookmarks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> nameList,urlList,detailList;
    ListView display;
    TextView URLDisp,detailsDisp;
    static final String CURRENT_SEL = "cselect";
    static final String NAMELIST = "nList";
    static final String URLLIST = "uList";
    static final String DETAILLIST = "dList";
    int currentSelection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameList = new ArrayList<>();
        urlList = new ArrayList<>();
        detailList = new ArrayList<>();
        display = (ListView)findViewById(R.id.listView_display);
        URLDisp = (TextView)findViewById(R.id.textView_URL);
        detailsDisp = (TextView)findViewById(R.id.textView_details);

        if(savedInstanceState != null){
            currentSelection=savedInstanceState.getInt(CURRENT_SEL);
            nameList = savedInstanceState.getStringArrayList(NAMELIST);
            urlList = savedInstanceState.getStringArrayList(URLLIST);
            detailList = savedInstanceState.getStringArrayList(DETAILLIST);
            System.err.println(currentSelection);
            if(currentSelection != -1){
                URLDisp.setText(urlList.get(currentSelection));
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    detailsDisp.setText(detailList.get(currentSelection));
                }
            }
        }else{
            currentSelection = -1;
            //Synopsises From Wikipedia
            nameList.add("Google"); urlList.add("www.google.com");detailList.add("Google is an American multinational technology company specializing in Internet-related services and products that include online advertising technologies, search, cloud computing, software, and hardware.");
            nameList.add("Reddit"); urlList.add("www.reddit.com");detailList.add("Reddit is a social news aggregation, web content rating, and discussion website. Reddit's registered community members can submit content, such as text posts or direct links.");
            nameList.add("Codecademy"); urlList.add("www.codecademy.com");detailList.add("Codecademy is an online interactive platform that offers free coding classes in 12 different programming languages including Python, Java, PHP, JavaScript, Ruby, SQL, and Sass, as well as markup languages HTML and CSS.");
            nameList.add("Amazon"); urlList.add("www.amazon.com");detailList.add("Amazon.com, often simply Amazon, is an American electronic commerce and cloud computing company, founded in July 5, 1994 by Jeff Bezos and based in Seattle, Washington.");
            nameList.add("SB Schools"); urlList.add("www.sbschools.org"); detailList.add("The South Brunswick Public Schools are a comprehensive community public school district, serving students in pre-Kindergarten through twelfth grade from South Brunswick Township in Middlesex County, New Jersey, United States.");
            nameList.add("Kali Linux");urlList.add("www.kali.org");detailList.add("Kali Linux is a Debian-derived Linux distribution designed for digital forensics and penetration testing. It is maintained and funded by Offensive Security Ltd. Mati Aharoni, Devon Kearns and Raphaël Hertzog are the core developers.");
            nameList.add("Fanfiction.NET");urlList.add("www.fanfiction.net");detailList.add("FanFiction.Net (often abbreviated as FF.net or FFN) is an automated fan fiction archive site. It was founded in 1998 by Los Angeles computer programmer Xing Li, who also runs the site. As of 2010, FanFiction.Net is the largest and most popular fan fiction website in the world. It has nearly 2.2 million registered users and hosts stories in over 30 languages.");
            nameList.add("Android Developers");urlList.add("developer.android.com/index.html");detailList.add("The official site for Android developers. Provides the Android SDK and documentation for app developers and designers.");
        }

        CustomAdapter myAdapter = new CustomAdapter(this,R.layout.list_layout,nameList,urlList);

        display.setAdapter(myAdapter);

        display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                URLDisp.setText(urlList.get(position));
                if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                    detailsDisp.setText(detailList.get(position));
                }
                currentSelection = position;
            }
        });
        display.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Uri link = Uri.parse("http://"+urlList.get(position));
                Intent intent = new Intent(Intent.ACTION_VIEW, link);
                startActivity(intent);

                return false;
            }
        });
    }

    public class CustomAdapter extends ArrayAdapter<String> {
        List list,urlList;
        Context mainContext;

        public CustomAdapter(Context context, int resource, List<String> objects, List<String> urls) {
            super(context, resource, objects);

            mainContext = context;
            list = objects;
            urlList = urls;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater)mainContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View layoutView = inflater.inflate(R.layout.list_layout,null);
            TextView textView = (TextView)layoutView.findViewById(R.id.textView);
            ImageView imageView = (ImageView)layoutView.findViewById(R.id.imageView);

            textView.setText(list.get(position).toString());
            new DownloadImageTask(imageView).execute(urlList.get(position).toString());

            return layoutView;
        }
    }

    //http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView setImage;

        public DownloadImageTask(ImageView setImage) {
            this.setImage = setImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap favicon = null;
            try {
                //InputStream in = new java.net.URL("http://api.byi.pw/favicon?url="+urldisplay).openStream();
                InputStream in = new java.net.URL("http://www.google.com/s2/favicons?domain="+urldisplay).openStream();
                favicon = BitmapFactory.decodeStream(in);
                favicon = Bitmap.createScaledBitmap(favicon, 64, 64, false);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return favicon;
        }

        protected void onPostExecute(Bitmap result) {
            setImage.setImageBitmap(result);
        }
    }
    protected void onSaveInstanceState(Bundle outBundle){
        outBundle.putInt(CURRENT_SEL,currentSelection);
        outBundle.putStringArrayList(NAMELIST,nameList);
        outBundle.putStringArrayList(URLLIST,urlList);
        outBundle.putStringArrayList(DETAILLIST,detailList);
        super.onSaveInstanceState(outBundle);
    }

    public void addBookmark(View view){
        Intent i = new Intent(this, addBookmark.class);
        i.putExtra(NAMELIST, nameList);
        i.putExtra(URLLIST,urlList);
        i.putExtra(DETAILLIST,detailList);
        startActivityForResult(i, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        nameList = data.getStringArrayListExtra(NAMELIST);
        urlList = data.getStringArrayListExtra(URLLIST);
        detailList = data.getStringArrayListExtra(DETAILLIST);
        currentSelection = data.getIntExtra(CURRENT_SEL, -1);
        this.recreate();
    }
}
