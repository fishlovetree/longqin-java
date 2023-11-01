package com.longqin.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Errorlog;
import com.longqin.system.service.IErrorlogService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/errorlog")
@Api(tags="错误日志类")
public class ErrorlogController {

	@Autowired
	IErrorlogService logService;

	/**
	 * @Description 分页获取错误日志列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "分页获取错误日志", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "beginDate", value = "检索条件：开始日期", required = false, dataType = "String"), 
		@ApiImplicitParam(name = "endDate", value = "检索条件：结束日期", required = false, dataType = "String"),
		@ApiImplicitParam(name = "page", value = "页数", dataType = "Integer"),
		@ApiImplicitParam(name = "size", value = "每页数量", dataType = "Integer") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getLogPage")
	@RequiredPermission("errorlog:view")
	public ResponseData getLogPage(String beginDate, String endDate, Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<Errorlog> logList = logService.getPage(beginDate, endDate, (page - 1) * size, size);
		int total = logService.getCount(beginDate, endDate);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", logList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 记录错误日志
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@PostMapping("/addException")
	public ResponseData addException(Errorlog log) throws Exception{
		int result = logService.insert(log);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "记录成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "记录失败", result);
		}
	}
}
