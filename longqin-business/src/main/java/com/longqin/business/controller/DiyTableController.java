package com.longqin.business.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.business.config.RequiredPermission;
import com.longqin.business.entity.DiyTable;
import com.longqin.business.entity.DiyTableColumns;
import com.longqin.business.service.IDiyTableService;
import com.longqin.business.util.ResponseData;
import com.longqin.business.util.ResponseEnum;
import com.longqin.business.util.SessionUtil;

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
 * @since 2023-11-09
 */
@RestController
@RequestMapping("/diyTable")
@Api(tags="自定义列表类")
public class DiyTableController {

	@Autowired
	IDiyTableService diyTableService;
	
	/**
	 * @Description 获取单个列表
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "获取单个列表", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "列表ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getById")
	public ResponseData getById(int id) {
		DiyTable form = diyTableService.getById(id);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", form);
	}
	
	/**
	 * @Description 分页获取列表清单
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "分页获取列表清单", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@GetMapping("/getTablePage")
	@RequiredPermission("tableList:view")
	public ResponseData getTablePage(Integer page, Integer size) {
		if (null == page || null == size) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		List<DiyTable> tableList = diyTableService.getPage(SessionUtil.getSessionUser().getOrganizationId(), (page - 1) * size, size);
		int total = diyTableService.getCount(SessionUtil.getSessionUser().getOrganizationId());
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", tableList);
		map.put("total", total);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
	
	/**
	 * @Description 创建列表
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "创建列表", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "entity", value = "列表实体项", required = true, dataType = "DiyTable") })
	@ApiResponses({ @ApiResponse(code = 1, message = "创建成功"), @ApiResponse(code = 0, message = "创建失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/create")
	@RequiredPermission("tableDesigner:view")
	public ResponseData create(@RequestBody DiyTable entity) throws Exception {
		if (null == entity || null == entity.getTableName()) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		entity.setCreator(SessionUtil.getSessionUser().getUserId());
		entity.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = diyTableService.create(entity);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "创建成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "创建失败", result);
		}
	}

	/**
	 * @Description 修改列表
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "修改列表", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "entity", value = "列表实体项", required = true, dataType = "DiyTable") })
	@ApiResponses({ @ApiResponse(code = 1, message = "修改成功"), @ApiResponse(code = 0, message = "修改失败"),
			@ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/update")
	@RequiredPermission("tableDesigner:view")
	public ResponseData update(@RequestBody DiyTable entity) throws Exception {
		if (null == entity || null == entity.getTableName()) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		entity.setCreator(SessionUtil.getSessionUser().getUserId());
		entity.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
		int result = diyTableService.update(entity);
		if (result > 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}

	/**
	 * @Description 删除列表
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "删除列表", httpMethod = "POST")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "表单ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "删除成功"), @ApiResponse(code = 0, message = "删除失败") })
	@PostMapping("/delete")
	@RequiredPermission("tableList:view")
	public ResponseData delete(int id) throws Exception {
		int result = diyTableService.delete(id);
		if (result >= 0){
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else{
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
	
	/**
	 * @Description 获取自定义列表表头
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "获取自定义列表表头", httpMethod = "GET")
	@ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "列表ID", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败") })
	@GetMapping("/getTableColumns")
	public ResponseData getTableColumns(int id) {
		List<DiyTableColumns> columns = diyTableService.getTableColumns(id);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", columns);
	}
	
	/**
	 * @Description 获取自定义列表数据
	 * @Author longqin
	 * @Time: 2023年11月13日
	 */
	@ApiOperation(value = "获取自定义列表数据", httpMethod = "GET")
	@ApiImplicitParams({ 
		@ApiImplicitParam(name = "page", value = "页数", required = true, dataType = "int"),
		@ApiImplicitParam(name = "size", value = "每页数量", required = true, dataType = "int") })
	@ApiResponses({ @ApiResponse(code = 1, message = "查询成功"), @ApiResponse(code = 0, message = "查询失败"), @ApiResponse(code = 3, message = "参数错误") })
	@PostMapping("/GetTableData")
	public ResponseData GetTableData(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, String> searchMap = new HashMap<>();
		Integer page = null; 
		Integer size = null; 
		Integer id = null;
		String dataSource = "";
		for(Map.Entry<String, Object> entry : params.entrySet()){
			if (entry.getKey().equals("page")){
				page = Integer.parseInt(entry.getValue().toString());
			}
			else if (entry.getKey().equals("size")){
				size = Integer.parseInt(entry.getValue().toString());
			}
			else if (entry.getKey().equals("id")){
				id = Integer.parseInt(entry.getValue().toString());
			}
			else if (entry.getKey().equals("dataSource")){
				dataSource = entry.getValue().toString();
			}
			else{
				searchMap.put(entry.getKey(), entry.getValue().toString());
			}
		}
		if (null == page || null == size || null == id || StringUtils.isEmpty(dataSource)) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		Map<String, Object> map = diyTableService.getTableData((page - 1) * size, size, id, dataSource, SessionUtil.getSessionUser().getOrganizationId(), searchMap);
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", map);
	}
}
