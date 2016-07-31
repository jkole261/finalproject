package edu.ramapo.jkole.css.parser;

import org.w3c.dom.css.CSSRule;

import javafx.scene.Node;

public class RuleParser{
	protected CSSRule rule;
	
	public static Node parseRule(CSSRule rule){
		System.out.println(rule.getCssText());
		
		return null;
	}
}
