/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


import java.util.*;


/**
 * 
 */
public interface IToken
{
  // ENUMERATIONS

  public enum TokenType
  {
    COMMENT, DOCTYPE, PI, TEXT, TAG;

    // METHODS

    public boolean isComment()
    {
      return this == COMMENT;
    }

    public boolean isDocType()
    {
      return this == DOCTYPE;
    }

    public boolean isProcessingInstruction()
    {
      return this == PI;
    }

    public boolean isText()
    {
      return this == TEXT;
    }

    public boolean isTag()
    {
      return this == TAG;
    }
  }

  // METHODS

  /**
   * @param aAttributeList
   */
  void addAttribute( final IAttribute... aAttributeList );

  /**
   * @param aChildList
   */
  void addChild( final IToken... aChildList );

  /**
   * @return
   */
  List<IAttribute> getAttributes();

  /**
   * @return
   */
  List<IToken> getChildren();

  /**
   * @return
   */
  String getName();

  /**
   * @return
   */
  IToken getParent();

  /**
   * @return
   */
  TokenType getType();

  /**
   * @param aAttribute
   * @return
   */
  boolean hasAttribute( final IAttribute aAttribute );

  /**
   * @param aParent
   */
  void setParent( final IToken aParent );
}
