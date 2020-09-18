package com.airisdk.sample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.YostarENAmazon.AzurLane.R;
import com.airisdk.sdkcall.AiriSDK;

import java.util.List;

/**
 * Date:2020/3/18
 * Time:18:23
 * author:mabinbin
 */
public class PurchaseAdapter extends RecyclerView.Adapter<PurchaseAdapter.ViewHolder> {

    private List<PurchasItem> mPurchaseItem;

    public PurchaseAdapter(List<PurchasItem> mPurchaseItem) {
        this.mPurchaseItem = mPurchaseItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ResourceUtils.getLayoutId(parent.getContext(),"pruduct_items"), parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final PurchasItem purchasItem = mPurchaseItem.get(position);
        viewHolder.prductID.setText(purchasItem.getProductID());
        viewHolder.price.setText(purchasItem.getPrice());
        viewHolder.currency.setText(purchasItem.getCurrency() + " : " );
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 亚马逊测试商品固定为："com.amazon.sample.iap.consumable.orange"
                 */
                AiriSDK.SDKBuy(purchasItem.getProductID(), "audit", "testxxxxxxx2020||" + System.currentTimeMillis());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPurchaseItem.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView prductID;
        TextView price;
        TextView currency ;

        public ViewHolder(View view) {
            super(view);
            prductID = (TextView) view.findViewById(ResourceUtils.getId(view.getContext(),"prudctID"));
            price = (TextView) view.findViewById(ResourceUtils.getId(view.getContext(),"price"));
            currency = view.findViewById(R.id.currency) ;
        }

    }
}
