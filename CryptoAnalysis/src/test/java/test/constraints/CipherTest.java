package test.constraints;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.beust.jcommander.internal.Lists;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import crypto.analysis.ConstraintSolver;
import crypto.analysis.EnsuredCryptSLPredicate;
import crypto.analysis.ParentPredicate;
import crypto.rules.CryptSLObject;
import crypto.rules.CryptSLPredicate;
import crypto.rules.CryptSLRule;
import crypto.rules.CryptSLRuleReader;
import test.IDEALCrossingTestingFramework;
import typestate.interfaces.ICryptSLPredicateParameter;

public class CipherTest{

	protected CryptSLRule getCryptSLFile() {
		return CryptSLRuleReader.readFromFile(new File(IDEALCrossingTestingFramework.RESOURCE_PATH + "Cipher.cryptslbin"));
	}
	
	@Test
	public void testCipher1() {
		Multimap<String, String> values = HashMultimap.create();
		values.put("transformation", "AES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest1", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 0);
	}

	@Test
	public void testCipher2() {
		//No mode of operation specified
		Multimap<String, String> values = HashMultimap.create();
		values.put("transformation", "AES");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest2", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 2);
	}
	
	@Test
	public void testCipher3() {
		//ECB Mode not allowed.
		Multimap<String, String> values = HashMultimap.create();
		values.put("transformation", "AES/ECB/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest3", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 1);
	}
	
	
	@Test
	public void testCipher4() {
		Multimap<String, String> values = HashMultimap.create();
		//algorithms of cipher and key mismatch
		values.put("transformation", "AES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "Blowfish");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest4", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 1);
	}
	
	@Test
	public void testCipher5() {
		Multimap<String, String> values = HashMultimap.create();
		//not allowed algorithm DES
		values.put("transformation", "DES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "DES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest5", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 1);
	}
	
	@Test
	public void testCipher6() {
		Multimap<String, String> values = HashMultimap.create();
		//not allowed algorithm DES and mismatch of key and cipher alg
		values.put("transformation", "DES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest6", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 2);
	}
	
	@Test
	public void testCipher7() {
		Multimap<String, String> values = HashMultimap.create();
		//macced predicate for plaintext
		values.put("transformation", "AES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variablesKey = new ArrayList<ICryptSLPredicateParameter>();
				variablesKey.add(new CryptSLObject("key"));
				variablesKey.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variablesKey, false);
				
				ArrayList<ICryptSLPredicateParameter> variablesMAC = new ArrayList<ICryptSLPredicateParameter>();
				variablesMAC.add(new CryptSLObject("plainText"));
				variablesMAC.add(new CryptSLObject("_"));
				CryptSLPredicate macPred = new CryptSLPredicate("macced", variablesMAC, false);
				
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				ensuredPredList.add(new EnsuredCryptSLPredicate(macPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest7", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 1);
	}
	
	@Test
	public void testCipher8() {
		Multimap<String, String> values = HashMultimap.create();
		values.put("transformation", "AES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		values.put("ranGen", "");
		
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				
				ArrayList<ICryptSLPredicateParameter> randVariables = new ArrayList<ICryptSLPredicateParameter>();
				randVariables.add(new CryptSLObject("ranGen"));
				
				CryptSLPredicate secRandPred = new CryptSLPredicate("random", randVariables, false);
				Multimap<String, String> collectedValuesRand = HashMultimap.create();
				
				ensuredPredList.add(new EnsuredCryptSLPredicate(secRandPred, collectedValuesRand));
				
				
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest1", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 0);
	}
	
	@Test
	public void testCipher9() {
		Multimap<String, String> values = HashMultimap.create();
		values.put("transformation", "AES/CBC/PKCS5Padding");
		values.put("encmode", "1");
		values.put("key", "");
		values.put("plainText", "");
		values.put("ranGen", "");
		
		ConstraintSolver cs = new ConstraintSolver(new ParentPredicate() {
			
			@Override
			public List<EnsuredCryptSLPredicate> getEnsuredPredicates() {
				List<EnsuredCryptSLPredicate> ensuredPredList = new ArrayList<EnsuredCryptSLPredicate>();
				ArrayList<ICryptSLPredicateParameter> variables = new ArrayList<ICryptSLPredicateParameter>();
				variables.add(new CryptSLObject("key"));
				variables.add(new CryptSLObject("alg"));
				CryptSLPredicate keygenPred = new CryptSLPredicate("generatedKey", variables, false);
				Multimap<String, String> collectedValues = HashMultimap.create();
				collectedValues.put("alg", "AES");
				
				ensuredPredList.add(new EnsuredCryptSLPredicate(keygenPred, collectedValues));
				return ensuredPredList;
			}
		}, getCryptSLFile(), values);
		
		ResultPrinter.evaluateResults("CipherTest1", cs.getAllConstraints().size(), cs.getRelConstraints().size(), cs.evaluateRelConstraints(), 1);
	}
	
}