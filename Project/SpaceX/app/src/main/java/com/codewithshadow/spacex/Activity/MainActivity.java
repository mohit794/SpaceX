package com.codewithshadow.spacex.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.codewithshadow.spacex.Adapter.CrewAdapter;
import com.codewithshadow.spacex.Model.CrewModel;
import com.codewithshadow.spacex.R;
import com.codewithshadow.spacex.RoomDatabase.RoomDB;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Response.Listener<JSONArray>, Response.ErrorListener {
    RecyclerView recyclerView;
    CrewAdapter adapter;
    List<CrewModel> list;
    LinearLayoutManager manager;
    ShimmerFrameLayout shimmer;
    Snackbar snackbar;
    RoomDB database;
    Button reset,refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        reset = findViewById(R.id.reset);
        shimmer = findViewById(R.id.shimmer);
        refresh = findViewById(R.id.refresh);

        //Room Database
        database = RoomDB.getInstance(this);
        list = database.mainDao().getAll();


        //RecyclerView
        manager = new LinearLayoutManager(this);
        adapter = new CrewAdapter(this,list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        //Internet Connection Check

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            String url = "https://api.spacexdata.com/v4/crew";
            RequestQueue queue = Volley.newRequestQueue(this);
            JsonArrayRequest microPostsRequest = new JsonArrayRequest(url, this, this);
            queue.add(microPostsRequest);
            connected = true;
        }
        else {
            View view =  findViewById(R.id.dashlayout);
            snackbar = Snackbar.make(view,"No internet connection.",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
            snackbar.setBackgroundTint(getResources().getColor(R.color.black));
            snackbar.setTextColor(Color.WHITE);
            snackbar.setDuration(5000);
            snackbar.show();
            shimmer.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            connected = false;
        }

        //Refresh Button

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FetchData(MainActivity.this);
            }
        });


        //Reset Button

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.mainDao().reset(list);
                database.clearAllTables();
                list.clear();
                list.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
                clearApplicationData();

            }
        });

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }


    //OnResponse Listeners

    @Override
    public void onResponse(JSONArray response) {
        FetchData(this);
    }


    //FetchData Function

    private void FetchData(Context context)
    {
        String url = "https://api.spacexdata.com/v4/crew";
        RequestQueue queue = Volley.newRequestQueue(context);
        recyclerView.setVisibility(View.VISIBLE);
        shimmer.setVisibility(View.GONE);
        JsonArrayRequest microPostsRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject object = response.getJSONObject(i);
                        String name = object.getString("name");
                        String agency = object.getString("agency");
                        String image = object.getString("image");
                        String wikipedia = object.getString("wikipedia");
                        String status = object.getString("status");
                        String id = object.getString("id");
                        CrewModel data = new CrewModel();
                        data.setName(name);
                        data.setAgency(agency);
                        data.setImage(image);
                        data.setStatus(status);
                        data.setWikipedia(wikipedia);
                        data.setID(id);
                        database.mainDao().insert(data);
                    }
                    list.clear();
                    list.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();

                }catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(microPostsRequest);
    }

    //Clear Application Cache

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int i = 0;
            while (i < children.length) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
                i++;
            }
        }

        assert dir != null;
        return dir.delete();
    }
    }


