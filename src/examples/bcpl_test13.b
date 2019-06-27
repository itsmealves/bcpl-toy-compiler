GLOBAL $(
    INT x : 0;
$)

LET start() INT $(
	INT x : 2;
	INT y : 3;
	
	WRITE(10 / (x * y));

	RETURN 0;
$)