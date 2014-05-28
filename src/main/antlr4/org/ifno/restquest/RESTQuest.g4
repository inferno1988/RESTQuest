grammar RESTQuest;

compileObject
    :   importDeclaration* requestDeclaration EOF
    ;

importDeclaration
    :   IMPORT importPath SEMI;

requestDeclaration
    :   REQUEST Identifier requestBody
    ;

requestBody
    : OCURLY segmentDeclaration* CCURLY
    ;

segmentDeclaration
    :  typedSegment | simpleSegment
    ;

simpleSegment
    :   (CONFIG|HEADER|MAIN) block
    ;

typedSegment
    :  type (PARAMETERS|RESPONSE) block
    ;

block
    : OCURLY functionCall* CCURLY
    ;

functionCall
    :   (METHOD|URL|AUTH) '(' (StringLiteral|expression) ')' SEMI
    ;

expression
    :   NEW Identifier '(' parameterList ')'
    ;

parameterList
    :   Identifier (',' Identifier)*
    ;

importPath
    : Identifier ('.' Identifier)*
    ;

IMPORT: 'import';
METHOD: 'method';
URL: 'url';
HEADER: 'header';
PARAMETERS: 'parameters';
RESPONSE: 'response';
MAIN: 'main';
REQUEST: 'request';
OCURLY: '{';
CCURLY: '}';
CONFIG: 'config';
AUTH: 'auth';
NEW: 'new';
JSON: 'json';
XML: 'xml';
DOT: '.';
COMMA: ',';
OBRACE: '(';
CBRACE: ')';
SEMI : ';';

Identifier
    :   RestquestLetter RestquestLetterOrDigit*
    ;

fragment RestquestLetter
    :   [a-zA-Z$_]
    ;

fragment RestquestLetterOrDigit
    :   [a-zA-Z0-9$_]
    ;

REQUEST_METHODS: (POST|GET|PUT|DELETE);
fragment POST: 'POST';
fragment GET: 'GET';
fragment PUT: 'PUT';
fragment DELETE: 'DELETE';

type
    : 'json'
    | 'xml'
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

