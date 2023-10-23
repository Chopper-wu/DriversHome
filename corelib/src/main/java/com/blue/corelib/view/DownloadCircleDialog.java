package com.blue.corelib.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.blue.corelib.R;
import com.blue.corelib.utils.transform.GlideRoundTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

/**
 * Created by Chopper on 2021/4/19
 * desc : 下载弹出框
 */
public class DownloadCircleDialog extends Dialog {
    public DownloadCircleDialog(Context context) {
        super(context, R.style.Theme_Ios_Dialog);
    }
    DownloadCircleView circleView;
    TextView tvMsg;
    ImageView ivLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_circle_dialog_layout);
        this.setCancelable(false);//设置点击弹出框外部，无法取消对话框
        circleView = findViewById(R.id.circle_view);
        tvMsg = findViewById(R.id.tv_msg);
        ivLogo = findViewById(R.id.iv_logo);
        setImg(R.mipmap.ic_launcher);
    }
    public void setProgress(int progress) {
        circleView.setProgress(progress);
    }
    public void setMsg(String msg){
        tvMsg.setText(msg);
    }
    public void setImg(int imgurl){
        Glide.with(getContext())
                .load(imgurl)
                .transform(new CenterCrop(),new GlideRoundTransform(5))
                .into(ivLogo);

    }
}