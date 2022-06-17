package furkanodev;
import aima.core.probability.ProbabilityModel;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.FiniteNode;
import aima.core.probability.bayes.impl.BayesNet;
import aima.core.probability.bayes.impl.FullCPTNode;
import aima.core.probability.bayes.model.FiniteBayesModel;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.RandVar;

//	https://www.upgrad.com/blog/bayesian-network-example/

public class App {
	
//	https://github.com/aimacode/aima-java/blob/AIMA3e/aima-core/src/main/java/aima/core/probability/example/ExampleRV.java
	public static RandVar createRV(String name) {
		return new RandVar(name, new BooleanDomain());
	}
	
//	https://github.com/aimacode/aima-java/blob/AIMA3e/aima-core/src/main/java/aima/core/probability/example/BayesNetExampleFactory.java
	public static BayesianNetwork constructUniversityNetwork() {
		FiniteNode examLevel = new FullCPTNode(createRV("ExamLevel"), new double[] { 0.3, 0.7 });
		FiniteNode iqLevel = new FullCPTNode(createRV("IQLevel"), new double[] { 0.2, 0.8 });
		
		FiniteNode aptiScore = new FullCPTNode(
				createRV("AptiScore"),
				new double[] {
						0.6, 0.4,
						0.25, 0.75
						},
				iqLevel);
		
		FiniteNode marks = new FullCPTNode(
				createRV("Marks"),
				new double[] {
						0.2, 0.8,
						0.5, 0.5,
						0.1, 0.9,
						0.4, 0.6
				},
				iqLevel, examLevel);
		
		FiniteNode admission = new FullCPTNode(
				createRV("Admission"),
				new double[] {
						0.1, 0.9,
						0.4, 0.6
				},
				marks);
		
		return new BayesNet(examLevel, iqLevel);
	}
	
// 	https://github.com/aimacode/aima-java/blob/AIMA3e/aima-core/src/test/java/aima/test/core/unit/probability/CommonProbabilityModelTests.java	
	public static void main(String[] args) {
		AssignmentProposition clever = new AssignmentProposition(createRV("IQLevel"), Boolean.TRUE);
		AssignmentProposition stupid = new AssignmentProposition(createRV("IQLevel"), Boolean.FALSE);
		AssignmentProposition hard = new AssignmentProposition(createRV("ExamLevel"), Boolean.TRUE);
		AssignmentProposition easy = new AssignmentProposition(createRV("ExamLevel"), Boolean.FALSE);
		AssignmentProposition accepted = new AssignmentProposition(createRV("Admission"), Boolean.TRUE);
		AssignmentProposition rejected = new AssignmentProposition(createRV("Admission"), Boolean.FALSE);
		AssignmentProposition passed = new AssignmentProposition(createRV("Marks"), Boolean.TRUE);
		AssignmentProposition failed = new AssignmentProposition(createRV("Marks"), Boolean.FALSE);
		AssignmentProposition apt = new AssignmentProposition(createRV("AptiScore"), Boolean.TRUE);
		AssignmentProposition inapt = new AssignmentProposition(createRV("AptiScore"), Boolean.FALSE);
		
		ProbabilityModel model = new FiniteBayesModel(constructUniversityNetwork());
		
		double case1_output = model.prior(accepted, passed, stupid, hard, inapt);
		System.out.println(case1_output);
		double case2_output = model.prior(rejected, failed, clever, easy, apt);
		System.out.println(case2_output);
	}
}