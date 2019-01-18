package com.neuedu.controller.backend;

import com.neuedu.common.ServerResponse;
import com.neuedu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片上传
 */
@Controller
@RequestMapping(value = "manage/upload")
public class UploadController {
    @Autowired
    ProductService productService;
    /**
     * 图片上传
     */
    @RequestMapping(value = "/upload.do",method = RequestMethod.GET)
    public String upload(){
        return "upload";
    }
    @RequestMapping(value = "/upload.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse upload2(@RequestParam(value = "file_upload",required = false) MultipartFile file){
        String path="/usr/yxy/imgs";
//        String path = "E:\\ftpfile";
        return productService.upload(file,path);
    }
}
