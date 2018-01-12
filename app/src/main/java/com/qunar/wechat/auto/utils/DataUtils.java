package com.qunar.wechat.auto.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.reflect.TypeToken;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class DataUtils {

    public static final String PREFERENCE_NAME = "QunarAutoWechatPreferences";
    public static final String S = "E50C75C5";

    private SharedPreferences sharedPreferences;

    private DataUtils(Context context) {
        sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, Activity.MODE_MULTI_PROCESS);
    }

    private static DataUtils instance = null;
    public static DataUtils getInstance(Context context)
    {
        if(instance == null)
        {
            instance = createSharedPreference(context);
        }
        return instance;
    }

    private static synchronized DataUtils createSharedPreference(Context context)
    {
        if(instance == null)
        {
            instance = new DataUtils(context);
        }
        return instance;
    }
    public  void removePreferences(String key) {
        if (key == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 加密字符串，敏感数据使用此方法
     * @param key
     * @param value
     */
    public synchronized void putPreferences(String key, String value) {
        if (key == null || value == null) {
            return;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            String eValue = DESUtils.encrypt(S, value);
            //LogUtil.d("debug",eValue);
            editor.putString(key, eValue);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        editor.apply();
    }

    public void putPreferences(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public void putPreferences(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putPreferences(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void putPreferences(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
    }

	/**
	 *
	 * 存泛型(List/Map 等...)用这个
	 * #ran.feng
	 */
    public void putPreferences(String key, Serializable value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, JsonUtils.getGson().toJson(value));
        editor.apply();
    }

    public boolean getPreferences(String key, boolean defaultValue) {
        try {
            return sharedPreferences.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public int getPreferences(String key, int defValue) {
        try {
            return sharedPreferences.getInt(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    /**
     * 解密密文
     * @param key
     * @param defValue
     * @return
     */
    public String getPreferences(String key, String defValue) {
        try {
            String originVal = sharedPreferences.getString(key, defValue);
            String result = DESUtils.decrypt(S, originVal);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public long getPreferences(String key, long defValue) {
        try {
            return sharedPreferences.getLong(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

    public float getPreferences(String key, float defValue) {
        try {
            return sharedPreferences.getFloat(key, defValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }

	/**
	 *
	 * 取泛型(List/Map 等...)用这个
	 * #ran.feng
	 */
    public <T extends Serializable> T getPreferencesT(String key, TypeToken<T> type) {
		try {
			return JsonUtils.getGson().fromJson(sharedPreferences.getString(key, null), type.getType());
		} catch (Exception e) {
            e.printStackTrace();
			return null;
		}
    }

    /**
     * @return 是否成功
     */
    public boolean saveObject(Context context, Object object, String key) {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos =context.openFileOutput(key, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            return true;
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            context.deleteFile(key);
        } finally {
            try {
                if (oos != null) {
                    oos.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                oos = null;
                fos = null;
            }
        }
        return false;
    }

    public Object loadObject(Context context, String key) {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(key);
            ois = new ObjectInputStream(fis);
            Object obj = ois.readObject();
            if (obj != null) {
                return obj;
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            context.deleteFile(key);
        } catch (ClassNotFoundException e) {
        } catch (ClassCastException e) {
            context.deleteFile(key);
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                ois = null;
                fis = null;
            }
        }
        return null;
    }
}
