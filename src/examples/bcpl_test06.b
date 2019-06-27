# Erro sintático
# RP não encontrado

LET prod(INT x, INT y) INT $(
	RETURN x * y;
$)

LET ok() INT $(
	RETURN 2;
$)

LET f() BE $(
	INT z : 0;
	IF 10 = 2 THEN $(
		z := VALOF prod(2,3;
		z := VALOF ok();
	$)
$)