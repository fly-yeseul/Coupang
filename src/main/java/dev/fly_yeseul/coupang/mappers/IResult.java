package dev.fly_yeseul.coupang.mappers;

public interface IResult <T extends Enum<?>>{
    T getResult();

    void setResult(T t);
}
