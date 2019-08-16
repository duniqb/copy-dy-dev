package cn.duniqb.copydy.controller;


import cn.duniqb.copydy.common.utils.JSONResult;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 用户相关的业务接口
 */
@RestController
@Api(value = "视频相关的业务接口", tags = {"视频相关的业务接口 controller"})
@RequestMapping("/video")
public class VideoController extends BasicController {

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

        String fileSpace = "E:\\upload";
        String uploadPathDb = "/" + userId + "/video";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
            if (file != null) {
                String filename = file.getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    // 最终路径
                    String finalVideoPath = fileSpace + uploadPathDb + "/" + filename;
                    // 数据库路径
                    uploadPathDb += ("/" + filename);

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

        return JSONResult.ok();
    }

}