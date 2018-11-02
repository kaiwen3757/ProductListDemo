package app.test.bbbao.productlistdemo;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


import java.io.IOException;
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

                if("0".equals(i)){
                    initDate(0, 8);
                    recycleAdapter.notifyDataSetChanged();
                    refreshlayout.finishRefresh();
                    i = 0;
                }
                else {
                    bean.clear();
                    initDate(0, 8);
                    recycleAdapter.notifyDataSetChanged();
                    refreshlayout.finishRefresh();
                    i = 0;
                }


                

            }
        });

        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {


                if("0".equals(i)){
                    LoadDate(0,8);
                    recycleAdapter.notifyDataSetChanged();
                    refreshlayout.finishLoadmore();
                    i++;
                    Log.e("凯文2","zai 0 li mian  " + i);

                }else {

                    LoadDate(i*8,i*8+8);
                    recycleAdapter.notifyDataSetChanged();
                    refreshlayout.finishLoadmore();
                    Log.e("凯文2"," zai else zhong" + i );
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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                Log.e("凯文", str);

                message.what = 1;
                Gson gson = new Gson();
                Commodity_Bean bean = gson.fromJson(str, Commodity_Bean.class);
                message.obj = bean;
                Log.e("凯文", bean.getResults().get(0).getName());
                mhandler.sendMessage(message);

            }
        });


    }



    private void LoadDate(Integer start, Integer limit) {

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

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                Log.e("凯文", str);

                message.what = 2;
                Gson gson = new Gson();
                Commodity_Bean bean = gson.fromJson(str, Commodity_Bean.class);
                message.obj = bean;
                Log.e("凯文", bean.getResults().get(0).getName());
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
                    Log.e("凯文", bean.get(0).getName());
                    recycleAdapter = new RecycleAdapter(getApplicationContext(), bean);
                    mRecycleView.setAdapter(recycleAdapter);
                    //mRecycleView.invalidate();
                    break;
                case 2:
                    Loadbean = ((Commodity_Bean) msg.obj).getResults();
                    Log.e("凯文", Loadbean.get(0).getName());
                    bean.addAll(Loadbean);
                    break;
            }

        }
    };




}
