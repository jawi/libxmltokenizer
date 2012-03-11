/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


import java.io.*;

import nl.lxtreme.xmltokenizer.impl.*;


/**
 * Tokenizes a stream of string-chunks into XML-tokens.
 */
public class XmlTokenizer
{
  // VARIABLES

  private final XmlScanner scanner;

  // CONSTRUCTORS

  /**
   * @param aXmlScanner
   *          the XML scanner to use.
   */
  public XmlTokenizer( final XmlScanner aXmlScanner )
  {
    this.scanner = aXmlScanner;
  }

  // METHODS

  /**
   * Returns the next XML-token.
   * 
   * @return a XML-token, can be <code>null</code> in case no remaining tokens
   *         are found.
   * @throws IOException
   *           in case of I/O problems.
   */
  public IToken nextToken() throws IOException
  {
    final String token = this.scanner.nextToken();
    final IToken result = createToken( token );
    return result;
  }

  /**
   * @param aToken
   * @return
   */
  private IToken createToken( final String aToken )
  {
    if ( aToken == null )
    {
      return null;
    }

    if ( aToken.startsWith( "<?" ) )
    {
      // XML processing instruction...
      return new ProcessingInstructionToken( stripNameAttributes( aToken, "<?", "?>" ) );
    }
    else if ( aToken.startsWith( "<!--" ) )
    {
      // Comment...
      return new CommentToken( strip( aToken, "<!--", "-->" ) );
    }
    else if ( aToken.startsWith( "<!" ) )
    {
      // XML doctype...
      return new DocTypeToken( stripNameAttributes( aToken, "<!DOCTYPE", ">" ) );
    }
    else if ( aToken.startsWith( "<" ) )
    {
      return new ElementToken( strip( aToken, "<", ">" ) );
    }
    else
    {
      return new TextToken( aToken );
    }
  }

  /**
   * @param aText
   * @param aStart
   * @param aEnd
   * @return
   */
  private String strip( final String aText, final String aStart, final String aEnd )
  {
    String text = aText.trim();
    if ( text.startsWith( aStart ) )
    {
      text = text.substring( aStart.length() );
    }
    if ( text.endsWith( aEnd ) )
    {
      final int length = text.length();
      text = text.substring( 0, length - aEnd.length() );
    }
    return text.trim();
  }

  /**
   * @param aText
   * @param aStart
   * @param aEnd
   * @return
   */
  private String[] stripNameAttributes( final String aText, final String aStart, final String aEnd )
  {
    return strip( aText, aStart, aEnd ).split( "\\s+", 2 );
  }
}
