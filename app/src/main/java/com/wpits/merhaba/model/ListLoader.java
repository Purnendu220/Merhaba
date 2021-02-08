package com.wpits.merhaba.model;

/**
 * Created by Varun John on 4/9/2018.
 */

public class ListLoader {

    private boolean isFinish;
    private boolean showText;
    private String finishText = "";
    private String loadingText = "";

    public ListLoader() {
    }

    public ListLoader(boolean showText, String finishText) {
        this.showText = showText;
        this.finishText = finishText;
    }

    public ListLoader(boolean showText, String finishText, String loadingText) {
        this.showText = showText;
        this.finishText = finishText;
        this.loadingText = loadingText;
    }

    public boolean isFinish() {
        return isFinish;
    }

    public void setFinish(boolean finish) {
        isFinish = finish;
    }

    public boolean isShowText() {
        return showText;
    }

    public void setShowText(boolean showText) {
        this.showText = showText;
    }

    public String getFinishText() {
        return finishText;
    }

    public void setFinishText(String finishText) {
        this.finishText = finishText;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }
}
