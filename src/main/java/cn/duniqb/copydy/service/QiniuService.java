package cn.duniqb.copydy.service;


import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class QiniuService {
    private static final Logger logger = LoggerFactory.getLogger(QiniuService.class);

    /**
     * 构造一个带指定Zone对象的配置类：华北
     */
    private Configuration cfg = new Configuration(Zone.zone1());
    /**
     * 创建上传对象
     */
    private UploadManager uploadManager = new UploadManager(cfg);
    /**
     * 设置好账号的ACCESS_KEY和SECRET_KEY
     */
    private String accessKey = "E1_SdUp7V_8VFtg60Z_xQgDc4SgP6bKZq5n0dbnj";
    private String secretKey = "PupmxGY1oKAmjk1_p8T19DuasBEOrdBdyAYimIqP";
    /**
     * 要上传的空间
     */
    private String bucket = "toutiao";

    /**
     * 默认不指定key的情况下，以文件内容的hash值作为文件名
     */
    private String key = null;
    private Auth auth = Auth.create(accessKey, secretKey);
    private String upToken = auth.uploadToken(bucket);

    /**
     * 上传文件
     *
     * @param file
     * @return
     * @throws IOException
     */
    @SuppressWarnings("all")
    public String saveImage(MultipartFile file) throws IOException {
        try {
            // 提取扩展名
            int dotPos = file.getOriginalFilename().lastIndexOf(".");
            if (dotPos < 0) {
                return null;
            }
            String fileExt = file.getOriginalFilename().substring(dotPos + 1).toLowerCase();
//            if (!ToutiaoUtil.isFileAllowed(fileExt)) {
//                return null;
//            }

            // 生成文件名
            String fileName = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileExt;

            // 上传
            Response response = uploadManager.put(file.getBytes(), fileName, upToken);
            System.out.println(response.bodyString());

            // 解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println("上传成功! putRet.key: " + putRet.key);
            System.out.println("上传成功! putRet.hash: " + putRet.hash);

            if (response.isOK() && response.isJson()) {
//                return ToutiaoUtil.QINIU_DOMAIN_PREFIX + "/" + putRet.key;
            } else {
                logger.error("七牛异常：" + response.bodyString());
                return null;
            }
        } catch (QiniuException e) {
            // 请求失败时打印的异常的信息
            logger.error("七牛异常:" + e.getMessage());
            return null;
        }
        return null;
    }
}
