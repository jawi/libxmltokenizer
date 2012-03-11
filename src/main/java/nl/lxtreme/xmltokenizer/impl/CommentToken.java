/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


/**
 * 
 */
public class CommentToken extends BaseToken
{
  // VARIABLES

  private String comment;

  // CONSTRUCTORS

  /**
   * @param aName
   */
  public CommentToken( final String aComment )
  {
    super( TokenType.COMMENT );
    this.comment = ( aComment == null ) ? "" : aComment.trim();
  }

  // METHODS

  /**
   * @return the comment
   */
  public String getComment()
  {
    return this.comment;
  }

  /**
   * @param aComment
   *          the comment to set
   */
  public void setComment( final String aComment )
  {
    this.comment = aComment;
  }
}
