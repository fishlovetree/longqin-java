import request from "@/utils/request";

const USER_BASE_URL = "/user";

class UserAPI {
  /**
   * 获取用户分页列表
   *
   * @param queryParams 查询参数
   */
  static getPage(queryParams: UserPageQuery) {
    return request<any, PageResult<UserPageVO[]>>({
      url: `${USER_BASE_URL}/getUserPage`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 获取用户列表
   *
   * @param queryParams 查询参数
   */
   static getUserList(queryParams: UserPageQuery) {
    return request<any, PageResult<UserPageVO[]>>({
      url: `${USER_BASE_URL}/getUserList`,
      method: "get",
      params: queryParams,
    });
  }

  /**
   * 添加用户
   *
   * @param data 用户表单数据
   */
  static add(data: UserForm) {
    return request({
      url: `${USER_BASE_URL}/create`,
      method: "post",
      data: data,
    });
  }

  /**
   * 修改用户
   *
   * @param data 用户表单数据
   */
  static update(data: UserForm) {
    return request({
      url: `${USER_BASE_URL}/update`,
      method: "post",
      data: data,
    });
  }

  /**
   * 重置用户密码
   *
   * @param id 用户ID
   * @param password 新密码
   */
   static resetPassword(id: number, password: string) {
    return request({
      url: `${USER_BASE_URL}/resetPassword`,
      method: "post",
      params: { userId: id, password: password},
    });
  }

  /**
   * 修改用户密码
   *
   * @param id 用户ID
   * @param oldPwd 旧密码
   * @param newPwd 新密码
   * @param confirmPwd 确认密码
   */
  static updatePassword(id: number, oldPwd: string, newPwd: string, confirmPwd: string) {
    return request({
      url: `${USER_BASE_URL}/updatePassword`,
      method: "post",
      params: { userId: id, oldPwd: oldPwd, newPwd: newPwd, confirmPwd: confirmPwd},
    });
  }

  /**
   * 删除用户
   *
   * @param id 用户ID
   */
  static deleteById(id: number) {
    return request({
      url: `${USER_BASE_URL}/delete?userId=${id}`,
      method: "post",
    });
  }

  /** 下载用户导入模板 */
  static downloadTemplate() {
    return request({
      url: `${USER_BASE_URL}/template`,
      method: "get",
      responseType: "arraybuffer",
    });
  }

  /**
   * 导出用户
   *
   * @param queryParams 查询参数
   */
  static export(queryParams: UserPageQuery) {
    return request({
      url: `${USER_BASE_URL}/export`,
      method: "get",
      params: queryParams,
      responseType: "arraybuffer",
    });
  }

  /**
   * 导入用户
   *
   * @param deptId 部门ID
   * @param file 导入文件
   */
  static import(deptId: number, file: File) {
    const formData = new FormData();
    formData.append("file", file);
    return request({
      url: `${USER_BASE_URL}/import`,
      method: "post",
      params: { deptId: deptId },
      data: formData,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
  }
}

export default UserAPI;

/** 登录用户信息 */
export interface UserInfo {
  /** 用户ID */
  userId?: number;

  /** 用户名 */
  userName?: string;

  /** 昵称 */
  nickName?: string;

  /** 头像URL */
  avatar?: string;

  /** 角色 */
  roles: string[];
}

/**
 * 用户分页查询对象
 */
export interface UserPageQuery extends PageQuery {
  /** 搜索昵称 */
  nickName?: string;

  /** 部门ID */
  departmentId?: number;
}

/** 用户分页对象 */
export interface UserPageVO {
  /** 用户头像URL */
  avatar?: string;
  /** 创建时间 */
  createTime?: Date;
  /** 部门名称 */
  departmentName?: string;
  /** 职位名称 */
  positionName?: string;
  /** 用户邮箱 */
  email?: string;
  /** 用户ID */
  userId?: number;
  /** 手机号 */
  phone?: string;
  /** 用户昵称 */
  nickName?: string;
  /** 账号名 */
  userName?: string;
  /** 账号角色 */
  roleIds?: string;
}

/** 用户表单类型 */
export interface UserForm {
  /** 用户头像 */
  avatar?: string;
  /** 部门ID */
  departmentId?: number;
  /** 职位ID */
  positionId?: number;
  /** 邮箱 */
  email?: string;
  /** 用户ID */
  userId?: number;
  /** 手机号 */
  phone?: string;
  /** 昵称 */
  nickName?: string;
  /** 账号名 */
  userName?: string;
  /** 账号角色 */
  roleIds?: string;
}
