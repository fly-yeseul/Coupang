package dev.fly_yeseul.coupang.mappers;

import dev.fly_yeseul.coupang.entities.temp.UploadEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface IUploadMapper {

    int insertFile(UploadEntity uploadEntity);


    UploadEntity selectFile(
            @Param(value="index") int index
    );
}
