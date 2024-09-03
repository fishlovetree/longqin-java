package com.longqin.system.service;

import com.longqin.system.entity.Matter;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 事项 服务类
 * </p>
 *
 * @author longqin
 * @since 2024-09-04
 */
public interface IMatterService extends IService<Matter> {

	Matter getById(int id);

    List<Matter> getList(String date, int userId);

    int delete(int id) throws Exception;

    int insert(Matter model) throws Exception;

    int update(Matter model) throws Exception;
    
    int updateStatus(int id) throws Exception;
}
