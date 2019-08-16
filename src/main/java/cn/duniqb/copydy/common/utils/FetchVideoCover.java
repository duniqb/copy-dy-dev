package cn.duniqb.copydy.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取图片截图
 */
public class FetchVideoCover {

    private String ffmpegEXE;

    public FetchVideoCover(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    /**
     * 对第一帧截图
     *
     * @param videoInputPath
     * @param coverOutputPath
     * @throws IOException
     */
    public void getCover(String videoInputPath, String coverOutputPath) throws IOException {
        // ffmpeg.exe -ss 00:00:01 -y -i wx1.mp4 -vframes 1 new.jpg

        List<String> command = new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-ss");
        command.add("00:00:01");

        command.add("-y");
        command.add("-i");
        command.add(videoInputPath);

        command.add("-vframes");
        command.add("1");

        command.add(coverOutputPath);

        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();

        InputStream errorStream = process.getErrorStream();
        InputStreamReader inputStreamReader = new InputStreamReader(errorStream);
        BufferedReader br = new BufferedReader(inputStreamReader);

        String line = "";
        while ((line = br.readLine()) != null) {

        }
        br.close();
        inputStreamReader.close();
        errorStream.close();
    }

    public static void main(String[] args) {
        FetchVideoCover ffMpeg = new FetchVideoCover("D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\ffmpeg.exe");
        try {
            ffMpeg.getCover("D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\wx1.mp4", "D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\new2.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
