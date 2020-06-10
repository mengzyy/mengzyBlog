package com.mzy.blog.bean.data;

/**
 * @program: mengzyBlog
 * @author: mengzy 18306299232@163.com
 * @create: 2020-06-09 10:40
 **/
public class UploadResult {
    public static int OK = 200;
    public static int ERROR = 400;

    /**
     * 上传状态
     */
    private int status;

    /**
     * 提示文字
     */
    private String message;

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件存放路径
     */
    private String path;

    public UploadResult ok(String message) {
        this.status = OK;
        this.message = message;
        return this;
    }

    public UploadResult error(String message) {
        this.status = ERROR;
        this.message = message;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
