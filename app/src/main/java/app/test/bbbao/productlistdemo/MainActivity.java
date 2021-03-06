package app.test.bbbao.productlistdemo;

import android.app.Activity;
import android.content.Context;
import android.icu.util.TimeUnit;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import app.test.bbbao.productlistdemo.Bean.Commodity_Bean;
import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends Activity {


    @BindView(R.id.rv)
    RecyclerView mRecycleView;
    @BindView(R.id.refreshLayout)
    RefreshLayout mRefreshLayout;

    RecycleAdapter recycleAdapter;

    List<Commodity_Bean.ResultsBean> bean;
    List<Commodity_Bean.ResultsBean> Loadbean;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycleView.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        //设置 Header 为 贝塞尔雷达 样式
        mRefreshLayout.setRefreshHeader(new MaterialHeader(this).setShowBezierWave(true));
        //设置 Footer 为 球脉冲 样式
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        mRecycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        initDate(0, 8);


        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {


                initDate(0, 8);

                refreshlayout.finishRefresh();


            }
        });

        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {


                /**
                 *判断是否是第一页
                 * 如果是第一页则传入0和8
                 * 如果不是第一页则传入计算出页数的初始值和结束值访问数据
                 */

                if ("0".equals(i)) {
                    LoadDate(0, 8);
                    recycleAdapter.notifyDataSetChanged();
                    refreshlayout.finishLoadmore();
                    i++;

                } else {

                    LoadDate((i + 1) * 8, (i + 2) * 8);

                    refreshlayout.finishLoadmore();
                    i++;


                }


            }
        });

    }

    //url字符
    private String getUrl(Integer start, Integer limit) {

        Gson gson = new Gson();
        HashMap<Object, Object> map = new HashMap<>();
        map.put("start", start);
        map.put("limit", limit);

        return gson.toJson(map);

    }


    //初始方法
    private void initDate(Integer start, Integer limit) {


        OkHttpClient client = new OkHttpClient();
        final Message message = new Message();
        MediaType Json = MediaType.parse("application/json;charset=utf-8");
        RequestBody body = RequestBody.create(Json, getUrl(start, limit));
        Request request = new Request.Builder()
                .url("http://rc.bbbao.com/api/fsearch?&source_id=6228&sort=default&v=a&")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (e instanceof SocketTimeoutException) {
                    //网络连接超时
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException) {
                    //网络连接异常
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"网络",Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
                if(e instanceof UnknownHostException){
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"请检查网络是否连接",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                message.what = 1;
                Gson gson = new Gson();
                Commodity_Bean bean = gson.fromJson(str, Commodity_Bean.class);
                Log.e("凯文88888", "你好 " + bean.getInfo());
                message.obj = bean;
                mhandler.sendMessage(message);

            }
        });


    }


    //加载更多
    private void LoadDate(Integer start2, Integer limit2) {

        getSystemService(Context.CONNECTIVITY_SERVICE);


        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .writeTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
                .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();
        final Message message = new Message();
        MediaType Json2 = MediaType.parse("application/json;charset=utf-8");
        //RequestBody body = RequestBody.create(Json2, getUrl(start2, limit2));
        Request request = new Request.Builder()
                .url("http://rc.bbbao.com/api/fsearch?&source_id=6228&sort=default&v=a&start=" + start2 + "&limit=" + limit2)

                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (e instanceof SocketTimeoutException) {
                    //网络连接超时
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"网络超时",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                if (e instanceof ConnectException) {
                    //网络连接异常
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"网络",Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
                if(e instanceof UnknownHostException){
                    Looper.prepare();
                    Toast.makeText(MainActivity.this,"请检查网络是否连接",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                message.what = 2;
                Gson gson = new Gson();
                Commodity_Bean bean = gson.fromJson(str, Commodity_Bean.class);
                message.obj = bean;
                mhandler.sendMessage(message);

            }
        });


    }


    Handler mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:

                    bean = ((Commodity_Bean) msg.obj).getResults();
                    recycleAdapter = new RecycleAdapter(getApplicationContext(), bean);
                    recycleAdapter.notifyDataSetChanged();
                    mRecycleView.setAdapter(recycleAdapter);
                    break;
                case 2:
                    Loadbean = ((Commodity_Bean) msg.obj).getResults();
                    recycleAdapter.notifyDataSetChanged();
                    bean.addAll(Loadbean);
                    break;
            }

        }
    };

    //这是我测试用的方法
    public void kaiwen() {


        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");
        Log.e("新", "asdfasdfasdfa");


        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");
        Log.e("美", "美美美美美美美美美美美");

        Log.e("美", "美美美美美美美美美美美");

        Log.e("凯文", "你好我要提价" + "我在联系冲突解决放啊" +
                "看看自己能不嗯给你解决冲突" +

                "gitle ");

    }


}
