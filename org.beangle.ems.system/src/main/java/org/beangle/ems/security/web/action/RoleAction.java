/* Copyright c 2005-2012.
 * Licensed under GNU  LESSER General Public License, Version 3.
 * http://www.gnu.org/licenses
 */
package org.beangle.ems.security.web.action;

import java.sql.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.beanutils.BeanPredicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.functors.EqualPredicate;
import org.beangle.commons.dao.query.builder.OqlBuilder;
import org.beangle.commons.entity.Entity;
import org.beangle.commons.entity.util.HierarchyEntityUtils;
import org.beangle.commons.transfer.exporter.PropertyExtractor;
import org.beangle.ems.security.helper.RolePropertyExtractor;
import org.beangle.ems.web.action.SecurityActionSupport;
import org.beangle.security.blueprint.Role;
import org.beangle.security.blueprint.User;
import org.beangle.security.blueprint.data.RoleProfile;
import org.beangle.security.blueprint.function.service.FuncPermissionService;
import org.beangle.security.blueprint.model.RoleBean;
import org.beangle.security.blueprint.service.RoleService;
import org.beangle.security.blueprint.service.UserService;

/**
 * 角色信息维护响应类
 * 
 * @author chaostone 2005-9-29
 */
public class RoleAction extends SecurityActionSupport {

  private RoleService roleService;

  private UserService userService;

  /**
   * 对组可管理意为<br>
   * 1 建立下级组
   * 2 移动下级组顺序
   * 不能改变组的1）动态属性、2）权限和3）直接成员，4）删除组，5）重命名，这些将看作组同部分一起看待的。
   * 只要拥有上级组的管理权限，才能变更这些，这些称之为写权限。
   * 成员关系可以等价于读权限
   * 授权关系可以等价于读权限传播
   * 拥有某组的管理权限，不意味拥有下级组的管理权限。新建组情况自动授予该组的其他管理者管理权限。
   */
  protected void editSetting(Entity<?> entity) {
    OqlBuilder<Role> query = OqlBuilder.from(getEntityName(), "role");
    if (!isAdmin()) {
      query.join("role.members", "gm");
      query.where("gm.user.id=:me and gm.manager=true", getUserId());
    }
    List<Role> roles = entityDao.search(query);
    Role role = (Role) entity;
    roles.removeAll(HierarchyEntityUtils.getFamily(role));
    CollectionUtils.filter(roles, new BeanPredicate("dynamic", new EqualPredicate(Boolean.FALSE)));
    put("parents", roles);
  }

  public String profile() {
    OqlBuilder<RoleProfile> builder = OqlBuilder.from(RoleProfile.class, "gp");
    builder.where("gp.role.id=:roleId", getId("role"));
    put("profiles", entityDao.search(builder));
    return forward();
  }

  @Override
  public String search() {
    List<Role> roles = entityDao.search(getQueryBuilder());
    put(getShortName() + "s", roles);
    put("editableRoles", roleService.editable(entityDao.get(User.class, getUserId()), roles));
    return forward();
  }

  protected OqlBuilder<Role> getQueryBuilder() {
    OqlBuilder<Role> entityQuery = OqlBuilder.from(getEntityName(), "role");
    if (!isAdmin()) {
      entityQuery.join("role.members", "gm");
      entityQuery.where("gm.user.id=:me and gm.manager=true", getUserId());
    }
    populateConditions(entityQuery);
    String orderBy = get("orderBy");
    if (null == orderBy) orderBy = "role.code";
    entityQuery.limit(getPageLimit()).orderBy(orderBy);
    return entityQuery;
  }

  protected PropertyExtractor getPropertyExtractor() {
    return new RolePropertyExtractor(getTextResource());
  }

  protected String saveAndForward(Entity<?> entity) {
    RoleBean role = (RoleBean) entity;
    if (entityDao.duplicate(Role.class, role.getId(), "name", role.getName())) return redirect("edit",
        "error.notUnique");
    if (!role.isPersisted()) {
      User creator = userService.get(getUserId());
      role.setCode("tmp");
      role.setOwner(creator);
      roleService.createRole(creator, role);
    } else {
      role.setUpdatedAt(new Date(System.currentTimeMillis()));
      if (!role.isPersisted()) role.setCreatedAt(new Date(System.currentTimeMillis()));
      entityDao.saveOrUpdate(role);
    }
    Long newParentId = getLong("parent.id");
    int indexno = getInteger("indexno");
    Role parent = null;
    if (null != newParentId) parent = entityDao.get(Role.class, newParentId);

    roleService.moveRole(role, parent, indexno);
    if (!role.isEnabled()) {
      Set<Role> family = HierarchyEntityUtils.getFamily((Role) role);
      for (Role one : family)
        ((RoleBean) one).setEnabled(false);
      entityDao.saveOrUpdate(family);
    }
    return redirect("search", "info.save.success");
  }

  /**
   * 删除一个或多个角色
   * 
   * @return
   */
  public String remove() {
    Long[] roleIds = getIds(getShortName());
    User curUser = userService.get(getUserId());
    roleService.removeRole(curUser, entityDao.get(Role.class, roleIds));
    return redirect("search", "info.remove.success");
  }

  public void setAuthorityService(FuncPermissionService authorityService) {
    this.funcPermissionService = authorityService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public void setRoleService(RoleService roleService) {
    this.roleService = roleService;
  }

  @Override
  protected String getEntityName() {
    return Role.class.getName();
  }

}
