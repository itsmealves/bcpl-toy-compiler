GLOBAL $(
	INT output : 0;
	INT input : 0;
$)

LET play() BE $(
	WRITE(output);
$)

LET capture() BE $(
	input := 10;
$)

LET filter(INT x, INT k) INT $(
	RETURN x * k;
$)

LET sigint.handler(INT sig) BOOLEAN $(
	RETURN TRUE;
$)

LET start() INT $(
	INT k : 98;

	WHILE TRUE DO $(
		capture();

		output := VALOF filter(input, k);
		play();

		IF VALOF sigint.handler(0) = TRUE THEN $(
			BREAK;
		$)
	$)

	RETURN 0;
$)