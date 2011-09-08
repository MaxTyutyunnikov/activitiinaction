package org.bpmnwithactiviti.chapter12.logic;

import java.io.File;

import org.bpmnwithactiviti.chapter12.model.LoanApplicant;
import org.drools.KnowledgeBase;
import org.drools.builder.DecisionTableConfiguration;
import org.drools.builder.DecisionTableInputType;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class CreditCheckDecisionTableRunner {

	public static boolean runRules(LoanApplicant loanApplicant) throws Exception {
		KnowledgeBase kbase = readKnowledgeBase();
		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
		System.out.println("Fire the rules!");
		ksession.execute(loanApplicant);
		return loanApplicant.isCheckCreditOk();
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		DecisionTableConfiguration dtconf = KnowledgeBuilderFactory
				.newDecisionTableConfiguration();
		dtconf.setInputType(DecisionTableInputType.XLS);
		
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(
				ResourceFactory.newClassPathResource("chapter12"
						+ File.separator + "decisiontable" + File.separator + "CreditCheck.xls"),
				ResourceType.DTABLE, dtconf);
		
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println("Error parsing " + error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = kbuilder.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}
