package com.airisdk.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.YostarENAmazon.AzurLane.R;
import com.airisdk.sdkcall.tools.utils.AiriSDKUtils;
import com.airisdk.sdkcall.tools.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SampleActivity extends AppCompatActivity {

    private Map<String, Object> maps = null;

    private RecyclerView adjustView = null;

    private List<Token> list = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ResourceUtils.getLayoutId(this, "activity_sample"));
        list = new ArrayList<>();
        maps = AiriSDKUtils.getInstance().getAdjustEventTokens();
        adjustView = findViewById(R.id.adjustView);
        for (String key : maps.keySet()) {
            list.add(new Token(key, (String) maps.get(key)));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        adjustView.setLayoutManager(layoutManager);

        AdjustAdapter adapter = new AdjustAdapter(list);
        adjustView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        list = null;
        maps = null;
        adjustView = null;
        LogUtil.e("onBackPressed onBackPressed onBackPressed");
        finish();
    }
}

class Token {

    public Token(String key, String token) {
        this.key = key;
        this.token = token;
    }

    private String key;
    private String token;

    public String getKey() {
        return key;
    }

    public String getToken() {
        return token;
    }

}



