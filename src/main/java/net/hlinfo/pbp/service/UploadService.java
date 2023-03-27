package net.hlinfo.pbp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.lang.Lang;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SmUtil;
import net.coobird.thumbnailator.Thumbnails;
import net.hlinfo.opt.Func;
import net.hlinfo.opt.HashUtils;
import net.hlinfo.pbp.entity.FileChunk;
import net.hlinfo.pbp.entity.FilesList;
import net.hlinfo.pbp.entity.MimeTypes;
import net.hlinfo.pbp.etc.FileUploadConf;
import net.hlinfo.pbp.opt.Resp;
import net.hlinfo.pbp.opt.vo.FileChunkQP;
import net.hlinfo.pbp.usr.exception.PbpException;

/**
 * @author hlinfo
 *
 */
public class UploadService {
	public static final Logger log = LoggerFactory.getLogger(UploadService.class);
	/**
     * 默认的分片大小：2MB
     */
    public static final long DEFAULT_CHUNK_SIZE = 2 * 1024 * 1024;
    
    /**
     * 文件名 正则字符串
     * 文件名支持的字符串：字母数字中文.-_()（） 除此之外的字符将被删除
     */
    private static String FILE_NAME_REGEX = "[^A-Za-z\\.\\(\\)\\-（）\\_0-9\\u4e00-\\u9fa5]";
    
	@Autowired
	private FileUploadConf fileUploadConf;
	
	@Autowired
	private Dao dao;
	
	public FilesList upload(MultipartFile file) throws PbpException,Exception {
			String dir = fileUploadConf.getSavePath();
			log.debug("文件保存根目录:{}",dir);
			
			// 检查目录写权限
			File fileBaseDir = new File(dir);
			if(!fileBaseDir.canWrite()){
				throw new PbpException("上传目录没有写权限。");
			}
			String orgFileName = file.getOriginalFilename();
			
			String ymd = Func.Times.nowDateSlash();
			String fileSuffix = getSuffix(file.getContentType(), null,orgFileName);
			// 检查后缀
			if(!Arrays.<String>asList(fileUploadConf.suffixMap().get("all").split(",")).contains(fileSuffix)){
				throw new PbpException("上传文件是不允许上传的文件类型。");
			}
			String fileName = this.getFileNameBySm3() + fileSuffix;
			String saveDirPath= File.separator + ymd;
			String saveFilePath = saveDirPath + File.separator + fileName;
			File saveDir = new File(dir, saveDirPath);
			if(!saveDir.exists()) {
				saveDir.mkdirs();
				touchEmptyHtml(saveDir.getAbsolutePath(),3);
			}
			
			File saveFile = new File(dir, saveFilePath);
			// 上传
			if(Arrays.<String>asList(fileUploadConf.suffixMap().get("imageNoGif").split(",")).contains(fileSuffix)){
				if(file.getSize()>2*1024*1024) {
					//上传图片大小超过限制，进行压缩处理后保存，按照比例缩小
					this.compress(file,saveFile, 0.7f);
				}else {
					file.transferTo(saveFile);
				}
			}else {
				file.transferTo(saveFile);
			}
			log.debug("新文件:{}",saveFile.getAbsolutePath());
			String url = fileUploadConf.getBaseUrl() + saveFilePath.replace(File.separator, "/");
			FilesList data =new FilesList();
			try {
				orgFileName = URLUtil.decode(orgFileName);
			}catch(Exception e) {}
			data.init();
			data.setName(getFileName(orgFileName));
			data.setMimeType(file.getContentType());
			data.setPath(saveFile.getAbsolutePath());
			data.setFileHash(SmUtil.sm3(new FileInputStream(saveFile)));
			if(fileUploadConf.isRelative()) {				
				data.setUrl(saveFilePath.replace(File.separator, "/"));
			}else {
				data.setUrl(url);
			}
			data.setSuffix(fileSuffix);
			data.setSize(saveFile.length());
			String userid = "anonymous";
			String usertype = "anonymous";
			try {
				if(StpUtil.isLogin()) {
					String[] ids = StpUtil.getLoginIdAsString().split("-");
					if(ids != null && ids.length >= 2) {
						userid = ids[1];
					}
					usertype= ids[0];
				}
			}catch(Exception e) {
				log.error("未登录用户上传，采用默认userid=anonymous");
			}
			data.setUserid(userid);
			return data;
		}
	
	private String getSuffix(String contentType, String filePath,String orgFileName) {
		String suffix = "";
		String fileSuffix = orgFileName.indexOf(".")>=0?(orgFileName.substring(orgFileName.lastIndexOf(".")).toLowerCase()):"";
		if(Func.isNotBlank(fileSuffix)) {
			return fileSuffix; 
		}
		if(Func.isNotBlank(contentType)) {
			Cnd cnd = Cnd.where("isdelete", "=", 0);
			cnd.and("content_type", "=", contentType);
			MimeTypes type = dao.fetch(MimeTypes.class, cnd);
			if(type != null) {
				suffix = type.getSuffix();
			}
		}
		if(Func.isBlank(suffix) && Func.isNotBlank(filePath)) {
			suffix = getFileType(filePath);
		}
		return suffix;
	}
	
	/**
	 * 获取文件的类型
	 * @param filePath 文件路径
	 * @return 文件的类型
	 */
	public String getFileType(String filePath) {
		try  {
			Path path = new File(filePath).toPath();
			String contentType = java.nio.file.Files.probeContentType(path);
			return "." + contentType.split("/")[1];
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	public String getFileContentType(String filePath) {
		try  {
			Path path = new File(filePath).toPath();
			String contentType = java.nio.file.Files.probeContentType(path);
			return contentType;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	/**
	 * 生成文件名,sm3加密（时间戳和UUID）生成
	 * @return
	 */
	public String getFileNameBySm3() {
		return HashUtils.sm3(System.currentTimeMillis() + Func.getRandom(10) + Func.UUID36());
	}
	
	private String getUserLoginId() {
		String userId = "anonymous";
		try {
			if(StpUtil.isLogin()) {
				String[] ids = StpUtil.getLoginIdAsString().split("-");
				if(ids != null && ids.length >= 2) {
					userId = ids[1];
				}
			}
		}catch(Exception e) {
			log.error("未登录用户上传，采用默认userid=anonymous");
		}
		return userId;
	}
	/**
	 * 在指定层级（当前及父层级目录往上计算）的目录中创建index.html文件<br>
	 * 目录层级示例：<pre>
	 * level=0  -> "/opt/aaa/bbb/cc/ddd"
	 * level=1  -> "/opt/aaa/bbb/cc"
	 * level=2  -> "/opt/aaa/bbb/"
	 * level=3  -> "/opt/aaa/"
	 * </pre>
	 * @param saveDirPath 目录或文件
	 * @param level 层级
	 */
	private void touchEmptyHtml(String saveDirPath, int level) {
		try {
			File file = new File(saveDirPath);
			for(int i=0;i<level;i++) {				
				File fx = FileUtil.getParent(file, i);
				if(fx!=null) {
					FileUtil.touch(fx, "index.html");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 压缩文件
	 * @param file 待处理文件
	 * @param saveFile 处理后保存的文件
	 * @param scale 压缩比
	 */
	private void compress(MultipartFile file,File saveFile,double scale) {
		try {
			Thumbnails.of(file.getInputStream()).scale(scale).toFile(saveFile.getPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 分片上传处理，追加模式
	 * @param saveFileName 保存文件名
	 * @param param 文件分片信息参数
	 * @return
	 */
	@Deprecated
	private boolean uploadFileByRandomAccessFile(String saveFileName, FileChunkQP param) {
        try (
        	RandomAccessFile randomAccessFile = new RandomAccessFile(saveFileName, "rw")) {
            // 分片大小，注意：必须和前端匹配，否则上传会导致文件损坏
            long chunkSize = param.getChunkSize() == 0L ? DEFAULT_CHUNK_SIZE : param.getChunkSize().longValue();
            // 偏移量
            long offset = chunkSize * (param.getChunkNumber() - 1);
            // 定位到该分片的偏移量
            randomAccessFile.seek(offset);
            // 写入
            randomAccessFile.write(param.getFile().getBytes());
        } catch (IOException e) {
            log.error("文件上传失败",e);
            return false;
        }
        return true;
    }
	@Deprecated
	public boolean uploadAppendFile(FileChunkQP param) {
        if (null == param.getFile()) {
            throw new RuntimeException("上传文件不能为空");
        }
        // 判断目录是否存在，不存在则创建目录
        File savePath = new File(fileUploadConf.getSavePath());
        if (!savePath.exists()) {
            boolean flag = savePath.mkdirs();
            if (!flag) {
                log.error("保存目录创建失败");
                return false;
            }
        }
        // 这里可以使用 uuid 来指定文件名，上传完成后再重命名
        String fullFileName = savePath + File.separator + param.getFilename();
        // 单文件上传
        if (param.getTotalChunks() == 1) {
        	try {
				param.getFile().transferTo(new File(fullFileName));
				return true;
			} catch (IllegalStateException e) {
				log.error(e.getMessage(),e);
			} catch (IOException e) {
				log.error(e.getMessage(),e);
			}
            return false;
        }
        // 分片上传，这里使用 uploadFileByRandomAccessFile 方法，也可以使用 uploadFileByMappedByteBuffer 方法上传
        boolean flag = uploadFileByRandomAccessFile(fullFileName, param);
        if (!flag) {
            return false;
        }
        // 保存分片上传信息
        this.saveFileChunk(param);
        return true;
    }
	@Deprecated
	public void saveFileChunk(FileChunkQP param) {
        FileChunk fileChunkDo = null;
        if (!param.isNew()) {
            fileChunkDo = this.dao.fetch(FileChunk.class, param.getId());
            if (null == fileChunkDo) {
                throw new RuntimeException("数据不存在");
            }
        }
        if (null == fileChunkDo) {
            fileChunkDo = new FileChunk();
        } else {
            fileChunkDo.setUpdateTime(new Date());
        }
        BeanUtils.copyProperties(param, fileChunkDo, "id");
        fileChunkDo.setFileName(param.getFilename());
        fileChunkDo.setMd5Identifier(param.getIdentifier());
        int result;
        if (param.isNew()) {
        	fileChunkDo.init();
            result = this.dao.insert(fileChunkDo) != null ? 1 : 0;
        } else {
            result = this.dao.update(fileChunkDo);
        }
        if (0 == result) {
            throw new RuntimeException("操作失败");
        }
    }
	/**
     * 分片上传 - 分割上传在合并
     *
     * @param file     : 文件流
     * @param hash     : 哈希值
     * @param filename : 文件名
     * @param seq      : 分片序号
     * @param type     : 文件类型
    */
    public Resp<String> uploadSlice(byte[] file, String hash, String filename, Integer seq, String type) {
        RandomAccessFile raf = null;
        String baseSavePath = fileUploadConf.getSavePath() + File.separator;
        try {
            // 创建目标文件夹
            File dir = new File(baseSavePath + hash);
            if (!dir.exists()) {
                dir.mkdir();
            }
            // 创建空格文件 名称带 seq 用于标识分块信息
            raf = new RandomAccessFile(baseSavePath + hash + File.separator + Lang.md5(filename) + "." + type + seq, "rw");
            // 写入文件流
            raf.write(file);
        } catch (IOException e) {
            e.printStackTrace();
            return Resp.ERROR("上传失败");
        } finally {
            try {
                if (raf != null) {
                    raf.close();
                }
            } catch (IOException e) {
                // ...打印异常日志...
            }
        }
        return Resp.OK("上传成功");
    }
    
    /**
    * 合并文件的业务代码
    *
    * @param fileName : 文件名
    * @param hash     : 文件哈希值
    * @param type     : 文件类型
    */
    public Resp<FilesList> uploadMergeSlice(String fileName, String type, String hash) {
        // 判断 hash 对应文件夹是否存在
    	String md5Filename = Lang.md5(fileName);
    	String baseSavePath = fileUploadConf.getSavePath() + File.separator;
        File dir = new File(baseSavePath + hash);
        if (!dir.exists()) {
            return Resp.ERROR("合并失败，请稍后重试");
        }
        // 这里通过 FileChannel 来实现信息流复制
        FileChannel out = null;
        // 获取目标 channel
        String savePath = baseSavePath + File.separator + hash;
        String saveFileName = savePath + File.separator + md5Filename + '.' + type;
        File file = new File(saveFileName);
        try (FileChannel in = new RandomAccessFile(file, "rw").getChannel()) {
            // 分片索引递增
            int index = 1;
            // 开始流位置
            long start = 0;
            while (true) {
                // 分片文件名
                String sliceName = saveFileName + index;
                // 到达最后一个分片 退出循环
                if (!new File(sliceName).exists()) {
                    break;
                }
                // 分片输入流
                out = new RandomAccessFile(sliceName, "r").getChannel();
                // 写入目标 channel
                in.transferFrom(out, start, start + out.size());
                // 位移量调整
                start += out.size();
                out.close();
                out = null;
                FileUtil.del(sliceName);
                // 分片索引调整
                index++;
            }
            // 文件合并完毕
            in.close();
            
            try {
            	fileName = URLUtil.decode(fileName);
            }catch(Exception e) {
            	
            }
            
            FilesList fileInfo = new FilesList();
            fileInfo.init();
            fileInfo.setName(fileName + "." + type);
            fileInfo.setPath(file.getAbsolutePath());
            fileInfo.setMimeType(this.getFileContentType(file.getAbsolutePath()));
            fileInfo.setFileHash(SmUtil.sm3(new FileInputStream(file)));
            fileInfo.setSize(FileUtil.size(file));
            fileInfo.setSuffix(type);
            fileInfo.setUrl(fileUploadConf.getBaseUrl() + "/" + hash + "/" + md5Filename + "." + type);
            if(StpUtil.isLogin()) {
            	fileInfo.setUserid(this.getUserLoginId());
            }else {
            	fileInfo.setUserid("anonymous");
            }
            dao.insert(fileInfo);
            return Resp.OK("上传成功",fileInfo);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return Resp.ERROR("上传失败，请稍后重试");
    }
    
    public Resp<FilesList> fastUploadSlice(String hash) {
    	FilesList fileInfo = dao.fetch(FilesList.class, Cnd.where("file_md5", "=", hash));
        if (fileInfo != null) {
            return Resp.OK("极速秒传成功", fileInfo);
        } else {
            return Resp.ERROR("极速秒传失败");
        }
    }
    
    /**
     * 判断文件名是否带盘符，重新处理
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName){
        // 判断是否带有盘符信息
        // 检查Unix样式的路径
        int unixSep = fileName.lastIndexOf('/');
        // 检查Windows样式的路径
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","")
                .replace("#", "").replace("“", "").replace("”", "");
        //替换上传文件名字中的空格
        fileName=fileName.replaceAll("\\s","");
        fileName = fileName.replaceAll(FILE_NAME_REGEX, "");
        return fileName;
    }
}
