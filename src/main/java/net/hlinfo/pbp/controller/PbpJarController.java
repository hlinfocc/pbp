package net.hlinfo.pbp.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(tags = "Jar包操作模块")
@RestController
public class PbpJarController {
	
	@ApiOperation("读取jar包同级目录的txt文件")
    @GetMapping("/{txtfile:\\w+}.txt")
    public String getTxtFile(@ApiParam("文件名") @PathVariable String txtfile
            , HttpServletRequest request, HttpServletResponse response) {
        txtfile = txtfile + ".txt";
        response.reset();
        response.setContentType("text/plain");
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        ApplicationHome jarHome = new ApplicationHome(getClass());
        File jarF = jarHome.getSource();
        String fileName = jarF.getParentFile().toString() + File.separatorChar + txtfile;
        File file = new File(fileName);
        if (!file.exists()) {
            return "welcome";
        }
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }

    @ApiOperation("读取jar包同级目录verify_开头的html文件")
    @GetMapping("/verify_{txtfile:\\w+}.html")
    public String getHtmlFile(@ApiParam("文件名") @PathVariable String txtfile) {
        txtfile = "verify_" + txtfile + ".html";
        ApplicationHome jarHome = new ApplicationHome(getClass());
        File jarF = jarHome.getSource();
        String fileName = jarF.getParentFile().toString() + File.separatorChar + txtfile;
        File file = new File(fileName);
        if (!file.exists()) {
            return "404 File Not Found^_^";
        }
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}

