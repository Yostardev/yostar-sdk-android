package com.airisdk.sample;

import android.text.TextUtils;

import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;

/**
 * Date:2020/3/18
 * Time:17:28
 * author:mabinbin
 */
public class PurchasItem {
    private String productID ;
    private String price ;
    private String currency ;

    public PurchasItem(String productID,String price,String currency){
        this.productID = productID ;
        this.currency = currency ;
        this.price = price ;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getPrice() {
        if (TextUtils.isEmpty(price)){
            price = "10" ;
        }
        return price;
    }

    public String getCurrency(){
        if (TextUtils.isEmpty(price)){
            price = "$" ;
        }
        return currency ;
    }

}
