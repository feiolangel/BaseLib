package com.amin.baselib.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amin.baselib.R;


/**
 * Created by Administrator on 2016/4/26.
 */
public class BaseTipDialog extends Dialog implements TextView.OnClickListener {

    private TextView tv_message;

    private TextView tv_cancel, tv_comfirm;

    private ImageView close;

    private String nameClass;

    private Intent mIntent;

    private Context context;

    private String eventMessage = "";

    private int type = 0; //1:退出

    public BaseTipDialog(Context context) {

        super(context);

        this.context = context;

        setContentView(R.layout.dialog_show_base);

        initView();

    }

    private void initView() {

        (tv_cancel = (TextView) findViewById(R.id.id_cancel)).setOnClickListener(this);

        (tv_comfirm = (TextView) findViewById(R.id.id_confirm)).setOnClickListener(this);

        tv_message = (TextView) findViewById(R.id.id_message);

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.id_cancel) {
            this.dismiss();
        } else if (id == R.id.id_confirm) {
            switch (type) {

                //普通提示，无操作
                case 0:

                    this.dismiss();

                    break;

                case 1:



                //EventBus提示，发送eventmessage
                case 666:
                    
                    break;


            }
        }
    }

    public void setTip(String message) {

        try {

            tv_message.setText(message);

        } catch (Exception e) {

        }

    }

    public void setEventMessage(String message) {

        this.eventMessage = message;
    }

    public void setType(int type){

        this.type = type;

    }

    public void setActivity(String name){

        this.nameClass = name;

    }


}
