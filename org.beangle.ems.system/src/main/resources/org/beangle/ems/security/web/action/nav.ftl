[#ftl]
[@b.navmenu title="权限管理"]
	[@b.navitem title="common.dashboard" href="/security/index"/]
	[@ems.guard res="/security/nav/menu"][@b.navitem title="entity.menu" href="/security/nav/menu" /][/@]
	[@ems.guard res="/security/resource"][@b.navitem title="entity.funcResource" href="/security/resource!search?orderBy=resource.name%20asc" /][/@]
	[@ems.guard res="/security/data/field"][@b.navitem title="entity.dataPermission" href="/security/data/field" /][/@]
	[@b.navitem title="我的账户" href="/security/my?nav=true" /]
[/@]