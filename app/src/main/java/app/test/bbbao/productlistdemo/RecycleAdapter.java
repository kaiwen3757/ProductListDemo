package app.test.bbbao.productlistdemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;

import java.util.List;

import app.test.bbbao.productlistdemo.Bean.Commodity_Bean;
import butterknife.BindView;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {


    private Context context;
    private List<Commodity_Bean.ResultsBean> mDatas;
    private LayoutInflater inflater;


    public RecycleAdapter(Context context, List<Commodity_Bean.ResultsBean> mDatas) {

        this.context = context;
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recycle_item, null);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {



        holder.commodity_name.setText(mDatas.get(position).getName()+"");
        holder.commodity_pice.setText("￥" + mDatas.get(position).getPrice()+"");
        holder.favorable_price.setText("￥" + mDatas.get(position).getCoupon_amount()+"");
        holder.hand_pices.setText("￥" + mDatas.get(position).getFinal_price() +"");
        holder.shop_name.setText(mDatas.get(position).getSeller_name() + position +"");
        Glide.with(context).load(mDatas.get(position).getImage_url()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView commodity_name,
                commodity_pice,
                favorable_price,
                hand_pices,
                shop_name;
        ImageView image;


        public MyViewHolder(View itemView) {
            super(itemView);


            commodity_name = itemView.findViewById(R.id.commodity_name);
            commodity_pice = itemView.findViewById(R.id.commodity_pice);
            favorable_price = itemView.findViewById(R.id.favorable_price);
            hand_pices = itemView.findViewById(R.id.hand_pices);
            shop_name = itemView.findViewById(R.id.shop_name);
            image = itemView.findViewById(R.id.image);



        }
    }
}
