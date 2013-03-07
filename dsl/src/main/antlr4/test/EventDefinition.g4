grammar EventDefinition;

statement : importDefinition
          | eventDefinition
          | valueObjectDefinition
          | identifierDefinition
          ;

importDefinition : 'import' packageName;

packageName : IDENTIFIER ;
alias : IDENTIFIER ;

eventDefinition : metaDef* 'event' className parentDef? attributes? classContent?;
valueObjectDefinition : 'value object' className attributes? ;

identifierDefinition : 'id' identifierName '>' className;

identifierName : IDENTIFIER ;
metaDef : 'abstract' ;
parentDef : '>' className ;

classContent : CODE_BLOCK ;

CODE_BLOCK : '{' ( CODE_BLOCK | ~[{}] )* '}' ;

className : IDENTIFIER;
arrayDef : className'[]' ;

attributes : '(' ( typeDefinition ( (',') typeDefinition ) * ) ? ')' ;
typeDefinition : paramName ':' paramType  ;

paramName : (IDENTIFIER | 'id' | 'value');
paramType : className | primitive | arrayDef;

IDENTIFIER   : [a-zA-Z$_\-\.]+;

primitive : 'int'
          | 'long'
          | 'short'
          | 'byte'
          | 'double'
          | 'float'
          | 'char'
          ;

// skip spaces, tabs, newlines
WS           : [ \t\r\n]+ -> skip ;
COMMENT      : '/*' .*? '*/' -> skip ;
LINE_COMMENT : '//' ~[\n\r]* [\r\n]+ -> skip ;
