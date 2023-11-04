package com.longqin.system.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longqin.system.config.RequiredPermission;
import com.longqin.system.entity.Position;
import com.longqin.system.service.IPositionService;
import com.longqin.system.util.ResponseData;
import com.longqin.system.util.ResponseEnum;
import com.longqin.system.util.SessionUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * <p>
 *  职位控制器
 * </p>
 *
 * @author longqin
 * @since 2023-10-21
 */
@RestController
@RequestMapping("/position")
@Api(tags="职位类")
public class PositionController {

	@Autowired
	IPositionService positionService;
	
	/**
	 * @Description 获取职位列表
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取职位列表", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getPositionList")
	@RequiredPermission("position:view")
	public ResponseData getPositionList() {
		List<Position> positionList = positionService.getPositionList(SessionUtil.getSessionUser().getOrganizationId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", positionList);
	}
	
	/**
	 * @Description 获取职位树形结构
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "获取职位树形结构", httpMethod = "GET")
	@ApiResponses({
	    @ApiResponse(code = 1, message="查询成功"),
	    @ApiResponse(code = 0, message="查询失败")
	})
	@GetMapping("/getPositionTree")
	public ResponseData getPositionTree() {
		List<Map<String, Object>> positionTree = positionService.getPositionTree(SessionUtil.getSessionUser().getOrganizationId());
		return new ResponseData(ResponseEnum.SUCCESS.getCode(), "查询成功", positionTree);
	}
	
	/**
	 * @Description 添加职位
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "添加职位", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "position", value = "职位实体", required = true, dataType = "Position")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="添加成功"),
	    @ApiResponse(code = 0, message="添加失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/create")
	@RequiredPermission("position:view")
	public ResponseData create(Position position) throws Exception {
		if (null == position) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		position.setCreator(SessionUtil.getSessionUser().getUserId());
		if (null == position.getOrganizationId()) {
			position.setOrganizationId(SessionUtil.getSessionUser().getOrganizationId());
        }
		int result = positionService.addPosition(position);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "添加成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "添加失败", result);
		}
	}
	
	/**
	 * @Description 修改职位
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "修改职位", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "position", value = "职位实体", required = true, dataType = "Position")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="修改成功"),
	    @ApiResponse(code = 0, message="修改失败"),
	    @ApiResponse(code = 3, message="参数错误")
	})
	@PostMapping("/update")
	@RequiredPermission("position:view")
	public ResponseData update(Position position) throws Exception {
		if (null == position) {
			return new ResponseData(ResponseEnum.BADPARAM.getCode(), "参数错误");
		}
		int result = positionService.editPosition(position);
		if (result > 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "修改成功", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "修改失败", result);
		}
	}
	
	/**
	 * @Description 删除职位
	 * @Author longqin
	 * @Time: 2023年10月22日
	 */
	@ApiOperation(value = "删除职位", httpMethod = "POST")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "positionId", value = "职位ID", required = true, dataType = "int")
    })
	@ApiResponses({
	    @ApiResponse(code = 1, message="删除成功"),
	    @ApiResponse(code = 0, message="删除失败")
	})
	@PostMapping("/delete")
	@RequiredPermission("position:view")
	public ResponseData delete(int positionId) throws Exception {
		int result = positionService.deletePosition(positionId);
		if (result >= 0) {
			return new ResponseData(ResponseEnum.SUCCESS.getCode(), "删除成功", result);
		}
		else if (result == -1) {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "存在子职位，请先删除子职位", result);
		}
		else {
			return new ResponseData(ResponseEnum.ERROR.getCode(), "删除失败", result);
		}
	}
}
