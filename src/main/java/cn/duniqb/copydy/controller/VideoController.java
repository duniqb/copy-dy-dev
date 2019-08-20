package cn.duniqb.copydy.controller;


import cn.duniqb.copydy.common.VideoStatusEnum;
import cn.duniqb.copydy.common.utils.FetchVideoCover;
import cn.duniqb.copydy.common.utils.JSONResult;
import cn.duniqb.copydy.common.utils.MergeVideoMp3;
import cn.duniqb.copydy.common.utils.PageResult;
import cn.duniqb.copydy.model.Bgm;
import cn.duniqb.copydy.model.Videos;
import cn.duniqb.copydy.service.BgmService;
import cn.duniqb.copydy.service.VideoService;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.util.Date;
import java.util.UUID;

/**
 * 用户相关的业务接口
 */
@RestController
@Api(value = "视频相关的业务接口", tags = {"视频相关的业务接口 controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

    @Autowired
    private BgmService bgmService;

    @Autowired
    private VideoService videoService;

    /**
     * 上传视频
     *
     * @param userId
     * @param bgmId
     * @param videoSeconds
     * @param videoWidth
     * @param videoHeight
     * @param desc
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传视频", notes = "上传视频的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "bgmId", value = "背景音乐id",
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoSeconds", value = "背景音乐长度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoWidth", value = "视频宽度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoHeight", value = "视频高度", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "desc", value = "视频描述",
                    dataType = "String", paramType = "form"),
    })
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    public JSONResult upload(String userId, String bgmId, double videoSeconds, int videoWidth, int videoHeight, String desc,
                             @ApiParam(value = "短视频", required = true)
                                     MultipartFile file) throws IOException {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户 ID 不能为空");
        }

//        String fileSpace = "E:\\upload";
        String uploadPathDb = "/" + userId + "/video";
        String coverPathDb = "/" + userId + "/video";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 最终路径
        String finalVideoPath = "";
        try {
            if (file != null) {
                String filename = file.getOriginalFilename();

                // 拿到文件名前缀
                String fileNamePrefix = filename.split("\\.")[0];

                if (StringUtils.isNotBlank(filename)) {
                    finalVideoPath = FILE_SPACE + uploadPathDb + "/" + filename;
                    // 数据库路径
                    uploadPathDb += ("/" + filename);
                    coverPathDb = coverPathDb + "/" + fileNamePrefix + ".jpg";

                    File outFile = new File(finalVideoPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    try {
                        fileOutputStream = new FileOutputStream(outFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputStream = file.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        IOUtils.copy(inputStream, fileOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return JSONResult.errorMsg("上传出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        // 开始合并音视频
        if (StringUtils.isNotBlank(bgmId)) {
            // 音频的路径
            Bgm bgm = bgmService.queryBgmById(bgmId);
            String mp3InputPath = FILE_SPACE + bgm.getPath();

            // 视频的路径
            String videoInputPath = finalVideoPath;

            // 最终输出的文件路径
            String videoOutputName = UUID.randomUUID().toString() + ".mp4";
            uploadPathDb = "/" + userId + "/video" + "/" + videoOutputName;
            finalVideoPath = FILE_SPACE + uploadPathDb;

            MergeVideoMp3 tool = new MergeVideoMp3(FFMPEG_EXE);
            tool.convertor(mp3InputPath, videoInputPath, videoSeconds, finalVideoPath);
        }

        System.out.println("uploadDb" + uploadPathDb);
        System.out.println("finalVideoPath" + finalVideoPath);

        // 对视频截取第一帧
        FetchVideoCover ffMpeg = new FetchVideoCover(FFMPEG_EXE);
        ffMpeg.getCover(finalVideoPath, FILE_SPACE + coverPathDb);


        // 保存视频到数据库
        Videos video = new Videos();
        video.setAudioId(bgmId);
        video.setUserId(userId);
        video.setVideoSeconds((float) videoSeconds);
        video.setVideoHeight(videoHeight);
        video.setVideoWidth(videoWidth);
        video.setVideoDesc(desc);
        video.setVideoPath(uploadPathDb);
        video.setCoverPath(coverPathDb);
        video.setStatus(VideoStatusEnum.SUCCESS.value);
        video.setCreateTime(new Date());

        String videoId = videoService.saveVideo(video);
        // 返回给前端视频的 id
        return JSONResult.ok(videoId);
    }

    /**
     * 上传视频封面
     *
     * @param userId
     * @param videoId
     * @param file
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "上传封面", notes = "上传封面的接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户id", required = true,
                    dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "videoId", value = "视频主键id", required = true,
                    dataType = "String", paramType = "form")
    })
    @PostMapping(value = "/uploadCover", headers = "content-type=multipart/form-data")
    public JSONResult uploadCover(String userId, String videoId,
                                  @ApiParam(value = "视频封面", required = true)
                                          MultipartFile file) throws IOException {

        if (StringUtils.isBlank(videoId) || StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("视频主键 ID 和用户 ID 不能为空");
        }

        String uploadPathDb = "/" + userId + "/video";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        // 最终路径
        String finalCoverPath = "";
        try {
            if (file != null) {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    finalCoverPath = FILE_SPACE + uploadPathDb + "/" + filename;
                    // 数据库路径
                    uploadPathDb += ("/" + filename);

                    File outFile = new File(finalCoverPath);
                    if (outFile.getParentFile() != null || !outFile.getParentFile().isDirectory()) {
                        // 创建父文件夹
                        outFile.getParentFile().mkdirs();
                    }

                    try {
                        fileOutputStream = new FileOutputStream(outFile);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputStream = file.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        IOUtils.copy(inputStream, fileOutputStream);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return JSONResult.errorMsg("上传出错");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                fileOutputStream.flush();
                fileOutputStream.close();
            }
        }

        videoService.updateVideo(videoId, uploadPathDb);

        return JSONResult.ok();
    }

    /**
     * 查询所有视频：分页
     *
     * @param video
     * @param isSaveRecord 1：需要保存，0或空：不需要保存
     * @param page
     * @return
     */
    @PostMapping(value = "/showAll")
    public JSONResult showAll(@RequestBody Videos video, Integer isSaveRecord, Integer page) {
        if (page == null) {
            page = 1;
        }

        PageResult result = videoService.getAllVideos(video, isSaveRecord, page, PAGE_SIZE);
        return JSONResult.ok(result);
    }

    /**
     * 热搜记录
     *
     * @return
     */
    @PostMapping(value = "/hot")
    public JSONResult hot() {

        return JSONResult.ok(videoService.getHotWords());
    }

    /**
     * 用户喜欢视频
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     * @return
     */
    @PostMapping(value = "/userLike")
    public JSONResult userLike(String userId, String videoId, String videoCreaterId) {
        System.out.println("用户喜欢视频.");
        videoService.userLikeVideo(userId, videoId, videoCreaterId);

        return JSONResult.ok();
    }

    /**
     * 用户不喜欢视频
     *
     * @param userId
     * @param videoId
     * @param videoCreaterId
     * @return
     */
    @PostMapping(value = "/userUnlike")
    public JSONResult userUnlike(String userId, String videoId, String videoCreaterId) {
        System.out.println("用户不喜欢视频.");

        videoService.userUnLikeVideo(userId, videoId, videoCreaterId);

        return JSONResult.ok();
    }
}