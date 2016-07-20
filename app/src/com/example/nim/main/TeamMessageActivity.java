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

public class TeamMessageActivity extends ActionBarActivity implements View.OnClickListener{
    // view
    private TextView teamName;
    private Button teamSend_b;
    private EditText teamSendText;
    private LinearLayout teamMes;

    // data
    private String receiverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_message_activity);
        setTitle(MyCache.getAccount());
        findViews();
        initData();
        setListener();
        setTeamName();
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
                            TextView messagetemp = new TextView(TeamMessageActivity.this);
                            messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
                            teamMes.addView(messagetemp);
                        }
                    }
                }
            };

    Observer<IMMessage> sendingmessage = new Observer<IMMessage>() {
        @Override
        public void onEvent(IMMessage message) {
            TextView messagetemp = new TextView(TeamMessageActivity.this);
            messagetemp.setText(message.getFromAccount() + ": " + message.getContent());
            teamMes.addView(messagetemp);
        }
    };



    private void findViews() {
        teamName = (TextView)findViewById(R.id.teamName);
        teamSend_b = (Button)findViewById(R.id.teamSend_b);
        teamSendText = (EditText)findViewById(R.id.teamSendText);
        teamMes = (LinearLayout)findViewById(R.id.teamMes);
    }

    private void initData() {
        Bundle bundle =getIntent().getExtras();
        receiverid = bundle.getString("teamId");
    }

    private void setListener() {
        teamSend_b.setOnClickListener(this);
    }

    private void setTeamName(){
        Bundle bundle =getIntent().getExtras();
        teamName.setText(bundle.getString("teamName"));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.teamSend_b:
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
                SessionTypeEnum.Team, // 聊天类型，单聊或群组
                teamSendText.getText().toString()// 文本内容
        );
        NIMClient.getService(MsgService.class).sendMessage(message, false);


    }




}
