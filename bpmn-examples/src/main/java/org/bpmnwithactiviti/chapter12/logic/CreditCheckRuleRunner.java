package org.bpmnwithactiviti.chapter12.logic;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bpmnwithactiviti.chapter12.model.LoanApplicant;
import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.command.Command;
import org.drools.command.CommandFactory;
import org.drools.io.ResourceFactory;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.drools.runtime.StatelessKnowledgeSession;

public class CreditCheckRuleRunner {

	public static boolean runRules(LoanApplicant loanApplicant) throws Exception {

		KnowledgeBase kbase = readKnowledgeBase();
		StatelessKnowledgeSession ksession = kbase.newStatelessKnowledgeSession();
	
		@SuppressWarnings("rawtypes")
		List<Command> cmds = new ArrayList<Command>();

		cmds.add(CommandFactory.newInsert(loanApplicant, "loanApplicant"));

		KnowledgeRuntimeLogger logger = KnowledgeRuntimeLoggerFactory
				.newFileLogger(ksession, "CreditChecker");

		System.out.println("Fire the rules!");
		ksession.execute(CommandFactory.newBatchExecution(cmds));
		
		logger.close();

		System.out.println("Done firing.. --> result = " + loanApplicant.isCheckCreditOk());

		return loanApplicant.isCheckCreditOk();
	}

	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(
				ResourceFactory.newClassPathResource("chapter10" + File.separator + "rules" 
				        + File.separator + "CreditCheck.drl"),
				ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}
