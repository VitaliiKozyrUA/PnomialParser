

# A Parser for Polynomial Expressions
This parser can be characterized by the following words:
> `simple` `deterministic` `LL` `recursive descent` `rule-based` `type-safe`

## Grammar
The parser's grammar, expressed in EBNF notation:
```EBNF  
<whole> ::= "0" | [1-9] [0-9]*  
<real> ::= "-"? <whole> ("." <whole>)?  
<variable> ::= "x"  
<term> ::= <real> (<variable> ("^" <whole>)?)?  
<polynomial> ::= <term> ("+" <term>)*  
<start> ::= <polynomial>  
```  
For example, the production rules for the nonterminal `real` in the [source code](https://github.com/VitaliiKozyrUA/PnomialParser/blob/6d18d763ba7dcc1fd4cbe52dd0d1048537aa6f20/src/main/kotlin/com/kozyr/pnomialparser/tree/Real.kt#L22C9-L22C69) are as follows:
```kotlin  
private val optionalMinus = optionalRule(C.Minus) { minus ->  
  minus  
}  
  
private val optionalFraction = optionalRule(C.Dot, Whole) { _, whole ->  
  whole  
}  
  
private val rule = rule(  
    optionalMinus, Whole, optionalFraction  
) { minus, whole, fraction ->  
  val sign = if (minus == null) '+' else '-'  
  Real(sign, whole.value, fraction?.value ?: 0u)  
}
```  

## Parsing
A successful parse of a polynomial expression results in the construction of an AST with the structure shown in the image:  
![AST](https://github.com/VitaliiKozyrUA/PnomialParser/assets/AST.png)

If the parser cannot understand the language of the provided expression, it may emit the following errors:
> Reference expression: ``5x^2 + 2.5x + -1``.

- **Error:** Unexpected symbol at position 4.    
  5x^ + 2.5x + -1    
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ Expecting a 'whole' symbol here.

- **Error:** Unexpected symbol at position 5.  
  5x^2 2.5x + -1  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;^ Expecting a 'plus' symbol here.

- **Error:** Unexpected symbol at position 1.    
  5^2 + 2.5x + -1    
  &nbsp;&nbsp;^ Expecting one of the following symbols here: 'dot', 'variable', 'plus'.

## Semantic analysis
After parsing, semantic analysis is performed. It verifies certain nodes in the generated AST and can report subsequent errors:  
**Input:** ``5x^2 + 2.5x^2 + -1``.    
**Error:** In the term '2.5x^2' the exponent should be 1.

**Input:** ``5x^2 + 2.5y + -1``.    
**Error:** All terms in the polynomial must have the same variable.