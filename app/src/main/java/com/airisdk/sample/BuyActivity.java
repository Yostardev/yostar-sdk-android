package com.airisdk.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.YostarENAmazon.AzurLane.R;
import com.airisdk.sdkcall.tools.entity.SDKSkudetails;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

public class BuyActivity extends AppCompatActivity {

    private List<PurchasItem> purchasItemList ;

    private RecyclerView recyclerView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        purchasItemList = new ArrayList<>() ;
        recyclerView = findViewById(R.id.productListView) ;
        Map<String,Object> skudetails = AiriSDKUtils.getInstance().getStoreSkudetails() ;
        for(String key:skudetails.keySet()){
            SDKSkudetails sku = (SDKSkudetails) skudetails.get(key);
            purchasItemList.add(new PurchasItem(sku.getProductId(),sku.getPrice(), Currency.getInstance(sku.getCurrency()).getSymbol())) ;
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        PurchaseAdapter adapter = new PurchaseAdapter(purchasItemList);
        recyclerView.setAdapter(adapter);
    }
}


