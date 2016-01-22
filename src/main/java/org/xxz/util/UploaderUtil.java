package org.xxz.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * 上传工具类
 * @author xxzkid
 *
 */
public final class UploaderUtil {

    private static final Log log = LogFactory.getLog(UploaderUtil.class);

    /**
     * springmvc 文件上传
     * @param request 
     * @param basePath 基础路径 以/开头
     * @param formFileName 表单中file的name值
     * @param dir 上传目标文件夹 以/开头
     * @param allowedPattern 允许的格式
     * @param fileSize 允许上传的大小 单位KB
     * @return 
     */
    public static UploadResult uploader(HttpServletRequest request, String basePath, String formFileName, String dir, List<String> allowedPattern, long fileSize) {
        String successCode = UploadResult.CODE_0;
        String successInfo = UploadResult.DESC_0;

        UploadResult result = new UploadResult();

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        File fileDir = new File(basePath + dir);
        if (!fileDir.exists() && !fileDir.mkdirs()) {
            log.error("upload mkdirs failed.");
            throw new RuntimeException("upload mkdirs failed.");
        }

        /* 文件路径集合 */
        List<String> list = new ArrayList<String>();

        /* 页面控件的文件流 */
        List<MultipartFile> multipartFileList = multipartRequest.getFiles(formFileName);
        if (multipartFileList.isEmpty()) {
            result.setCode(successCode);
            // 添加一个空路径
            list.add("");
            result.setUrls(list);
            result.setDesc(successInfo);
            return result;
        }

        if (allowedPattern == null || allowedPattern.size() == 0) {
            allowedPattern = new ArrayList<String>();
            allowedPattern.add(".gif");
            allowedPattern.add(".jpg");
            allowedPattern.add(".jpeg");
            allowedPattern.add(".png");
        }

        if (fileSize <= 0) {
            fileSize = 1 * 1024 * 1024;
        } else {
            fileSize *= 1024;
        }

        try {
            for (MultipartFile multipartFile : multipartFileList) {
                String originalFilename = multipartFile.getOriginalFilename();
                if ("".equals(originalFilename)) {
                    result.setCode(successCode);
                    // 添加一个空路径
                    list.add(UploadResult.EMPTY_URL);
                    result.setUrls(list);
                    result.setDesc(successInfo);
                    return result;
                }
                /* 获取文件的后缀 */
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
                if (!allowedPattern.contains(suffix)) {
                    result.setCode(UploadResult.CODE_1);
                    result.setUrls(null);
                    result.setDesc(UploadResult.DESC_1);
                    return result;
                }
                if (multipartFile.getSize() > fileSize) {
                    result.setCode("2");
                    result.setUrls(null);
                    result.setDesc(String.format(UploadResult.DESC_2, fileSize));
                    return result;
                }
                /* 使用时间戳生成文件名称 */
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nowTime = sdf.format(new Date());
                String filename = nowTime + suffix;// 构建文件名称

                /** 拼成完整的文件保存路径加文件 **/
                String fullFilename = fileDir + "/" + filename;
                File file = new File(fullFilename);
                multipartFile.transferTo(file);
                list.add(dir + "/" + filename);
            }
            result.setCode(successCode);
            result.setUrls(list);
            result.setDesc(successInfo);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("返回内容为：" + result.toString());
        return result;
    }

    /**
     * 上传后返回结果
     * @author xxzkid
     */
    public static class UploadResult {
        
        /** @see UploadResult#DESC_0 */
        public static final String CODE_0 = "0";
        /** @see UploadResult#DESC_1 */
        public static final String CODE_1 = "1";
        /** @see UploadResult#DESC_2 */
        public static final String CODE_2 = "2";
        
        /** 成功 success */
        public static final String DESC_0 = "成功";
        /** 上传文件格式不正确 upload pattern not true */
        public static final String DESC_1 = "上传文件格式不正确";
        /** 上传文件大小超过了%sKB upload file size gt %s KB */
        public static final String DESC_2 = "上传文件大小超过了%sKB";

        /** 空路径 */
        public static final String EMPTY_URL = "";
        
        private String code; // 返回码
        private String desc; // 描述
        List<String> urls = new ArrayList<String>(); // 上传文件路径的集合

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public void setUrls(List<String> urls) {
            this.urls = urls;
        }

        public List<String> getUrls() {
            return urls;
        }

        @Override
        public String toString() {
            return "UploadResult [code=" + code + ", desc=" + desc + ", urls=" + urls + "]";
        }
    }

}
