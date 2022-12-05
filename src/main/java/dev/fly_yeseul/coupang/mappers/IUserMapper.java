package dev.fly_yeseul.coupang.mappers;

import dev.fly_yeseul.coupang.entities.member.SessionEntity;
import dev.fly_yeseul.coupang.entities.member.user.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IUserMapper {

    int insertSession(SessionEntity sessionEntity);

    int insertUser(UserEntity userEntity);

    UserEntity selectUser(UserEntity userEntity);

    UserEntity selectUserByEmail(
            @Param(value = "email") String email
    );

    int selectUserCountByEmail(
            @Param(value = "email") String email
    );

    SessionEntity selectSessionByKey(
            @Param(value = "key") String key
    );

    int updateSession(SessionEntity sessionEntity);

    int updateSessionExpiredByEmail(
            @Param(value = "email") String email
    );

}
