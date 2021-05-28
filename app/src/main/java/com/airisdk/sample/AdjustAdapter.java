package com.airisdk.sample;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.airisdk.sdkcall.AiriSDK;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date:2020/5/14
 * Time:15:13
 * author:mabinbin
 */
public class AdjustAdapter extends RecyclerView.Adapter<AdjustAdapter.ViewHolder> {

    private List<Token> tokens;

    public AdjustAdapter(List<Token> tokens) {
        this.tokens = tokens;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(ResourceUtils.getLayoutId(parent.getContext(),"adjust_items"), parent, false);
        AdjustAdapter.ViewHolder holder = new AdjustAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        Token token = tokens.get(i) ;
        viewHolder.adtTv.setText(token.getToken());
        viewHolder.adtBtn.setText(token.getKey());
        viewHolder.adtBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("test1", "t_test_1");
                map.put("test2", "t_test_2");
                map.put("test3", "t_test_3");
                String json = AiriSDKUtils.getInstance().getGs().toJson(map);
                AiriSDK.getInstance().SDKUserEventUpload(viewHolder.adtBtn.getText().toString(),json);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView adtTv;
        Button adtBtn;

        public ViewHolder(View view) {
            super(view);
            adtTv = view.findViewById(ResourceUtils.getId(view.getContext(), "adtTv"));
            adtBtn = view.findViewById(ResourceUtils.getId(view.getContext(), "adtBtn"));
        }

    }

}
