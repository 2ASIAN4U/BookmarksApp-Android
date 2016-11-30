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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;

public class addBookmark extends AppCompatActivity {
    ArrayList<String> nameList,urlList,detailList;
    ListView display;
    EditText NameDisp,URLDisp,detailsDisp;
    Button addList, saveList, delList, doneList;
    static final String CURRENT_SEL = "cselect";
    static final String NAMELIST = "nList";
    static final String URLLIST = "uList";
    static final String DETAILLIST = "dList";
    int currentSelection = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bookmark);
        setTitle("Edit Bookmarks");

        nameList = getIntent().getStringArrayListExtra(NAMELIST);
        urlList = getIntent().getStringArrayListExtra(URLLIST);
        detailList = getIntent().getStringArrayListExtra(DETAILLIST);
        display = (ListView)findViewById(R.id.listView_display);
        NameDisp = (EditText)findViewById(R.id.editText_name);
        URLDisp = (EditText) findViewById(R.id.editText_URL);
        detailsDisp = (EditText) findViewById(R.id.editText_Desc);
        addList = (Button) findViewById(R.id.button_add);
        saveList = (Button)findViewById(R.id.button_edit);
        delList = (Button)findViewById(R.id.button_del);
        doneList = (Button)findViewById(R.id.button_Done);

        final CustomAdapter myAdapter = new CustomAdapter(this,R.layout.editlist_layout,nameList,urlList);

        display.setAdapter(myAdapter);

        display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NameDisp.setText(nameList.get(position));
                URLDisp.setText(urlList.get(position));
                detailsDisp.setText(detailList.get(position));
                currentSelection = position;
            }
        });

        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameList.add(currentSelection+1,NameDisp.getText().toString());
                urlList.add(currentSelection+1,URLDisp.getText().toString());
                detailList.add(currentSelection+1,detailsDisp.getText().toString());
                NameDisp.setText("");
                URLDisp.setText("");
                detailsDisp.setText("");
                currentSelection = -1;
                myAdapter.notifyDataSetChanged();
            }
        });

        saveList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentSelection != -1){
                    nameList.set(currentSelection,NameDisp.getText().toString());
                    urlList.set(currentSelection,URLDisp.getText().toString());
                    detailList.set(currentSelection,detailsDisp.getText().toString());
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
        delList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentSelection != -1){
                    nameList.remove(currentSelection);
                    urlList.remove(currentSelection);
                    detailList.remove(currentSelection);
                    NameDisp.setText("");
                    URLDisp.setText("");
                    detailsDisp.setText("");
                    currentSelection = -1;
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
        doneList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("USEDATA",true);
                intent.putExtra(NAMELIST, nameList);
                intent.putExtra(URLLIST,urlList);
                intent.putExtra(DETAILLIST,detailList);
                intent.putExtra(CURRENT_SEL,currentSelection);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("USEDATA", false);
        setResult(RESULT_OK, intent);
        finish();
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
            View layoutView = inflater.inflate(R.layout.editlist_layout,null);
            TextView textView = (TextView)layoutView.findViewById(R.id.textView);
            TextView urlText = (TextView)layoutView.findViewById(R.id.textView_URL);

            textView.setText(list.get(position).toString());
            urlText.setText(urlList.get(position).toString());

            return layoutView;
        }
    }
}
