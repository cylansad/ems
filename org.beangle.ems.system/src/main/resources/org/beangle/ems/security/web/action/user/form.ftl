[#ftl]
[@b.head/]
<script type="text/javascript">
	bg.ui.load("tabletree");
</script>
[#assign labInfo][#if user.id??]${b.text("action.modify")}[#else]${b.text("action.new")}[/#if] ${b.text("entity.user")}[/#assign]
[@b.toolbar title=labInfo]bar.addBack("${b.text("action.back")}");[/@]
[@b.messages/]
[@b.tabs]
[@b.form name="userForm" action="!save" class="listform" theme="list"]
	<input type="hidden" name="user.id" value="${user.id!}" />
	[@b.tab label="ui.userInfo"]
		[@b.textfield name="user.name" value="${user.name!}" style="width:200px;" required="true" maxlength="30"/]
		[@b.radios name="user.enabled" value=user.enabled items="1:action.activate,0:action.freeze"/]
		[@b.textfield name="user.fullname" value="${user.fullname!}" style="width:200px;" required="true" maxlength="50" /]
		[@b.password label="user.password" name="password" value="" maxlength=settings.maxPwdLength?string showStrength="true"/]
		[@b.email name="user.mail" value="${user.mail!}" style="width:300px;" required="true" maxlength="50"/]
		[#if isadmin|| isme]
		[@b.startend label="user.effective-invalid" name="user.effectiveAt,user.invalidAt" required="true,false" start=user.effectiveAt end=user.invalidAt format="datetime" disabled="disabled"/]
		[#else]
		[@b.startend label="user.effective-invalid" name="user.effectiveAt,user.invalidAt" required="true,false" start=user.effectiveAt end=user.invalidAt format="datetime"/]
		[/#if]
		[@b.datepicker name="user.passwordExpiredAt" value=user.passwordExpiredAt format="datetime"/]
		[@b.textarea cols="50" rows="1" name="user.remark" value=user.remark! maxlength="50"/]
		[@b.formfoot][@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit"/][/@]
	[/@]
	[@b.tab label="user.members"]
		[@b.grid  items=roles?sort_by("code") var="role" sortable="false"]
			[@b.row]
				<tr [#if role??]id="${role.code}"[/#if]>
				[@b.col title="common.index" width="5%"]${role_index+1}[/@]
				[@b.treecol title="entity.role" property="name"]
					<span [#if !role.enabled]class="ui-disabled" title="${b.text('action.freeze')}"[/#if]>
					${role.code} ${role.name}[#if role.dynamic] (动态)[/#if] [#if !role.enabled] (禁用)[/#if]
					</span>
				[/@]
				[@b.col title="member.member" width="10%"]
					[#assign displayMember=(role.enabled && !role.dynamic)]
					<input type="checkbox" [#if !displayMember]style="display:none"[/#if] name="member${role.id}" onchange="changeMember(${role.id},this)" ${(memberMap.get(role).member)?default(false)?string('checked="checked"','')}/>
					[#if !displayMember && (memberMap.get(role).member)!false]&radic;[/#if]
				[/@]
				[@b.col title="member.granter" width="10%"]
					[#assign displayGranter=(role.enabled && !role.dynamic && curMemberMap.get(role).manager)/]
					<input type="checkbox" name="granter${role.id}" [#if !displayGranter]style="display:none"[/#if] ${(memberMap.get(role).granter)?default(false)?string('checked="checked"','')}/>
					[#if !displayGranter && (memberMap.get(role).granter)!false]&radic;[/#if]
				[/@]
				[@b.col title="member.manager" width="10%"]
					[#assign displayManager=(role.enabled && !role.dynamic && curMemberMap.get(role).manager)/]
					<input type="checkbox" name="manager${role.id}" [#if !displayManager]style="display:none"[/#if] ${(memberMap.get(role).manager)?default(false)?string('checked="checked"','')}/>
					[#if !displayManager && (memberMap.get(role).manager)!false]&radic;[/#if]
				[/@]
				[@b.col title="common.updatedAt" width="20%"]${(memberMap.get(role).updatedAt?string("yyyy-MM-dd HH:mm"))!}[/@]
				</tr>
			[/@]
		[/@]
	[/@]
[/@]
	[#if user.id??]
	[@b.tab label="全局数据权限" href="/security/data/profile!info?forEdit=1&holder.id=${user.id}&type=user" /]
	[/#if]
[/@]
<script  type="text/javascript">
	/**
	 * 改变每行之前的复选框
	 */
	function changeMember(roleId,checkbox){
		if(null==checkbox) return;
		treeToggle(checkbox)
		newStatus=checkbox.checked
		var form=document.userForm;
		if(typeof form['member'+roleId]!="undefined"){
			form['member'+roleId].checked=newStatus;
		}
		/*if(typeof form['granter'+roleId]!="undefined"){
			if(!form['granter'+roleId].disabled) form['granter'+roleId].checked=newStatus;
		}
		if(typeof form['manager'+roleId]!="undefined"){
			if(!form['manager'+roleId].disabled) form['manager'+roleId].checked=newStatus;
		}*/
	}
</script>
[@b.foot/]