package com.mobi.core;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 20:12
 * @Dec 略
 */
public class AdParams {

    private String codeId;
    private boolean supportDeepLink;
    private int expressViewWidth;
    private int expressViewHeight;
    private int adCount;
    private boolean isAutoPlay;
    private String rewardName;
    private int rewardAmount;
    private String mediaExtra;
    private String userID;
    private int orientation;
    private int imageWidth;
    private int imageHeight;

    //gdt
    private int maxVideoDuration;
    private boolean isSplashNotAllowSdkCountdown;

    private boolean autoShowAd;

    private AdParams(Builder builder) {
        codeId = builder.codeId;
        supportDeepLink = builder.supportDeepLink;
        expressViewWidth = builder.expressViewWidth;
        expressViewHeight = builder.expressViewHeight;
        adCount = builder.adCount;
        isAutoPlay = builder.isAutoPlay;
        rewardName = builder.rewardName;
        rewardAmount = builder.rewardAmount;
        mediaExtra = builder.mediaExtra;
        userID = builder.userID;
        orientation = builder.orientation;
        imageWidth = builder.imageWidth;
        imageHeight = builder.imageHeight;
        maxVideoDuration = builder.maxVideoDuration;
        isSplashNotAllowSdkCountdown = builder.isSplashNotAllowSdkCountdown;
        autoShowAd = builder.autoShowAd;

    }

    public String getCodeId() {
        return codeId;
    }

    public boolean isSupportDeepLink() {
        return supportDeepLink;
    }

    public int getExpressViewWidth() {
        return expressViewWidth;
    }

    public int getExpressViewHeight() {
        return expressViewHeight;
    }

    public int getAdCount() {
        return adCount;
    }

    public boolean isAutoPlay() {
        return isAutoPlay;
    }

    public String getRewardName() {
        return rewardName;
    }

    public int getRewardAmount() {
        return rewardAmount;
    }

    public String getMediaExtra() {
        return mediaExtra;
    }

    public String getUserID() {
        return userID;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public int getMaxVideoDuration() {
        return maxVideoDuration;
    }

    public boolean isSplashNotAllowSdkCountdown() {
        return isSplashNotAllowSdkCountdown;
    }

    public boolean isAutoShowAd() {
        return autoShowAd;
    }

    public static final class Builder {
        private String codeId = "";
        private boolean supportDeepLink = true;
        private int expressViewWidth = 300;
        private int expressViewHeight = 300;
        private int adCount = 1;
        private boolean isAutoPlay = true;
        private String rewardName = "gold coin";
        private int rewardAmount = 3;
        private String mediaExtra = "media_extra";
        private String userID = "";
        private int orientation = MobiConstantValue.VERTICAL;
        private int imageWidth = 640;
        private int imageHeight = 320;

        private int maxVideoDuration = 30;

        private boolean isSplashNotAllowSdkCountdown = false;

        private boolean autoShowAd = true;

        public Builder() {
        }

        public Builder setCodeId(String codeId) {
            this.codeId = codeId;
            return this;
        }

        public Builder setSupportDeepLink(boolean supportDeepLink) {
            this.supportDeepLink = supportDeepLink;
            return this;
        }

        public Builder setImageAcceptedSize(int imageWidth, int imageHeight) {
            this.imageWidth = imageWidth;
            this.imageHeight = imageHeight;
            return this;
        }

        public Builder setExpressViewAcceptedSize(int expressViewWidth, int expressViewHeight) {
            this.expressViewWidth = expressViewWidth;
            this.expressViewHeight = expressViewHeight;
            return this;
        }

        public Builder setAdCount(int adCount) {
            this.adCount = adCount;
            return this;
        }

        public Builder setAutoPlay(boolean isAutoPlay) {
            this.isAutoPlay = isAutoPlay;
            return this;
        }

        public Builder setRewardName(String rewardName) {
            this.rewardName = rewardName;
            return this;
        }

        public Builder setRewardAmount(int rewardAmount) {
            this.rewardAmount = rewardAmount;
            return this;
        }

        public Builder setMediaExtra(String mediaExtra) {
            this.mediaExtra = mediaExtra;
            return this;
        }

        public Builder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setMaxVideoDuration(int maxVideoDuration) {
            this.maxVideoDuration = maxVideoDuration;
            return this;
        }

        public Builder setSplashNotAllowSdkCountdown(boolean splashNotAllowSdkCountdown) {
            isSplashNotAllowSdkCountdown = splashNotAllowSdkCountdown;
            return this;
        }

        public Builder setAutoShowAd(boolean autoShowAd) {
            this.autoShowAd = autoShowAd;
            return this;
        }

        public AdParams build() {
            return new AdParams(this);
        }
    }

}
