package org.bpmnwithactiviti.chapter10.logic;

import java.io.File;

import org.bpmnwithactiviti.chapter10.model.CreditCheckResult;
import org.bpmnwithactiviti.chapter10.model.LoanApplicant;
import org.drools.KnowledgeBase;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.compiler.DecisionTableFactory;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class CreditCheckDecisionTableRunner {

	public static CreditCheckResult runRules(LoanApplicant loanApplicant)
			throws Exception {
		
		KnowledgeBase kbase = readKnowledgeBase();
		StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();

		ksession.insert(loanApplicant);
		
		CreditCheckResult result = new CreditCheckResult();
		ksession.setGlobal("result", result);

		System.out.println("Fire the rules!");
		ksession.fireAllRules();

		ksession.dispose();

		return result;
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		dtconf.setInputType(DecisionTableInputType.XLS);
		dtconf.setWorksheetName("CreditCheck");
		
		String drl = DecisionTableFactory
		.loadFromInputStream(ResourceFactory
			.newClassPathResource("chapter10"
					+ File.separator + "decisiontable" + File.separator + "CreditCheck.xls")
			.getInputStream(), dtconf);

		System.out.println(drl);
		
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
				.newKnowledgeBuilder();
		kbuilder.add(
				ResourceFactory.newClassPathResource("chapter10"
						+ File.separator + "decisiontable" + File.separator + "CreditCheck.xls"),
				ResourceType.DTABLE, dtconf);
		
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		return kbase;
	}

}
