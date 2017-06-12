 .ident "Coins Compiler version: coins-1.4.3.1 + BackEnd-1.0"
/* JavaCG for target:x86 convention:cygwin */
	.text
	.align	1
_string.3:
	.byte	37
	.byte	100
	.byte	10
	.byte	0

	.align	4
	.global	_main
_main:
	pushl	%ebp
	movl	%esp,%ebp
	pushl	_a
	pushl	$_string.3
	call	_printf
	leal	8(%esp),%esp
	movl	$0,%eax
	leave
	ret

	.data
	.align	4
	.global	_a
_a:
	.long	12345
