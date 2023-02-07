package com.credit.oem.admin.common.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * @author chenzj
 * @since 2018/6/6
 */
@Component
public class UploadUtils {

    /**
     * 上传图片  存放路径: 201806/3efff300a48a4d3dbcd6c0c1eac49f44.png
     */
    public String uploadPicture(MultipartFile file, String basePath,String baseSealImageUploadPath) throws IOException {
        String fileName;
        String type;
        String suffix;
        try {
            SimpleImageInfo imageInfo = new SimpleImageInfo(file.getInputStream());
            type = imageInfo.getImageType().getType();
            suffix = "." + type;
            fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;
        } catch (IOException e) {
            throw new RuntimeException("无法识别图片类型");
        }
        String yearMonth = String.valueOf(DateTimeUtil.formateDateToYearMonthInt(new Date())) + "/";
        String path = basePath  + "/" + yearMonth;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 上传
        try {
            Files.write(Paths.get(path + fileName), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("无法保存图片");
        }
        if (StringUtils.isNotBlank(baseSealImageUploadPath)){
            File source =  new File(path + fileName);
            File dest =  new File(baseSealImageUploadPath + "/" + fileName);
            FileUtils.copyFile(source, dest);
        }

        return yearMonth + fileName;
    }

    /**
     * 上传txt  存放路径: 201806/3efff300a48a4d3dbcd6c0c1eac49f44.txt
     */
    public String uploadTxt(MultipartFile file, String basePath, String folderName) {
        String fileName;
        String type = "txt";
        String suffix;
        suffix = "." + type;
        fileName = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

        String yearMonth = String.valueOf(DateTimeUtil.formateDateToYearMonthInt(new Date())) + "/";
        String path = basePath + folderName + "/" + yearMonth;
        File fileDir = new File(path);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        // 上传
        try {
            Files.write(Paths.get(path + fileName), file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("无法保存文件");
        }
        return folderName + "/" + yearMonth + fileName;
    }


}
