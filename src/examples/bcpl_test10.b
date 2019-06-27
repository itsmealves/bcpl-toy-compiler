# Programa sem erros

GLOBAL $(
	INT x : 10;
	INT y : 100;
	BOOLEAN z : TRUE;
$)

#comment

LET start() INT $(
	WRITE(x);
	WRITE(y);
	WRITE(z);

	x := (10 + 2);
	y := x;
	x := y + 9;
	z := x != y;
	z := x = y;
	z := x <= y;
	z := x >= y;
	z := x > y;
	z := x < y;

	RETURN 0;
$)

