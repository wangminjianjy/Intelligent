package cn.com.sample.intelligent.util.ListAlert;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.com.sample.intelligent.R;

/**
 * Created by wangm on 2018/10/15.
 */

public class AlertListUtil extends Dialog {

    private Context context;
    private TextView dialogListTitle;
    private TextView dialogListDataNull;
    private ListView dialogListData;

    private String title;
    private List strList;
    private AlertListAdapter alertListAdapter;

    private AdapterView.OnItemClickListener listener;

    public AlertListUtil(Context context) {
        super(context);
        this.context = context;
    }

    public AlertListUtil(Context context, String title, List strList, AdapterView.OnItemClickListener listener) {
        super(context);
        this.context = context;
        this.title = title;
        this.strList = strList;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.setContentView(R.layout.dialog_list_layout);
        dialogListTitle = findViewById(R.id.tv_dialog_list_title);
        dialogListDataNull = findViewById(R.id.tv_dialog_list_data_null);
        dialogListData = findViewById(R.id.lv_dialog_list_data);

        dialogListDataNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialogListTitle.setText(title);
        if (strList.size() == 0) {
            dialogListDataNull.setVisibility(View.VISIBLE);
            dialogListData.setVisibility(View.GONE);
        } else {
            dialogListDataNull.setVisibility(View.GONE);
            dialogListData.setVisibility(View.VISIBLE);
            alertListAdapter = new AlertListAdapter(context);
            alertListAdapter.setData(strList);
            dialogListData.setAdapter(alertListAdapter);
            dialogListData.setOnItemClickListener(listener);
        }
    }
}
