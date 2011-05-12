<#import "../activiti.lib.ftl" as activitiLib>
<#import "history.lib.ftl" as historyLib>
{
  "data": <@historyLib.printProcessHistoryList historyProcesses/>,
  <@activitiLib.printPagination/>
}
