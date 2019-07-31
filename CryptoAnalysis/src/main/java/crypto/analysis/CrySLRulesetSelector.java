package crypto.analysis;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import com.google.common.collect.Lists;

import crypto.rules.CryptSLRule;
import crypto.rules.CryptSLRuleReader;
import crypto.cryptslhandler.CryslReaderUtils;
import org.apache.commons.io.FilenameUtils;

public class CrySLRulesetSelector {
	public static enum RuleFormat {
		SOURCE() {
			public String toString() {
				return".cryptsl";
			}
		},
		BINARY() {
			public String toString() {
				return".cryptslbin";
			}
		},
	}

	public static enum Ruleset {
		JavaCryptographicArchitecture, BouncyCastle, Tink
	}

	public static List<CryptSLRule> makeFromRuleset(String rulesBasePath, RuleFormat ruleFormat, Ruleset... set) {
		List<CryptSLRule> rules = Lists.newArrayList();
		for (Ruleset s : set) {
			rules.addAll(getRulesset(rulesBasePath, ruleFormat, s));
		}
		if (rules.isEmpty()) {
			System.out.println("No CrySL rules found for rulesset " + set);
		}
		return rules;
	}

	/**
	 * Computes the ruleset from a string. The sting
	 * 
	 * @param rulesetString
	 * @return
	 */
	public static List<CryptSLRule> makeFromRulesetString(String rulesBasePath, RuleFormat ruleFormat,
			String rulesetString) {
		String[] set = rulesetString.split(",");
		List<Ruleset> ruleset = Lists.newArrayList();
		for (String s : set) {
			if (s.equalsIgnoreCase(Ruleset.JavaCryptographicArchitecture.name())) {
				ruleset.add(Ruleset.JavaCryptographicArchitecture);
			}
			if (s.equalsIgnoreCase(Ruleset.BouncyCastle.name())) {
				ruleset.add(Ruleset.BouncyCastle);
			}
			if (s.equalsIgnoreCase(Ruleset.Tink.name())) {
				ruleset.add(Ruleset.Tink);
			}
		}
		if (ruleset.isEmpty()) {
			throw new RuntimeException("Could not parse " + rulesetString + ". Was not able to find rulesets.");
		}
		return makeFromRuleset(rulesBasePath, ruleFormat, ruleset.toArray(new Ruleset[ruleset.size()]));
	}

	private static List<CryptSLRule> getRulesset(String rulesBasePath, RuleFormat ruleFormat, Ruleset s) {
		List<CryptSLRule> rules = Lists.newArrayList();
		File[] listFiles = new File(rulesBasePath + s + "/").listFiles();
		for (File file : listFiles) {
			if (ruleFormat.equals(RuleFormat.BINARY) && file.getName().endsWith(ruleFormat.toString())) {
				rules.add(CryptSLRuleReader.readFromFile(file));
			}

			if (ruleFormat.equals(RuleFormat.SOURCE) && file.getName().endsWith(ruleFormat.toString())) {
				try {
					rules.add(CryptSLRuleReader.readFromSourceFile(file));
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return rules;
	}

	public static CryptSLRule makeSingleRule(String rulesBasePath, RuleFormat ruleFormat, Ruleset ruleset,
			String rulename) {
		if (ruleFormat.equals(RuleFormat.BINARY)) {
			File file = new File(rulesBasePath + "/" + ruleset + "/" + rulename + RuleFormat.BINARY);
			if (!file.exists()) {
				throw new RuntimeException("Could not locate rule " + rulename + " within set " + ruleset);
			}
			return CryptSLRuleReader.readFromFile(file);
		} else {
			File file = new File(rulesBasePath + "/" + ruleset + "/" + rulename + RuleFormat.SOURCE);
			if (file.exists()) {
				try {
					CryptSLRule rule = CryptSLRuleReader.readFromSourceFile(file);
					return rule;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static List<CryptSLRule> makeFromPath(File resourcesPath, RuleFormat ruleFormat) {
		if (!resourcesPath.isDirectory())
			throw new RuntimeException("The specified path is not a directory" + resourcesPath);
		List<CryptSLRule> rules = Lists.newArrayList();
		File[] listFiles = resourcesPath.listFiles();
		for (File file : listFiles) {
			if (ruleFormat.equals(RuleFormat.BINARY) && file.getName().endsWith(RuleFormat.BINARY.toString())) {
				rules.add(CryptSLRuleReader.readFromFile(file));
			} 
			if(ruleFormat.equals(RuleFormat.SOURCE) && file.getName().endsWith(RuleFormat.SOURCE.toString())) {
				try {
					rules.add(CryptSLRuleReader.readFromSourceFile(file));
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		if (rules.isEmpty()) {
			System.out.println("No CrySL rules found in " + resourcesPath);
		}
		return rules;
	}
}
