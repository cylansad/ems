[#ftl]
[@b.head/]
[@b.toolbar title="entity.dataProfileField"]bar.addBack("${b.text("action.back")}");[/@]
[@b.form action="!save" theme="list"]
	[@b.textfield label="common.name" name="field.name" value="${field.name!}" required="true" maxlength="50"/]
	[@b.textfield label="标题" name="field.title" value="${field.title!}" required="true" maxlength="50"/]
	[@b.select items=types name="field.type.id" value=field.type! required="true" label="类型" style="width:100px"/]
	[@b.textfield label="数据源" name="field.source" value="${(field.source)!}" style="width:400px;" maxlength="100" comment="java.lang中的基本类型，此处可以为空"/]
	[@b.radios label="是否允许多值" name="field.multiple"  value=field.multiple/]
	[@b.formfoot]
		<input type="hidden" name="field.id" value="${(field.id)!}"/>
		[@b.reset/]&nbsp;&nbsp;[@b.submit value="action.submit" /]
	[/@]
</table>
[/@]
[@b.foot/]