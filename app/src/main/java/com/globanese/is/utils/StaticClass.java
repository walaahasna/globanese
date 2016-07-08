package com.globanese.is.utils;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;


/**
 * Created by 7amdy on 14/8/2014.
 */
public class StaticClass {


    static String fontName="Lato-Regular.ttf";
    static String fontNameBold="Lato-Bold.ttf";
    static String fontNameLight="Lato-Light.ttf";


    public static void overrideFonts(final Context context, final View v , String fontName) {
        try {
            if (v instanceof ViewGroup) {
                if(v.getTag()!= null && v.getTag().equals("noFont")) {

                }else {
                    ViewGroup vg = (ViewGroup) v;
                    for (int i = 0; i < vg.getChildCount(); i++) {
                        View child = vg.getChildAt(i);
                        overrideFonts(context, child, fontName);
                    }
                }
            } else if (v instanceof TextView) {

                if (v.getTag() != null && v.getTag().equals("noFont")) {

                } else if (v.getTag() != null && (v.getTag().equals("bold") || ((TextView)v).getTypeface().getStyle()==Typeface.BOLD)) {
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontNameBold));
                }else if(v.getTag()!= null && v.getTag().equals("light")){
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontNameLight));
                }else{
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
                }

            }
        } catch (Exception e) {
        }
    }

    public static void overrideFonts(final Context context, final View v) {
        overrideFonts(context, v, fontName);
    }

    public static String encodeURL(String u){

        //String imageUrlEncoded=Uri.encode(items.get(i).getImageThumURL());

        if(u!=null && !u.isEmpty()) {
            String host = u.substring(0, u.lastIndexOf("/"));
            String file = u.substring(u.lastIndexOf("/") + 1);
            return host+"/"+( Uri.encode(file)).toString();
        }
        return "";

    }

    public static String encode(String st){
        return ( Uri.encode(st)).toString();
    }

    public static int convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int)px;
    }

    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return dp;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
//            totalHeight += 15;
        }
        Log.d("totalHeight", totalHeight + "");
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setGridViewHeightBasedOnChildren(GridView gridView) {
        ListAdapter listAdapter = gridView.getAdapter();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(gridView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i+=gridView.getNumColumns()) {
            view = listAdapter.getView(i, view, gridView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        totalHeight += 5;
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight ;
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition ) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    public static int getStatusBarHeight(Context con) {
        int result = 0;
        int resourceId = con.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = con.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getUDID(Context context){
        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        return  uuid;
    }

    public static File getAppTempFolder(Context context){
        if( Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ) {
            File f = new File(Environment.getExternalStorageDirectory(), ".QuranRadioTemp");
            if (!f.exists()) {
                f.mkdir();
            }
            File nomedia = new File(f, ".nomedia");
            if (!nomedia.exists()) {
                try {
                    nomedia.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return f;
        }else{
            File f = context.getCacheDir();
            return f;
        }
    }


    public static String getLanguageCode(){
//        String lng=Locale.getDefault().getLanguage();
//        if(lng.isEmpty()){
//            lng="en";
//        }
//        return lng;
//        String lng=Locale.getDefault().getLanguage();
        String lng="en";
        return SharedPrefSingleton.getInstance().getPrefs().getString("lng", lng);
    }

    public static void setLanguageCode(String code){
//        String lng=Locale.getDefault().getLanguage();
//        if(lng.isEmpty()){
//            lng="en";
//        }
//        return lng;
//        return SharedPrefSingleton.getInstance().getPrefs().getString("lng", "en");
        SharedPrefSingleton.getInstance().getPrefs().edit().putString("lng",code).commit();
    }


    public static void setRegId(String reg_id){
        SharedPrefSingleton.getInstance().getPrefs().edit().putString("reg_id",reg_id).apply();
    }

    public static String getRegId(){
        return SharedPrefSingleton.getInstance().getPrefs().getString("reg_id","");
    }


    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.widthPixels;
    }

    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return  dm.heightPixels;
    }

    public static void hide_keyboard_from(Context context, View view) {
        view.clearFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
