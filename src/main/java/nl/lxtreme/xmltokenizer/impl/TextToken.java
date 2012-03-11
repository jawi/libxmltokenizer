/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


/**
 * 
 */
public class TextToken extends BaseToken
{
  // VARIABLES

  private final String text;

  // CONSTRUCTORS

  /**
   * 
   */
  public TextToken( final String aText )
  {
    super( TokenType.TEXT );

    this.text = aText;
  }

  // METHODS

  /**
   * @return the text
   */
  public String getText()
  {
    return this.text;
  }
}
