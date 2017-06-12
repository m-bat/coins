 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.6:
	.byte	101
	.byte	110
	.byte	117
	.byte	109
	.byte	32
	.byte	109
	.byte	111
	.byte	110
	.byte	116
	.byte	104
	.byte	32
	.byte	109
	.byte	61
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$11,_m
	pushl	_m
	pushl	$_string.6
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.comm	_m,4
