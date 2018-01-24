package com.qunar.wechat.auto;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.common.BackgroundExecutor;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.event.AcceptFriendEvent;
import com.qunar.wechat.auto.event.WechatDBEvent;
import com.qunar.wechat.auto.jsonbean.TodoTask;
import com.qunar.wechat.auto.utils.ContactUtil;
import com.qunar.wechat.auto.utils.DataUtils;
import com.qunar.wechat.auto.utils.SharedPrefsHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AccessibilityManager.AccessibilityStateChangeListener {
    private String TAG = MainActivity.class.getSimpleName();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private AccessibilityManager accessibilityManager;
    private Button openServcice, startAutoAddFriends, insert_contact, get_wx_id,autoAddContact,send_comments,remoteTask;
    private TextView status;

    private ProgressBar upload_message_progress;
    private TextView upload_status;

    private CheckBox sysMessage;

    private TextView accept_counts, wx_id;
    private int count;//自动处理接受好友请求数

    private EditText contacts_edit,comments_content;

    SharedPreferences weChatSp;

    int index;
    StringBuilder sb = new StringBuilder("正在上传消息");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        // 监听AccessibilityService 变化
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        accessibilityManager.addAccessibilityStateChangeListener(this);

        upload_message_progress = (ProgressBar) findViewById(R.id.upload_message_progress);
        upload_message_progress.setVisibility(View.GONE);
        upload_status = (TextView) findViewById(R.id.upload_status);

        Timer timer = new Timer();
        timer.schedule(timerTask,500,500);

        openServcice = (Button) findViewById(R.id.openServcice);
        startAutoAddFriends = (Button) findViewById(R.id.startAutoAddFriends);

        status = (TextView) findViewById(R.id.status);

        openServcice.setOnClickListener(this);
        startAutoAddFriends.setOnClickListener(this);

        accept_counts = (TextView) findViewById(R.id.accept_counts);
        accept_counts.setText("已添加人数：" + count);

        contacts_edit = (EditText) findViewById(R.id.contacts_edit);
        insert_contact = (Button) findViewById(R.id.insert_contact);
        insert_contact.setOnClickListener(this);

        Constants.LOCAL_WX_ID = DataUtils.getInstance(this).getPreferences(Constants.Preferences.WX_ID, "");
        wx_id = (TextView) findViewById(R.id.wx_id);

        get_wx_id = (Button) findViewById(R.id.get_wx_id);
        get_wx_id.setOnClickListener(this);
        get_wx_id.setVisibility(View.GONE);

        autoAddContact = (Button) findViewById(R.id.autoAddContact);
        autoAddContact.setOnClickListener(this);

        send_comments = (Button) findViewById(R.id.send_comments);
        send_comments.setOnClickListener(this);

        remoteTask = (Button) findViewById(R.id.remoteTask);
        remoteTask.setOnClickListener(this);

        comments_content = (EditText) findViewById(R.id.comments_content);
        weChatSp = new SharedPrefsHelper(this).getWeChatSp();
        sysMessage = (CheckBox) findViewById(R.id.sysMessage);
        sysMessage.setChecked(weChatSp.getBoolean(Constants.SP_WECHAT_KEY_SERVICE_ISENABLED,true));
        sysMessage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                weChatSp.put(Constants.SP_WECHAT_KEY_SERVICE_ISENABLED,b);
            }
        });
    }

    private void getRemoteTask(){
        if(!TextUtils.isEmpty(Constants.LOCAL_WX_ID)){
            BackgroundExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    List<TodoTask> list = WechatApi.getToDoTask(Constants.LOCAL_WX_ID);
                    if(list == null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("无可执行的任务！！！");
                                return;
                            }
                        });
                    }
//                    List<TodoTask> list = new ArrayList<TodoTask>();
//
//                    TodoTask todoTask1 = new TodoTask();
//                    todoTask1.id = 3;
//                    todoTask1.tasktype = 2;
//                    todoTask1.params.message = "hi";
//                    todoTask1.params.remark = "AA";
//                    todoTask1.params.searchkey = "L19,10";
//                    list.add(todoTask1);
//
//                    TodoTask todoTask = new TodoTask();
//                    todoTask.id = 4;
//                    todoTask.tasktype = 2;
//                    todoTask.params.message = "hi";
//                    todoTask.params.remark = "A";
//                    todoTask.params.searchkey = "L19,16";
//                    list.add(todoTask);
                    List<TodoTask> type2Tasks = new ArrayList<>();
                    for(TodoTask todoTask : list){
                        if(todoTask.tasktype == 2){
                            type2Tasks.add(todoTask);
                        }
                    }
                    Constants.todoTasks = type2Tasks;
                }
            });
        }
    }

    TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            if(index%4 == 0){
                sb = sb.delete(6,10);
            }
            sb.append("。");
            index++;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    upload_status.setText(sb.toString());
                }
            });
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        updateServiceStatus();
        wx_id.setText("当前微信id:" + Constants.LOCAL_WX_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowAcceptFriendRequestCounts(AcceptFriendEvent event) {
        if (event != null) {
            count++;
            accept_counts.setText("已添加人数：" + count);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onShowCurrentFriendCount(WechatDBEvent event){
        if(event != null){
            int count = event.count;
            if(count>4500){
                Toast.makeText(this," 已超出最大好友数！！！！！！！！！！！" + count,Toast.LENGTH_LONG).show();
            }else {
//                Toast.makeText(this,"当前好友数" + count,Toast.LENGTH_LONG).show();
            }
            boolean hasMsg = event.hasMsg;
            if(!hasMsg){
                timerTask.cancel();
                upload_status.setText("消息上传完毕！！！");
            }else {
                timerTask.run();
            }
        }
    }

    @Override
    public void onAccessibilityStateChanged(boolean enabled) {
        updateServiceStatus();
    }

    /**
     * updateServiceStatus
     */
    private void updateServiceStatus() {
        if (isServiceEnabled()) {
            status.setText("服务已启动");
            status.setTextColor(getResources().getColor(R.color.green));
        } else {
            status.setText("服务已关闭");
            status.setTextColor(getResources().getColor(R.color.red));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openServcice:
                openAccessibilityServiceSettings();
                break;
            case R.id.startAutoAddFriends:
                Constants.autoType = Constants.AutoType.AUTO_ACCEPT_FRIEND_REQUEST;
                openWechat();
                break;
            case R.id.insert_contact:
                if (TextUtils.isEmpty(contacts_edit.getText().toString())) {
                    toast("手机好不能为空!");
                    return;
                }
                ContactUtil.insertContact(this, contacts_edit.getText().toString());
                toast("导入完毕！");
                break;
            case R.id.get_wx_id:
                openWechat();
                break;
            case R.id.autoAddContact:
                Constants.autoType = Constants.AutoType.AUTO_ADD_CONTACT_FRIEND;
                openWechat();
                break;
            case R.id.send_comments:
                Constants.autoType = Constants.AutoType.AUTO_PUBLISH_WECHAT_COMMENTS;
                Constants.WECHAT_COMMENTS_CONTENT = comments_content.getText().toString();
                openWechat();
                break;
            case R.id.remoteTask:
                Constants.autoType = Constants.AutoType.AUTO_EXCUTE_TODO_TASK;
                getRemoteTask();
                openWechat();
                break;
        }
    }

    /**
     * 打开辅助服务的设置
     */
    private void openAccessibilityServiceSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openWechat() {
        Intent wxIntent = getPackageManager().getLaunchIntentForPackage(Constants.WECHAT_PACKAGENAME);
        if (wxIntent != null) {
            try {
                startActivity(wxIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(MainActivity.this, "您还没有安装微信",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*** Private ***/
    private boolean isServiceEnabled() {
        List<AccessibilityServiceInfo> accessibilityServices =
                accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : accessibilityServices) {
            if (info.getId().equals(getPackageName() + "/.service.AutoWechatService")) {
                return true;
            }
        }
        return false;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    public void jump2Desktop(Context context) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(intent);
    }

    boolean isSoftinputShow() {
        final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    @Override
    public void onBackPressed() {
        if (isSoftinputShow())
            super.onBackPressed();
        else jump2Desktop(this);
    }
}
