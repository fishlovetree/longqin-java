import request from "@/utils/request";

const ROLE_BASE_URL = "/role";

class RoleAPI {
  /** 获取角色分页数据 */
  static getList() {
    return request<any, RolePageVO[]>({
      url: `${ROLE_BASE_URL}/getRoleList`,
      method: "get",
    });
  }

  /**
   * 获取角色的菜单ID集合
   *
   * @param roleId 角色ID
   * @returns 角色的菜单ID集合
   */
  static getRoleMenuIds(roleId: number) {
    return request<any, number[]>({
      url: `${ROLE_BASE_URL}/getRoleMenu?roleId=${roleId}`,
      method: "get",
    });
  }

  /**
   * 分配菜单权限
   *
   * @param roleId 角色ID
   * @param data 菜单ID集合
   */
  static updateRoleMenus(roleId: number, data: string) {
    return request({
      url: `${ROLE_BASE_URL}/updateRoleMenu?roleId=${roleId}&menuIdStr=${data}`,
      method: "post",
    });
  }

  /** 添加角色 */
  static add(data: RoleForm) {
    return request({
      url: `${ROLE_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 更新角色
   *
   * @param data 角色表单数据
   */
  static update(data: RoleForm) {
    return request({
      url: `${ROLE_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 删除角色
   *
   * @param id 角色ID
   */
  static deleteById(id: string) {
    return request({
      url: `${ROLE_BASE_URL}/delete?roleId=${id}`,
      method: "post",
    });
  }
}

export default RoleAPI;

/** 角色分页对象 */
export interface RoleVO {
  /** 角色ID */
  roleId?: number;
  /** 角色名称 */
  roleName?: string;
  /** 角色备注 */
  description?: string;
  /** 所属公司 */
  organizationId?: number;
  /** 创建时间 */
  createTime?: Date;
}

/** 角色表单对象 */
export interface RoleForm {
  /** 角色ID */
  roleId?: number;
  /** 角色名称 */
  roleName?: string;
  /** 角色备注 */
  description?: string;
  /** 所属公司 */
  organizationId?: number;
}
