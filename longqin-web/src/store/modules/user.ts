import AuthAPI, { LoginData } from "@/api/auth";
import UserAPI, { UserInfo } from "@/api/user";
import { resetRouter } from "@/router";
import { store } from "@/store";

export const useUserStore = defineStore("user", () => {
  const avatar = ref();
  const user = ref<UserInfo>({
    roles: [],
    perms: [],
  });

  /**
   * 登录
   *
   * @param {LoginData}
   * @returns
   */
  function login(loginData: LoginData) {
    return new Promise<void>((resolve, reject) => {
      AuthAPI.login(loginData)
        .then(({data}) => {
          // const { tokenType, accessToken } = data;
          // localStorage.setItem(TOKEN_KEY, tokenType + " " + accessToken); // Bearer eyJhbGciOiJIUzI1NiJ9.xxx.xxx
          const { user, sessionid, menujson } = data;
					sessionStorage.setItem('organizationId', user.organizationId);
					sessionStorage.setItem('userId', user.userId);
          sessionStorage.setItem('sessionid', sessionid);
          sessionStorage.setItem('avatar', user.avatar);
          sessionStorage.setItem('userName', user.userName);
          sessionStorage.setItem('nickName', user.nickName);
          sessionStorage.setItem('phone', user.phone);
          sessionStorage.setItem('email', user.email);
          sessionStorage.setItem('systemName', user.systemName);
          
          sessionStorage.setItem('logoPath', user.logoPath);
          localStorage.setItem('menu', JSON.stringify(menujson));
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  }

  // 获取信息(用户昵称、头像、角色集合、权限集合)
  function getUserInfo() {
    return new Promise<UserInfo>((resolve, reject) => {
      UserAPI.getInfo()
        .then((data) => {
          if (!data) {
            reject("Verification failed, please Login again.");
            return;
          }
          if (!data.roles || data.roles.length <= 0) {
            reject("getUserInfo: roles must be a non-null array!");
            return;
          }
          Object.assign(user.value, { ...data });
          resolve(data);
        })
        .catch((error) => {
          reject(error);
        });
    });
  }

  // user logout
  function logout() {
    return new Promise<void>((resolve, reject) => {
      AuthAPI.logout()
        .then(() => {
          // localStorage.setItem(TOKEN_KEY, "");
          resetToken();
          location.reload(); // 清空路由
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  }

  // remove token
  function resetToken() {
    // console.log("resetToken");
    return new Promise<void>((resolve) => {
      // localStorage.setItem(TOKEN_KEY, "");
      sessionStorage.setItem('organizationId', '');
      sessionStorage.setItem('userId', '');
      sessionStorage.setItem('sessionid', '');
      sessionStorage.setItem('avatar', '');
      localStorage.setItem('menu', '');
      resetRouter();
      resolve();
    });
  }

  return {
    user,
    avatar,
    login,
    getUserInfo,
    logout,
    resetToken,
  };
});

/**
 * 用于在组件外部（如在Pinia Store 中）使用 Pinia 提供的 store 实例。
 * 官方文档解释了如何在组件外部使用 Pinia Store：
 * https://pinia.vuejs.org/core-concepts/outside-component-usage.html#using-a-store-outside-of-a-component
 */
export function useUserStoreHook() {
  return useUserStore(store);
}
