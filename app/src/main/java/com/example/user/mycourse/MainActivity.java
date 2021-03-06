package com.example.user.mycourse;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private CustomAdapter adapter;
    private List<MyData> data_list;
    ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//10:22
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoadingProgress = findViewById(R.id.pb_loading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        data_list = new ArrayList<>();
        load_data_from_server(0);

        gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        adapter = new CustomAdapter(this, data_list);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == data_list.size() - 1) {
                    load_data_from_server(data_list.get(data_list.size() - 1).getId());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.card_list_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    private void load_data_from_server(final int id) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://mm.s-ct.asia/arrayOut.php?id=" + id)
                        .build();
                try {
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        int tocNum = object.getJSONArray("toc").length();
                        String[] table_of_contents = new String[tocNum];
                        for (int j = 0; j < tocNum; j++) {
                            table_of_contents[j] = object.getJSONArray("toc").get(j).toString();
                        }

                        System.out.println(object);

                        MyData data = new MyData(object.getInt("id"), object.getString("description"),
                                object.getString("image"), object.getString("longDesc"), table_of_contents);
                        data_list.add(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mLoadingProgress.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                TextView error_msg = findViewById(R.id.error_msg);
                mLoadingProgress.setVisibility(View.INVISIBLE);
                if (data_list == null) {
                    error_msg.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
                adapter.addData(data_list);

                recyclerView.setAdapter(adapter);
            }
        };
        task.execute(id);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        ArrayList<MyData> newList = new ArrayList<>();
        for (MyData myData : data_list) {
            String description = myData.getDescription().toLowerCase();
            if (description.contains(newText)) {
                newList.add(myData);
            }
        }
        adapter.setFilter(newList);

        return true;
    }
}
