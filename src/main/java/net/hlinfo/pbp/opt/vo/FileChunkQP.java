/**
 * 
 */
package net.hlinfo.pbp.opt.vo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import net.hlinfo.opt.Func;
import net.hlinfo.opt.Jackson;

public class FileChunkQP {
	private Long id;

    @NotNull(message = "当前分片不能为空")
    private Integer chunkNumber;

    @NotNull(message = "分片大小不能为空")
    private Float chunkSize;

    @NotNull(message = "当前分片大小不能为空")
    private Float currentChunkSize;

    @NotNull(message = "文件总数不能为空")
    private Integer totalChunks;

    @NotBlank(message = "文件标识不能为空")
    private String identifier;

    @NotBlank(message = "文件名不能为空")
    private String filename;

    private String fileType;

    private String relativePath;

    @NotNull(message = "文件总大小不能为空")
    private Float totalSize;

    private MultipartFile file;

	/**
	 *  Getter method for property <b>id</b>.
	 * @return property value of id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Setter method for property <b>id</b>.
	 *
	 * @param id value to be assigned to property id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 *  Getter method for property <b>chunkNumber</b>.
	 * @return property value of chunkNumber
	 */
	public Integer getChunkNumber() {
		return chunkNumber;
	}

	/**
	 * Setter method for property <b>chunkNumber</b>.
	 *
	 * @param chunkNumber value to be assigned to property chunkNumber
	 */
	public void setChunkNumber(Integer chunkNumber) {
		this.chunkNumber = chunkNumber;
	}

	/**
	 *  Getter method for property <b>chunkSize</b>.
	 * @return property value of chunkSize
	 */
	public Float getChunkSize() {
		return chunkSize;
	}

	/**
	 * Setter method for property <b>chunkSize</b>.
	 *
	 * @param chunkSize value to be assigned to property chunkSize
	 */
	public void setChunkSize(Float chunkSize) {
		this.chunkSize = chunkSize;
	}

	/**
	 *  Getter method for property <b>currentChunkSize</b>.
	 * @return property value of currentChunkSize
	 */
	public Float getCurrentChunkSize() {
		return currentChunkSize;
	}

	/**
	 * Setter method for property <b>currentChunkSize</b>.
	 *
	 * @param currentChunkSize value to be assigned to property currentChunkSize
	 */
	public void setCurrentChunkSize(Float currentChunkSize) {
		this.currentChunkSize = currentChunkSize;
	}

	/**
	 *  Getter method for property <b>totalChunks</b>.
	 * @return property value of totalChunks
	 */
	public Integer getTotalChunks() {
		return totalChunks;
	}

	/**
	 * Setter method for property <b>totalChunks</b>.
	 *
	 * @param totalChunks value to be assigned to property totalChunks
	 */
	public void setTotalChunks(Integer totalChunks) {
		this.totalChunks = totalChunks;
	}

	/**
	 *  Getter method for property <b>identifier</b>.
	 * @return property value of identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Setter method for property <b>identifier</b>.
	 *
	 * @param identifier value to be assigned to property identifier
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 *  Getter method for property <b>filename</b>.
	 * @return property value of filename
	 */
	public String getFilename() {
		return filename;
	}

	/**
	 * Setter method for property <b>filename</b>.
	 *
	 * @param filename value to be assigned to property filename
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 *  Getter method for property <b>fileType</b>.
	 * @return property value of fileType
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Setter method for property <b>fileType</b>.
	 *
	 * @param fileType value to be assigned to property fileType
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 *  Getter method for property <b>relativePath</b>.
	 * @return property value of relativePath
	 */
	public String getRelativePath() {
		return relativePath;
	}

	/**
	 * Setter method for property <b>relativePath</b>.
	 *
	 * @param relativePath value to be assigned to property relativePath
	 */
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}

	/**
	 *  Getter method for property <b>totalSize</b>.
	 * @return property value of totalSize
	 */
	public Float getTotalSize() {
		return totalSize;
	}

	/**
	 * Setter method for property <b>totalSize</b>.
	 *
	 * @param totalSize value to be assigned to property totalSize
	 */
	public void setTotalSize(Float totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 *  Getter method for property <b>file</b>.
	 * @return property value of file
	 */
	public MultipartFile getFile() {
		return file;
	}

	/**
	 * Setter method for property <b>file</b>.
	 *
	 * @param file value to be assigned to property file
	 */
	public void setFile(MultipartFile file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return Jackson.entityToString(this);
	}
	
	public boolean isNew() {
        return this.id==null;
    }
}
