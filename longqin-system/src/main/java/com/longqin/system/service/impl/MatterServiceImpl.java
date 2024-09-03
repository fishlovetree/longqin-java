package com.longqin.system.service.impl;

import com.longqin.system.entity.Matter;
import com.longqin.system.mapper.MatterMapper;
import com.longqin.system.service.IMatterService;
import com.longqin.system.util.OperationLog;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 事项 服务实现类
 * </p>
 *
 * @author longqin
 * @since 2024-09-04
 */
@Service
public class MatterServiceImpl extends ServiceImpl<MatterMapper, Matter> implements IMatterService {

	@Autowired
	MatterMapper matterMapper;
	
	@Override
	public Matter getById(int id){
		return matterMapper.selectById(id);
	}

	@Override
    public List<Matter> getList(String date, int userId){
    	return matterMapper.selectByDate(date, userId);
    }

	@OperationLog(title = "删除事项", content = "'事项id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception{
    	return matterMapper.deleteById(id);
    }

	@OperationLog(title = "添加事项", content = "'事项内容：' + #entity.getMatter()", operationType = "0")
	@Override
    public int insert(Matter entity) throws Exception{
    	return matterMapper.insert(entity);
    }

	@OperationLog(title = "修改事项", content = "'事项id：' + #entity.getId()", operationType = "2")
	@Override
    public int update(Matter entity) throws Exception{
		return matterMapper.updateById(entity);
	}
    
	@OperationLog(title = "修改事项状态为已办", content = "'事项id：' + #id", operationType = "2")
	@Override
    public int updateStatus(int id) throws Exception{
		return matterMapper.updateStatus(id);
	}
}
