package com.sbkj.car.service;

import com.sbkj.car.component.exception.SbException;
import com.sbkj.car.model.FilePo;

/**
 * @Description:  上传文件
 * @Author: 臧东运
 * @CreateTime: 2019/4/29 11:22
 */
public interface FileService {
    /**
     * 保存文件
     * @param filePo
     * @throws SbException
     */
    void saveFile(FilePo filePo)  throws SbException;

}
