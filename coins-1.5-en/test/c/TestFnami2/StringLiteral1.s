 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	10
	.byte	0
	.align	1
_string.4:
	.byte	65
	.byte	66
	.byte	67
	.byte	68
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movsbl	_string.4+2,%eax
	pushl	%eax
	pushl	$1
	pushl	$5
	pushl	$_string.3
	call	_printf
	leal	16(%esp),%esp
	movl	$0,%eax
	leave
	ret

