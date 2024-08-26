import request from "@/utils/request";

const TABLE_BASE_URL = "/bus/diyTable";

class DiytableAPI {
  /** 新增列表接口*/
  static create(data: DiytableData) {
    return request({
      url: `${TABLE_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /** 修改列表接口*/
  static update(data: DiytableData) {
    return request({
      url: `${TABLE_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 获取列表分页列表
   *
   * @param queryParams 查询参数
   */
   static getPage(queryParams: DiytablePageQuery) {
    return request<any, PageResult<DiyTablePageVO[]>>({
      url: `${TABLE_BASE_URL}/getTablePage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 根据id获取列表信息
   *
   * @param id 列表ID
   */
   static getById(id: number) {
    return request<any, DiytableData>({
      url: `${TABLE_BASE_URL}/getById?id=${id}`,
      method: "get",
    });
  }

  /**
   * 删除列表
   *
   * @param id 列表ID
   */
   static deleteById(id: number) {
    return request({
      url: `${TABLE_BASE_URL}/delete?id=${id}`,
      method: "post",
    });
  }

  /**
   * 获取自定义列表表头
   *
   * @param id 列表ID
   */
   static getTableColumns(id: number) {
    return request<any>({
      url: `${TABLE_BASE_URL}/getTableColumns?id=${id}`,
      method: "get",
    });
  }

  /**
   * 获取自定义列表数据
   *
   * @param formData 查询参数
   */
   static GetTableData(formData) {
    return request({
      url: `${TABLE_BASE_URL}/GetTableData`,
      method: "post",
      data: formData,
    });
  }
}

export default DiytableAPI;

/** 保存列表请求参数 */
export interface DiytableData {
  /** 主键id */
  id?: number;
  /** 列表名称 */
  tableName?: string;
  /** 数据源表名 */
  dataSource?: string;
  /** 列表详情 */
  data?: string;
}

/**
 * 列表分页查询对象
 */
 export interface DiytablePageQuery extends PageQuery {
  /** 搜索关键字 */
  tableName?: string;
}

/** 列表分页对象 */
export interface DiyTablePageVO {
  /** 主键id */
  id?: number;
  /** 列表名称 */
  tableName?: string;
  /** 数据源表名 */
  dataSource?: string;
  /** 数据源表单名 */
  formName?: string;
  /** 列表详情 */
  data?: string;
  /** 创建人 */
  creatorName?: string;
  /** 创建时间 */
  createTime?: Date;
}
