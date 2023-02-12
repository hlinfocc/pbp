package net.hlinfo.pbp.controller;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.lang.Lang;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.extra.qrcode.QrCodeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.opt.RedisKey;
import net.hlinfo.pbp.opt.Resp;

@Api(tags = "验证码模块")
@Controller
@RequestMapping("/system/pbp/captcha")
public class PbpCaptchaController {
	@Autowired
	private RedisUtils redisCache;
	
	@ApiOperation(value="获取验证码，直接访问[数字、字母]")
	@GetMapping("/verifycode.jpg")
	public void getVrcodeJpg(
			@ApiParam(value="验证码时间戳,由js生成，验证时需带上",required = false) @RequestParam(name="time",defaultValue = "",required = false) String time
			, HttpServletRequest req
			, HttpServletResponse resp) throws IOException{
		RandomGenerator randomGenerator = new RandomGenerator("abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ23456789", 4);
//		LineCaptcha c = CaptchaUtil.createLineCaptcha(200, 80, 4, 20);
		CircleCaptcha c = CaptchaUtil.createCircleCaptcha(240, 100, 4, 10);
//		ShearCaptcha c = CaptchaUtil.createShearCaptcha(240, 100, 4, 4);
		c.setGenerator(randomGenerator);
		c.setBackground(Color.decode("#E1E8F3"));
		String code = c.getCode();
		String verifycodekey = Func.isBlank(time)?Lang.getIP(req):(Lang.getIP(req)+":"+time);
		String key = RedisKey.VERIFYCODE + verifycodekey;
		redisCache.setCacheData(key, code,3);
		c.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
	}
	
	@ApiOperation(value="获取验证码，直接访问[数字]")
	@GetMapping("/verifycode.png")
	public void getVrcodePng(
			@ApiParam(value="验证码时间戳,由js生成，验证时需带上",required = false) @RequestParam(name="time",defaultValue = "",required = false) String time
			, HttpServletRequest req
			, HttpServletResponse resp) throws IOException{
		RandomGenerator randomGenerator = new RandomGenerator("0123456789", 4);
//		LineCaptcha c = CaptchaUtil.createLineCaptcha(200, 80, 4, 20);
		CircleCaptcha c = CaptchaUtil.createCircleCaptcha(240, 100, 4, 10);
//		ShearCaptcha c = CaptchaUtil.createShearCaptcha(240, 100, 4, 4);
		c.setGenerator(randomGenerator);
		c.setBackground(Color.decode("#E1E8F3"));
		String code = c.getCode();
		String verifycodekey = Func.isBlank(time)?Lang.getIP(req):(Lang.getIP(req)+":"+time);
		String key = RedisKey.VERIFYCODE + verifycodekey;
		redisCache.setCacheData(key, code,3);
		c.write(resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
	}
	
	@ApiOperation(value="生成二维码")
	@GetMapping("/qrcode")
	public Resp<String> qrcode(@ApiParam(value="二维码内容",required = false) @RequestParam(name="data",defaultValue = "",required = false) String data
			,HttpServletRequest request,HttpServletResponse response) {
		 try{
			 response.setContentType("image/jpg");
			 response.setHeader("content-type", "image/jpg");
			 response.setHeader("Pragma", "no-cache");
			 response.setHeader("Cache-Control", "no-cache");
			 response.setDateHeader("Expires", 0);
			 //获取输出流
			OutputStream os = response.getOutputStream();
			//生成二维码
			QrCodeUtil.generate(data, 500, 500, "jpg", os);
			os.close();
			return null;
		 } catch (Exception e) {
			//e.printStackTrace();
			 response.setContentType("application/json");
			 response.setHeader("Content-Type", "application/json;charset=UTF-8");
			return new Resp().error("ERROR:"+e.getMessage());
		}
	}
	
	public static String getURLEncoderString(String str) {
        String result = str;
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}

