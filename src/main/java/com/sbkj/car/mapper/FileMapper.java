package com.sbkj.car.mapper;

import com.sbkj.car.model.FilePo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Description: 上传文件
 * @Author: 臧东运
 * @CreateTime: 2019/4/29 11:24
 */
@Mapper
@Repository
public interface FileMapper {
    /**
     * 保存文件
     * @param filePo
     * @return
     */
    int saveFile(FilePo filePo);

}
