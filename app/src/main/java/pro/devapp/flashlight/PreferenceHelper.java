package pro.devapp.flashlight;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Сохранение настроек
 */
public class PreferenceHelper {

    /**
     * Сохранение настройки
     * @param ctx
     * @param pref
     * @param val
     */
    public static void save(Context ctx, String pref, String val){
        SharedPreferences sPref =  ctx.getApplicationContext().getSharedPreferences("SettingsApp", ctx.MODE_MULTI_PROCESS);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString(pref, val);
        ed.apply();
    }

    /**
     * Получение настройки
     * @param ctx
     * @param pref
     * @param default_val
     * @return
     */
    public static String get(Context ctx, String pref, String default_val){
        SharedPreferences sPref =  ctx.getApplicationContext().getSharedPreferences("SettingsApp", ctx.MODE_MULTI_PROCESS);
        return sPref.getString(pref, default_val);
    }
}
