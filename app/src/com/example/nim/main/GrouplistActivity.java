package com.example.nim.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nim.MyCache;
import com.example.nim.R;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.friend.FriendService;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.attachment.ImageAttachment;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GrouplistActivity extends ActionBarActivity {


    private ListView groupList;
    ArrayAdapter<String> groupAd;
    private List<String> teamNames = new ArrayList<String>();
    List<Team> teams = NIMClient.getService(TeamService.class).queryTeamListBlock();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.grouplist_activity);
        groupList = (ListView) this.findViewById(R.id.groupList);

        for (Team team : teams) {
            teamNames.add(team.getName());
        }

        groupAd = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,teamNames);
        groupList.setAdapter(groupAd);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(GrouplistActivity.this, TeamMessageActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("teamId", teams.get(arg2).getId());
                mBundle.putString("teamName", teamNames.get(arg2));
                intent.putExtras(mBundle);
                startActivity(intent);

            }


        });


    }
}