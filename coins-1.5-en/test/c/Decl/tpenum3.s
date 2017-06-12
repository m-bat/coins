 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.5:
	.byte	37
	.byte	100
	.byte	32
	.byte	37
	.byte	100
	.byte	32
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	movl	$256,%eax
	movl	$512,%eax
	pushl	$512
	pushl	$256
	pushl	$_string.5
	call	_printf
	leal	12(%esp),%esp
	movl	$0,%eax
	leave
	ret

