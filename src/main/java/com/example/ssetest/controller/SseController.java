package com.example.ssetest.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;


/**
 * @Author minhongwei
 * @DateTime 2023/2/21 12:00 星期二
 * @Description: TODO
 */
@RestController
public class SseController {
    /**
     * 服务端循环发送
     *
     * @param httpServletResponse
     * @return
     */
    @GetMapping("/sse")
    public String getStreamData(HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType("text/event-stream");
        httpServletResponse.setCharacterEncoding("utf-8");

        String s = "";
        while (true) {
            s = "data: " + LocalTime.now() + "\n\n";
            try {
                PrintWriter pw = httpServletResponse.getWriter();
                TimeUnit.SECONDS.sleep(1);
                pw.write(s);
                pw.flush();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 客户端固定时间重试
     *
     * @param httpServletResponse
     */
    @GetMapping("/sse-retry")
    public void getDataRetry(HttpServletResponse httpServletResponse) {
        httpServletResponse.setContentType("text/event-stream");
        httpServletResponse.setCharacterEncoding("utf-8");

        String s = "retry: 2000\n";
        s += "data: " + LocalTime.now() + "\n\n";
        PrintWriter pw = null;
        try {
            pw = httpServletResponse.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        pw.write(s);
        pw.flush();
    }

    /**
     * 客户端固定时间重试（简洁版）
     *
     * @return
     */
    @GetMapping("/sse-retry-new")
    public String getDataRetry() {
        /*
         * 数据格式：
         * --------------------------
         * retry: 重试时间（毫秒值）
         * 单换行\n
         * data: 具体数据
         * 双换行\n\n
         * --------------------------
         * retry和data后面是【半角冒号 + 空格】
         * --------------------------
         */
        String result = new StringBuilder()
                // retry: 重试毫秒值 单换行
                .append("retry: 2000\n")
                // data: 具体数据
                .append("data: 当前时间：")
                .append(LocalTime.now())
                // 双换行
                .append("\n\n")
                .toString();

        return result;
    }
}
