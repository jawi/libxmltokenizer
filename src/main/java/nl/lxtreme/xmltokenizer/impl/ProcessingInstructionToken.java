/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


/**
 * 
 */
public class ProcessingInstructionToken extends BaseToken
{
  // CONSTRUCTORS

  /**
   * @param aPIText
   *          the processing instruction text.
   */
  public ProcessingInstructionToken( final String... aPIParts )
  {
    super( TokenType.PI );

    if ( aPIParts.length > 0 )
    {
      setName( aPIParts[0] );
    }

    if ( aPIParts.length > 1 )
    {
      parseAttributes( aPIParts[1] );
    }
  }
}
