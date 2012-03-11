/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


import java.io.*;


/**
 * Provides a "configurable" key-value pair parser, consecutively returning any
 * key-value pair found.
 * <p>
 * By default this tokenizer allows one to find key/value pairs in the form of
 * "key = value", respecting single/double quoted strings and escaped quotes.
 * </p>
 * <p>
 * This tokenizer can be configured to parse key-value pairs in various forms.
 * As long as the 'grammar' is more or less unambiguous and yields key-value
 * pairs, this tokenizer is able to parse it.<br/>
 * Comments are supported as well, and will be ignored by this tokenizer.
 * Comments can be formed using up to two opening characters and up to two
 * closing characters. This way, C-like comments '/ *' and '* /' are supported.
 * </p>
 */
final class KeyValuePairParser
{
  // INNER TYPES

  /**
   * Provides the configuration for an KeyValuePairParser.
   */
  public interface Config
  {
    /**
     * Returns whether the given characters together form an end-of-comment.
     * 
     * @param aChar1
     *          the first character;
     * @param aChar2
     *          the second character.
     * @return <code>true</code> if the end-of-comment is found,
     *         <code>false</code> otherwise.
     */
    boolean isCommentEnd( final int aChar1, final int aChar2 );

    /**
     * Returns whether the given characters together form an start-of-comment.
     * 
     * @param aChar1
     *          the first character;
     * @param aChar2
     *          the second character.
     * @return <code>true</code> if the start-of-comment is found,
     *         <code>false</code> otherwise.
     */
    boolean isCommentStart( final int aChar1, final int aChar2 );

    /**
     * Returns whether an escape character is given.
     * 
     * @param aChar
     *          the character that might be an escape character.
     * @return <code>true</code> if the given character is an escape character
     *         is found, <code>false</code> otherwise.
     */
    boolean isEscapeChar( final int aChar );

    /**
     * Returns whether an key-value pair is terminated or not.
     * 
     * @param aChar
     *          the character that indicates the end of the attribute, >= 0.
     * @return <code>true</code> if the attribute is terminated,
     *         <code>false</code> otherwise.
     */
    boolean isKeyValuePairEnd( final int aChar );

    /**
     * Returns whether a key/value separator character is given.
     * 
     * @param aChar
     *          the character that might be an key/value separator.
     * @return <code>true</code> if the given character is a key/value
     *         separator, <code>false</code> otherwise.
     */
    boolean isKeyValueSeparator( final int aChar );

    /**
     * Returns whether a end-of-quote character is given.
     * 
     * @param aChar
     *          the character that might be a end-of-quote.
     * @return <code>true</code> if a end-of-quote character is given,
     *         <code>false</code> otherwise.
     */
    boolean isQuoteEndChar( final int aChar );

    /**
     * Returns whether a start-of-quote character is given.
     * 
     * @param aChar
     *          the character that might be a start-of-quote.
     * @return <code>true</code> if a start-of-quote character is given,
     *         <code>false</code> otherwise.
     */
    boolean isQuoteStartChar( final int aChar );

    /**
     * Returns whether a whitespace character is given.
     * 
     * @param aChar
     *          the character that might be whitespace.
     * @return <code>true</code> if a whitespace character is given,
     *         <code>false</code> otherwise.
     */
    boolean isWhitespace( final int aChar );

    /**
     * Returns whether or not to skip whitespace characters.
     * 
     * @return <code>true</code> if whitespace characters should be skipped,
     *         <code>false</code> otherwise.
     */
    boolean skipWhitespace();
  }

  /**
   * Provides a default configuration for this tokenizer.
   */
  public static class DefaultConfig implements Config
  {
    /**
     * @returns <code>true</code> if the given characters together form '* /'
     *          (without the space), <code>false</code> otherwise.
     */
    @Override
    public boolean isCommentEnd( final int aChar1, final int aChar2 )
    {
      return ( '*' == aChar1 ) && ( '/' == aChar2 );
    }

    /**
     * @returns <code>true</code> if the given characters together form '/ *'
     *          (without the space), <code>false</code> otherwise.
     */
    @Override
    public boolean isCommentStart( final int aChar1, final int aChar2 )
    {
      return ( '/' == aChar1 ) && ( '*' == aChar2 );
    }

    /**
     * @returns <code>true</code> when a single backslash (\) is given,
     *          <code>false</code> otherwise.
     */
    @Override
    public boolean isEscapeChar( final int aChar )
    {
      return '\\' == aChar;
    }

    /**
     * @return <code>true</code> when a whitespace character is given,
     *         <code>false</code> otherwise.
     * @see #isWhitespace(int)
     */
    @Override
    public boolean isKeyValuePairEnd( final int aChar )
    {
      return Character.isWhitespace( aChar );
    }

    /**
     * @return <code>true</code> when '=' is given, <code>false</code>
     *         otherwise.
     */
    @Override
    public boolean isKeyValueSeparator( final int aChar )
    {
      return '=' == aChar;
    }

    /**
     * @return <code>true</code> when a single or double quote is given,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isQuoteEndChar( final int aChar )
    {
      return ( '"' == aChar ) || ( '\'' == aChar );
    }

    /**
     * @return <code>true</code> when a single or double quote is given,
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isQuoteStartChar( final int aChar )
    {
      return ( '"' == aChar ) || ( '\'' == aChar );
    }

    /**
     * @return <code>true</code> when a whitespace character is given,
     *         <code>false</code> otherwise.
     * @see Character#isWhitespace(int)
     */
    @Override
    public boolean isWhitespace( final int aChar )
    {
      return Character.isWhitespace( aChar );
    }

    /**
     * @returns always <code>true</code>.
     */
    @Override
    public boolean skipWhitespace()
    {
      return true;
    }
  }

  // VARIABLES

  private final Reader reader;
  private Config       config;

  // CONSTRUCTORS

  /**
   * Creates a new KeyValuePairParser.
   * 
   * @param aReader
   *          the reader to tokenize.
   * @throws IllegalArgumentException
   *           in case the given reader was <code>null</code>.
   */
  public KeyValuePairParser( final Reader aReader ) throws IllegalArgumentException
  {
    if ( aReader == null )
    {
      throw new IllegalArgumentException( "Reader cannot be null!" );
    }
    // We want a reader that supports marks, otherwise we create one that
    // does...
    this.reader = ( aReader.markSupported() ) ? aReader : new BufferedReader( aReader );
    this.config = new DefaultConfig();
  }

  /**
   * Creates a new KeyValuePairParser.
   * 
   * @param aString
   *          the string to tokenize.
   * @throws IllegalArgumentException
   *           in case the given string was <code>null</code>.
   */
  public KeyValuePairParser( final String aString ) throws IllegalArgumentException
  {
    if ( aString == null )
    {
      throw new IllegalArgumentException( "String cannot be null!" );
    }
    this.reader = new BufferedReader( new StringReader( aString ) );
    this.config = new DefaultConfig();
  }

  // METHODS

  /**
   * Returns the current configuration.
   * 
   * @return the configuration of this tokenizer, never <code>null</code>.
   */
  public final Config getConfig()
  {
    return this.config;
  }

  /**
   * Returns the next key/value pair.
   * 
   * @return the next key/value pair as array of Strings (of length 2), can be
   *         <code>null</code> in case no attribute could be created. In case
   *         the returned attribute has no name-part it could mean that a
   *         "continuation" for the previously returned attribute is found.
   * @throws IOException
   *           in case of I/O problems.
   */
  public String[] nextKeyValuePair() throws IOException
  {
    StringBuilder sb = new StringBuilder();
    String name = null;

    int ch = skipWhitespace();
    int prevCh = 0;
    boolean quotedText = false;

    do
    {
      if ( this.config.isCommentStart( prevCh, ch ) )
      {
        // Found a comment, skip until the end of the comment is found...
        // remove the (already added) comment-start character
        if ( sb.length() > 0 )
        {
          sb.setLength( sb.length() - 1 ); // = fast!
        }

        while ( ( ch != -1 ) && !this.config.isCommentEnd( prevCh, ch ) )
        {
          prevCh = ch;
          ch = this.reader.read();
        }

        // Avoid the last character of the comment-end in our end-result...
        ch = skipWhitespace();
      }

      if ( this.config.isKeyValueSeparator( ch ) )
      {
        if ( !quotedText && ( name == null ) )
        {
          // name part finished...
          name = sb.toString().trim();

          // fast way of clearing the string builder...
          sb.setLength( 0 );

          // skip whitespace until the value starts...
          ch = skipWhitespace();
        }
      }

      if ( this.config.isQuoteStartChar( ch ) || this.config.isQuoteEndChar( ch ) )
      {
        // If we're in quoted text, we're now unquoted and vice versa...
        if ( !this.config.isEscapeChar( prevCh ) )
        {
          quotedText = !quotedText;
          ch = 0;
        }
        else
        {
          // Escaped quotation; remove the (already added) escape character
          // and add the current quotation character like a normal character...
          sb.setLength( sb.length() - 1 ); // = fast!
        }
      }
      else if ( this.config.isKeyValuePairEnd( ch ) )
      {
        // done?!
        if ( !quotedText && ( name != null ) )
        {
          break;
        }
      }

      if ( ch > 0 )
      {
        // Avoid adding 'NUL' characters...
        sb.append( ( char )ch );
      }

      prevCh = ch;
      ch = this.reader.read();
    }
    while ( ch != -1 );

    String value = sb.toString().trim();
    if ( ( ch == -1 ) && ( name == null ) && value.isEmpty() )
    {
      return null;
    }

    // Try to automagically close any missing quote...
    if ( quotedText && !value.isEmpty() )
    {
      value = value + value.charAt( 0 );
    }

    return new String[] { name, value };
  }

  /**
   * Sets the (optional) configuration for this tokenizer.
   * 
   * @param aConfig
   *          the configuration to set, cannot be <code>null</code>.
   * @throws IllegalArgumentException
   *           in case the given configuration was <code>null</code>.
   */
  public final void setConfig( final Config aConfig ) throws IllegalArgumentException
  {
    if ( aConfig == null )
    {
      throw new IllegalArgumentException( "Config cannot be null!" );
    }
    this.config = aConfig;
  }

  /**
   * Skips as much whitespace as found, if required by our configuration.
   * 
   * @return the first non-whitespace character found, or the first whitespace
   *         character found if the configuration does not allow us to skip
   *         whitespace.
   * @throws IOException
   *           in case of I/O problems.
   */
  protected int skipWhitespace() throws IOException
  {
    int ch;
    do
    {
      ch = this.reader.read();
    }
    while ( ( ch != -1 ) && this.config.skipWhitespace() && this.config.isWhitespace( ch ) );
    return ch;
  }
}
