import request from "@/utils/request";

// const DEPT_BASE_URL = "/api/v1/dept";
const DEPT_BASE_URL = "/department";

class DeptAPI {
  /**
   * 获取部门列表
   *
   * @returns 部门列表
   */
  static getList() {
    return request<any, DeptVO[]>({
      url: `${DEPT_BASE_URL}/getDepartmentList`,
      method: "get",
    });
  }

  /**
   * 获取部门树形列表
   *
   * @returns 部门树形列表
   */
   static getTree() {
    return request<any, DeptTree[]>({
      url: `${DEPT_BASE_URL}/getDepartmentTree`,
      method: "get",
    });
  }

  /**
   * 新增部门
   *
   * @param data 部门表单数据
   * @returns 请求结果
   */
  static add(data: DeptForm) {
    return request({
      url: `${DEPT_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改部门
   *
   * @param data 部门表单数据
   * @returns 请求结果
   */
  static update(data: DeptForm) {
    return request({
      url: `${DEPT_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除部门
   *
   * @param id 部门ID
   * @returns 请求结果
   */
  static deleteById(id: number) {
    return request({
      url: `${DEPT_BASE_URL}/delete?departmentId=${id}`,
      method: "post",
    });
  }
}

export default DeptAPI;

/** 部门类型 */
export interface DeptVO {
  /** 子部门 */
  children?: DeptVO[];
  /** 创建时间 */
  createTime?: Date;
  /** 部门ID */
  departmentId?: number;
  /** 部门名称 */
  departmentName?: string;
  /** 父部门ID */
  parentId?: number;
  /** 描述 */
  description?: string;
  /** 状态(1:启用；0:删除) */
  status?: number;
}

/** 部门树 */
export interface DeptTree {
  /** 子部门 */
  children?: DeptTree[];
  /** 部门ID */
  id?: number;
  /** 部门ID */
  departmentId?: number;
  /** 部门名称 */
  departmentName?: string;
  /** 部门名称 */
  title?: string;
  /** 父部门ID */
  parentId?: number;
  /** 描述 */
  description?: string;
  /** 所属公司id */
  organizationId?: number;
}

/** 部门表单类型 */
export interface DeptForm {
  /** 部门ID(新增不填) */
  departmentId?: number;
  /** 部门名称 */
  departmentName?: string;
  /** 父部门ID */
  parentId?: number;
  /** 描述 */
  description?: string;
  /** 所属公司 */
  organizationId?: number;
}
