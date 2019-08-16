package cn.duniqb.copydy.common;

/**
 * 视频状态枚举
 */
public enum VideoStatusEnum {
    /**
     * 可以播放该视频，发布成功
     */
    SUCCESS(1),

    /**
     * 视频禁止播放，管理员操作
     */
    FORBID(2);

    public final int value;

    VideoStatusEnum(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
