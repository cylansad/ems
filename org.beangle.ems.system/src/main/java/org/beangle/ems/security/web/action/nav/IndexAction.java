/* Copyright c 2005-2012.
 * Licensed under GNU  LESSER General Public License, Version 3.
 * http://www.gnu.org/licenses
 */
package org.beangle.ems.security.web.action.nav;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.beangle.commons.collection.CollectUtils;
import org.beangle.commons.entity.util.HierarchyEntityUtils;
import org.beangle.commons.lang.Strings;
import org.beangle.ems.web.action.SecurityActionSupport;
import org.beangle.security.blueprint.Resource;
import org.beangle.security.blueprint.User;
import org.beangle.security.blueprint.nav.Menu;
import org.beangle.security.blueprint.nav.MenuProfile;
import org.beangle.security.blueprint.nav.service.MenuService;

/**
 * 菜单浏览导航器
 * 
 * @author chaostone
 * @version $Id: MenuNavAction.java Jun 24, 2011 8:25:47 PM chaostone $
 */
public class IndexAction extends SecurityActionSupport {

  private MenuService menuService;

  public String index() throws Exception {
    final String name = get("name");
    final MenuProfile profile;
    final Long menuId = getLong("menu.id");
    final Set<Menu> family;
    final Menu givenMenu;
    User user = entityDao.get(User.class, getUserId());
    if (null != menuId) {
      givenMenu = entityDao.get(Menu.class, menuId);
      profile = givenMenu.getProfile();
      family = HierarchyEntityUtils.getFamily(givenMenu);
    } else {
      family = null;
      givenMenu = null;
      profile = menuService.getProfile(user, getLong("profile.id"));
    }
    put("profile", profile);

    List<Menu> menus = Collections.emptyList();
    if (null != profile) {
      menus = menuService.getMenus(profile, user);
    }
    CollectionUtils.filter(menus, new Predicate() {
      public boolean evaluate(Object object) {
        Menu amenu = (Menu) object;
        if (null != family && !family.contains(amenu)) return false;
        if (Strings.isNotEmpty(name)) {
          if (!amenu.getChildren().isEmpty()) return false;
          StringBuilder searchTarget = new StringBuilder(Strings.concat(amenu.getName(), amenu.getTitle(),
              amenu.getRemark()));
          for (Resource res : amenu.getResources()) {
            searchTarget.append(Strings.concat(res.getName(), res.getTitle(), res.getRemark()));
          }
          return searchTarget.toString().contains(name);
        } else return true;
      }
    });
    if (Strings.isNotEmpty(name)) {
      HierarchyEntityUtils.addParent(menus, givenMenu);
      Collections.sort(menus);
    }

    List<Menu> menuPath = CollectUtils.newArrayList();
    if (null != givenMenu) {
      // menus.retainAll(family);
      menus.remove(givenMenu);
      menuPath = HierarchyEntityUtils.getPath(givenMenu);
    }
    put("menuPath", menuPath);
    put("menus", menus);
    put("tops", HierarchyEntityUtils.getRoots(menus));
    return forward();
  }

  public String search() {
    User user = entityDao.get(User.class, getUserId());
    MenuProfile profile = menuService.getProfile(user, getLong("profile.id"));
    List<Menu> menus = menuService.getMenus(profile, user);
    List<Menu> menuPath = CollectUtils.newArrayList();
    Long menuId = getLong("menu.id");
    if (null != menuId) {
      Menu menu = entityDao.get(Menu.class, menuId);
      menus.retainAll(HierarchyEntityUtils.getFamily(menu));
      menus.remove(menu);
      menuPath = HierarchyEntityUtils.getPath(menu);
    }
    put("menuPath", menuPath);
    if (null != profile) {
      put("menus", menus);
    } else {
      put("menus", Collections.EMPTY_LIST);
    }
    put("tops", HierarchyEntityUtils.getRoots(menus));
    put("user", user);
    return forward();
  }

  public String access() {
    Long menuId = getLong("menu.id");
    Menu menu = entityDao.get(Menu.class, menuId);
    List<Menu> paths = HierarchyEntityUtils.getPath(menu);
    put("menu", menu);
    put("paths", paths);
    return forward();
  }

  public void setMenuService(MenuService menuService) {
    this.menuService = menuService;
  }
}
