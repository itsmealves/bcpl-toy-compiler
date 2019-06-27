GLOBAL $(
    INT maria : 10;
    INT joao : 100;
$)

LET screen(INT a) BE $(
	WRITE(a);
$)

LET square(INT a) INT $(
	RETURN a * a;
$)

LET start() INT $(
	INT x : 10;
	x := x * x;
	INT y : VALOF square(x + 6);
	WRITE(x);
	WRITE(y);

	RETURN x;
$)

LET aumenta.idade(INT valor) BOOLEAN $(
	WHILE valor < joao DO $(
		valor := 1;
	$)
	
	IF maria = joao THEN $(
		INT  idade : 
			maria + valor;
	$)
	
	TEST maria != joao THEN $(
		WHILE maria != joao DO $(
		    maria := maria + 1;
		$)
	$) ELSE $(
		RETURN TRUE;
	$)

	WRITE(maria);
	RETURN FALSE;
$)

