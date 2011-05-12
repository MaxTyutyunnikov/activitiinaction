package org.activiti.rest.api.history;

import java.util.Map;

import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.impl.HistoricProcessInstanceQueryProperty;
import org.activiti.rest.util.ActivitiPagingWebScript;
import org.activiti.rest.util.ActivitiRequest;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.Status;

public class HistoryProcessesGet extends ActivitiPagingWebScript {

    public HistoryProcessesGet() {
        properties.put("id", HistoricProcessInstanceQueryProperty.PROCESS_INSTANCE_ID_);
        properties.put("processDefinitionId", HistoricProcessInstanceQueryProperty.PROCESS_DEFINITION_ID);
    }

    @Override
    protected void executeWebScript(ActivitiRequest req, Status status, Cache cache, Map<String, Object> model) {
        paginateList(req, createQuery(req), "historyProcesses", model, "id");
    }
    
    private HistoricProcessInstanceQuery createQuery(ActivitiRequest req) {
        HistoricProcessInstanceQuery query = getHistoryService().createHistoricProcessInstanceQuery();
        query = query.finished();
        String processDefinitionId = req.getString("processDefinitionId", null);
        query = processDefinitionId == null ? query : query.processDefinitionId(processDefinitionId);
        return query;
    }

}

