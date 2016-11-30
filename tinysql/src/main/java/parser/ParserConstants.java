/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int digit = 9;
  /** RegularExpression Id. */
  int letter = 10;
  /** RegularExpression Id. */
  int integer = 11;
  /** RegularExpression Id. */
  int data_type = 12;
  /** RegularExpression Id. */
  int name = 13;
  /** RegularExpression Id. */
  int column_name = 14;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"(\"",
    "\")\"",
    "\"+\"",
    "\"*\"",
    "<digit>",
    "<letter>",
    "<integer>",
    "<data_type>",
    "<name>",
    "<column_name>",
    "\"DROP\"",
    "\"TABLE\"",
    "\"CREATE\"",
    "\",\"",
  };

}