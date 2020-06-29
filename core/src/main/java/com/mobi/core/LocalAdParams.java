package com.mobi.core;

import com.mobi.core.bean.ShowAdBean;
import com.mobi.core.utils.MD5Helper;

/**
 * @author zhousaito
 * @version 1.0
 * @date 2020/6/5 20:12
 * @Dec 略
 */
public class LocalAdParams {

    /**
     * 用户是用的codeId
     */
    private String mobiCodeId;
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
    /**
     * 平台的 postId
     */
    private String postId;

    private int maxVideoDuration;
    private boolean isSplashNotAllowSdkCountdown;

    /**
     * 是否自动显示广告
     */
    private boolean autoShowAd;

    private int sortType;
    private String md5;
    private boolean isPushMessage;

    private LocalAdParams(Builder builder) {
        mobiCodeId = builder.mobiCodeId;
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
        postId = builder.postId;
        maxVideoDuration = builder.maxVideoDuration;
        isSplashNotAllowSdkCountdown = builder.isSplashNotAllowSdkCountdown;
        autoShowAd = builder.autoShowAd;
        sortType = builder.sortType;
        md5 = builder.md5;
        isPushMessage = builder.isPushMessage;

    }

    public static LocalAdParams create(int sortType, AdParams adParams, ShowAdBean showAdBean, String md5) {
        if (adParams == null || showAdBean == null) {
            return null;
        }

        //请求的一条串行标志
        String postId = showAdBean.getPostId();

        return new Builder()
                .setPostId(postId)
                .setSortType(sortType)
                .setMd5(md5)
                .setPushMessage(showAdBean.isPushMessage())
                .setMobiCodeId(adParams.getCodeId())
                .setAdCount(adParams.getAdCount())
                .setExpressViewAcceptedSize(adParams.getExpressViewWidth(), adParams.getExpressViewHeight())
                .setImageHeight(adParams.getImageHeight())
                .setImageWidth(adParams.getImageWidth())
                .setOrientation(adParams.getOrientation())
                .setAutoShowAd(adParams.isAutoShowAd())
                .setIsAutoPlay(adParams.isAutoPlay())
                .setSupportDeepLink(adParams.isSupportDeepLink())
                .setRewardName(adParams.getRewardName())
                .setRewardAmount(adParams.getRewardAmount())
                .setMediaExtra(adParams.getMediaExtra())
                .setUserID(adParams.getUserID())
                .setMaxVideoDuration(adParams.getMaxVideoDuration())
                .setSplashNotAllowSdkCountdown(adParams.isSplashNotAllowSdkCountdown())
                .build();
    }

    public String getMobiCodeId() {
        return mobiCodeId;
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

    public String getPostId() {
        return postId;
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

    public int getSortType() {
        return sortType;
    }

    public String getMd5() {
        return md5;
    }

    public boolean isPushMessage() {
        return isPushMessage;
    }

    public static final class Builder {
        private String mobiCodeId = "";
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

        private String postId = "";

        private int maxVideoDuration = 30;
        private boolean isSplashNotAllowSdkCountdown = false;

        /**
         * 是否自动显示广告
         */
        private boolean autoShowAd = true;

        private int sortType;
        private String md5;

        private boolean isPushMessage = true;

        public Builder() {
        }

        public Builder setMobiCodeId(String mobiCodeId) {
            this.mobiCodeId = mobiCodeId;
            return this;
        }

        public Builder setSupportDeepLink(boolean supportDeepLink) {
            this.supportDeepLink = supportDeepLink;
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

        public Builder setIsAutoPlay(boolean isAutoPlay) {
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

        public Builder setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }

        public Builder setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        public Builder setSplashNotAllowSdkCountdown(boolean splashNotAllowSdkCountdown) {
            isSplashNotAllowSdkCountdown = splashNotAllowSdkCountdown;
            return this;
        }

        public Builder setPostId(String postId) {
            this.postId = postId;
            return this;
        }

        public Builder setAutoShowAd(boolean autoShowAd) {
            this.autoShowAd = autoShowAd;
            return this;
        }

        public Builder setSortType(int sortType) {
            this.sortType = sortType;
            return this;
        }

        public Builder setMd5(String md5) {
            this.md5 = md5;
            return this;
        }

        public Builder setPushMessage(boolean pushMessage) {
            isPushMessage = pushMessage;
            return this;
        }

        public LocalAdParams build() {
            return new LocalAdParams(this);
        }
    }

}
