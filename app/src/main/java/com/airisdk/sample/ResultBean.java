package com.airisdk.sample;

/**
 * Date:2020/3/18
 * Time:15:22
 * author:mabinbin
 */
public class ResultBean {
    private String METHOD;
    private int R_CODE;
    private String R_MSG;
    private String[] offer_ids ;

    public String getMETHOD() {
        return METHOD;
    }

    public void setMETHOD(String METHOD) {
        this.METHOD = METHOD;
    }

    public int getR_CODE() {
        return R_CODE;
    }

    public void setR_CODE(int R_CODE) {
        this.R_CODE = R_CODE;
    }

    public String getR_MSG() {
        return R_MSG;
    }

    public void setR_MSG(String R_MSG) {
        this.R_MSG = R_MSG;
    }

    public String[] getOffer_ids() {
        return offer_ids;
    }

    public void setOffer_ids(String[] offer_ids) {
        this.offer_ids = offer_ids;
    }
}
