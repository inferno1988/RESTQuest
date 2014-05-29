grammar RESTQuest;

request
    :  PACKAGE packagePath SEMI REQUEST Identifier '{' requestSegment* '}' EOF
    ;

packagePath
    :   Identifier ('.' Identifier)* 
    ;

requestSegment
    :   Segment '(' paramList? ')' block
    ;

paramList
    :   Identifier (',' Identifier)*
    ;

block
    :   '{' expression* '}'
    ;

expression
    :   declaration
    |   assignment
    ;

declaration
    :   ReservedWord dec_value SEMI
    ;

assignment
    :   ReservedWord key COLON value SEMI
    ;

key
    :   (StringLiteral|parameter|formattedString)
    ;

value
    :   (StringLiteral|parameter|formattedString)
    ;

dec_value
    :   (StringLiteral|parameter|formattedString)
    ;

formattedString
    : SINGLE .*? parameter .*? (.*? parameter .*?)+ SINGLE
    ;

parameter
    : Parameter
    ;

Segment
    :   DECLARATION
    |   HEADERS
    |   DATA
    ;

ReservedWord
    :   METHOD
    |   URL
    |   HEADER
    |   RAW
    ;

Parameter
    :   DOLLAR Identifier
    ;

METHOD: 'method';
URL: 'url';
HEADERS: 'headers';
HEADER: 'header';
DATA: 'data';
RAW: 'raw';
REQUEST: 'request';
OCURLY: '{';
CCURLY: '}';
DECLARATION: 'declaration';
PACKAGE: 'package';
DOT: '.';
COMMA: ',';
OBRACE: '(';
CBRACE: ')';
SEMI : ';';
DOLLAR: '$';
COLON: ':';
SINGLE: '\'';

Identifier
    :   RestquestLetter RestquestLetterOrDigit*
    ;

fragment RestquestLetter
    :   [a-zA-Z$_]
    ;

fragment RestquestLetterOrDigit
    :   [a-zA-Z0-9$_]
    ;

StringLiteral
    : '"' StringCharacters? '"'
    ;

fragment
    StringCharacters
    : StringCharacter+
    ;

fragment
    StringCharacter
    : ~["\\]
    | EscapeSequence
    ;

fragment
    EscapeSequence
    : '\\' [btnfr"'\\]
    | OctalEscape
    | UnicodeEscape
    ;

fragment
OctalEscape
    : '\\' OctalDigit
    | '\\' OctalDigit OctalDigit
    | '\\' ZeroToThree OctalDigit OctalDigit
    ;

fragment
UnicodeEscape
    : '\\' 'u' HexDigit HexDigit HexDigit HexDigit
    ;

fragment
ZeroToThree
    : [0-3]
    ;

fragment
HexDigit
    : [0-9a-fA-F]
    ;

fragment
OctalDigit
    : [0-7]
    ;

WS : [ \t\r\n\u000C]+ -> skip
;

COMMENT
: '/*' .*? '*/' -> skip
;

LINE_COMMENT
: '//' ~[\r\n]* -> skip
;

