package cn.duniqb.copydy.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FFMpegTest {

    private String ffmpegEXE;

    public FFMpegTest(String ffmpegEXE) {
        this.ffmpegEXE = ffmpegEXE;
    }

    public void convertor(String videoInputPath, String videoOutputPath) throws IOException {
        List<String> command = new ArrayList<>();
        command.add(ffmpegEXE);
        command.add("-i");
        command.add(videoInputPath);
        command.add(videoOutputPath);

        for (String s : command) {
            System.out.println(s + " ");
        }
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
        FFMpegTest ffMpeg = new FFMpegTest("D:\\360极速浏览器下载\\ffmpeg-20190815-ddd92ba-win64-static\\bin\\ffmpeg.exe");
        try {
            ffMpeg.convertor("D:\\360极速浏览器下载\\ffmpeg-20190815-ddd92ba-win64-static\\bin\\wx1.mp4", "D:\\360极速浏览器下载\\ffmpeg-20190815-ddd92ba-win64-static\\bin\\bjbj.avi");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
