package com.airisdk.sample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Date:2020/3/17
 * Time:18:32
 * author:mabinbin
 */
public class InputLayout extends Dialog {

    private String[] editHints;
    private String[] btnTexts;

    public EditText edit_1, edit_2, edit_3;
    public Button btn_1, btn_2, btn_3;

    private View.OnClickListener[] btn_Click;


    public InputLayout(Context context, String[] edithints, String[] btnTexts) {
        super(context);
        this.editHints = edithints;
        this.btnTexts = btnTexts;
    }

    public void setBtnClick(View.OnClickListener[] clicks) {
        this.btn_Click = clicks;
    }

    public void setLayoutParams(View view) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(ResourceUtils.getLayoutId(getContext(),"input_layout"), null, false);
        setContentView(view);

        edit_1 = view.findViewById(ResourceUtils.getId(getContext(),"edit_1"));
        edit_2 = view.findViewById(ResourceUtils.getId(getContext(),"edit_2"));
        edit_3 = view.findViewById(ResourceUtils.getId(getContext(),"edit_3"));
        setLayoutParams(edit_1);
        setLayoutParams(edit_2);
        setLayoutParams(edit_3);

        btn_1 = view.findViewById(ResourceUtils.getId(getContext(),"btn1"));
        btn_2 = view.findViewById(ResourceUtils.getId(getContext(),"btn2"));
        btn_3 = view.findViewById(ResourceUtils.getId(getContext(),"btn3"));
        setLayoutParams(btn_1);
        setLayoutParams(btn_2);
        setLayoutParams(btn_3);

        dissAllEditText();
        if (editHints.length == 0) {
        } else if (editHints.length == 1) {
            edit_1.setVisibility(View.VISIBLE);
            edit_1.setHint(editHints[0]);
        } else if (editHints.length == 2) {
            edit_1.setVisibility(View.VISIBLE);
            edit_1.setHint(editHints[0]);
            edit_2.setVisibility(View.VISIBLE);
            edit_2.setHint(editHints[1]);
        } else if (editHints.length == 3) {
            edit_1.setVisibility(View.VISIBLE);
            edit_1.setHint(editHints[0]);
            edit_2.setVisibility(View.VISIBLE);
            edit_2.setHint(editHints[1]);
            edit_3.setVisibility(View.VISIBLE);
            edit_3.setHint(editHints[2]);
        }
        dissAllButton();
        if (btnTexts.length == 1) {
            btn_1.setVisibility(View.VISIBLE);
            btn_1.setText(btnTexts[0]);
            btn_1.setOnClickListener(btn_Click[0]);
        } else if (btnTexts.length == 2) {
            btn_1.setVisibility(View.VISIBLE);
            btn_1.setText(btnTexts[0]);
            btn_2.setVisibility(View.VISIBLE);
            btn_2.setText(btnTexts[1]);
            btn_1.setOnClickListener(btn_Click[0]);
            btn_2.setOnClickListener(btn_Click[1]);
        } else if (btnTexts.length == 3) {
            btn_1.setVisibility(View.VISIBLE);
            btn_1.setText(btnTexts[0]);
            btn_2.setVisibility(View.VISIBLE);
            btn_2.setText(btnTexts[1]);
            btn_3.setVisibility(View.VISIBLE);
            btn_3.setText(btnTexts[2]);
            btn_1.setOnClickListener(btn_Click[0]);
            btn_2.setOnClickListener(btn_Click[1]);
            btn_3.setOnClickListener(btn_Click[2]);
        }
    }

    private void dissAllEditText() {
        edit_1.setVisibility(View.GONE);
        edit_2.setVisibility(View.GONE);
        edit_3.setVisibility(View.GONE);
    }

    private void dissAllButton() {
        btn_1.setVisibility(View.GONE);
        btn_2.setVisibility(View.GONE);
        btn_3.setVisibility(View.GONE);
    }

}
