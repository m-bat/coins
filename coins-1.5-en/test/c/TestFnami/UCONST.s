 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	117
	.byte	32
	.byte	37
	.byte	117
	.byte	10
	.byte	0
	.align	1
_string.5:
	.byte	37
	.byte	120
	.byte	32
	.byte	37
	.byte	120
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	$1073741824
	pushl	$1073741824
	pushl	$_string.3
	call	_printf
	leal	12(%esp),%esp
	pushl	$1135780240
	pushl	$1135780240
	pushl	$_string.5
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

