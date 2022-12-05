package dev.fly_yeseul.coupang.controllers;

import com.sun.org.apache.xerces.internal.impl.io.UCSReader;
import com.sun.org.apache.xpath.internal.operations.Mod;
import dev.fly_yeseul.coupang.entities.temp.UploadEntity;
import dev.fly_yeseul.coupang.enums.temp.UploadResult;
import dev.fly_yeseul.coupang.services.UploadService;
import dev.fly_yeseul.coupang.vos.temp.UploadVo;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.Http2;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Controller(value = "dev.fly_yeseul.coupang.controllers.TempController")
@RequestMapping(value = "/temp")
public class TempController {

    private final UploadService uploadService;

    @Autowired
    public TempController(UploadService uploadService) {
        this.uploadService = uploadService;
    }


    @RequestMapping(value = "upload", method = RequestMethod.GET)
    public ModelAndView getFile(
            @RequestAttribute(value = "uploadEntity", required = false) UploadEntity uploadEntity,
            HttpServletResponse response,
//            UploadEntity uploadEntity,
//            HttpServletRequest request,
            ModelAndView modelAndView
    ) {
        modelAndView.setViewName("temp/upload");
        return modelAndView;
    }

//    @RequestMapping(value = "upload", method = RequestMethod.POST)
//    public ModelAndView postFile(
////            @RequestAttribute(name = "upload") int index
////            UploadEntity uploadEntity,
////            HttpServletResponse response,
//            UploadVo uploadVo,
//            ModelAndView modelAndView
//    ) {
//        uploadVo.setResult(null);
//        this.uploadService.addFile(uploadVo);
//        if(uploadVo.getResult() == UploadResult.SUCCESS) {
//            modelAndView.setViewName("redirect:/");
//        } else {
//            modelAndView.addObject("uploadVo", uploadVo);
//            modelAndView.setViewName("temp/upload");
//        }
//        return modelAndView;
//    }

//    @RequestMapping(value = "upload/upload-file", method = RequestMethod.POST)
//    @ResponseBody
//    public String postUploadFile(
//            HttpServletResponse response,
//            @RequestAttribute(name = "uploadEntity", required = false) UploadEntity uploadEntity
////            @RequestParam(name = "upload") MultipartFile[] files
//    ) throws IOException{
//        Date createdAt = new Date();
//        this.uploadService.uploadFile(uploadEntity);
//        JSONObject responseJson = new JSONObject();
//        JSONArray urlJson = new JSONArray();
//        urlJson.put(String.format("http://localhost:8080/temp/download?index=%s", uploadEntity.getIndex()));
//        responseJson.put("url", urlJson);
//        return responseJson.toString();
//    }

    @RequestMapping(value = "download", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> getDownloadFile(
            @RequestParam(value = "index", required = false) int index
    ) {
        UploadEntity uploadEntity = null;
        if(index != 0) {
            uploadEntity = this.uploadService.getFile(index);
        }
        byte[] data = null;
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.NOT_FOUND;
        if(uploadEntity != null && uploadEntity.getData() != null) {
            data = uploadEntity.getData();
            headers.add("Content-Type", uploadEntity.getFileType());
            headers.add("Content-Disposition", String.format("attachment; filename=\"%d\"", uploadEntity.getFileName()));
            headers.add("Content-Length", String.valueOf(data.length));
            status = HttpStatus.OK;
        }
        return new ResponseEntity<>(data, headers, status);
    }






//    @RequestMapping(value = "download", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> getDownloadFile(
//            @RequestParam(value = "index", required = false) int index
//    ) {
//        UploadEntity uploadEntity = null;
//        if (index != 0) {
//            uploadEntity = this.uploadService.getFile(index);
//        }
//        byte[] data = null;
//        HttpHeaders headers = new HttpHeaders();
//        HttpStatus status = HttpStatus.NOT_FOUND;
//
//        if (uploadEntity != null && uploadEntity.getData() != null) {
//            data = uploadEntity.getData();
//            headers.add("Content-Type", uploadEntity.getFileType());
//            headers.add("Content-Disposition", String.format("attachment;filename=\"%s\"", uploadEntity.getFileName()));
//            headers.add("Content-Length", String.valueOf(data.length));
//            status = HttpStatus.OK;
//        }
//        return new ResponseEntity<>(data, headers, status);
//    }
//
//
    @RequestMapping(value = "upload/upload-file", method = RequestMethod.POST)
    @ResponseBody
    public String postFile(
            @RequestAttribute(name = "uploadEntity", required = false) UploadEntity uploadEntity,
            @RequestParam(name = "upload") MultipartFile[] files
    ) throws IOException {
        UploadEntity[] uploadEntities = new UploadEntity[files.length];
        for(int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            Date createdAt = new Date();
            int index = uploadEntities[i].getIndex();
            byte[] data = file.getBytes();
            uploadEntities[i] = new UploadEntity(index, createdAt, file.getOriginalFilename(), file.getContentType(), file.getBytes());
        }
        this.uploadService.uploadFile(uploadEntities);
        JSONObject responseJson = new JSONObject();
        JSONArray urlJson = new JSONArray();
        for(UploadEntity i : uploadEntities) {
            urlJson.put(String.format("http://localhost:8080/temp/download?index=%d", i.getIndex()));
        }
        responseJson.put("url", urlJson);
        return responseJson.toString();
    }
}
