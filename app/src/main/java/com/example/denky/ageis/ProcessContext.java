package com.example.denky.ageis;

import android.view.ContextMenu;
import android.webkit.WebView;

/**
 * Created by denky on 2017-06-11.
 */

public class ProcessContext {
    private String contextTitle = "";
    private String contextfirstMenu = "";
    private String contextsecondMenu = "";
    private String contextthirdMenu = "";
    private CustomizedWebView wv;
    public CustomizedHandler handler;
    public String getUrl() {       return url;    }
    public void setUrl(String url) {        this.url = url;    }

    private String url="";
    private ContextMenu contextMenu;
    private final String TITLE_HYPERLINK ="하이퍼링크";
    private final String ITEM_IMG_DOWNLOAD = "이미지 다운로드";
    private final String ITEM_IMAG_DOWNLOAD_HIDDEN="숨김 파일로 이미지 다운로드";
    private final String ITEM_IMG_SHARE = "공유하기";
    private final String TITLE_IMG = "이미지";
    private final String ITEM_GO_TO_URL = "주소로 이동하기";
    private final String ITEM_PASTE_URL = "주소 복사하기";
    private final String ITEM_SHARE_URL ="주소 공유하기";
    private final boolean IMG_SHARE_ON = true;
    private final boolean IMG_SHARE_OFF = false;


    private int menuBtnVolume = 3;
    private int typeOfLongClickedItem = 0;//롱클릭된 아이템의 타입. 0 기본값 1은 anchor type, 2는 image, 3은 anchor-image

    ProcessContext(CustomizedWebView wv, CustomizedHandler handler){
        this.wv = wv;
        this.handler = handler;
    } //이 함수에서 웹뷰와 메인액티비티 핸들러에 접근해야함

    public int getMenuBtnVolume() {
        return menuBtnVolume;
    }

    public void setMenuBtnVolume(int menuBtnVolume) {
        this.menuBtnVolume = menuBtnVolume;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public String getContextTitle() {
        return contextTitle;
    }

    public void setContextTitle(String contextTitle) {
        this.contextTitle = contextTitle;
    }

    public String getContextfirstMenu() {
        return contextfirstMenu;
    }

    public void setContextfirstMenu(String contextfirstMenu) {
        this.contextfirstMenu = contextfirstMenu;
    }

    public String getContextsecondMenu() {
        return contextsecondMenu;
    }

    public void setContextsecondMenu(String contextsecondMenu) {
        this.contextsecondMenu = contextsecondMenu;
    }

    public String getContextthirdMenu() {
        return contextthirdMenu;
    }

    public void setContextthirdMenu(String contextthirdMenu) {
        this.contextthirdMenu = contextthirdMenu;
    }

    public void setContextMenuContents(ContextMenu cm){
        cm.setHeaderTitle(contextTitle);
        cm.add(0,1,100,contextfirstMenu);
        cm.add(0,2,100,contextsecondMenu);
        cm.add(0,3,100,contextthirdMenu);
    }// reference : https://developer.android.com/reference/android/view/ContextMenu.html

    public void longClickEvent(WebView.HitTestResult hitTestResult, String url) {
        this.url = url;
        switch(hitTestResult.getType()) {
            case  WebView.HitTestResult.SRC_ANCHOR_TYPE: //앙코르 타입이면
                menuBtnVolume = 3;
                typeOfLongClickedItem = 1;
                setContextTitle(TITLE_HYPERLINK);
                setContextfirstMenu(ITEM_GO_TO_URL);
                setContextsecondMenu(ITEM_PASTE_URL);
                setContextthirdMenu(ITEM_SHARE_URL);
               // Log.d("widae", "Anchor Link:"+url);
                break;
            case WebView.HitTestResult.IMAGE_TYPE: //이미지 타입이면
                menuBtnVolume = 3;
                typeOfLongClickedItem = 2;
                setContextTitle(TITLE_IMG);
                setContextfirstMenu(ITEM_IMG_DOWNLOAD);
                setContextsecondMenu(ITEM_IMAG_DOWNLOAD_HIDDEN);
                setContextthirdMenu(ITEM_IMG_SHARE);
               // Log.d("widae", "Image Link:"+url);
                break;
            case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                menuBtnVolume = 3;
                typeOfLongClickedItem = 3;
                setContextTitle(TITLE_IMG);
                setContextfirstMenu(ITEM_IMG_DOWNLOAD);
                setContextsecondMenu(ITEM_IMAG_DOWNLOAD_HIDDEN);
                setContextthirdMenu(ITEM_IMG_SHARE);
               // Log.d("widae", "Image Anchor Link:"+url);
                break;
            default:
                this.url="";
                menuBtnVolume = 0;
                break;
        }
    }
    private void imgDownload(boolean share, boolean isHidden){
        ImageDownload downloader = new ImageDownload();
        downloader.handler = this.handler;
        downloader.processContext = this;
        downloader.share = share;
        downloader.isHidden=isHidden;
        downloader.execute(this.url);
    }

    public void onContextItemSelected(int itemId) {
        switch(typeOfLongClickedItem) {
            case 1 : // anchor tag
                switch (itemId){
                    case 1 : //주소로 이동
                        wv.goToURL(url);
                       break;
                    case 2 :
                        handler.sendMsgQuick(handler.ANCHOR_COPY);
                        break;
                    case 3 :
                        handler.sendMsgQuick(handler.ANCHOR_SHARE);
                        break;
                }
                break;
            case 2 :// img tag
                switch (itemId){
                    case 1 : //save img
                       imgDownload(IMG_SHARE_OFF, false);
                        break;
                    case 2 : //save img as hidden file
                        imgDownload(IMG_SHARE_OFF, true);
                        break;
                    case 3: //share img
                        imgDownload(IMG_SHARE_ON, false);
                        break;
                }
                break;
            case 3 :// anchor img tag
                url = url.substring(0, this.url.indexOf('?')); //url 파싱해야함
                switch (itemId){
                    case 1 : //save img
                        imgDownload(IMG_SHARE_OFF, false);
                        break;
                    case 2 : //save img as hidden file
                        imgDownload(IMG_SHARE_OFF, true);
                        break;
                    case 3: //share img
                        imgDownload(IMG_SHARE_ON, false);
                        break;
                }
                break;
            default: //anchor, img, anchor-img 중 아무것도 아니면 함수 종료
                return;
        }

    }
}
