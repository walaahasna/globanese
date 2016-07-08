package com.kennyc.bottomsheet;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;


/**
 * Created by 7amdy on 14/8/2014.
 */
public class StaticClass {


    public static String fontName="fonts/GE_SS_Two_Light.otf";
    static String fontNameBold=fontName;
//    public static String fontName= "fonts/DroidNaskh-Regular.otf";
//    static String fontNameBold= "fonts/DroidNaskh-Bold.otf";
    static String results_font= "fonts/amiri-regular.ttf";


    static String fontNameLight=fontNameBold;
    static String fontAwesome="fontawesome-webfont.ttf";



    public static void overrideFonts(final Context context, final View v ,String fontName) {
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

                if (v.getTag() != null && v.getTag().equals("no_font")) {

                } else if (v.getTag() != null && v.getTag().equals("bold")) {
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontNameBold));
                }else if(v.getTag()!= null && v.getTag().equals("light")){
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontNameLight));
                }else if(v.getTag()!= null && v.getTag().equals("fa")){
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontAwesome));
                }else if(v.getTag()!= null && v.getTag().equals("result")){
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), results_font));
                }else{
                    ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), fontName));
                }



            }
        } catch (Exception e) {
        }
    }

    public static void overrideFonts(final Context context, final View v) {
        overrideFonts(context,v,fontName);
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




    public String saveBitmap(Bitmap bmp)  {

        try {

            String path;
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/HarajAghnamTEMP";
            File dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File nomedia=new File(dir,".nomedia");
            if(!nomedia.exists())
                nomedia.createNewFile();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new java.util.Date());

//            File file = new File(dir, timeStamp + "_Image_tmp");
            File file =new File(dir,timeStamp + "_Image_tmp.jpg"  );
            file.deleteOnExit();
            path = file.getAbsolutePath();
            FileOutputStream fOut = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            fOut.flush();
            fOut.close();





            return path;

        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static String getUDID(Context context){
        TelephonyManager tManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        return  uuid;
    }


    public static void clearTMP(){
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/HarajAghnamTEMP";
        File tmpFolder=new File(file_path);
        if(tmpFolder.exists()){
            for(File file:tmpFolder.listFiles()){
                if(file.getName().endsWith("jpg")){
                    file.delete();
                }
            }
        }
    }

    public static int getScreenWidth(Activity activity){
        int width ;
        int height ;
        Display display = activity.getWindowManager().getDefaultDisplay();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }else{
            width = display.getWidth();
            height = display.getHeight();
        }
        return width;
    }

}
