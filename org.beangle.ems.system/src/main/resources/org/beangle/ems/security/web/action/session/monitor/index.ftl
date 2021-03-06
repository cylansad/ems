[#ftl]
[@b.head/]
[#include "nav.ftl"/]
[#assign sessions=0]
[#assign capacity=0]
[#list sessionStats as stat]
	[#assign sessions=sessions+stat.online/]
	[#assign capacity=capacity+stat.capacity/]
[/#list]
[#assign statTitle] ${sessions}/${capacity}(${b.now?string('yyyy-MM-dd HH:mm:ss')}) [#list sessionStats as stat]${stat.category}(${stat.online}/${stat.capacity}) [/#list][/#assign]
[@b.toolbar title="当前服务器会话 ${statTitle}"][/@]
[#assign refreshInterval=Parameters['interval']!"10"/]
[@b.grid items=sessioninfos var="sessioninfo" refresh="${refreshInterval}"]
	[@b.gridbar]
	bar.addItem("过期",action.multi('invalidate','确定过期选择的会话吗?'),'action-edit-delete');
	[/@]
	[@b.row]
		[@b.boxcol/]
		[@b.col width="15%" title="sessioninfo.username" property="username"]
			[@b.a href="user!dashboard?user.name=${sessioninfo.username}" target="_blank"]${(sessioninfo.fullname!(''))?html}(${(sessioninfo.username!(''))?html})[/@]
		[/@]
		[@b.col width="10%" title="sessioninfo.loginAt" property="loginAt"]<span title="${sessioninfo.id}">${sessioninfo.loginAt?string("yy-MM-dd HH:mm")}</span>[/@]
		[@b.col width="12%" title="最后访问" property="lastAccessAt"]${(sessioninfo.lastAccessAt?string("MM-dd HH:mm:ss"))!}[/@]
		[@b.col width="9%" title="sessioninfo.onlineTime" property="onlineTime" sort="lastAccessAt-loginAt"]${(sessioninfo.onlineTime)/1000/60}min[/@]
		[@b.col width="13%" title="sessioninfo.ip" property="ip"/]
		[@b.col width="10%" title="sessioninfo.os" property="os"/]
		[@b.col width="12%" title="sessioninfo.agent" property="agent"/]
		[@b.col width="8%" title="sessioninfo.category" property="category"/]
		[@b.col width="8%" title="sessioninfo.expired" property="expiredAt"]${sessioninfo.expired?string("过期","在线")}[/@]
	[/@]
[/@]
<div>定时每${refreshInterval}秒刷新</div>
[@b.foot/]