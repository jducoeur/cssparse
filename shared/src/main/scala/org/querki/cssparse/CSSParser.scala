package org.querki.cssparse

import fastparse.all._

/**
 * A CSS Parser/Validator.
 * 
 * As of this writing, this is extremely simplistic, providing only very basic validation
 * and being too permissive in some respects. This needs a lot of work to make the validator
 * more precise and correct, and to turn it into an actual parser that generates an AST.
 * 
 * Note that, for the moment, this is just worrying about validating syntax, and is only
 * dealing with semantics enough to rule out the capabilities that allow JavaScript. It
 * currently allows all sorts of property/value combinations that are semantically illegal.
 * 
 * @author jducoeur
 */
object CSSParser {
  
  val comment = P("/*" ~ (!"*/" ~ AnyChar).rep.! ~ "*/")
  
  val white = P(CharPred(_.isWhitespace))
  val optwhite = P(white.rep)
  val somewhite = P(white.rep(1))
  val anywhite = P((comment ~ white).rep)
  
  val name = P(CharPred(c => CharPredicates.isLetter(c) || c == '-'))

  
  val propName = name
  
  val propVal = P("TODO")
  
  val decl = P(propName ~ optwhite ~ ":" ~ optwhite ~ propVal)
  
  
  val semiwhite = P(anywhite ~ ";" ~ anywhite)
  val block = P("{" ~ anywhite ~ decl.rep(sep=semiwhite) ~ anywhite ~ ";".? ~ anywhite ~ "}")
  
  
  val typeSelector = P(name)
  val classSelector = P("." ~ name)
  val idSelector = P("#" ~ name)
  
  val attrName = name
  // TODO: attrVal needs a lot of thought, since it's potentially enormously complex
  val attrVal = P("\"" ~ (!"\"" ~ AnyChar) ~ "\"")
  val attrSelector = P(attrName ~ (StringIn("=", "~=", "|=", "^=", "$=", "*=") ~ attrVal).?)
  
  val adjacentSiblings:Parser[(Any,Any)] = P(selector ~ white ~ "+" ~ white ~ selector)
  val generalSiblings:Parser[(Any,Any)] = P(selector ~ white ~ "~" ~ white ~ selector)
  val childSelector:Parser[(Any,Any)] = P(selector ~ white ~ ">" ~ white ~ selector)
  val descendantSelector:Parser[(Any,Any)] = P(selector ~ white ~ selector)
  
  val selector = P(
      typeSelector | classSelector | idSelector | attrSelector | 
      adjacentSiblings | generalSiblings | childSelector | descendantSelector)
  
  
  val rule = P(selector.rep(sep=",") ~ block)
  
  val cssfile = P(anywhite ~ rule.rep(sep=optwhite) ~ anywhite)
  
  def parse(css:String):fastparse.core.Result[_] = cssfile.parse(css)
}
