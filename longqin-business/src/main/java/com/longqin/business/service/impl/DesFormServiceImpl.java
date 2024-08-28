package com.longqin.business.service.impl;

import com.longqin.business.entity.DesForm;
import com.longqin.business.entity.DesFormColumn;
import com.longqin.business.entity.DesFormData;
import com.longqin.business.mapper.DesFormMapper;
import com.longqin.business.service.IDesFormService;
import com.longqin.business.util.OperationLog;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author longqin
 * @since 2023-10-31
 */
@Service
public class DesFormServiceImpl extends ServiceImpl<DesFormMapper, DesForm> implements IDesFormService {

	@Autowired
	DesFormMapper desFormMapper;
	
	@OperationLog(title = "创建表单", content = "'表单名称：' + #entity.getFormName()", operationType = "0")
	@Override
    public int create(DesForm entity) throws Exception {
		String tableName = "des_" + entity.getTableName() + "_" + entity.getOrganizationId();
    	entity.setTableName(tableName);
		int count = desFormMapper.selectCountByTableName(tableName, 0);
		if (count > 0) {
			// 表名已存在
			return -2;
		}
    	JSONObject jsonObj = JSONObject.parseObject(entity.getJsonData());
    	JSONArray jsonArray = jsonObj.getJSONArray("widgetList");
    	if (jsonArray == null){
    		return -1;
    	}
    	StringBuilder columns = new StringBuilder();
    	generateColumns(jsonArray, columns);
    	int result = desFormMapper.createFormTable(tableName, columns.toString(), entity.getFormName());
    	if (result >= 0){
    		result = desFormMapper.insert(entity);
    	}
    	return result;
    }
	
	@OperationLog(title = "修改表单", content = "'表单名称：' + #entity.getFormName()", operationType = "2")
	@Override
    public int update(DesForm entity) throws Exception {
		String tableName = "des_" + entity.getTableName() + "_" + entity.getOrganizationId();
		entity.setTableName(tableName);
		int count = desFormMapper.selectCountByTableName(tableName, entity.getId());
		if (count > 0) {
			// 表名已存在
			return -2;
		}
		// 若数据库表已存在，旧表置为备份表
		int tableCount = desFormMapper.selectTableCount(tableName);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String datetime = sdf.format(new Date());
		if (tableCount > 0){
			desFormMapper.renameTable(tableName, tableName + "_bak" + datetime);
		}
		JSONObject jsonObj = JSONObject.parseObject(entity.getJsonData());
    	JSONArray jsonArray = jsonObj.getJSONArray("widgetList");
    	if (jsonArray == null){
    		return -1;
    	}
    	StringBuilder columns = new StringBuilder();
    	generateColumns(jsonArray, columns);
    	int result = desFormMapper.createFormTable(tableName, columns.toString(), entity.getFormName());
    	if (result >= 0){
    		result = desFormMapper.updateById(entity);
    	}
    	return result;
    }
	
	// 递归组装数据库表列
	private void generateColumns(JSONArray jsonArray, StringBuilder columns){
    	for(int i = 0; i < jsonArray.size(); i++){
            JSONObject obj = (JSONObject)jsonArray.get(i);
            String columnType = "";
            String type = obj.getString("type");
            JSONObject options = obj.getJSONObject("options");
            String isNull = "true".equals(options.getString("required")) ? "NOT NULL" : "NULL DEFAULT NULL";
            switch (type)
            {
                case "input":
                    columnType = "varchar(100)";
                    columns.append("`").append(options.getString("name")).append("` ").append(columnType)
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
                    .append(options.getString("label")).append("',");
                    break;
                case "textarea":
                    columnType = "varchar(510)";
                    columns.append("`").append(options.getString("name")).append("` ").append(columnType)
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
                    .append(options.getString("label")).append("',");
                    break;
                case "rich-editor":
                    columnType = "text";
                    columns.append("`").append(options.getString("name")).append("` ").append(columnType)
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
                    .append(options.getString("label")).append("',");
                    break;
                case "file-upload":
                    columnType = "varchar(2000)";
                    columns.append("`").append(options.getString("name")).append("` ").append(columnType)
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
                    .append(options.getString("label")).append("',");
                    break;
                case "grid":
                	JSONArray cols = obj.getJSONArray("cols");
                	for (int j =0 ; j < cols.size(); j++){
                		JSONObject col = (JSONObject)cols.get(j);
                		JSONArray colChildren = col.getJSONArray("widgetList");
                		generateColumns(colChildren, columns);
                	}
                	break;
                case "table":
                	JSONArray rows = obj.getJSONArray("rows");
                	for (int j =0 ; j < rows.size(); j++){
                		JSONObject row = (JSONObject)rows.get(j);
                		JSONArray rowCols = row.getJSONArray("cols");
                		for (int k = 0; k < rowCols.size(); k++){
                			JSONObject rowCol = (JSONObject)rowCols.get(k);
                			JSONArray colChildren = rowCol.getJSONArray("widgetList"); 
                			generateColumns(colChildren, columns);
                		}
                	}
                	break;
                case "tab":
                	JSONArray tabs = obj.getJSONArray("tabs");
                	for (int j =0 ; j < tabs.size(); j++){
                		JSONObject tab = (JSONObject)tabs.get(j);
                		JSONArray tabChildren = tab.getJSONArray("widgetList");
                		generateColumns(tabChildren, columns);
                	}
                	break;
                case "card":
                	JSONArray cardChildren = obj.getJSONArray("widgetList");
            		generateColumns(cardChildren, columns);
                	break;
                case "number":
                case "radio":
                case "checkbox":
                case "select":
                case "time":
                case "time-range":
                case "date":
                case "date-range":
                case "switch":
                case "rate":
                case "color":
                case "slider":
                case "cascader":
                	columnType = "varchar(50)";
                    columns.append("`").append(options.getString("name")).append("` ").append(columnType)
                    .append(" CHARACTER SET utf8 COLLATE utf8_general_ci ").append(isNull).append(" COMMENT '")
                    .append(options.getString("label")).append("',");
                    break;
                default:
                    break;
            }
    	}
	}
	
	@OperationLog(title = "删除表单", content = "'表单id：' + #id", operationType = "1")
	@Override
    public int delete(int id) throws Exception {
		return desFormMapper.deleteForm(id);
    }
	
	@Override
    public DesForm getById(int id) {
        return desFormMapper.selectById(id);
    }

	@Override
    public List<DesForm> getPage(int organizationId, int startIndex, int pageSize) {
        return desFormMapper.selectPage(organizationId, startIndex, pageSize);
    }
    
	@Override
    public int getCount(int organizationId) {
        return desFormMapper.selectCount(organizationId);
    }
	
	// 根据数据库表名获取字段集合
	@Override
    public List<DesFormColumn> getTableColumns(String tableName) {
        List<DesFormColumn> columns = desFormMapper.selectTableColumns(tableName);
        if (columns != null && columns.size() != 0) {
            DesFormColumn creatorName = new DesFormColumn();
            creatorName.setColumnName("creatorName");
            creatorName.setDescription("创建者名称");
            columns.add(creatorName);
        }
        return columns;
    }
	
	@Override
	public int insertFormData(DesFormData entity){
		return desFormMapper.insertFormData(entity);
	}
	
	@Override
	public int updateFormData(String tableName, List<String> columns, List<String> vals, Integer id){
		if (null == columns || columns.size() == 0 || null == vals || columns.size() != vals.size()){
			return -2; //参数错误
		}
		StringBuilder sb = new StringBuilder("");
		for(int i = 0; i < columns.size(); i++){
			String column = columns.get(i);
			String val = vals.get(i);
			sb.append(column + "=" + val + ",");
		}
		sb.deleteCharAt(sb.length() - 1);
		return desFormMapper.updateFormData(tableName, sb.toString(), id);
	}
	
	@Override
    public DesForm getByTableName(String tableName) {
        return desFormMapper.selectByTableName(tableName);
    }
}
