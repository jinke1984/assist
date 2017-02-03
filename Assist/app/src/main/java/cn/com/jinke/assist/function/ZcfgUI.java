package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ViewInject;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;

/**
 * Created by apple on 2017/1/19.
 */

public class ZcfgUI extends ProjectBaseUI {

    @ViewInject(R.id.zcfg_no_data_iv)
    private ImageView mNoDataIV;

    @ViewInject(R.id.list_view)
    private PullToRefreshListView mListView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_zcfg);
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, ZcfgUI.class);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.titleText.setText(getString(R.string.zcfg));
            header.rightLayout.setVisibility(View.GONE);
        }
    }
}
