package com.bear.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
//import org.jsoup.select.Elements;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;

public class OnlineMusicFragment extends Fragment {
    private WebView webview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.online_music_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        webview = (WebView) view.findViewById(R.id.online_music_webView);
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);


        //支持缩放
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);//设定支持缩放

        //打开的网址
        webview.loadUrl("https://freemusicarchive.org/genre/Classical/");
    }


//    ListView lv;
//    ArrayList<String> url_list=new ArrayList<String>();
//    ArrayList<String> as_list=new ArrayList<String>();
//
//    private String url1,as1;
//    private DownloadService.DownloadBinder downloadBinder;//服务与活动间的通信
//    private ServiceConnection connection=new ServiceConnection() {//ServiceConnection匿名类，
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            downloadBinder=(DownloadService.DownloadBinder) service;//获取downloadBinder实例，用于在活动中调用服务提供的各种方法
//        }
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//        }
//    };
//    private PipedInputStream Jsoup;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.online_music_fragment, container, false);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onViewCreated(view, savedInstanceState);
//
//        MainActivity a = (MainActivity) getActivity();
//        Intent intent1 = new Intent(a, DownloadService.class);
//        startActivity(intent1);//启动服务
//        //bindService(intent1, connection, BIND_AUTO_CREATE);//绑定服务
//        if (ContextCompat.checkSelfPermission(a,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(a,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//        }
//
//        parseHTMLwithJSOUP();//将html数据解析出来并传到界面上
//    }
//
//    //解析html数据
//    public void parseHTMLwithJSOUP() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    DocumentsContract.Document doc = Jsoup.connect("https://freemusicarchive.org/genre/Classical/").ignoreContentType(true);
//                    Elements urls = doc.select("a.icn-arrow");
//                    Elements artists = doc.select("span.ptxt-artist");
//                    Elements songs = doc.select("span.ptxt-track");
//                    for (int i = 0; i < urls.size(); i++) {
//                        String url = urls.get(i).attr("href");
//                        String artist = artists.get(i + 1).text();
//                        String song = songs.get(i + 1).text();
//                        Log.e("URL:", url);
//                        Log.e("ARTIST:", artist);
//                        Log.e("SONG:", song);
//                        //id_list.add(i);
//                        url_list.add(url);
//                        as_list.add(song + "\n" + artist);
//                    }
//                    showResponse();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//    //显示在界面上
//    private void showResponse() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                lv = (ListView) lv.findViewById(R.id.lv_like);
//                final MainActivity activity = (MainActivity) getActivity();
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                        activity, android.R.layout.simple_list_item_1, as_list);
//                lv.setAdapter(adapter);
//
//                //点击item事件
//                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        //得到当前歌曲的相关信息
//                        url1 = url_list.get(position);//得到歌曲下载链接
//                        String name = url1.substring(44);
//                        // int id1=id_list.get(position);
//                        as1 = as_list.get(position);//得到song和artist
//                        Log.d("MainActivity:", "url is " + url1);
//
//                        //如果歌曲不存在，则先下载，如果存在，则直接跳转
//                        if (downloadBinder == null) {
//                            return;
//                        }
//                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "" + name + "");
//
//                        if (!file.exists()) {
//                            downloadBinder.startDownload(url1);//若音乐文件不存在，则进行下载
//                        }
//
//                        Intent intent = new Intent(activity, SongActivity.class);
//                        intent.putExtra("url", url1);
//                        intent.putExtra("as", as1);
//                        // intent.putExtra("id",id1);
//                        intent.putExtra("url_list", url_list);
//                        intent.putExtra("as_list", as_list);
//
//                        startActivity(intent);
//
//                    }
//                });
//            }
//        });
//    }
//
//    private void runOnUiThread(Runnable runnable) {
//    }

}




