/**
 * 
 */
package net.hlinfo.pbp.entity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

import io.swagger.annotations.ApiModel;

@Table(value = "file_chunk",prefix = "pbp_")
@Comment("文件分片信息")
@ApiModel("文件分片信息")
public class FileChunk extends BaseEntity {
	private static final long serialVersionUID = 1L;

	@Column(value = "chunk_number")
    private Integer chunkNumber;

    @Column(value = "chunk_size")
    private Float chunkSize;

    @Column(value = "current_chunk_size")
    private Float currentChunkSize;

    @Column(value = "total_chunk")
    private Integer totalChunks;

    @Column(value = "md5_identifier")
    private String md5Identifier;

    @Column(value = "file_name")
    private String fileName;

    @Column(value = "file_type")
    private String fileType;

    @Column(value = "relative_path")
    private String relativePath;

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
	 *  Getter method for property <b>md5Identifier</b>.
	 * @return property value of md5Identifier
	 */
	public String getMd5Identifier() {
		return md5Identifier;
	}

	/**
	 * Setter method for property <b>md5Identifier</b>.
	 *
	 * @param md5Identifier value to be assigned to property md5Identifier
	 */
	public void setMd5Identifier(String md5Identifier) {
		this.md5Identifier = md5Identifier;
	}

	/**
	 *  Getter method for property <b>fileName</b>.
	 * @return property value of fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Setter method for property <b>fileName</b>.
	 *
	 * @param fileName value to be assigned to property fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
}
