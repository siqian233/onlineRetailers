package com.lightning.utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


//TODO: java 通过文件内容的魔术字检查文件类型
public class FileTypeValidator {
    private static final Set<String> ALLOWED_MIME_TYPES = new HashSet<>(Arrays.asList(
            "image/jpeg",
            "image/png",
            "image/gif",
            "application/pdf",
            "application/msword", // .doc
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .docx
            "application/vnd.ms-excel", // .xls
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" // .xlsx
    ));

    //允许的文件扩展名（用于双重检查，不如MIME类型可靠）
    private static final Set<String> ALLOWED_EXTENSIONS = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif",
            "pdf",
            "doc", "docx",
            "xls", "xlsx"
    ));

    /**
     * 验证文件类型是否合法
     *
     * @param contentType 文件的MIME类型
     * @param filename 文件名
     * @return true表示文件类型合法，false表示文件类型非法
     */
    public static boolean isValidFileType(String contentType, String filename) {
        //检查参数
        if (contentType == null || filename == null || contentType.isEmpty() || filename.isEmpty()) {
            return false;
        }
        //检查MIME类型
        if (!ALLOWED_MIME_TYPES.contains(contentType.toLowerCase())) {
            return false;
        }
        //检查文件扩展名
        String fileExtension = getFileExtension(filename);
        if (fileExtension == null || !ALLOWED_EXTENSIONS.contains(fileExtension.toLowerCase())) {
            return false;
        }

        return true;
    }

    /**
     * 获取文件的扩展名
     *
     * @param filename 文件名
     * @return 文件的扩展名，如果没有扩展名则返回null
     */
    private static String getFileExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < filename.length() - 1) {
            return filename.substring(dotIndex + 1);
        }
        return null;
    }
}
