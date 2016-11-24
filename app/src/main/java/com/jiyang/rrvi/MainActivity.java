package com.jiyang.rrvi;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.jiyang.rrvi.adapter.StoriesAdapter;
import com.jiyang.rrvi.api.GetNews;
import com.jiyang.rrvi.bean.Result;
import com.jiyang.rrvi.bean.Stories;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Context context;
    private StoriesAdapter storiesAdapter;
    private List<Stories> storiesList;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        //init UI,set adapter
        initView();
    }

    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.main_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        storiesList = new ArrayList<>();
        storiesAdapter = new StoriesAdapter(context, storiesList);
        recyclerView.setAdapter(storiesAdapter);
        Button btnByRR = (Button) findViewById(R.id.btn_RxJava);
        Button btnByVolley = (Button) findViewById(R.id.btn_volley);
        Button btnClear = (Button) findViewById(R.id.btn_clear);
        btnByRR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataByRetrofit();
            }
        });
        btnByVolley.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDataByVolley();
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storiesList.clear();
                storiesAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * get data by Retrofit & RxJava
     */
    private void getDataByRetrofit() {
        progressBar.setVisibility(View.VISIBLE);
        storiesAdapter.setGetPicByRR(true);// tell adapter get pic by retrofit
        Subscriber<Result> subscriber = new Subscriber<Result>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
                storiesList.clear();
            }

            @Override
            public void onNext(Result result) {
                storiesList.addAll(result.getStories());
                storiesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        };
        GetNews getNews = RetrofitManager.getInstance().getRetrofit().create(GetNews.class);
        getNews.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }

    /**
     * get data by volley
     */
    private void getDataByVolley() {
        progressBar.setVisibility(View.VISIBLE);
        storiesAdapter.setGetPicByRR(false);// tell adapter get pic by volley
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                RetrofitManager.BASE_URL + "latest",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        storiesList.addAll(gson.fromJson(response, Result.class).getStories());
                        storiesAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        VolleyManager.getInstance().getRequestQueue(this.getApplication()).add(stringRequest);
    }
}
