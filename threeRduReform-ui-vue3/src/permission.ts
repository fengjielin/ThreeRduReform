import router from './router';
import { ElMessage } from 'element-plus';
import NProgress from 'nprogress';
import 'nprogress/nprogress.css';
import { getToken } from '@/utils/auth';
import { isHttp, isPathMatch } from '@/utils/validate';
import { isRelogin } from '@/utils/request';
import { getFirstPath, traverseTree } from '@/utils/index';

import useUserStore from '@/store/modules/user';
import useSettingsStore from '@/store/modules/settings';
import usePermissionStore from '@/store/modules/permission';
import useSystemStore from '@/store/modules/system';

NProgress.configure({ showSpinner: false });

const whiteList = ['/login', '/register', '/chat', '/sports', '/chat/sports'];

const isWhiteList = (path) => {
  return whiteList.some((pattern) => isPathMatch(pattern, path));
};

router.beforeEach((to, from, next) => {
  console.log({ to, from });
  NProgress.start();
  useSystemStore().GetCasEnable();

  if (getToken()) {
    to.meta.title && useSettingsStore().setTitle(to.meta.title as string);
    /* has token*/
    if (to.path === '/login') {
      next({ path: '/' });
      NProgress.done();
    } else if (isWhiteList(to.path)) {
      next();
    } else {
      if (useUserStore().roles.length === 0) {
        isRelogin.show = true;
        // 判断当前用户是否已拉取完user_info信息
        useUserStore()
          .getInfo()
          .then(() => {
            isRelogin.show = false;
            usePermissionStore()
              .generateRoutes()
              .then((accessRoutes: any[]) => {
                console.log(accessRoutes);
                // 根据roles权限生成可访问的路由表
                accessRoutes.forEach((route: any) => {
                  if (!isHttp(route.path)) {
                    router.addRoute(route); // 动态添加可访问路由表
                  }
                });

                // 由于首页放在菜单管理里面，所以一个角色是可能会存在没有菜单的情况的，需要判断当前用户是否拥有一个有效的菜单
                let firstMenuPath = usePermissionStore().topbarRouters.find((item: any) => {
                  if (item.path == '/' && item.children.length > 0) {
                    return true;
                  } else if (!item.hidden) {
                    return true;
                  }
                });
                console.log(usePermissionStore().topbarRouters, firstMenuPath);
                if (firstMenuPath) {
                  if (from.path == '/login' || from.path == '/register') {
                    // 判断从登录页进入后的第一个页面应该重定向到哪里，如果重定向地址存在就跳转到重定向地址，否则跳转到第一个菜单
                    let redirectPath = to.fullPath;

                    // 需要判断当前用户的第一个菜单是什么
                    let path = getFirstPath(firstMenuPath);
                    console.log({ 重定向地址: redirectPath, 第一个菜单: path });
                    if (redirectPath == '/' || useUserStore().userType == '00') {
                      redirectPath = path;
                    }
                    next({ path: redirectPath || path, replace: true }); // hack方法 确保addRoutes已完成
                  } else {
                    // 获取 所有 路由的 路径
                    let pathList = [];
                    for (let i = 0; i < usePermissionStore().routes.length; i++) {
                      let nodePathList = [];
                      const item = usePermissionStore().routes[i];
                      traverseTree(item, [], (nodePath) => {
                        // 修复如果菜单仅为一层的情况，导致菜单路径为 //xxx 而不是 /xxx，出现刷新页面后调整回第一个菜单
                        if (nodePath[0] == '/') {
                          nodePath[0] = '';
                        }

                        // 如果路径包含冒号，去除冒号及其后面的所有内容
                        let lastSegment = nodePath[nodePath.length - 1];
                        lastSegment = lastSegment.split('/:')[0];
                        nodePath[nodePath.length - 1] = lastSegment;

                        nodePathList.push(nodePath.join('/'));
                      });
                      pathList.push(...nodePathList);
                    }

                    // 处理 path为 '/expert/applicationDetailVerify/index/4'情况的匹配，'/expert/applicationDetail/index/:edApplicationUseId(\\d+)'
                    let toPath = to.path;

                    let paths = to.path.split('/');
                    let lastSegment = paths[paths.length - 1];
                    // 判断lastSegment是否为数值
                    let isLastSegmentNumber = !isNaN(Number(lastSegment)) && lastSegment !== '';
                    if (isLastSegmentNumber) {
                      // 移除最后的数字段
                      paths.pop();
                      toPath = paths.join('/');
                    }

                    // 判断是否拥有to.path的路径
                    let flag = false;
                    pathList.forEach((path) => {
                      if (path == toPath) {
                        flag = true;
                      }
                    });
                    console.log(pathList);
                    console.log(flag, toPath, to);
                    if (flag) {
                      next({ ...to, replace: true }); // hack方法 确保addRoutes已完成
                    } else {
                      let path = getFirstPath(firstMenuPath);
                      console.log(path);
                      next({ path: path, replace: true }); // hack方法 确保addRoutes已完成
                    }
                  }
                } else {
                  useUserStore()
                    .logOut()
                    .then(() => {
                      ElMessage.error('您未被分配菜单，请联系系统管理员');
                      next({ path: '/login', replace: true }); // 重定向到登录页
                    });
                }
              });
          })
          .catch((err: any) => {
            useUserStore()
              .logOut()
              .then(() => {
                ElMessage.error(err);
                next({ path: '/' });
              });
          });
      } else {
        next();
      }
    }
  } else {
    // 没有token
    if (isWhiteList(to.path)) {
      // 在免登录白名单，直接进入
      next();
    } else {
      if (!useSystemStore().casEnable) {
        next(`/login?redirect=${to.fullPath}`); // 否则全部重定向到登录页
      } else {
        window.location.href = useSystemStore().casLoginUrl;
      }
      NProgress.done();
    }
  }
});

router.afterEach(() => {
  NProgress.done();
});
