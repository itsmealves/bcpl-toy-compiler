GLOBAL $(
	INT h : 10;
	INT j : 89;
	BOOLEAN m : TRUE;
$)

LET test() BE $(
	WRITE(FALSE);
$)

LET printTudo(INT a, BOOLEAN b) BE $(
	WRITE(a);
	WRITE(b);
	WRITE(FALSE);
$)

LET soma.10(INT x) INT $(
	RETURN x + 10;
$)

LET start() INT $(
	INT x : 10;
	INT x....ahsduiahid : 0;
	BOOLEAN y : TRUE;

	test();

	TEST y THEN $(
		x := VALOF soma.10(10);
	$)
	ELSE $(
		y := FALSE;
	$)

	WHILE TRUE DO $(
		printTudo(x, y);

		CONTINUE
		;
		BREAK
		;
		RETURN
		10 ;
		RETURN x - 1;
		RETURN x * 1;
		RETURN x + 1;
		RETURN x / 1;
		RETURN FALSE;
		#hausdhaisd
		RETURN 
#asiduahdias
		VALOF soma.10     ( 10 ); #hausidhasdiasodiasdjoajsdoas ok

		WHILE FALSE DO $(
			BREAK;

			IF (x....ahsduiahid + 2) = 10 THEN $(
				y := x <= 10;
				y := x < 10;
				y := x >= 10;
				y := x > 10;
				y := x != 10;
				y := x = 10;

				TEST x = y THEN $(
					WRITE(TRUE);
				$)
				ELSE $(
					WRITE(FALSE);
				$)
			$)
		$)
	$)

	IF x >= (5 + 2) THEN $(
		WRITE(x);
	$)
$)