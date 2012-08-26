[#ftl]
[@b.head/]
[#include "../nav.ftl"/]
[#include "scope.ftl"/]
[@b.toolbar title="新建/修改资源"]bar.addBack();[/@]
<div style="width:850px">
[@b.form action="!save" title="security.resource.info" theme="list"]
	[@b.textfield name="resource.name" required="true" label="common.name" value="${resource.name!}" maxlength="50" comment="资源名称唯一"/]
	[@b.textfield name="resource.title" required="true" label="标题" value="${resource.title!}" maxlength="50"/]
	[@b.radios label="菜单入口" name="resource.entry" value=resource.entry items="1:是(访问时不需要额外参数),0:否(需要其它参数)"/]
	[@b.radios label="可见范围" name="resource.scope" items=scopeTitles value=(resource.scope?string)!'PRIVATE'/]
	[@b.radios label="common.status" name="resource.enabled" value=resource.enabled items="1:action.activate,0:action.freeze"/]
	[@b.textfield name="resource.remark" label="common.remark" value="${resource.remark!}" maxlength="50"/]
	[@b.formfoot]
		[@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/]
		<input type="hidden" name="resource.id" value="${(resource.id)!}" />
	[/@]
[/@]
</div>
[@b.foot/]