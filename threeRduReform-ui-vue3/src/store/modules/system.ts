import { listConfig } from '@/api/system/config';
import defaultSettings from '@/settings.js';

const useSystemStore = defineStore('system', {
  state: () => ({
    pageParams: JSON.parse(sessionStorage.getItem('pageParams') || null), // 缓存页面参数
    refresh: false,
    CDNAcceleration: false, // 是否启用cdn加速
    CDNUrl: '', // cdn加速地址
    OSSConfig: '', // oss配置
    OSSUrl: '', // oss地址

    casEnable: false, // 单点登录
    casLoginUrl: '', // cas登陆url
    casLogoutUrl: '', // cas登出url
  }),
  actions: {
    setPageParams(data) {
      this.pageParams = data;
      sessionStorage.setItem('pageParams', JSON.stringify(data));
    },
    setRefresh(value) {
      this.refresh = value;
    },

    // 获取cdn加速配置
    GetCDNConfig() {
      return new Promise((resolve, reject) => {
        listConfig({ configKey: 'sys.cdn.CDNAcceleration' })
          .then((res) => {
            if (res.rows && res.rows[0]) {
              this.CDNAcceleration = res.rows[0].configValue == 'true' ? true : false;
              this.CDNUrl = res.rows[0].remark ? res.rows[0].remark : '';
            }
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    GetOSSConfig() {
      return new Promise((resolve, reject) => {
        listConfig({ configKey: 'sys.config.oss' })
          .then((res) => {
            if (res.rows && res.rows[0]) {
              this.OSSConfig = res.rows[0].configValue == 'true' ? true : false;
              this.OSSUrl = res.rows[0].remark ? res.rows[0].remark : '';
            }
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取单点登录的配置
    GetCasEnable() {
      return new Promise(async (resolve, reject) => {
        try {
          // 不确定是否应该把 listConfig 这个接口加到白名单内，暂时先注释掉
          // let res = await listConfig({ configKey: 'sys.login.casEnable' });
          // if (res.rows && res.rows[0]) {
          //   let casEnable = res.rows[0].configValue == 'true' ? true : false;
          //   this.casEnable = casEnable;

          //   if (casEnable) {
          //     if (parseJSON(res.rows[0].remark)) {
          //       let { casLoginUrl, casLogoutUrl } = parseJSON(res.rows[0].remark);
          //       if (casLoginUrl && casLogoutUrl) {
          //         this.casLoginUrl = casLoginUrl;
          //         this.casLogoutUrl = casLogoutUrl;
          //       } else {
          //         console.error('单点登录配置错误', { casLoginUrl, casLogoutUrl });
          //         this.casEnable = false;
          //       }
          //     } else {
          //       console.error('单点登录配置错误');
          //       this.casEnable = false;
          //     }
          //   }
          // }
          this.casEnable = defaultSettings.casEnable;
          this.casLoginUrl = defaultSettings.casLoginUrl;
          this.casLogoutUrl = defaultSettings.casLogoutUrl;

          resolve();
        } catch (error) {
          reject(error);
        }
      });
    },
  },
});

export default useSystemStore;
