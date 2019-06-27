GLOBAL $(
	INT x : 10;
	BOOLEAN y : TRUE;
$)

# comentário qualquer

LET imPriMe.3.variaveis(INT a, BOOLEAN b, INT c) BE $(
	y := TRUE;
	WRITE(a);
	WRITE(b);
	WRITE(c);
	WRITE(TRUE);
$)

LET SOMA.com.2(INT x) INT $(
	INT resultado.da.operacao : x + 2;
	RETURN resultado.da.operacao;
$)

LET start() INT $(
	INT z : 100;
	INT k : 2;
	WRITE(y #
	)
	;

	WHILE x < z DO $(
		#comentário qualquer
		x := 
		x 
		* 
		2;

		IF y THEN $(
			x := x - 1;
			CONTINUE;
		$)

		TEST z = 79 THEN $(
			BREAK;
		$)
		ELSE $(
			y := FALSE;
		$)

		k := VALOF SOMA.com.2(x / (k * 2));

		IF y = FALSE THEN $(
			imPriMe.3.variaveis
			(k, y, z);y := VALOF SOMA.com.2(70) = 2;
		$)
	$) 

	RETURN 0;
$)