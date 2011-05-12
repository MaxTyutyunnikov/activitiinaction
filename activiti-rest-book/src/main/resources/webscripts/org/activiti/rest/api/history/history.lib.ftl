<#escape x as jsonUtils.encodeJSONString(x)>

<#macro printProcessHistoryList historyProcesses>
[
  <#list historyProcesses as historyProcess><@printHistoryProcess historyProcess/><#if historyProcess_has_next>,</#if></#list>
]
</#macro>

<#macro printHistoryProcess historyProcess>
{
  "id": "${historyProcess.id}",
  "processDefinitionId": "${historyProcess.processDefinitionId}",
  "startTime": "${iso8601Date(historyProcess.startTime)}",
  "endTime": "${iso8601Date(historyProcess.endTime)}",
  "duration": ${historyProcess.durationInMillis}
}
</#macro>
</#escape>