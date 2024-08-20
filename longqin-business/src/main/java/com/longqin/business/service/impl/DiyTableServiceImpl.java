package com.longqin.business.service.impl;

import com.longqin.business.entity.DiyTable;
import com.longqin.business.entity.DiyTableColumns;
import com.longqin.business.feign.FeignService;
import com.longqin.business.mapper.DiyTableColumnsMapper;
import com.longqin.business.mapper.DiyTableMapper;
import com.longqin.business.service.IDiyTableService;
import com.longqin.business.util.OperationLog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-11-09
 */
@Service
public class DiyTableServiceImpl extends ServiceImpl<DiyTableMapper, DiyTable> implements IDiyTableService {

	@Autowired
	DiyTableMapper diyTableMapper;
	
	@Autowired
	DiyTableColumnsMapper diyTableColumnsMapper;
	
	@Autowired
	FeignService feignService;
	
	@Override
    public DiyTable getById(int id) {
		DiyTable diyTable =  diyTableMapper.selectById(id);
		List<DiyTableColumns> columns = diyTableColumnsMapper.selectTableColumns(id);
		diyTable.setColumns(columns);
		return diyTable;
    }
	
	@Override
    public List<DiyTable> getPage(int organizationId, int startIndex, int pageSize) {
        return diyTableMapper.selectPage(organizationId, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId) {
        return diyTableMapper.selectCount(organizationId);
    }
	
	@OperationLog(title = "删除列表", content = "'列表id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
		return diyTableMapper.delete(id);
    }
	
	@OperationLog(title = "创建列表", content = "'列表名称：' + #entity.getTableName()", operationType = "0")
	@Transactional(rollbackFor = Exception.class)
	@Override
    public int create(DiyTable entity) throws Exception {
		int result = diyTableMapper.insert(entity);
		if (result > 0) {
			// 插入列表列属性
            JSONArray jsonArray = JSONArray.parseArray(entity.getData());
            for (int i = 0; i < jsonArray.size(); i++) {
            	JSONObject obj = (JSONObject)jsonArray.get(i);
            	DiyTableColumns column = new DiyTableColumns();
                column.setColumnName(obj.getString("columnName"));
                column.setDescription(obj.getString("description"));
                column.setColumnIndex(i);
                column.setWidth(obj.getIntValue("width"));
                column.setOrderBy(obj.getIntValue("orderby"));
                column.setSearchType(obj.getIntValue("searchType"));
                column.setFormula(obj.getIntValue("formula"));
                column.setFormulaValue(obj.getString("formulaValue"));
                column.setColumnType(obj.getString("columnType"));
                column.setTableId(entity.getId());
                diyTableColumnsMapper.insert(column);
            }

            // 生成菜单
            feignService.diyTableMenu(entity.getTableName(), "/diytable/view/" + entity.getId(), entity.getId() + 2, false);
		}
		return result;
	}
	
	@OperationLog(title = "修改列表", content = "'列表名称：' + #entity.getTableName()", operationType = "2")
	@Override
    public int update(DiyTable entity) throws Exception {
		int result = diyTableMapper.updateById(entity);
		if (result > 0) {
			diyTableColumnsMapper.deleteTableColumns(entity.getId());
			// 插入列表列属性
            JSONArray jsonArray = JSONArray.parseArray(entity.getData());
            for (int i = 0; i < jsonArray.size(); i++) {
            	JSONObject obj = (JSONObject)jsonArray.get(i);
            	DiyTableColumns column = new DiyTableColumns();
                column.setColumnName(obj.getString("columnName"));
                column.setDescription(obj.getString("description"));
                column.setColumnIndex(i);
                column.setWidth(obj.getIntValue("width"));
                column.setOrderBy(obj.getIntValue("orderby"));
                column.setSearchType(obj.getIntValue("searchType"));
                column.setFormula(obj.getIntValue("formula"));
                column.setFormulaValue(obj.getString("formulaValue"));
                column.setColumnType(obj.getString("columnType"));
                column.setTableId(entity.getId());
                diyTableColumnsMapper.insert(column);
            }

            // 生成菜单
            feignService.diyTableMenu(entity.getTableName(), "/diytable/view/" + entity.getId(), entity.getId() + 2, true);
		}
		return result;
	}
	
	@Override
    public List<DiyTableColumns> getTableColumns(int id) {
        return diyTableColumnsMapper.selectTableColumns(id);
    }
	
	@OperationLog(title = "插入列表字段", content = "'列表id：' + #tableId", operationType = "0")
	@Override
    public int insertTableColumns(int tableId, List<DiyTableColumns> list) throws Exception {
        int result = diyTableColumnsMapper.deleteTableColumns(tableId);
        if (result >= 0) {
            for(DiyTableColumns item : list) {
                item.setTableId(tableId);
                diyTableColumnsMapper.insert(item);
            }
        }
        return result;
	}
	
	@Override
	public Map<String, Object> getTableData(int startIndex, int pageSize, int id, String dataSource, 
			int organizationId, Map<String, String> searchMap) {
        List<DiyTableColumns> columns = diyTableColumnsMapper.selectTableColumns(id);
        String dataSql = "select"; // 分页查询数据sql
        String columnSql = "1"; // 查询内容sql
        String whereSql = " where s.status = 1 and s.organization_id = " + organizationId; // 条件sql
        String leftJoin = ""; // 链接其他表sql
        String orderBy = ""; // 排序sql
        for (DiyTableColumns column : columns) {
            String sourceName = ""; // 未加工原始字段sql
            // 拼接查询项
            if ("creatorName".equals(column.getColumnName())) {
                columnSql += ", u.nick_name as creatorName";
                sourceName = "u.user_name";
                leftJoin += " left join sys_user u on u.user_id = s.creator";
            }
            else {
                sourceName = "s." + column.getColumnName();
                // 字段加工
                if (!StringUtils.isEmpty(column.getFormulaValue())) {
                    switch (column.getFormula()) {
                        case 0: // 默认不加工
                            columnSql += ", s." + column.getColumnName();
                            break;
                        case 1: // 加（后续实现）
                            columnSql += ", s." + column.getColumnName();
                            break;
                        case 2: // 减（后续实现）
                            columnSql += ", s." + column.getColumnName();
                            break;
                        case 3: // 乘（后续实现）
                            columnSql += ", s." + column.getColumnName();
                            break;
                        case 4: // 除（后续实现）
                            columnSql += ", s." + column.getColumnName();
                            break;
                        case 5: // 拼接
                            columnSql += ", cast(s." + column.getColumnName() + " as char) + '" + column.getFormulaValue() + "' as " + column.getColumnName();
                            break;
                        default:
                            break;
                    }
                }
                else  {
                    columnSql += ", s." + column.getColumnName();
                }
            }
            // 拼接条件
            if (column.getSearchType() == 1) { // 等于 
                String searchValue = searchMap.get(column.getColumnName());
                if (!StringUtils.isEmpty(searchValue)) {
                    whereSql += " and " + sourceName + " = '" + searchValue + "'";
                }
            }
            else if (column.getSearchType() == 2) { // 模糊查询
                String searchValue = searchMap.get(column.getColumnName());
                if (!StringUtils.isEmpty(searchValue)) {
                    whereSql += " and " + sourceName + " like '%" + searchValue + "%'";
                }
            }
            else if (column.getSearchType() == 3) {
                String searchBeginValue = searchMap.get(column.getColumnName() + "_begin");
                String searchEndValue = searchMap.get(column.getColumnName() + "_end");
                if (!StringUtils.isEmpty(searchBeginValue)) {
                    whereSql += " and " + sourceName + " >= '" + searchBeginValue + "'";
                }
                if (!StringUtils.isEmpty(searchEndValue)) {
                    whereSql += " and " + sourceName + " <= '" + searchEndValue + "'";
                }
            }
            // 拼接排序项
            if (column.getOrderBy() == 1) {
                if (orderBy.equals("")) {
                    orderBy += " order by " + sourceName + " asc";
                }
                else {
                    orderBy += " , " + sourceName + " desc";
                }
            }
        }
        orderBy = orderBy.equals("") ? "order by s.id desc" : orderBy;
        dataSql += columnSql + " from " + dataSource + " s" + leftJoin + whereSql 
            + orderBy + " limit " + startIndex + " " + pageSize;
        String countSql = "select count(0) from " + dataSource + " s"; // 查询数量sql
        countSql += leftJoin;
        countSql += whereSql;
        Map<String, String> dataList = diyTableMapper.selectTableData(dataSql);
        int total = diyTableMapper.selectTableDataCount(countSql);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list", dataList);
		map.put("total", total);
        return map;
    }
}
