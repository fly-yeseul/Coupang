package dev.fly_yeseul.coupang.services;

import dev.fly_yeseul.coupang.entities.temp.UploadEntity;
import dev.fly_yeseul.coupang.enums.product.AddResult;
import dev.fly_yeseul.coupang.enums.temp.UploadResult;
import dev.fly_yeseul.coupang.mappers.IUploadMapper;
import dev.fly_yeseul.coupang.vos.temp.UploadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.soap.Addressing;

@Service (value = "dev.fly_yeseul.coupang.services.UploadService")
public class UploadService {
    private final IUploadMapper uploadMapper;

    @Autowired
    public UploadService(IUploadMapper uploadMapper) {
        this.uploadMapper = uploadMapper;
    }

    public void addFile(UploadVo uploadVo) {
        if(this.uploadMapper.insertFile((uploadVo)) == 0) {
            uploadVo.setResult(UploadResult.FAILURE);
        } else {
            uploadVo.setResult(UploadResult.SUCCESS);
        }
    }
    public UploadEntity getFile(int index) {
        return this.uploadMapper.selectFile(index);
    }

    public void uploadFile(UploadEntity... uploadEntities){
            for(UploadEntity uploadEntity: uploadEntities){
                this.uploadMapper.insertFile(uploadEntity);
            }
    }
}
