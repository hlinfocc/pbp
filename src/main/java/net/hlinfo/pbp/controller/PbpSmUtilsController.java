package net.hlinfo.pbp.controller;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.BCUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.HashUtils;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.etc.EnvConfig;
import net.hlinfo.pbp.opt.Resp;
/**
 * 
 * @author cy
 *
 */
@Api(tags = "国密工具模块")
@RestController
@RequestMapping("/system/pbp/smUtils")
public class PbpSmUtilsController {
	public static final Logger log = LoggerFactory.getLogger(PbpSmUtilsController.class);
	@Autowired
	private RedisUtils redisCache;
	
	@ApiOperation("sm3")
	@GetMapping("/sm3")
	public Resp<String> sm3(@ApiParam("str")@RequestParam(name="str", defaultValue = "") String str
			, HttpServletRequest request){
		return new Resp().ok("成功",HashUtils.sm3(str));
	}
	
	/**
	 * 生成一对 C1C3C2 格式的SM2密钥对
	 @param request
	 @return sm2公钥
	 @throws Exception
	 */
	@ApiOperation(value = "获取sm2公钥",notes = "密钥格式C1C3C2，js端对应的cipherMode=1<br><pre style=\"background-color:#eee;color: #7F0055;\">JS端使用：<br>//引入依赖：npm install --save sm-crypto<br>const sm2 = require('sm-crypto').sm2<br>const cipherMode = 1  // 1 - C1C3C2，0 - C1C2C3，默认为1<br>//sm2公钥，从后端请求<br>let publicKey = \"\";<br>let strdata = \"123456\";//需要加密的数据<br>//sm2加密<br>let encryptData= sm2.doEncrypt(strdata, publicKey, cipherMode) <br>//加密后传给后端需要在加密后的密文前面加上04<br>encryptData = \"04\"+encryptData;<br>console.log(\"密文：\");<br>console.log(encryptData);<br></pre>")
	@GetMapping("/sm2PublicKey")
	public Resp<String> sm2PublicKey(HttpServletRequest request){
		try {
			String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
			if(Func.isNotBlank(publicKeyStr)) {
				return new Resp().ok("成功",publicKeyStr);
			}
			 //创建sm2 对象
			SM2 sm2 = SmUtil.sm2();
			byte[] privateKeyByte = BCUtil.encodeECPrivateKey(sm2.getPrivateKey());
			//这里公钥不压缩  公钥的第一个字节用于表示是否压缩  可以不要
			byte[] publicKeyByte = ((BCECPublicKey) sm2.getPublicKey()).getQ().getEncoded(false);
			
			String publicKey = HexUtil.encodeHexStr(publicKeyByte);
			String privateKey = HexUtil.encodeHexStr(privateKeyByte);
			//按照天存储公钥私钥，每天不一样
			redisCache.resetCacheData("sm2PublicKey:"+Func.Times.nowDateBasic(), publicKey, 24*60);
			redisCache.resetCacheData("sm2PrivateKey:"+Func.Times.nowDateBasic(), privateKey, 24*60);
			return new Resp().ok("成功",publicKey);
		} catch (Exception e) {
			log.error("获取SM2密钥出错:",e);
			return Resp.FAIL("获取加密密钥出错");
		}
	}
	
	@ApiOperation("sm2加密测试")
	@GetMapping("/sm2Encrypt")
	@Profile({"dev", "test"})
	public Resp<String> sm2Encrypt(@ApiParam("str")@RequestParam(name="str", defaultValue = "") String str
			, HttpServletRequest request) throws Exception{
		
		String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
		String privateKeyStr = redisCache.getObject("sm2PrivateKey:"+Func.Times.nowDateBasic());
		if(Func.isBlank(publicKeyStr) || Func.isBlank(privateKeyStr)) {
			return Resp.FAIL("密钥过期");
		}
		SM2 sm2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
		// 公钥加密，私钥解密
		String encryptStr = sm2.encryptBcd(str, KeyType.PublicKey);
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		this.log.debug(decryptStr);
		return new Resp().ok("成功",NutMap.NEW().addv("密文", encryptStr)
				.addv("明文", decryptStr));
	}
	
	@ApiOperation("sm2解密测试")
	@GetMapping("/sm2Decrypt")
	@Profile({"dev", "test"})
	public Resp<String> sm2Decrypt(@ApiParam("密文")@RequestParam(name="encryptStr", defaultValue = "") String encryptStr
			, HttpServletRequest request) throws Exception{
		
		String publicKeyStr = redisCache.getObject("sm2PublicKey:"+Func.Times.nowDateBasic());
		String privateKeyStr = redisCache.getObject("sm2PrivateKey:"+Func.Times.nowDateBasic());
		if(Func.isBlank(publicKeyStr) || Func.isBlank(privateKeyStr)) {
			return Resp.FAIL("密钥过期");
		}
		
		SM2 sm2 = SmUtil.sm2(privateKeyStr, publicKeyStr);
		// 公钥加密，私钥解密
		String decryptStr = StrUtil.utf8Str(sm2.decryptFromBcd(encryptStr, KeyType.PrivateKey));
		return new Resp().ok("成功",decryptStr);
	}
	
	@ApiOperation(value = "sm4加解密测试",notes = "<pre style=\"background-color:#eee;\">对称<br>加密</pre>")
	@PostMapping("/sm4Ecrypt")
	@Profile({"dev", "test"})
	public Resp<String> sm4Ecrypt(
			@ApiParam("需要加密的内容")@RequestParam(name="content", defaultValue = "") String content
			,@ApiParam("0加密，1解密")@RequestParam(name="type", defaultValue = "0") int type
			,@ApiParam(value="SM4密钥",defaultValue = "0123456789abcedf")@RequestParam(name="key", defaultValue = "0123456789abcedf") String key
			, HttpServletRequest request) throws Exception{
		//密钥为128 bit:英文字符串16位,汉字5个加一个英文字符
//		String key = "@永不忘初心";
//		String keyhex = HexUtil.encodeHexStr(key, CharsetUtil.CHARSET_UTF_8);
		SymmetricCrypto sm4 = SmUtil.sm4(key.getBytes());
		String encryptHex = content;
		if(type==0) {
			encryptHex = sm4.encryptHex(content);
		}
		String decryptStr = sm4.decryptStr(encryptHex, CharsetUtil.CHARSET_UTF_8);
		return new Resp().ok("成功",NutMap.NEW().addv("密文", encryptHex)
				.addv("解密后的明文", decryptStr));
	}
	
	@ApiOperation(value = "sm4文件加解密测试",notes = "<pre style=\"background-color:#eee;\">对称<br>加密</pre>")
	@GetMapping("/sm4EcryptFile")
	@Profile({"dev", "test"})
	public Resp<String> sm4EcryptFile(@ApiParam("需要加密的内容")@RequestParam(name="content", defaultValue = "") String content
			, HttpServletRequest request) throws Exception{
		//密钥为128 bit:英文字符串16位,汉字5个加一个英文字符
		String key = "a111111111111111";
		SymmetricCrypto sm4 = SmUtil.sm4(Func.genSM4key(key));
		//加密
		FileInputStream fileInputStream=new FileInputStream("/htcdc/projectTest/upload/2022/daaed5.png");
       FileOutputStream fileOutputStream=new FileOutputStream("/htcdc/projectTest/upload/2022/daaed5.png.enc");
       BufferedInputStream inputStream=new BufferedInputStream(fileInputStream);
       BufferedOutputStream outputStream=new BufferedOutputStream(fileOutputStream);
       sm4.encrypt(inputStream, outputStream, true);
       //解密
       BufferedInputStream decryptInputStream=new BufferedInputStream(new FileInputStream("/htcdc/projectTest/upload/2022/daaed5.png.enc"));
       BufferedOutputStream decryptOutputStream = new BufferedOutputStream(new FileOutputStream("/htcdc/projectTest/upload/2022/daaed5-1.png"));
       sm4.decrypt(decryptInputStream, decryptOutputStream, true);
		return new Resp().ok("成功");
	}
	
	
}
