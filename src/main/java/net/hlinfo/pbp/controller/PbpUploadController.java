package net.hlinfo.pbp.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.util.cri.SqlExpressionGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.hlinfo.pbp.entity.FilesList;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.opt.vo.MergeInfo;
import net.hlinfo.pbp.service.UploadService;
import net.hlinfo.pbp.usr.exception.PbpException;

@Api(tags = "文件上传模块")
@RestController
@RequestMapping("/system/pbp/upload")
public class PbpUploadController {
	public static final Logger log = LoggerFactory.getLogger(UploadService.class);
	
	@Autowired
	private UploadService uploadService;
	
	@Autowired
	private Dao dao;
	
	@ApiOperationSupport(order = 1)
	@ApiOperation(value="单文件上传方法",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="file",value="文件流数据",required = true,paramType = "form",dataType = "__File")
	})
	@PostMapping("/singleFile")
	public Resp<FilesList> singleFile(
			@RequestPart("file") 
			@RequestParam(value="file",required = true) MultipartFile file
			,HttpServletRequest request){
		try {
			FilesList fileInfo = uploadService.upload(file);			
			return Resp.OK("上传成功", fileInfo);
		}catch (PbpException e) {
			log.error(e.getMessage(),e);
			return new Resp<FilesList>().error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return Resp.ERROR("上传失败" + e.getMessage());
		} 
	}

	@ApiOperationSupport(order = 2)
	@Operation(summary ="批量文件上传方法")
	@Parameter(name = "file",description = "文件",in = ParameterIn.DEFAULT,ref="file",schema = @Schema(type = "file",ref = "file"))
	@PostMapping("/multiple")
	public Resp<List<FilesList>> multipleFiles(
			@RequestPart(value = "files") 
			@RequestParam("files") List<MultipartFile> files,HttpServletRequest request){
		if(files == null|| files.size() <= 0) {
			return new Resp<List<FilesList>>().error("文件列表为空");
		}
		try {
			List<FilesList> fileList = new ArrayList<FilesList>();
			for(MultipartFile fileItem : files) {
				FilesList fileInfo = uploadService.upload(fileItem);
				fileList.add(fileInfo);
			}
			return Resp.OK("上传成功", fileList);
		}catch (PbpException e) {
			log.error(e.getMessage(),e);
			return new Resp<List<FilesList>>().error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			return new Resp<List<FilesList>>().error("上传失败" + e.getMessage());
		}
	}
	
	@ApiOperationSupport(order = 3)
	@ApiOperation(value="[保存数据库]单文件上传",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="file",value="文件流数据",required = true,paramType = "form",dataType = "__File")
	})
	@PostMapping("/singleSaveDB")
	public Resp<FilesList> singleFileSaveDB(
			@RequestPart("file") 
			@RequestParam(value="file",required = true) MultipartFile file
			,HttpServletRequest request){
		try {
			FilesList fileInfo = uploadService.upload(file);	
			dao.insert(fileInfo);
			return Resp.OK("上传成功", fileInfo);
		}catch (PbpException e) {
			log.error(e.getMessage(),e);
			return new Resp<FilesList>().error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			return Resp.ERROR("上传失败" + e.getMessage());
		} 
	}
	
	@ApiOperationSupport(order = 4)
	@ApiOperation(value="[保存数据库]批量文件上传方法",consumes="multipart/form-data")
	@ApiImplicitParams({
		@ApiImplicitParam(name="files",value="文件流数据",dataTypeClass = MultipartFile.class,required = true,paramType = "form",dataType = "__File")
	})
	@PostMapping("/multipleSaveDB")
	public Resp<List<FilesList>> multipleFilesSaveDB(@RequestPart("files") MultipartFile[] files,HttpServletRequest request){
		if(files == null|| files.length <= 0) {
			return new Resp<List<FilesList>>().error("文件列表为空");
		}
		try {
			List<FilesList> fileList = new ArrayList<FilesList>();
			for(MultipartFile file : files) {
				FilesList fileInfo = uploadService.upload(file);
				fileList.add(fileInfo);
			}
			if(fileList!=null && !fileList.isEmpty()) {
				dao.insert(fileList);
				return Resp.OK("上传成功", fileList);
			}else {
				return new Resp<List<FilesList>>().error("上传失败");
			}
		}catch (PbpException e) {
			log.error(e.getMessage(),e);
			return new Resp<List<FilesList>>().error(e.getMessage());
		}catch (Exception e) {
			log.error(e.getMessage(),e);
			return new Resp<List<FilesList>>().error("上传失败" + e.getMessage());
		}
	}
	
	@ApiOperationSupport(order = 5)
	@ApiOperation(value="分片上传 - 检查极速秒传接口")
	@GetMapping("/slice/check")
	public Resp<FilesList> sliceCheck(@RequestParam(value = "hash") String hash) {
	    return uploadService.fastUploadSlice(hash);
	}
	
	@ApiOperationSupport(order = 6)
	@ApiOperation(value="分片上传 - 上传分片的接口", notes = "分段上传，上传完了后需要调用合并合并文件")
	@PostMapping("/slice/upload")
	public Resp<String> sliceUpload(
			@RequestParam(value = "file") 
			@ApiParam("文件信息")
			MultipartFile file,
			
			@RequestParam(value = "hash") 
			@ApiParam("文件哈希值")
			String hash,
			
			@RequestParam(value = "filename") 
			@ApiParam("文件名")
			String filename,
			
			@RequestParam(value = "seq") 
			@ApiParam("分片序号")
			Integer seq,
			
			@RequestParam(value = "type") 
			@ApiParam("文件类型")
			String type) {
		try {
	        return uploadService.uploadSlice(file.getBytes(), hash, filename, seq, type);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return Resp.ERROR("上传失败");
	    }
	}
	
	@ApiOperationSupport(order = 6)
	@ApiOperation(value="分片上传 - 文件合并接口")
	@PostMapping("/slice/merge")
	public Resp<FilesList> sliceMerge(@RequestBody MergeInfo mergeInfo) {
	    if (mergeInfo!=null) {
	        String filename = mergeInfo.getFilename();
	        String type = mergeInfo.getType();
	        String hash = mergeInfo.getHash();
	        return uploadService.uploadMergeSlice(filename, type, hash);
	    }
	    return Resp.ERROR("文件合并失败");
	}
	
	@ApiOperationSupport(order = 7)
	@ApiOperation(value="查询文件列表")
	@PostMapping("/getFileList")
	public Resp<List<FilesList>> getFileList(@ApiParam("fileIds 文件ID")@RequestBody List<String> fileIds
			, HttpServletRequest request, HttpServletResponse response) {
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		SqlExpressionGroup e = Cnd.cri().where().andInStrList("id",fileIds);;
		cnd.and(e);
		List<FilesList> list=dao.query(FilesList.class,cnd);
		return Resp.OK("successful",list);
	}
	
	@ApiOperationSupport(order = 8)
	@ApiOperation(value="根据ID查询文件")
	@PostMapping("/getFileById")
	public Resp<FilesList> getFileById(@ApiParam("fileId 文件ID")@RequestBody List<String> fileId
			, HttpServletRequest request, HttpServletResponse response) {
		Cnd cnd = Cnd.where("isdelete", "=", 0);
		cnd.and("id","=",fileId);
		FilesList fileInfo = dao.fetch(FilesList.class,cnd);
		return Resp.OK("获取成功",fileInfo);
	}
	
}

