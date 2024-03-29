package net.hlinfo.pbp.controller;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import cn.hutool.core.img.Img;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.RedisUtils;
import net.hlinfo.pbp.entity.FilesList;
import net.hlinfo.pbp.etc.FileUploadConf;
import net.hlinfo.pbp.opt.PbpRedisKey;
/**
 * 
 * @author hlinfo
 *
 */
@Api(tags = "下载模块")
@RestController
@RequestMapping("/system/pbp/download")
public class PbpDownloadController extends BaseController {
	@Autowired
	private RedisUtils redisCache;
	
	@Autowired
	private FileUploadConf fileUploadConf;
	
	@Autowired
	private Dao dao;
	
	@ApiOperationSupport(order = 1)
	@ApiOperation(value="excel导出下载",notes="")
	@GetMapping("/excel")
	public Object excelDownload(@ApiParam("code")@RequestParam(name="code", defaultValue = "") String code
			,HttpServletRequest request,HttpServletResponse response) {
		//设置响应头
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
		
	    if(Func.isBlank(code)) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('code参数不能为空');</script>";
		}
	    String saveFilePath = redisCache.getObject(PbpRedisKey.DOWNLOAD+code);
		String docTitle = redisCache.getObject(PbpRedisKey.DOWNLOAD+code+":title");
		if(Func.isBlank(saveFilePath)) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('鉴权失败，code失效，请重新下载');</script>";
		}
		String fileSaveName = saveFilePath;
		if(!saveFilePath.startsWith(fileUploadConf.getSavePath())) {
			fileSaveName = fileUploadConf.getSavePath()+saveFilePath;
		}
		File file = new File(fileSaveName);
		if(!file.exists()) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('下载导出文件失败【文件不存在】，请重新导出');</script>";
		}
		
		String ext = FileUtil.extName(docTitle);
		if(Func.isBlank(docTitle)) {
			ext = "xlsx";
			docTitle = "下载文件-" + Func.Times.nowNumber()+StrUtil.DOT+ext;
		}
		if(!docTitle.endsWith(StrUtil.DOT+ext)) {
			docTitle = docTitle+StrUtil.DOT+ext;
		}
		//设置下载头部信息
		this.setDownloadHeader(request, response, docTitle,headers);
		
		try {
			return ResponseEntity.ok().headers(headers)
			.contentLength(file.length())
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			headers.remove("Content-Disposition");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
					.body("<script>alert('下载文件失败，原因："+e.getMessage()+"');</script>");
		}
	}
	
	@ApiOperationSupport(order = 2)
	@ApiOperation(value="下载文件",notes="")
	@GetMapping("/file")
	public Object fileDownload(@ApiParam("文件地址")@RequestParam(name="url", defaultValue = "") String url
			, @ApiParam("显示中文文件名")@RequestParam(name="name", defaultValue = "") String name
			,HttpServletRequest request,HttpServletResponse response) {
		//设置响应头
	    HttpHeaders headers = new HttpHeaders();
	    headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
	    headers.add("Pragma", "no-cache");
	    headers.add("Expires", "0");
		
		String fileSavePath = url;
		if(url.startsWith(fileUploadConf.getBaseUrl())) {
			fileSavePath = url.replace(fileUploadConf.getBaseUrl(),fileUploadConf.getSavePath());
		}
		File file = new File(fileSavePath);
		if(!file.exists()) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('下载文件失败，文件不存在');</script>";
		}
		String ext = FileUtil.extName(url);
		if(Func.isBlank(name)) {
			name = "下载文件-" + Func.Times.nowNumber()+StrUtil.DOT+ext;
		}
		if(!name.endsWith(StrUtil.DOT+ext)) {
			name = name+StrUtil.DOT+ext;
		}
		//设置下载头部信息
		this.setDownloadHeader(request, response, name,headers);
		
		try {
			return ResponseEntity.ok().headers(headers)
			.contentLength(file.length())
			.contentType(MediaType.parseMediaType("application/octet-stream"))
			.contentLength(file.length())
			.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			headers.remove("Content-Disposition");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
					.body("<script>alert('下载文件失败，原因："+e.getMessage()+"');</script>");
		}
	}
	
	@ApiOperationSupport(order = 3)
	@ApiOperation(value="根据ID下载文件",notes="")
	@GetMapping("/file/{id:\\w+}")
	public Object fileDownloadById(@ApiParam("文件保存在数据库中信息的ID") @PathVariable String id
			,HttpServletRequest request,HttpServletResponse response) {
		//设置响应头
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");
		
		FilesList fileInfo = dao.fetch(FilesList.class,id);
		if(fileInfo==null) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('下载文件失败，文件信息不存在');</script>";
		}
		String fileSavePath = fileInfo.getPath();
		File file = new File(fileSavePath);
		if(!file.exists()) {
			response.reset();
			response.setContentType("text/html");
			response.setHeader("Content-Type", "text/html;charset=UTF-8");
			return "<script>alert('下载文件失败，文件不存在');</script>";
		}
		String ext = fileInfo.getSuffix();
		if(!ext.startsWith(StrUtil.DOT)) {
			ext = StrUtil.DOT + ext;
		}
		String name =fileInfo.getName();
		if(Func.isBlank(name)) {
			name = "下载文件-" + Func.Times.nowNumber()+ext;
		}
		if(!name.endsWith(ext)) {
			name = name+ext;
		}
		//设置下载头部信息
		this.setDownloadHeader(request, response, name,headers);
		
		try {
			return ResponseEntity.ok().headers(headers)
					.contentLength(file.length())
					.contentType(MediaType.parseMediaType("application/octet-stream"))
					.contentLength(file.length())
					.body(new InputStreamResource(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			headers.remove("Content-Disposition");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("text/html;charset=UTF-8"))
					.body("<script>alert('下载文件失败，原因："+e.getMessage()+"');</script>");
		}
	}
	
	/**
     * 预览图片&下载文件
     *
     * @param request
     * @param response
     */
	@ApiOperationSupport(order = 4)
	@ApiOperation(value="相对路径模式预览图片、PDF",notes="")
    @GetMapping(value = "/view")
    public Object view(
            @ApiParam("文件地址") @RequestParam(name = "url") String url,
            @ApiParam("是否强制下载，0强制下载，1预览(图片，PDF可预览，其他的为下载)") @RequestParam(name="f", defaultValue = "0") int f,
            @ApiParam("显示文件名") @RequestParam(name="fileName", defaultValue = "") String fileName,
            HttpServletRequest request, HttpServletResponse response) {
        // 设置头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        int download = f;
        OutputStream outputStream = null;
        try {
            String fileUrl = url;
            if(Func.isBlank(fileUrl)){
                response.setStatus(404);
                throw new RuntimeException("文件地址不能为空");
            }
            fileUrl = fileUrl.replace("..", "").replace("../","");
            if (fileUrl.endsWith(StrUtil.COMMA)) {
                fileUrl = fileUrl.substring(0, fileUrl.length() - 1);
            }
            String fileSavePath = url;
    		if(url.startsWith(fileUploadConf.getBaseUrl())) {
    			fileSavePath = fileUrl.replace(fileUploadConf.getBaseUrl(),fileUploadConf.getSavePath());
    		}else {
    			fileSavePath = fileUploadConf.getSavePath() + File.separator + fileUrl;
    		}
            File file = new File(fileSavePath);
            if(!file.exists()){
                response.setStatus(404);
                throw new RuntimeException("文件["+fileUrl+"]不存在..");
            }
            String suffix = fileUrl.substring(fileUrl.lastIndexOf(".")).toLowerCase();
            if(Func.isEmpty(fileName)){
                fileName = file.getName();
            }
            if(!fileName.contains(StrUtil.DOT) || !fileName.endsWith(suffix)){
                fileName = fileName + suffix;
            }

            String[] imgSuffix = new String[]{".gif",".jpg",".jpeg",".png",".bmp"};
            if(download ==1 && Arrays.<String>asList(imgSuffix).contains(suffix)){
                String ext = suffix.replace(".","");
                response.setContentType("image/" + ext);
                outputStream = response.getOutputStream();
                Img img = new Img(ImgUtil.read(file),ext);
                img.write(outputStream);
                return null;
            }else if(download ==1 && Func.equals(suffix,".pdf")){
                return ResponseEntity.ok().headers(headers)
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_PDF)
                        .contentLength(file.length())
                        .body(new InputStreamResource(Files.newInputStream(file.toPath())));
            }else{
                // 设置强制下载不打开
                // 设置响应头
                this.setDownloadHeader(request, response, fileName,headers);
                return ResponseEntity.ok().headers(headers)
                        .contentLength(file.length())
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .contentLength(file.length())
                        .body(new InputStreamResource(Files.newInputStream(file.toPath())));
            }
        } catch (IOException e) {
            log.error("预览文件失败" + e.getMessage());
            response.setStatus(404);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return ResponseEntity.notFound();
    }
	
	/**
	 * 设置下载头部信息
	 @param request
	 @param response
	 @param fileName
	 @param headers
	 @return
	 */
	private boolean setDownloadHeader(HttpServletRequest request, HttpServletResponse response, String fileName,HttpHeaders headers) {
        try {
        	if(headers==null) {
        		headers = new HttpHeaders();
        	}
            response.setContentType("application/octet-stream");
            response.setHeader("content-type", "application/octet-stream");
            String browser = request.getHeader("User-Agent");
            if (-1 < browser.indexOf("MSIE 6.0") || -1 < browser.indexOf("MSIE 7.0")) {
                // IE6, IE7 浏览器
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
                headers.add("Content-Disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else if (-1 < browser.indexOf("MSIE 8.0")) {
                // IE8
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
                headers.add("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("MSIE 9.0")) {
                // IE9
                response.addHeader("content-disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
                headers.add("Content-Disposition", "attachment;filename="
                        + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Chrome")) {
                // 谷歌
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
                headers.add("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            } else if (-1 < browser.indexOf("Safari")) {
                // 苹果
                response.addHeader("content-disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
                headers.add("Content-Disposition", "attachment;filename="
                        + new String(fileName.getBytes(), "ISO8859-1"));
            } else {
                // 火狐或者其他的浏览器
                response.addHeader("content-disposition",
                        "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
                headers.add("Content-Disposition", "attachment;filename*=UTF-8''" + URLEncoder.encode(fileName, "UTF-8"));
            }
            return true;
        } catch (Exception e) {
            //log.error(e.getMessage());
            return false;
        }
    }
}
