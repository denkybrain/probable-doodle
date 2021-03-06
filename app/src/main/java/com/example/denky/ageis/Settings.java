package com.example.denky.ageis;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

/**
 * Created by WINDOWS7 on 2017-05-31.
 */

public class Settings{

    //This Inner Class is only using when saving setting value to file.
    private static class SetInfo implements Serializable{

        public boolean useJavaScript=true;
        public boolean permissionStartNewWindow=true;
        public boolean permissionFileDownload=false;
        public boolean permissionAppCache=true;
        public String homeUri="http://denkybrain.cafe24.com/ageis/main.php";
        public HashMap<String,String> favoriteSiteList=new HashMap<>();

        public boolean useVulnerabilityFindAlgorithm=true;
        public boolean permissionDangerousSite =false;
        public boolean permissionAutoRemoveHistory=true;

        public SetInfo(){
            initiate();
        }

        private void initiate(){
            useJavaScript=true;
            permissionStartNewWindow=true;
            permissionFileDownload=false;
            permissionAppCache=true;
            homeUri="http://denkybrain.cafe24.com/ageis/main.php";
            favoriteSiteList=new HashMap<String, String>();

            //Test Code
            favoriteSiteList.put("네이버", "www.naver.com");
            favoriteSiteList.put("다음", "www.daum.net");
            favoriteSiteList.put("구글", "www.google.com");
            //

            useVulnerabilityFindAlgorithm=true;
            permissionDangerousSite =false;
            permissionAutoRemoveHistory=true;
        }
    }

    static final String TAG="Setting File";

    //Setting Value
    public static boolean useJavaScript=true;
    public static boolean permissionStartNewWindow=true;
    public static boolean permissionFileDownload=false;
    public static boolean permissionAppCache=true;
    public static String homeUri="http://denkybrain.cafe24.com/ageis/main.php";
    public static HashMap<String,String> favoriteSiteList=new HashMap<>();

    public static boolean useVulnerabilityFindAlgorithm=true;
    public static boolean permissionDangerousSite =false;
    public static boolean permissionAutoRemoveHistory=true;
    public static boolean autoClearUrl=true;

    //Stream for File I/O
    private static ObjectInputStream inputSettings=null;
    private static ObjectOutputStream outputSettings=null;

    private static String filePath=Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Ageis"+File.separator+".AgeisAppSettings.set";

    //This class is never instantiated at other class
    private Settings(){}

    public static void resetSetting(){
        try {
            SetInfo newSetInfo=new SetInfo();
            outputSettings=new ObjectOutputStream(new FileOutputStream(filePath));
            outputSettings.writeObject(newSetInfo);
            outputSettings.close();
        } catch (IOException e) {
            Log.i(TAG, "Can't restore Setting File");
        }
        loadSettings();
    }
    public static void resetSetting_exceptFavoriteSiteList(){
        try {
            HashMap<String, String> temp=favoriteSiteList;

            SetInfo newSetInfo=new SetInfo();
            newSetInfo.favoriteSiteList=temp;
            outputSettings=new ObjectOutputStream(new FileOutputStream(filePath));
            outputSettings.writeObject(newSetInfo);
            outputSettings.close();
        } catch (IOException e) {
            Log.i(TAG, "Can't restore Setting File");
        }
        loadSettings();
    }

    public static boolean loadSettings(){
        File settingFile=new File(filePath);

        File dir=new File(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+"Ageis");
        if(!dir.exists()){
            dir.mkdirs();
        }
        
        try{
            if(settingFile.exists()==false){
                Log.i(TAG, "Setting File is not existing");
                //make Output Stream (when not existing setting file)
                resetSetting();
                Log.i(TAG, "Success writing new object");
            }else{
                Log.i(TAG, "Setting File is exist");
            }

            //make Input Stream
            inputSettings=new ObjectInputStream(new FileInputStream(settingFile));

            //Read Setting file
            SetInfo info=null;
            Object readObject=inputSettings.readObject();
            if(readObject instanceof  SetInfo){
                info=(SetInfo)readObject;
            }else{
                Log.i(TAG, "Fail to read object in file");
                return false;
            }
            Log.i(TAG, "Success Reading");

            if(info!=null){
                useJavaScript=info.useJavaScript;
                permissionStartNewWindow=info.permissionStartNewWindow;
                permissionFileDownload=info.permissionFileDownload;
                permissionAppCache=info.permissionAppCache;
                useVulnerabilityFindAlgorithm=info.useVulnerabilityFindAlgorithm;
                permissionDangerousSite =info.permissionDangerousSite;
                permissionAutoRemoveHistory=info.permissionAutoRemoveHistory;
                homeUri=info.homeUri;
                favoriteSiteList=info.favoriteSiteList;
            }else{
                Log.i(TAG, "Fail to read object in file");
                return false;
            }
            inputSettings.close();
            Log.i(TAG, "Successfully Read");
        }catch(Exception e) {
            Log.i(TAG, "Exception Occur");
            return false;
        }
        Log.i("List Counter", String.valueOf(favoriteSiteList.size()));
        return true;
    }

    public static void saveSettings(){
        SetInfo info=new SetInfo();

        info.useJavaScript=useJavaScript;
        info.permissionStartNewWindow=permissionStartNewWindow;
        info.permissionFileDownload=permissionFileDownload;
        info.permissionAppCache=permissionAppCache;
        info.homeUri=homeUri;
        info.useVulnerabilityFindAlgorithm=useVulnerabilityFindAlgorithm;
        info.permissionDangerousSite = permissionDangerousSite;
        info.permissionAutoRemoveHistory=permissionAutoRemoveHistory;
        info.favoriteSiteList=favoriteSiteList;

        try {
            outputSettings=new ObjectOutputStream(new FileOutputStream(filePath));
            outputSettings.writeObject(info);
            outputSettings.close();
        } catch (IOException e) {
            Log.i(TAG, "Can't save settings");
        }
    }
}
