package com.miaoshaproject.controller;

import com.miaoshaproject.utils.CommonReturnType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
public class UploadController {

    InputStream inputStream = null;

    @PostMapping("/upload")
    public CommonReturnType uploadFile(@RequestParam("file")MultipartFile file) throws IOException {
        if(file.isEmpty()) {
            return CommonReturnType.createCommonReturnType("上传失败，文件不能为空","fail");
        }
        String name = file.getOriginalFilename();
        String filePath = "C:/Users/admin/Desktop/miaosha/src/main/resources/static/";
        File dest = new File(filePath + name);
        try {
            System.out.println(dest.getPath());
            file.transferTo(dest);
            inputStream = file.getInputStream();
            return CommonReturnType.createCommonReturnType("文件上传成功");
        } catch (IOException e) {
            e.printStackTrace();
            return CommonReturnType.createCommonReturnType("文件上传失败","fail");
        }
    }

    @GetMapping("download")
    public CommonReturnType downFile() {
        try {
            if(inputStream != null) {
                OutputStream out = new FileOutputStream("ss.xls");
                int b;
                while ((b = inputStream.read()) != -1) {
                    out.write(b);
                }
                out.flush();
                out.close();
                return CommonReturnType.createCommonReturnType(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
