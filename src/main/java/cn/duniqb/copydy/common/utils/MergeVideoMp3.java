package cn.duniqb.copydy.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MergeVideoMp3 {

    private String ffmpegEXE;

    public MergeVideoMp3(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    /**
     * 合并音视频
     *
     * @param mp3InputPath
     * @param videoInputPath
     * @param seconds
     * @param videoOutputPath
     * @throws IOException
     */
    public void convertor(String mp3InputPath, String videoInputPath, double seconds, String videoOutputPath) throws IOException {
        // ffmpeg.exe -i bgm_1.mp3 -i wx1.mp4  -t 4 -y out.avi

        List<String> command = new ArrayList<>();
        command.add(ffmpegEXE);

        command.add("-i");
        command.add(mp3InputPath);
        command.add("-i");
        command.add(videoInputPath);
        command.add("-t");
        command.add(String.valueOf(seconds));
        command.add("-y");
        command.add(videoOutputPath);

//        for (String s : command) {
//            System.out.println(s + " ");
//        }
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
        MergeVideoMp3 ffMpeg = new MergeVideoMp3("D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\ffmpeg.exe");
        try {
            ffMpeg.convertor("D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\bgm_3.mp3", "D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\wx1.mp4", 4, "D:\\360极速浏览器下载\\ffmpeg-3.4.2-win64-static\\bin\\new.mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
