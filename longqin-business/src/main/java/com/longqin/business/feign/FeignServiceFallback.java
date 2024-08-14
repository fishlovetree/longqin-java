package com.longqin.business.feign;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.longqin.business.entity.Errorlog;
import com.longqin.business.entity.Log;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;

/**
 * feign熔断
 */
@Component
public class FeignServiceFallback implements FeignService {

	@Override
	public ResponseData addLog(@RequestBody Log log) {
		return new ResponseData(ResponseEnum.ERROR.getCode(), "记录日志超时");
	}

	@Override
	public ResponseData addException(@RequestBody Errorlog errorlog) {
		return new ResponseData(ResponseEnum.ERROR.getCode(), "记录错误日志超时");
	}
	
	@Override
	public ResponseData getNickNameById(@RequestParam(value = "userId") Integer userId){
		return new ResponseData(ResponseEnum.ERROR.getCode(), "");
	}
	
	@Override
	public ResponseData getUserPositionLevel(@RequestParam(value = "userId") Integer userId){
		return new ResponseData(ResponseEnum.ERROR.getCode(), "获取用户职级超时");
	}
	
	@Override
	public ResponseData getFlowHandlers(@RequestParam(value = "userId") Integer userId, 
			@RequestParam(value = "departmentId") Integer departmentId, @RequestParam(value = "positionId") Integer positionId,
			@RequestParam(value = "submitterId") Integer submitterId){
		return new ResponseData(ResponseEnum.ERROR.getCode(), "根据部门和职位获取用户超时");
	}
	
	@Override
	public ResponseData diyTableMenu(@RequestParam(value = "menuName") String menuName, @RequestParam(value = "menuUrl") String menuUrl,
			@RequestParam(value = "groupSeq") Integer groupSeq, @RequestParam(value = "isEdit") boolean isEdit){
		return new ResponseData(ResponseEnum.ERROR.getCode(), "自定义列表生成菜单超时");
	}
}
