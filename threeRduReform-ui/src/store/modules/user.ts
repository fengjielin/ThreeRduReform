import router from '@/router';
import { ElMessageBox } from 'element-plus';
import { login, logout, getInfo } from '@/api/login';
import { getToken, setToken, removeToken } from '@/utils/auth';
import { isHttp, isEmpty } from '@/utils/validate';
import defAva from '@/assets/images/profile.jpg';

interface UserState {
  token: string | undefined;
  id: string | number;
  name: string;
  nickName: string;
  avatar: string;
  roles: string[];
  permissions: string[];
  isAdmin: boolean;
  isStudent: boolean;
  isTeacher: boolean;
}

const useUserStore = defineStore('user', {
  state: (): UserState => ({
    token: getToken(),
    id: '',
    name: '',
    nickName: '',
    avatar: '',
    roles: [],
    permissions: [],
    isAdmin: null,
    isStudent: null,
    isTeacher: null,
  }),
  actions: {
    // 登录
    login(userInfo: { username: string; password: string; code: string; uuid: string }) {
      sessionStorage.removeItem('cachedViews');
      sessionStorage.removeItem('visitedViews');
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const code = userInfo.code;
      const uuid = userInfo.uuid;
      return new Promise<void>((resolve, reject) => {
        login(username, password, code, uuid)
          .then((res) => {
            setToken(res.token);
            this.token = res.token;
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取用户信息
    getInfo() {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            const user = res.user;
            let avatar = user.avatar || '';
            if (!isHttp(avatar)) {
              avatar = isEmpty(avatar) ? defAva : import.meta.env.VITE_APP_BASE_API + avatar;
            }
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              this.roles = res.roles;
              this.permissions = res.permissions;
              this.setIsAdmin();
              this.setIsStudent();
              this.setIsTeacher();
            } else {
              this.roles = ['ROLE_DEFAULT'];
            }
            this.id = user.userId || '';
            this.name = user.userName || '';
            this.nickName = user.nickName || '';
            this.avatar = avatar;
            /* 初始密码提示 */
            if (res.isDefaultModifyPwd) {
              ElMessageBox.confirm('您的密码还是初始密码，请修改密码！', '安全提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
                .then(() => {
                  router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } });
                })
                .catch(() => {});
            }
            /* 过期密码提示 */
            if (!res.isDefaultModifyPwd && res.isPasswordExpired) {
              ElMessageBox.confirm('您的密码已过期，请尽快修改密码！', '安全提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' })
                .then(() => {
                  router.push({ name: 'Profile', params: { activeTab: 'resetPwd' } });
                })
                .catch(() => {});
            }
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 退出系统
    logOut() {
      return new Promise<void>((resolve, reject) => {
        logout()
          .then(() => {
            sessionStorage.removeItem('cachedViews');
            sessionStorage.removeItem('visitedViews');

            this.token = '';
            this.roles = [];
            this.permissions = [];
            removeToken();
            resolve();
          })
          .catch((error: any) => {
            reject(error);
          });
      });
    },
    setIsAdmin() {
      const roles = this.roles || [];
      this.isAdmin = roles.includes && (roles.includes('admin') || roles.includes('systemAdmin'));
    },
    setIsStudent() {
      const roles = this.roles || [];
      this.isStudent = roles.includes && (roles.includes('student') || roles.indexOf('student') !== -1) ? true : false;
    },
    setIsTeacher() {
      const roles = this.roles || [];
      this.isTeacher = roles.includes && (roles.includes('teacher') || roles.indexOf('teacher') !== -1) ? true : false;
    },
  },
});

export default useUserStore;
