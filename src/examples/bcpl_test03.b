# Erro léxico
# Token NEQUALS mal formado

GLOBAL $(
	INT x : 10;
$)

LET start() BE $(
	BOOLEAN y : x ! 10 
$)