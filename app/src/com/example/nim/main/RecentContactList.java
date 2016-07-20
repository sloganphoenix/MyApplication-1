package com.example.nim.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.nim.R;
import com.netease.nimlib.sdk.InvocationFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.TeamService;
import com.netease.nimlib.sdk.team.model.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by apple on 2016/7/20.
 */
public class RecentContactList extends ActionBarActivity {

    private ListView contactList;
    List<String> contactIds = new ArrayList<>();
    List<String> contactIds1 = new ArrayList<>();
    List<String> contents = new ArrayList<>();
    List<SessionTypeEnum> sessionTypes = new ArrayList<>();
    List<String> times = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.recent_contact);
        contactList = (ListView) this.findViewById(R.id.contactList);


        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, true);






        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                if (sessionTypes.get(arg2) == SessionTypeEnum.P2P) {
                    intent.setClass(RecentContactList.this, MessageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("friendAccount", contactIds.get(arg2));
                    intent.putExtras(mBundle);
                    startActivity(intent);
                } else if (sessionTypes.get(arg2) == SessionTypeEnum.Team) {
                    intent.setClass(RecentContactList.this, TeamMessageActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putString("teamId", contactIds1.get(arg2));
                    mBundle.putString("teamName", contactIds.get(arg2));
                    intent.putExtras(mBundle);
                    startActivity(intent);

                }


            }


        });



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        NIMClient.getService(MsgServiceObserve.class)
                .observeRecentContact(messageObserver, false);
    }

    Observer<List<RecentContact>> messageObserver =
            new Observer<List<RecentContact>>() {
                @Override
                public void onEvent(List<RecentContact> messages) {
                    NIMClient.getService(MsgService.class).queryRecentContacts()
                            .setCallback(new RequestCallbackWrapper<List<RecentContact>>() {
                                @Override
                                public void onResult(int code, List<RecentContact> recents, Throwable e) {
                                    // recents参数即为最近联系人列表（最近会话列表）
                                    for (RecentContact recent : recents){
                                        if(recent.getSessionType() == SessionTypeEnum.P2P) {
                                            contactIds.add(recent.getContactId());
                                        }
                                        else if (recent.getSessionType() == SessionTypeEnum.Team) {
                                            NIMClient.getService(TeamService.class).queryTeam(recent.getContactId())
                                                    .setCallback(new RequestCallbackWrapper<Team>() {
                                                        @Override
                                                        public void onResult(int code, Team t, Throwable exception) {
                                                            contactIds.add(t.getName());
                                                        }
                                                    });

                                        }
                                        contactIds1.add(recent.getContactId());
                                        contents.add(recent.getContent());
                                        sessionTypes.add(recent.getSessionType());
                                    }
                                }
                            });

                    List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < contactIds.size(); i++) {
                        Map<String, Object> listItem = new HashMap<String, Object>();
                        listItem.put("contactId", contactIds.get(i));
                        listItem.put("content", contents.get(i));
                        listItems.add(listItem);
                    }

                    SimpleAdapter recentAd = new SimpleAdapter(RecentContactList.this, listItems,
                            R.layout.item, new String[] { "contactId", "content"},
                            new int[] {R.id.name,R.id.content});

                    contactList.setAdapter(recentAd);


                }
            };
}
