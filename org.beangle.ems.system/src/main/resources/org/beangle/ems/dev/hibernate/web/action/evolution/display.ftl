[#ftl]
[@b.head/]
[@b.css href="code.css"/]
<h2>欢迎使用${databaseName}数据库升级程序</h2>
<table width="100%">
	<tr>
	  <td>
		<img src="${b.theme.iconurl('actions/go-previous.png','48x48')}" width="18px" height="18px"/>
		[@b.a href="!index"]返回[/@]${resource.URL}
	  </td>
	  <td >
		[@b.form action="!exec"]
			<input type="hidden" value="${resource.URL}" name="sqlfile"/>
			[@b.submit value="执行"/]
		[/@]
	</td>
	</tr>
</table>
<table class="filecontent CodeRay">
	<tbody>
		[#list sqllines as line]
		<tr><th class="line-num" id="L${line_index+1}"><a href="#L${line_index+1}">${line_index+1}</a></th>
			<td class="line-code"><pre>${line?html}</pre></td>
		</tr>
		[/#list]
	</tbody>
</table>
[@b.foot/]