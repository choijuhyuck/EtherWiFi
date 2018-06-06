package comks_kim.github.etherwifi.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Hun-joyce on 2018-01-12.
 */

public class Preference {
    private final static String PREF_NAME = "priavte_wallet";


    private static Preference instance;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public synchronized static Preference getInstance() {
        if (instance == null) {
            instance = new Preference();
        }
        return instance;
    }

    public void putStringArrayPref(String key, ArrayList<HashMap<String,String>> values) {
        JSONArray a = new JSONArray();

        for(HashMap<String,String> map:values){
            Set<Map.Entry<String, String>> set = map.entrySet();
            for (Map.Entry<String, String> e : set) {
                a.put(e.getKey());
                a.put(e.getValue());
            }
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    public ArrayList<HashMap<String,String>> getStringArrayPref(String key) {
        String json = sharedPref.getString(key, null);
        ArrayList<HashMap<String,String>> urls = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i+=2) {
                    HashMap<String,String> map = new HashMap<>();
                    String url1 = a.optString(i);
                    String url2 = a.optString(i+1);
                    map.put(url1,url2);
                    urls.add(map);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
    //specific key 삭제
    public void remove(String key){
        editor.remove(key).commit();
    }

    //전체 삭제
    public void removeall(){
        editor.clear().commit();
    }

    public void putString (String key, String value) {
        editor.putString(key, value).commit();
    }

    public void putBoolean (String key, boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public void putInt (String key, int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public void putStringSet (String key, Set<String> value) {
        editor.putStringSet(key, value);
        editor.commit();
    }



    public Set<String> getStringSet(String key){
        return sharedPref.getStringSet(key, new HashSet<String>());
    }

    public boolean getBoolean (String key, boolean defaultvalue) {
        return sharedPref.getBoolean(key, defaultvalue);
    }

    public int getInt (String key) {
        return sharedPref.getInt(key, 0);
    }

    public String getString (String key) {
        return sharedPref.getString(key, "");
    }

    @SuppressLint("CommitPrefEdits")
    public void setContext(Context context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }


}
