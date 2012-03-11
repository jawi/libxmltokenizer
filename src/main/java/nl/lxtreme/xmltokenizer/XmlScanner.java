/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


import java.io.*;


/**
 * Breaks up a string of characters into chunks of either whitespace or apparent
 * XML-tags.
 */
public final class XmlScanner
{
  // VARIABLES

  private final Reader reader;

  // CONSTRUCTORS

  /**
   * Creates a new XmlScanner.
   * 
   * @param aReader
   *          the reader to use while tokenizing;
   * @param aCleanerConfig
   *          the cleaner configuration to use.
   */
  public XmlScanner( final Reader aReader )
  {
    this.reader = new BufferedReader( aReader );
  }

  // METHODS

  /**
   * Splits up the contained stream into chunks of character data.
   * 
   * @return a token-string, can be <code>null</code> in case of no more data.
   */
  public String nextToken() throws IOException
  {
    final StringBuilder sb = new StringBuilder();

    int ch;
    boolean openTag = false;
    boolean closeTag = false;

    do
    {
      this.reader.mark( 2 );

      ch = this.reader.read();
      if ( '<' == ch )
      {
        if ( sb.length() > 0 )
        {
          int peek = this.reader.read();
          if ( !Character.isWhitespace( peek ) )
          {
            // Already collected some stuff; emit that token first, before
            // going to process this (presumed) tag...
            this.reader.reset();
            break;
          }
          else
          {
            // Spurious '<' found; append the whitespace
            // 
            sb.append( ( char )ch );
            ch = peek;
          }
        }

        openTag = true;
      }
      else if ( openTag && ( '>' == ch ) )
      {
        closeTag = true;
      }

      if ( ch >= 0 )
      {
        sb.append( ( char )ch );
      }
    }
    while ( ( ch != -1 ) && !closeTag );

    return ( ch == -1 ) && ( sb.length() == 0 ) ? null : sb.toString();
  }
}
