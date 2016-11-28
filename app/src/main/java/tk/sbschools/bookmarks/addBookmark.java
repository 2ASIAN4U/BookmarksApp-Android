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

public class addBookmark extends AppCompatActivity {
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
        }

        CustomAdapter myAdapter = new CustomAdapter(this,R.layout.editList_layout,nameList,urlList);

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
            View layoutView = inflater.inflate(R.layout.editList_layout,null);
            TextView textView = (TextView)layoutView.findViewById(R.id.textView);
            TextView urlText = (TextView)layoutView.findViewById(R.id.textView_URL);

            textView.setText(list.get(position).toString());
            urlText.setText(urlList.get(position).toString());

            return layoutView;
        }
    }
}

/*
Intent intent = new Intent();
intent.putExtra("edittextvalue","value_here");
setResult(RESULT_OK, intent);
finish();
 */
