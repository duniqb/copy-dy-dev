package cn.duniqb.copydy.controller;


import cn.duniqb.copydy.common.utils.JSONResult;
import cn.duniqb.copydy.model.Users;
import cn.duniqb.copydy.model.vo.PublisherVideo;
import cn.duniqb.copydy.model.vo.UsersVO;
import cn.duniqb.copydy.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * 用户相关的业务接口
 */
@RestController
@Api(value = "用户相关的业务接口", tags = {"用户相关的业务接口 controller"})
@RequestMapping("/user")
public class UserController extends BasicController {

    @Autowired
    private UserService userService;


    /**
     * 上传头像
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户上传头像", notes = "用户上传头像的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/uploadFace")
    public JSONResult uploadFace(String userId, @RequestParam("file") MultipartFile[] files) throws IOException {

        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户 ID 不能为空");
        }

        String fileSpace = "E:\\upload";
        String uploadPathDb = "/" + userId + "/face";
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
            if (files != null && files.length > 0) {
                String filename = files[0].getOriginalFilename();
                if (StringUtils.isNotBlank(filename)) {
                    // 最终路径
                    String finalFacePath = fileSpace + uploadPathDb + "/" + filename;
                    // 数据库路径
                    uploadPathDb += ("/" + filename);

                    File outFile = new File(finalFacePath);
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
                        inputStream = files[0].getInputStream();
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

        // 更新到数据库
        Users user = new Users();
        user.setId(userId);
        user.setFaceImage(uploadPathDb);
        userService.updateUserInfo(user);

        return JSONResult.ok(uploadPathDb);
    }

    /**
     * 查询用户信息
     *
     * @param userId
     * @return
     */
    @ApiOperation(value = "查询用户信息", notes = "查询用户信息的接口")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true,
            dataType = "String", paramType = "query")
    @PostMapping("/query")
    public JSONResult query(String userId, String fanId) {
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorMsg("用户 ID 不能为空");
        }

        Users userInfo = userService.queryUserInfo(userId);
        UsersVO userVO = new UsersVO();
        BeanUtils.copyProperties(userInfo, userVO);

        // 是否已经关注
        userVO.setFollow(userService.queryIsFollow(userId, fanId));

        return JSONResult.ok(userVO);
    }

    /**
     * 查询视频发布者
     *
     * @param
     * @return
     */
    @PostMapping("/queryPublisher")
    public JSONResult queryPublisher(String loginUserId, String videoId, String publishUserId) {
        if (StringUtils.isBlank(publishUserId)) {
            return JSONResult.errorMsg("");
        }

        // 查询视频发布者的信息
        Users userInfo = userService.queryUserInfo(publishUserId);
        UsersVO publisher = new UsersVO();
        BeanUtils.copyProperties(userInfo, publisher);

        // 查询当前关注者和发布者的关系
        boolean userLikeVideo = userService.isUserLikeVideo(loginUserId, videoId);

        PublisherVideo publisherVideo = new PublisherVideo();
        publisherVideo.setPublisher(publisher);
        publisherVideo.setUserLikeVideo(userLikeVideo);

        return JSONResult.ok(publisherVideo);
    }

    /**
     * 关注
     *
     * @param userId
     * @param fanId
     * @return
     */
    @PostMapping("/beYourFans")
    public JSONResult beYourFans(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JSONResult.errorMsg("");
        }

        userService.saveUserFanRelation(userId, fanId);

        return JSONResult.ok("关注成功");
    }

    /**
     * 取消关注
     *
     * @param userId
     * @param fanId
     * @return
     */
    @PostMapping("/dontBeYourFans")
    public JSONResult dontBeYourFans(String userId, String fanId) {
        if (StringUtils.isBlank(userId) || StringUtils.isBlank(fanId)) {
            return JSONResult.errorMsg("");
        }

        System.out.println(userId.toString());
        System.out.println(fanId.toString());
        userService.deleteUserFanRelation(userId, fanId);

        return JSONResult.ok("取消关注成功");
    }

}