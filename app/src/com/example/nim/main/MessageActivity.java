package com.example.nim.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;

import java.io.File;
import java.util.List;

public class MessageActivity extends ActionBarActivity implements View.OnClickListener{
    // view
    private TextView friendName;
    private Button send_b;
    private EditText sendText;
    private LinearLayout mes;

    // data
    private String receiverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);
        setTitle(MyCache.getAccount());
        findViews();
        initData();
        setListener();
        setFriendName();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, true);
        NIMClient.getService(MsgServiceObserve.class)
                .observeMsgStatus(sendingmessage,true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NIMClient.getService(MsgServiceObserve.class)
                .observeReceiveMessage(incomingMessageObserver, false);
        NIMClient.getService(MsgServiceObserve.class)
                .observeMsgStatus(sendingmessage,false);
    }

    Observer<List<IMMessage>> incomingMessageObserver =
                            new Observer<List<IMMessage>>() {
                                @Override
                                public void onEvent(List<IMMessage> messages) {
                                    // 处理新收到的消息，为了上传处理方便，SDK保证参数messages全部来自同一个聊天对象。
                                    for (IMMessage message : messages) {
                                        if (message.getMsgType() == MsgTypeEnum.image) {
                                        } else {
                                            TextView messagetemp = new TextView(MessageActivity.this);
                                            messagetemp.setText(message.getContent());
                                            mes.addView(messagetemp);
                                        }
                                    }
                }
            };

    Observer<IMMessage> sendingmessage = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            TextView messagetemp = new TextView(MessageActivity.this);
            messagetemp.setText(message.getContent());
            mes.addView(messagetemp);
        }
    };



    private void findViews() {
        friendName = (TextView)findViewById(R.id.friendName);
        send_b = (Button)findViewById(R.id.send_b);
        sendText = (EditText)findViewById(R.id.sendText);
        mes = (LinearLayout)findViewById(R.id.mes);
    }

    private void initData() {
        Bundle bundle =getIntent().getExtras();
        receiverid = bundle.getString("friendAccount");
    }

    private void setListener() {
        send_b.setOnClickListener(this);
    }
    private void setFriendName(){
        friendName.setText(receiverid);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_b:
                sendText();
                break;
            default:
                break;
        }
    }

    /**
     * 发送文本按钮响应事件
     */
    private void sendText() {
        IMMessage message = MessageBuilder.createTextMessage(
                receiverid, // 聊天对象的ID，如果是单聊，为用户账号，如果是群聊，为群组ID
                SessionTypeEnum.P2P, // 聊天类型，单聊或群组
                sendText.getText().toString()// 文本内容
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);


    }




}
