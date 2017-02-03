package cn.com.jinke.assist.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import cn.com.jinke.assist.R;
import cn.com.jinke.assist.booter.ProjectBaseUI;

/**
 * Created by apple on 2017/1/19.
 */

public class JybfUI extends ProjectBaseUI {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_jybf);
    }

    public static final void startActivity(Context aContext){
        Intent intent = new Intent(aContext, JybfUI.class);
        aContext.startActivity(intent);
    }

    @Override
    protected void onInitView() {
        Header header = getHeader();
        if(header != null){
            header.titleText.setText(getString(R.string.jybf));
            header.rightLayout.setVisibility(View.GONE);
        }
    }
}
